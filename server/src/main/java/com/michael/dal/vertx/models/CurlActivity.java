package com.michael.dal.vertx.models;

import java.time.LocalDateTime;

public class CurlActivity {
  private final String body;
  private final String environment;
  private final String serviceName;
  private final LocalDateTime createdAt;

  public CurlActivity(
      String body, String environment, String serviceName, LocalDateTime createdAt) {
    this.body = body;
    this.environment = environment;
    this.serviceName = serviceName;
    this.createdAt = createdAt;
  }

  public String getBody() {
    return body;
  }

  public String getEnvironment() {
    return environment;
  }

  public String getServiceName() {
    return serviceName;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
