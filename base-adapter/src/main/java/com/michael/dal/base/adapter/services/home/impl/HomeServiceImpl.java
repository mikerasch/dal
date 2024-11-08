package com.michael.dal.base.adapter.services.home.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.michael.dal.annotations.Service;
import com.michael.dal.base.adapter.services.terminal.impl.CMDExecutor;
import com.michael.dal.database.repositories.CurlActivityRepository;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.models.Config;
import com.michael.dal.services.config.models.SecurityServiceConnectionConfig;
import com.michael.dal.services.discovery.ServiceLocator;
import com.michael.dal.services.discovery.models.ServiceInformation;
import com.michael.dal.services.home.HomeService;
import com.michael.dal.services.terminal.TerminalExecutor;
import com.michael.dal.utils.AutoComplete;
import com.michael.dal.utils.DalConstants;
import com.michael.dal.utils.ObjectMapperProvider;
import com.michael.dal.vertx.VertxServer;
import com.michael.dal.vertx.models.CurlActivity;
import io.vertx.core.eventbus.EventBus;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Singleton
public class HomeServiceImpl implements HomeService {
  private final ServiceLocator serviceLocator;
  private final VertxServer vertxServer;
  private final Cache<String, CompletableFuture<List<ServiceInformation>>>
      serviceInformationAsyncCache =
          Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(5)).build();
  private final ConfigManager configManager;
  private final CurlActivityRepository curlActivityRepository;
  private static final Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);

  @Inject
  public HomeServiceImpl(
      final ServiceLocator serviceLocator,
      final VertxServer vertxServer,
      final ConfigManager configManager,
      final CurlActivityRepository curlActivityRepository) {
    this.serviceLocator = serviceLocator;
    this.vertxServer = vertxServer;
    this.configManager = configManager;
    this.curlActivityRepository = curlActivityRepository;
  }

  @Override
  public void kickStartDiscoverAllServices(final String env) {
    if (serviceInformationAsyncCache.getIfPresent(env) == null) {
      serviceInformationAsyncCache.put(env, fetchAllServices(env));
    }
  }

  @Override
  public List<String> autoComplete(final String text, final String environment, final int limit) {
    if (serviceInformationAsyncCache.getIfPresent(environment) == null) {
      kickStartDiscoverAllServices(environment);
    }
    return Objects.requireNonNull(serviceInformationAsyncCache.getIfPresent(environment))
        .thenApply(
            serviceInformationList -> {
              List<String> serviceNames =
                  serviceInformationList.stream().map(ServiceInformation::serviceName).toList();
              return AutoComplete.findMostSimilar(serviceNames, text, limit);
            })
        .join();
  }

  @Override
  public String getSwaggerUrl(final String selectedItem, final String env) {
    ServiceInformation serviceInformation =
        fetchAllServices(env)
            .thenApply(
                serviceInformationList ->
                    serviceInformationList.stream()
                        .filter(service -> service.serviceName().equals(selectedItem))
                        .findFirst()
                        .orElse(null))
            .join();

    if (serviceInformation == null) {
      logger.warn(
          "Service Information not found for service: {}. Defaulting to pet clinic url.",
          selectedItem);
      return DalConstants.BASE_SWAGGER_URL;
    }
    return buildUrlBasedOnK8OrLegacy(serviceInformation);
  }

  @Override
  public EventBus getEventBus() {
    return vertxServer.getVertx().eventBus();
  }

  @Override
  public String executeCurlCommand(
      final String body, final String environment, final String serviceName) {
    TerminalExecutor terminalExecutor = new CMDExecutor();

    String bearerToken = findBearerToken(environment);
    curlActivityRepository.saveCurlActivity(body, environment, serviceName);
    publishCurlActivity(body, environment, serviceName);
    String bearerWithAuthorization = body;
    if (StringUtils.isNotEmpty(bearerToken)) {
      bearerWithAuthorization =
          body.concat(" -H 'Authorization: Bearer %s'".formatted(bearerToken));
    }
    String result = terminalExecutor.executeCurlCommand(bearerWithAuthorization);

    if (StringUtils.isEmpty(result)) {
      return "No Response TODO: put the status code";
    }
    // Let's beautify the JSON response
    ObjectMapper objectMapper = ObjectMapperProvider.getInstance();
    try {
      // I dislike how this is needed
      Object json = objectMapper.readValue(result, Object.class);
      return ObjectMapperProvider.getInstance()
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(json);
    } catch (JsonProcessingException e) {
      return "{\"error\": \"Failed to process JSON response\"}";
    }
  }

  @Override
  public List<CurlActivity> fetchAllCurlActivities() {
    return curlActivityRepository.findAll().stream()
        .map(
            result ->
                new CurlActivity(
                    result.getCommand(),
                    result.getEnvironment(),
                    result.getServiceName(),
                    result.getCreatedAt()))
        .toList();
  }

  /**
   * K8 Services are running at port 80. Legacy Services run at a range of ports. For some reason,
   * the legacy services are running at port + 1. On top of this, the legacy services are running on
   * HTTPS and the K8 services are running on HTTP.
   */
  private String buildUrlBasedOnK8OrLegacy(final ServiceInformation serviceInformation) {
    // don't ask me
    int servicePort = Integer.parseInt(serviceInformation.servicePort());
    if (servicePort == 80) {
      String redirectUrl =
          DalConstants.HTTP_SWAGGER_REDIRECT_URL.formatted(
              serviceInformation.serviceAddress(), servicePort, serviceInformation.contextPath());
      return DalConstants.SWAGGER_URL_FORMAT.formatted(redirectUrl);
    }
    servicePort += 1;
    String redirectUrl =
        DalConstants.HTTPS_SWAGGER_REDIRECT_URL.formatted(
            serviceInformation.serviceAddress(), servicePort, serviceInformation.contextPath());
    return DalConstants.SWAGGER_URL_FORMAT.formatted(redirectUrl);
  }

  private String findBearerToken(final String environment) {
    Config config = configManager.readConfig();
    return config.securityServiceConnectionConfig().stream()
        .filter(
            connectionConfig ->
                StringUtils.equalsIgnoreCase(connectionConfig.environment(), environment))
        .findFirst()
        .map(SecurityServiceConnectionConfig::bearerToken)
        .orElse(null);
  }

  private CompletableFuture<List<ServiceInformation>> fetchAllServices(final String env) {
    return CompletableFuture.supplyAsync(() -> serviceLocator.fetchAllServices(env));
  }

  private void publishCurlActivity(
      final String body, final String environment, final String serviceName) {
    vertxServer
        .getVertx()
        .eventBus()
        .publish(
            "curl-history", new CurlActivity(body, environment, serviceName, LocalDateTime.now()));
  }
}
