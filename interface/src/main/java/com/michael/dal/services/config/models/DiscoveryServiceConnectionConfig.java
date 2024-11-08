package com.michael.dal.services.config.models;

import com.michael.dal.services.discovery.models.ServiceType;

public record DiscoveryServiceConnectionConfig(
    String environment, String url, ServiceType serviceType) {}
