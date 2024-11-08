package com.michael.dal.base.adapter.providers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.michael.dal.base.adapter.MainModule;
import com.michael.dal.manager.ServiceProvider;

public class ServiceProviderImpl implements ServiceProvider {
  private static final Injector injector = Guice.createInjector(new MainModule());

  @Override
  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }
}
