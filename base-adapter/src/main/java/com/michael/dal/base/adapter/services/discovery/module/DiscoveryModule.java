package com.michael.dal.base.adapter.services.discovery.module;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.services.discovery.impl.ConsulServiceLocator;
import com.michael.dal.services.discovery.ServiceLocator;

public class DiscoveryModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ServiceLocator.class).to(ConsulServiceLocator.class);
  }
}
