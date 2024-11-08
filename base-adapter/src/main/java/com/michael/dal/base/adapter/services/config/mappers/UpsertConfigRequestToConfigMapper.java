package com.michael.dal.base.adapter.services.config.mappers;

import com.michael.dal.controllers.config.security.SecurityEnvironment;
import com.michael.dal.services.config.models.Config;
import com.michael.dal.services.config.models.DiscoveryServiceConnectionConfig;
import com.michael.dal.services.config.models.SecurityServiceConnectionConfig;
import com.michael.dal.services.config.models.UpsertConfigRequest;
import com.michael.dal.services.discovery.models.ServiceType;
import java.util.List;
import java.util.Optional;

public class UpsertConfigRequestToConfigMapper {
  private UpsertConfigRequestToConfigMapper() {}

  public static Config map(
      final UpsertConfigRequest upsertConfigRequest, final Config currentConfig) {
    List<SecurityServiceConnectionConfig> securityUrls =
        buildSecurityServiceConnectionConfig(upsertConfigRequest, currentConfig);
    List<DiscoveryServiceConnectionConfig> discoveryUrls =
        buildDiscoveryServiceConnectionConfig(upsertConfigRequest);

    return new Config(securityUrls, discoveryUrls);
  }

  private static List<DiscoveryServiceConnectionConfig> buildDiscoveryServiceConnectionConfig(
      final UpsertConfigRequest upsertConfigRequest) {
    return upsertConfigRequest.discoveryEnvironments().stream()
        .map(
            env ->
                new DiscoveryServiceConnectionConfig(
                    env.environment(), env.url(), ServiceType.valueOf(env.serviceType())))
        .toList();
  }

  private static List<SecurityServiceConnectionConfig> buildSecurityServiceConnectionConfig(
      final UpsertConfigRequest upsertConfigRequest, final Config currentConfig) {
    return upsertConfigRequest.securityEnvironments().stream()
        .map(
            env ->
                new SecurityServiceConnectionConfig(
                    env.environment(),
                    env.url(),
                    findBearerTokenIfAlreadyExists(env, currentConfig).orElse(null)))
        .toList();
  }

  private static Optional<String> findBearerTokenIfAlreadyExists(
      final SecurityEnvironment securityConfig, final Config currentConfig) {
    return currentConfig.securityServiceConnectionConfig().stream()
        .filter(
            config ->
                config.environment().equals(securityConfig.environment())
                    && config.url().equals(securityConfig.url()))
        .map(SecurityServiceConnectionConfig::bearerToken)
        .findFirst();
  }
}
