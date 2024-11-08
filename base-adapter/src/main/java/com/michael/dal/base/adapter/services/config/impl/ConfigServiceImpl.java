package com.michael.dal.base.adapter.services.config.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.michael.dal.annotations.Service;
import com.michael.dal.base.adapter.services.config.mappers.UpsertConfigRequestToConfigMapper;
import com.michael.dal.controllers.config.discovery.DiscoveryEnvironment;
import com.michael.dal.controllers.config.security.SecurityEnvironment;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.ConfigService;
import com.michael.dal.services.config.models.Config;
import com.michael.dal.services.config.models.UpsertConfigRequest;
import java.util.List;
import java.util.Optional;

@Singleton
@Service
public class ConfigServiceImpl implements ConfigService {
  private final ConfigManager configManager;

  @Inject
  public ConfigServiceImpl(final ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public void upsertConfig(
      final List<SecurityEnvironment> securityEnvironments,
      final List<DiscoveryEnvironment> discoveryEnvironments) {
    var upsertConfigRequest = new UpsertConfigRequest(securityEnvironments, discoveryEnvironments);

    Config currentConfig = configManager.readConfig();

    Config configMapped = UpsertConfigRequestToConfigMapper.map(upsertConfigRequest, currentConfig);

    configManager.upsertConfig(configMapped);
  }

  @Override
  public Optional<Config> readConfig() {
    return Optional.ofNullable(configManager.readConfig());
  }
}
