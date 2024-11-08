package com.michael.dal.base.adapter.services.home.module;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.services.home.impl.HomeServiceImpl;
import com.michael.dal.services.home.HomeService;

public class HomeModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(HomeService.class).to(HomeServiceImpl.class);
  }
}
