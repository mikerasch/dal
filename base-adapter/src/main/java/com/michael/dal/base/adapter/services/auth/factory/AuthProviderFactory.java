package com.michael.dal.base.adapter.services.auth.factory;

import com.google.inject.Inject;
import com.michael.dal.base.adapter.services.auth.impl.BearerTokenProvider;
import com.michael.dal.clients.SecurityServiceClient;
import com.michael.dal.services.auth.AuthProvider;
import com.michael.dal.services.auth.models.BasicAuthInformation;
import com.michael.dal.services.config.ConfigManager;

public class AuthProviderFactory {
  private final SecurityServiceClient securityServiceClient;
  private final ConfigManager configManager;

  @Inject
  public AuthProviderFactory(
      final SecurityServiceClient securityServiceClient, final ConfigManager configManager) {
    this.securityServiceClient = securityServiceClient;
    this.configManager = configManager;
  }

  public AuthProvider bearerTokenProvider(final String username, final String password) {
    var basicAuthInformation = new BasicAuthInformation(username, password);

    return new BearerTokenProvider(basicAuthInformation, securityServiceClient, configManager);
  }
}
