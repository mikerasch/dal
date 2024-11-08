package com.michael.dal.base.adapter.services.auth.impl;

import com.michael.dal.clients.SecurityServiceClient;
import com.michael.dal.clients.models.BearerTokenResult;
import com.michael.dal.services.auth.AuthProvider;
import com.michael.dal.services.auth.models.AuthResult;
import com.michael.dal.services.auth.models.BasicAuthInformation;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.models.Config;
import java.util.ArrayList;
import java.util.List;

public class BearerTokenProvider implements AuthProvider {

  private final BasicAuthInformation basicAuthInformation;
  private final SecurityServiceClient securityServiceClient;
  private final ConfigManager configManager;

  public BearerTokenProvider(
      final BasicAuthInformation basicAuthInformation,
      final SecurityServiceClient securityServiceClient,
      final ConfigManager configManager) {
    this.basicAuthInformation = basicAuthInformation;
    this.securityServiceClient = securityServiceClient;
    this.configManager = configManager;
  }

  @Override
  public void authenticate() {
    Config config = configManager.readConfig();
    List<AuthResult> authResults = new ArrayList<>();
    config
        .securityServiceConnectionConfig()
        .forEach(
            securityUrl -> {
              BearerTokenResult bearerTokenResult =
                  securityServiceClient.processBearerToken(securityUrl.url(), basicAuthInformation);
              authResults.add(
                  new AuthResult(
                      bearerTokenResult.statusCode(),
                      bearerTokenResult.token(),
                      securityUrl.environment()));
            });
    configManager.updateAuthResults(authResults);
  }
}
