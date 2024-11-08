package com.michael.dal.services.config;

import com.michael.dal.controllers.config.discovery.DiscoveryEnvironment;
import com.michael.dal.controllers.config.security.SecurityEnvironment;
import com.michael.dal.services.config.models.Config;
import java.util.List;
import java.util.Optional;

public interface ConfigService {
  void upsertConfig(
      List<SecurityEnvironment> securityEnvironments,
      List<DiscoveryEnvironment> discoveryEnvironments);

  Optional<Config> readConfig();
}
