package com.michael.dal.base.adapter.providers.module;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.providers.ServiceProviderImpl;
import com.michael.dal.manager.ServiceProvider;

public class ProviderModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ServiceProvider.class).to(ServiceProviderImpl.class);
  }
}
