package com.michael.dal.controllers.home.tabs;

import com.google.inject.Inject;
import com.michael.dal.annotations.Controller;
import com.michael.dal.manager.ScreenStage;
import com.michael.dal.manager.StageManager;
import com.michael.dal.services.auth.AuthService;
import com.michael.dal.services.auth.models.AuthType;
import com.michael.dal.services.config.ConfigService;
import com.michael.dal.services.config.models.DiscoveryServiceConnectionConfig;
import com.michael.dal.services.home.HomeService;
import com.michael.dal.thread.SingleTaskExecutor;
import com.michael.dal.widgets.BearerTokenPopupWidget;
import com.michael.dal.widgets.BrowserWidget;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeTabController {
  private final StageManager stageManager;
  private final HomeService homeService;
  private final AuthService authService;
  private final ConfigService configService;

  private final SingleTaskExecutor singleTaskExecutor = new SingleTaskExecutor();

  @FXML private ChoiceBox<String> environmentChoice;
  @FXML private TextField searchTextBar;
  @FXML private ListView<String> searchResults;
  @FXML private TextArea curlResultArea;
  private TabPane tabPane;

  private static final Logger logger = LoggerFactory.getLogger(HomeTabController.class);

  @Inject
  public HomeTabController(
      final StageManager stageManager,
      final HomeService homeService,
      final AuthService authService,
      final ConfigService configService) {
    this.stageManager = stageManager;
    this.homeService = homeService;
    this.authService = authService;
    this.configService = configService;
  }

  @FXML
  public void initialize() {
    initializeTabPane();
    handleInitialCheckForValidAuth();
    populateEnvironmentChoice();
    addListenerToEnvironmentChoice();
    initializeEventBus();
  }

  /** Switches to the configuration view. */
  @FXML
  public void editConfig() throws IOException {
    stageManager.switchScene(ScreenStage.MODIFY_CONFIG);
  }

  @FXML
  public void resetToken() {
    displayPopupToLogin();
  }

  @FXML
  public void searchServiceKeyPress() {
    singleTaskExecutor.submitTask(
        () -> {
          String environment = environmentChoice.getSelectionModel().getSelectedItem();
          List<String> relatedServices =
              homeService.autoComplete(searchTextBar.getText(), environment, 3);
          // Update the UI on the JavaFX thread
          Platform.runLater(
              () -> {
                searchResults.getItems().clear();
                searchResults.getItems().addAll(relatedServices);
              });
        });
  }

  @FXML
  public void listViewMouseClicked(final MouseEvent mouseEvent) {
    if (mouseEvent.getClickCount() == 2
        && StringUtils.isNotEmpty(searchResults.getSelectionModel().getSelectedItem())) {

      String selectedService = searchResults.getSelectionModel().getSelectedItem();
      String selectedEnvironment = environmentChoice.getSelectionModel().getSelectedItem();
      displayWebBrowser(selectedService, selectedEnvironment);
    }
  }

  @FXML
  public void copyKeyPressed() {
    if (StringUtils.isEmpty(curlResultArea.getText())) {
      return;
    }
    ClipboardContent content = new ClipboardContent();
    content.putString(curlResultArea.getText());
    Clipboard.getSystemClipboard().setContent(content);
  }

  @FXML
  public void clearKeyPressed() {
    curlResultArea.clear();
  }

  private void displayPopupToLogin() {
    if (Objects.requireNonNull(authService.getAuthType()) == AuthType.BEARER_TOKEN) {
      displayPopupForBearerTokenAuthValidation(null);
    }
  }

  private void displayWebBrowser(final String title, final String environment) {
    String titleWithEnvironment = title + " - " + environment;
    // Check if the tab already exists
    for (Tab tab : tabPane.getTabs()) {
      if (StringUtils.equals(tab.getText(), titleWithEnvironment)) {
        tabPane.getSelectionModel().select(tab);
        logger.info("Switched to existing tab {}", titleWithEnvironment);
        return;
      }
    }
    BrowserWidget browserWidget = new BrowserWidget(1600.0, 900.0);
    String url = homeService.getSwaggerUrl(title, environment);
    browserWidget.loadURL(url);
    tabPane.getTabs().add(new Tab(titleWithEnvironment, browserWidget));
    // Switch to the newly created tab
    tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
  }

  private void displayPopupForBearerTokenAuthValidation(String initialMessage) {
    Dialog<Pair<String, String>> dialog = BearerTokenPopupWidget.create(initialMessage);
    dialog
        .showAndWait()
        .ifPresent(
            result -> {
              String username = result.getKey();
              String password = result.getValue();
              if (!authService.bearerTokenAuth(username, password)) {
                displayPopupForBearerTokenAuthValidation("Invalid username or password");
              }
            });
  }

  private void initializeTabPane() {
    // The first one will be the home tab
    // The home tab should not be closable
    tabPane.getTabs().get(0).setClosable(false);
    tabPane.getTabs().get(1).setClosable(false);
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
  }

  private void handleInitialCheckForValidAuth() {
    boolean isAuthValid = authService.isAuthValid();

    if (!isAuthValid) {
      displayPopupToLogin();
    }
  }

  private void populateEnvironmentChoice() {
    configService
        .readConfig()
        .ifPresent(
            conf -> {
              List<String> environments =
                  conf.discoveryServiceConnectionConfigs().stream()
                      .map(DiscoveryServiceConnectionConfig::environment)
                      .toList();
              environmentChoice.getItems().addAll(environments);
            });
  }

  private void addListenerToEnvironmentChoice() {
    searchTextBar.setDisable(true); // Disable until environment is selected
    environmentChoice
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (StringUtils.isNotEmpty(newValue)) {
                if (!StringUtils.equals(oldValue, newValue)) {
                  searchTextBar.clear();
                  searchResults.getItems().clear();
                  curlResultArea.clear();
                }
                homeService.kickStartDiscoverAllServices(newValue);
                searchTextBar.setDisable(false);
              }
            });
  }

  private void initializeEventBus() {
    curlRequestEventBus();
    curlResponseEventBus();
  }

  private void curlResponseEventBus() {
    homeService
        .getEventBus()
        .consumer("curl-result", message -> curlResultArea.setText((String) message.body()));
  }

  private void curlRequestEventBus() {
    homeService
        .getEventBus()
        .consumer(
            "curl",
            message -> {
              String body = (String) message.body();
              Platform.runLater(
                  () -> {
                    String environment = environmentChoice.getSelectionModel().getSelectedItem();
                    String serviceName = searchResults.getSelectionModel().getSelectedItem();
                    String curlResult =
                        homeService.executeCurlCommand(body, environment, serviceName);
                    curlResultArea.setText(curlResult);
                    switchToHomeTab();
                  });
            });
    logger.info("Event bus initialized for curl messages");
  }

  private void switchToHomeTab() {
    tabPane.getSelectionModel().select(0);
  }

  public void setTabPane(final TabPane tabPane) {
    this.tabPane = tabPane;
  }
}
