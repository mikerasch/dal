package com.michael.dal.services.config.models;

import java.util.List;

public record Config(
    List<SecurityServiceConnectionConfig> securityServiceConnectionConfig,
    List<DiscoveryServiceConnectionConfig> discoveryServiceConnectionConfigs) {}
