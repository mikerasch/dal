package com.michael.dal.services.config.models;

import com.michael.dal.controllers.config.discovery.DiscoveryEnvironment;
import com.michael.dal.controllers.config.security.SecurityEnvironment;
import java.util.List;

public record UpsertConfigRequest(
    List<SecurityEnvironment> securityEnvironments,
    List<DiscoveryEnvironment> discoveryEnvironments) {}
