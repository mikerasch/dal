package com.michael.dal.clients.models;

import java.util.Map;

public record ServiceCatalogResult(Map<String, ServiceCatalogMetadata> serviceCatalog) {}
