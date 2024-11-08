package com.michael.dal.base.adapter.services.config.module;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.services.config.impl.ConfigServiceImpl;
import com.michael.dal.base.adapter.services.config.impl.JSONConfigManager;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.ConfigService;

public class ConfigModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ConfigManager.class).to(JSONConfigManager.class);
    bind(ConfigService.class).to(ConfigServiceImpl.class);
  }
}
