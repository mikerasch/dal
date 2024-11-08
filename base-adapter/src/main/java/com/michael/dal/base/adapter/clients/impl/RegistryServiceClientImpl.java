package com.michael.dal.base.adapter.clients.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.michael.dal.annotations.Client;
import com.michael.dal.clients.RegistryServiceClient;
import com.michael.dal.clients.models.ServiceCatalogMetadata;
import com.michael.dal.clients.models.ServiceCatalogResult;
import com.michael.dal.utils.ObjectMapperProvider;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Client
@Singleton
public class RegistryServiceClientImpl implements RegistryServiceClient {
  private static final String ALL_SERVICES_CATALOG_FORMAT = "%s/v1/catalog/services";
  private static final String INDIVIDUAL_SERVICES_CATALOG_FORMAT = "%s/v1/catalog/service/%s";

  private static final Logger logger = LoggerFactory.getLogger(RegistryServiceClientImpl.class);

  @Override
  public ServiceCatalogResult fetchServiceCatalog(final String baseUrl) {
    Request request =
        new Request.Builder().url(ALL_SERVICES_CATALOG_FORMAT.formatted(baseUrl)).get().build();

    Call call = new OkHttpClient().newCall(request);

    try (Response response = call.execute()) {
      if (response.isSuccessful()) {
        return parseServiceCatalog(response);
      }
    } catch (IOException e) {
      logger.error("Error occurred while fetching service catalog", e);
    }
    return null;
  }

  @Override
  public String fetchFirstServiceNodeFromServiceName(
      final String serviceName, final String baseUrl) {
    Request request =
        new Request.Builder()
            .url(INDIVIDUAL_SERVICES_CATALOG_FORMAT.formatted(baseUrl, serviceName))
            .get()
            .build();

    Call call = new OkHttpClient().newCall(request);

    try (Response response = call.execute()) {
      if (response.isSuccessful()) {
        return parseServiceNode(response);
      }
    } catch (IOException e) {
      logger.error("Error occurred while fetching service node", e);
    }
    return null;
  }

  private String parseServiceNode(final Response response) {
    ObjectMapper objectMapper = ObjectMapperProvider.getInstance();

    try {
      return objectMapper.readTree(response.body().string()).get(0).get("ServiceAddress").asText();
    } catch (IOException e) {
      logger.error("Error occurred while parsing service node", e);
    }
    return null;
  }

  private ServiceCatalogResult parseServiceCatalog(final Response response) {
    ObjectMapper objectMapper = ObjectMapperProvider.getInstance();
    try {
      Map<String, List<String>> serviceCatalog =
          objectMapper.readValue(response.body().string(), new TypeReference<>() {});
      Map<String, ServiceCatalogMetadata> serviceCatalogWithRecord =
          serviceCatalog.entrySet().stream()
              .collect(
                  Collectors.toMap(
                      Map.Entry::getKey, entry -> parseServiceCatalogMetaData(entry.getValue())));
      // TODO MAKE THIS PROPER JACKSON OBJECT MAPPING
      return new ServiceCatalogResult(serviceCatalogWithRecord);
    } catch (IOException e) {
      logger.error("Error occurred while parsing service catalog", e);
    }
    return null;
  }

  /**
   * For some reason, the response will look like this: [ "port=11184",
   * "contextPath=/customer-matching-service/v1" ] So let's parse the port and contextPath from this
   * response. It is not guaranteed that the port is the first index and the contextPath is the
   * second index.
   */
  // TODO NOT MAKE THIS DUMB
  private ServiceCatalogMetadata parseServiceCatalogMetaData(final List<String> value) {
    try {
      String contextPath = null;
      String port = null;
      for (String s : value) {
        if (s.contains("contextPath")) {
          contextPath = s.split("=")[1];
        } else if (s.contains("port")) {
          port = s.split("=")[1];
        }
      }
      return new ServiceCatalogMetadata(port, contextPath);
    } catch (Exception e) {
      logger.error("Error occurred while parsing service catalog metadata", e);
    }
    return new ServiceCatalogMetadata("0", "0");
  }
}
