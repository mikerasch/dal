package com.michael.dal.base.adapter;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.clients.ClientModule;
import com.michael.dal.base.adapter.providers.module.ProviderModule;
import com.michael.dal.base.adapter.services.auth.module.AuthModule;
import com.michael.dal.base.adapter.services.config.module.ConfigModule;
import com.michael.dal.base.adapter.services.discovery.module.DiscoveryModule;
import com.michael.dal.base.adapter.services.home.module.HomeModule;
import com.michael.dal.database.module.DatabaseModule;

public class MainModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ConfigModule());
    install(new AuthModule());
    install(new ClientModule());
    install(new DiscoveryModule());
    install(new HomeModule());
    install(new ProviderModule());
    install(new DatabaseModule());
  }
}
