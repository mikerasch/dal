package com.michael.dal.services.config;

import com.michael.dal.services.auth.models.AuthResult;
import com.michael.dal.services.config.models.Config;
import java.util.List;

public interface ConfigManager {
  boolean configExists();

  boolean upsertConfig(Config config);

  Config readConfig();

  void updateAuthResults(List<AuthResult> authResults);
}
