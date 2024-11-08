package com.michael.dal.utils;

public class DalConstants {
  private DalConstants() {}

  public static final String BASE_SWAGGER_URL = "http://localhost:8080/swagger";
  public static final String SWAGGER_URL_FORMAT = BASE_SWAGGER_URL + "?url=%s";
  public static final String HTTPS_SWAGGER_REDIRECT_URL = "https://%s:%s%s/v3/api-docs";
  public static final String HTTP_SWAGGER_REDIRECT_URL = "http://%s:%s%s/v3/api-docs";
}
