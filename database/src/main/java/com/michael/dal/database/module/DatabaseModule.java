package com.michael.dal.database.module;

import com.google.inject.AbstractModule;
import com.michael.dal.database.repositories.CurlActivityRepository;
import com.michael.dal.database.repositories.impl.CurlActivityRepositoryImpl;

public class DatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CurlActivityRepository.class).to(CurlActivityRepositoryImpl.class);
  }
}
