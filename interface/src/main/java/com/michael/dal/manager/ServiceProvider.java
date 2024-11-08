package com.michael.dal.manager;

public interface ServiceProvider {
  <T> T getInstance(Class<T> clazz);
}
