package com.michael.dal.clients;

import com.michael.dal.clients.models.BearerTokenResult;
import com.michael.dal.services.auth.models.BasicAuthInformation;

public interface SecurityServiceClient {
  BearerTokenResult processBearerToken(String url, BasicAuthInformation basicAuthInformation);
}
