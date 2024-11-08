package com.michael.dal.base.adapter;

import atlantafx.base.theme.Dracula;
import com.michael.dal.base.adapter.providers.ServiceProviderImpl;
import com.michael.dal.manager.ScreenStage;
import com.michael.dal.manager.StageManager;
import com.michael.dal.services.config.ConfigManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Runner extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    var serviceProvider = new ServiceProviderImpl();
    ConfigManager configManager = serviceProvider.getInstance(ConfigManager.class);
    StageManager stageManager = serviceProvider.getInstance(StageManager.class);
    stageManager.setPrimaryStage(stage);

    ScreenStage initialStage =
        configManager.configExists() ? ScreenStage.INITIAL_STAGE : ScreenStage.MODIFY_CONFIG;

    stageManager.switchScene(initialStage);
  }

  public static void main(String[] args) {
    launch();
  }
}
