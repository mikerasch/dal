package com.michael.dal.base.adapter.clients;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.clients.impl.RegistryServiceClientImpl;
import com.michael.dal.base.adapter.clients.impl.SecurityServiceClientImpl;
import com.michael.dal.clients.RegistryServiceClient;
import com.michael.dal.clients.SecurityServiceClient;

public class ClientModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SecurityServiceClient.class).to(SecurityServiceClientImpl.class);
    bind(RegistryServiceClient.class).to(RegistryServiceClientImpl.class);
  }
}
