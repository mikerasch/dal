package com.michael.dal.services.auth;

import com.michael.dal.services.auth.models.AuthType;

public interface AuthService {
  boolean isAuthValid();

  AuthType getAuthType();

  boolean bearerTokenAuth(String username, String password);
}
