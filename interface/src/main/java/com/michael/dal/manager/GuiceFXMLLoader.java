package com.michael.dal.manager;

import com.michael.dal.Runner;
import javafx.fxml.FXMLLoader;

public class GuiceFXMLLoader {
  private final ServiceProvider serviceProvider;

  public GuiceFXMLLoader(final ServiceProvider serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  public FXMLLoader load(final String fxmlFile) {
    // TODO FIX THIS WE DONT NEED THIS RUNNER CLASS
    FXMLLoader loader = new FXMLLoader(Runner.class.getResource(fxmlFile));
    loader.setControllerFactory(
        clazz -> {
          try {
            return classHasDependencies(clazz)
                ? serviceProvider.getInstance(clazz)
                : clazz.getDeclaredConstructor().newInstance();
          } catch (Exception e) {
            throw new IllegalStateException(e);
          }
        });
    return loader;
  }

  private boolean classHasDependencies(final Class<?> clazz) {
    // Check for a no-arg constructor or no constructor at all
    return clazz.getDeclaredConstructors().length == 0
        || clazz.getDeclaredConstructors()[0].getParameterCount() > 0;
  }
}
