package com.michael.dal.base.adapter.services.auth.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.michael.dal.annotations.Service;
import com.michael.dal.base.adapter.exceptions.UnauthorizedException;
import com.michael.dal.base.adapter.services.auth.factory.AuthProviderFactory;
import com.michael.dal.services.auth.AuthService;
import com.michael.dal.services.auth.AuthValidator;
import com.michael.dal.services.auth.models.AuthType;
import java.util.Objects;

@Singleton
@Service
public class AuthServiceImpl implements AuthService {
  private final AuthValidator authValidator;
  private final AuthProviderFactory authProviderFactory;

  @Inject
  public AuthServiceImpl(
      final AuthValidator authValidator, final AuthProviderFactory authProviderFactory) {
    this.authValidator = authValidator;
    this.authProviderFactory = authProviderFactory;
  }

  @Override
  public boolean isAuthValid() {
    return authValidator.validate();
  }

  @Override
  public AuthType getAuthType() {
    AuthType authType = null;
    if (Objects.requireNonNull(authValidator) instanceof BearerTokenAuthValidation) {
      authType = AuthType.BEARER_TOKEN;
    } else {
      throw new IllegalStateException("Unexpected value: " + authValidator);
    }
    return authType;
  }

  @Override
  public boolean bearerTokenAuth(final String username, final String password) {
    try {
      authProviderFactory.bearerTokenProvider(username, password).authenticate();
      return true;
    } catch (UnauthorizedException e) {
      return false;
    }
  }
}
