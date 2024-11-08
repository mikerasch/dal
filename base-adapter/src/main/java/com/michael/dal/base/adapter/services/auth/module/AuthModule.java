package com.michael.dal.base.adapter.services.auth.module;

import com.google.inject.AbstractModule;
import com.michael.dal.base.adapter.services.auth.impl.AuthServiceImpl;
import com.michael.dal.base.adapter.services.auth.impl.BearerTokenAuthValidation;
import com.michael.dal.services.auth.AuthService;
import com.michael.dal.services.auth.AuthValidator;

public class AuthModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AuthValidator.class).to(BearerTokenAuthValidation.class);
    bind(AuthService.class).to(AuthServiceImpl.class);
  }
}
