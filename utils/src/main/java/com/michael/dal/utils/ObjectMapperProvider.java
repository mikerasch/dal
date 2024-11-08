package com.michael.dal.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {
  private ObjectMapperProvider() {}

  private static final ObjectMapper objectMapper =
      new ObjectMapper()
          .setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY))
          .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

  public static ObjectMapper getInstance() {
    return objectMapper;
  }
}
