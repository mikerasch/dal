package com.michael.dal.manager;

public enum ScreenStage {
  INITIAL_STAGE("initial-view.fxml", "Developers Are Lazy"),
  MODIFY_CONFIG("modify-config.fxml", "Modify Config"),
  HOME_TAB("home-tab.fxml", "Home Tab"),
  HISTORY_TAB("history-tab.fxml", "History Tab");
  private final String fxmlFile;
  private final String title;

  ScreenStage(final String fxmlFile, final String title) {
    this.fxmlFile = fxmlFile;
    this.title = title;
  }

  public String getFxmlFile() {
    return fxmlFile;
  }

  public String getTitle() {
    return title;
  }
}
