package com.michael.dal.controllers.home;

import com.google.inject.Inject;
import com.michael.dal.annotations.Controller;
import com.michael.dal.controllers.home.tabs.HistoryTabController;
import com.michael.dal.controllers.home.tabs.HomeTabController;
import com.michael.dal.manager.ScreenStage;
import com.michael.dal.manager.StageManager;
import com.michael.dal.services.auth.AuthService;
import com.michael.dal.services.config.ConfigService;
import com.michael.dal.services.home.HomeService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

@Controller
public class InitialViewController {
  private final AuthService authService;
  private final ConfigService configService;
  private final HomeService homeService;
  private final StageManager stageManager;

  @FXML private TabPane tabPane;

  @Inject
  public InitialViewController(
      final AuthService authService,
      final ConfigService configService,
      final HomeService homeService,
      final StageManager stageManager) {
    this.authService = authService;
    this.configService = configService;
    this.homeService = homeService;
    this.stageManager = stageManager;
  }

  @FXML
  public void initialize() throws IOException {
    Tab homeTab = new Tab("Home");
    Tab historyTab = new Tab("History");
    tabPane.getTabs().add(homeTab);
    tabPane.getTabs().add(historyTab);

    setHomeTabContent(homeTab);
    setHistoryTabContent(historyTab);
  }

  private void setHistoryTabContent(Tab historyTab) throws IOException {
    FXMLLoader fxmlLoader = stageManager.loadFXML(ScreenStage.HISTORY_TAB);
    HistoryTabController historyTabController = new HistoryTabController(homeService);
    historyTabController.setTabPane(tabPane);
    fxmlLoader.setController(historyTabController);
    historyTab.setContent(fxmlLoader.load());
  }

  private void setHomeTabContent(final Tab homeTab) throws IOException {
    FXMLLoader fxmlLoader = stageManager.loadFXML(ScreenStage.HOME_TAB);
    HomeTabController homeTabController =
        new HomeTabController(stageManager, homeService, authService, configService);
    homeTabController.setTabPane(tabPane);
    fxmlLoader.setController(homeTabController);
    homeTab.setContent(fxmlLoader.load());
  }

  @FXML
  public void detectKeyPressOnHomeScreen(final KeyEvent keyEvent) {
    // If cntrl + tab is pressed, cycle between the tabs
    if (keyEvent.isControlDown() && keyEvent.getCode().toString().equals("TAB")) {
      int currentTabIndex = tabPane.getSelectionModel().getSelectedIndex();
      int nextTabIndex = (currentTabIndex + 1) % tabPane.getTabs().size();
      tabPane.getSelectionModel().select(nextTabIndex);
    }
    // if cntrl + number is pressed, try and switch to the tab
    if (keyEvent.isControlDown() && StringUtils.isNumeric(keyEvent.getText())) {
      int tabIndex = Integer.parseInt(keyEvent.getText()) - 1;
      if (tabIndex < tabPane.getTabs().size()) {
        tabPane.getSelectionModel().select(tabIndex);
      }
    }
  }
}
