package com.michael.dal.base.adapter.services.auth.impl;

import com.auth0.jwt.JWT;
import com.google.inject.Inject;
import com.michael.dal.services.auth.AuthValidator;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.models.Config;
import java.time.Instant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BearerTokenAuthValidation implements AuthValidator {
  private static final Logger logger = LoggerFactory.getLogger(BearerTokenAuthValidation.class);
  private final ConfigManager configManager;

  @Inject
  public BearerTokenAuthValidation(final ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public boolean validate() {
    Config config = configManager.readConfig();
    return config.securityServiceConnectionConfig().stream()
        .allMatch(
            securityConfig -> {
              boolean matches = isBearerTokenValid(securityConfig.bearerToken());
              if (!matches) {
                logger.info("Bearer token is invalid or expired for URL {}.", securityConfig.url());
              }
              return matches;
            });
  }

  private boolean isBearerTokenValid(final String bearerToken) {
    return StringUtils.isNotEmpty(bearerToken) && isTokenNotExpired(bearerToken);
  }

  private boolean isTokenNotExpired(final String bearerToken) {
    Instant expiresAtAsInstant = JWT.decode(bearerToken).getExpiresAtAsInstant();

    return expiresAtAsInstant.isAfter(Instant.now());
  }
}
