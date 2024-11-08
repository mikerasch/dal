package com.michael.dal.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Singleton
public class StageManager {
  private final ServiceProvider serviceProvider;
  private Stage primaryStage;

  @Inject
  public StageManager(final ServiceProvider serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  public void setPrimaryStage(final Stage stage) {
    primaryStage = stage;
  }

  public void switchScene(final ScreenStage screenStage) throws IOException {
    FXMLLoader fxmlLoader = loadFXML(screenStage);
    Scene scene = new Scene(fxmlLoader.load());
    primaryStage.setResizable(false);
    primaryStage.setTitle(screenStage.getTitle());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public FXMLLoader loadFXML(final ScreenStage screenStage) {
    GuiceFXMLLoader guiceFXMLLoader = new GuiceFXMLLoader(serviceProvider);
    return guiceFXMLLoader.load(screenStage.getFxmlFile());
  }
}
