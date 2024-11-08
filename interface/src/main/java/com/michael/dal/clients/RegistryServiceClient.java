package com.michael.dal.clients;

import com.michael.dal.clients.models.ServiceCatalogResult;

public interface RegistryServiceClient {
  ServiceCatalogResult fetchServiceCatalog(String baseUrl);

  String fetchFirstServiceNodeFromServiceName(String serviceName, String baseUrl);
}
