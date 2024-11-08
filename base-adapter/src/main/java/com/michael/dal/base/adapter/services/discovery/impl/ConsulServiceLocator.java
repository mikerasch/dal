package com.michael.dal.base.adapter.services.discovery.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.michael.dal.annotations.Service;
import com.michael.dal.clients.RegistryServiceClient;
import com.michael.dal.clients.models.ServiceCatalogMetadata;
import com.michael.dal.clients.models.ServiceCatalogResult;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.models.Config;
import com.michael.dal.services.config.models.DiscoveryServiceConnectionConfig;
import com.michael.dal.services.discovery.ServiceLocator;
import com.michael.dal.services.discovery.models.ServiceInformation;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConsulServiceLocator implements ServiceLocator {
  private final RegistryServiceClient registryServiceClient;
  private final ConfigManager configManager;
  private final Cache<String, List<ServiceInformation>> serviceInformationCache =
      Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(5)).build();
  private static final Logger logger = LoggerFactory.getLogger(ConsulServiceLocator.class);

  @Inject
  public ConsulServiceLocator(
      final RegistryServiceClient registryServiceClient, final ConfigManager configManager) {
    this.registryServiceClient = registryServiceClient;
    this.configManager = configManager;
  }

  @Override
  public List<ServiceInformation> fetchAllServices(final String env) {
    return serviceInformationCache.get(env, key -> fetchAllServicesFromConsul(env));
  }

  private List<ServiceInformation> fetchAllServicesFromConsul(final String env) {
    Config config = configManager.readConfig();

    String baseUrl = findServiceDiscoveryUrlFromEnv(config, env).orElse(null);

    if (StringUtils.isEmpty(baseUrl)) {
      logger.error("Service discovery URL not found for environment");
      return Collections.emptyList();
    }

    ServiceCatalogResult serviceCatalogResult = registryServiceClient.fetchServiceCatalog(baseUrl);

    return fetchIndividualServiceInformation(serviceCatalogResult, baseUrl);
  }

  // TODO LET'S FIX THIS SO IT'S NOT MAKING 1 CALL PER SERVICE. THIS WILL KILL PERFORMANCE
  private List<ServiceInformation> fetchIndividualServiceInformation(
      final ServiceCatalogResult serviceCatalogResult, final String baseUrl) {
    List<CompletableFuture<ServiceInformation>> serviceInformation =
        serviceCatalogResult.serviceCatalog().entrySet().stream()
            .map(
                entry ->
                    CompletableFuture.supplyAsync(
                        () -> {
                          ServiceCatalogMetadata metaData = entry.getValue();
                          String serviceName = entry.getKey();
                          String serviceUrl = fetchServiceUrlFromServiceName(serviceName, baseUrl);
                          String contextPath = metaData.contextPath();
                          String port = metaData.port();
                          return new ServiceInformation(serviceName, serviceUrl, port, contextPath);
                        }))
            .toList();

    CompletableFuture<List<ServiceInformation>> combinedFuture =
        CompletableFuture.allOf(serviceInformation.toArray(new CompletableFuture[0]))
            .thenApply(v -> serviceInformation.stream().map(CompletableFuture::join).toList());

    return combinedFuture.join();
  }

  private String fetchServiceUrlFromServiceName(final String serviceName, final String baseUrl) {
    return registryServiceClient.fetchFirstServiceNodeFromServiceName(serviceName, baseUrl);
  }

  private Optional<String> findServiceDiscoveryUrlFromEnv(final Config config, final String env) {
    return config.discoveryServiceConnectionConfigs().stream()
        .filter(
            discoveryServiceConnectionConfig ->
                discoveryServiceConnectionConfig.environment().equals(env))
        .map(DiscoveryServiceConnectionConfig::url)
        .findFirst();
  }
}
