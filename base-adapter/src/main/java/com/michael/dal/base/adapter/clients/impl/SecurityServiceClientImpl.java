package com.michael.dal.base.adapter.clients.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.michael.dal.annotations.Client;
import com.michael.dal.base.adapter.exceptions.UnauthorizedException;
import com.michael.dal.clients.SecurityServiceClient;
import com.michael.dal.clients.models.BearerTokenResult;
import com.michael.dal.services.auth.models.BasicAuthInformation;
import com.michael.dal.utils.ObjectMapperProvider;
import java.io.IOException;
import java.util.Base64;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Client
public class SecurityServiceClientImpl implements SecurityServiceClient {
  private static final Logger logger = LoggerFactory.getLogger(SecurityServiceClientImpl.class);
  private static final String BASIC_AUTH_PASSWORD_FORMAT = "%s:%s";

  @Override
  public BearerTokenResult processBearerToken(
      final String url, final BasicAuthInformation basicAuthInformation) {
    Base64.Encoder encoder = Base64.getEncoder();
    String basicAuthPasswordEncoded =
        encoder.encodeToString(
            BASIC_AUTH_PASSWORD_FORMAT
                .formatted(basicAuthInformation.username(), basicAuthInformation.password())
                .getBytes());
    Request request =
        new Request.Builder()
            .url(url)
            .header("Authorization", "Basic " + basicAuthPasswordEncoded)
            .post(RequestBody.create("", null))
            .build();

    Call call = new OkHttpClient().newCall(request);

    try (Response response = call.execute()) {

      if (response.isSuccessful()) {
        String extractedToken = parseBearerToken(response);
        return new BearerTokenResult(extractedToken, response.code());
      } else if (response.code() == 401) {
        throw new UnauthorizedException("Failed to authenticate with security service");
      }
    } catch (IOException e) {
      logger.error("Error occurred while processing bearer token", e);
    }
    return null;
  }

  // TODO come up with a better way to parse the bearer token
  private String parseBearerToken(final Response response) {
    ObjectMapper objectMapper = ObjectMapperProvider.getInstance();
    try {
      return objectMapper.readTree(response.body().string()).get("token").asText();
    } catch (IOException e) {
      logger.error("Error occurred while parsing bearer token", e);
    }
    return null;
  }
}
