package com.michael.dal.controllers.config;

import static com.michael.dal.controllers.config.discovery.DiscoveryInputValidation.validateDiscovery;
import static com.michael.dal.controllers.config.security.SecurityInputValidation.validateSecurity;

import com.google.inject.Inject;
import com.michael.dal.annotations.Controller;
import com.michael.dal.controllers.config.discovery.DiscoveryEnvironment;
import com.michael.dal.controllers.config.security.SecurityEnvironment;
import com.michael.dal.manager.ScreenStage;
import com.michael.dal.manager.StageManager;
import com.michael.dal.services.config.ConfigService;
import com.michael.dal.services.config.models.Config;
import com.michael.dal.services.discovery.models.ServiceType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Controller
public class ConfigController {
  @FXML private TableView<DiscoveryEnvironment> discoveryEnvironments;
  @FXML private TableColumn<DiscoveryEnvironment, String> discoveryEnvironmentColumn;
  @FXML private TableColumn<DiscoveryEnvironment, String> discoveryUrlColumn;
  @FXML private TableColumn<DiscoveryEnvironment, String> discoveryTypeColumn;
  @FXML private TextField discoveryUrlTextBox;
  @FXML private TextField discoveryEnvironmentTextBox;
  @FXML private ChoiceBox<String> serviceDiscoveryTypes;
  @FXML private TextField securityEnvironmentTextBox;
  @FXML private TextField securityUrlTextBox;
  @FXML private TableView<SecurityEnvironment> securityEnvironments;
  @FXML private TableColumn<SecurityEnvironment, String> securityEnvironmentColumn;
  @FXML private TableColumn<SecurityEnvironment, String> securityUrlColumn;

  private final ConfigService configService;
  private final StageManager stageManager;

  @Inject
  public ConfigController(final ConfigService configService, final StageManager stageManager) {
    this.configService = configService;
    this.stageManager = stageManager;
  }

  @FXML
  public void initialize() {
    bindTables();
    configService.readConfig().ifPresent(this::populateConfig);
    Arrays.stream(ServiceType.values())
        .forEach(serviceType -> serviceDiscoveryTypes.getItems().add(serviceType.name()));
  }

  @FXML
  public void addDiscoveryEnvironment() {
    String discoveryEnvironmentBox = discoveryEnvironmentTextBox.getText();
    String discoveryUrlBox = discoveryUrlTextBox.getText();
    String serviceType = serviceDiscoveryTypes.getValue();

    var discoveryEnvironment =
        new DiscoveryEnvironment(discoveryEnvironmentBox, discoveryUrlBox, serviceType);

    if (!validateDiscovery(discoveryEnvironment, discoveryEnvironments.getItems())) {
      return;
    }

    discoveryEnvironments
        .getItems()
        .add(new DiscoveryEnvironment(discoveryEnvironmentBox, discoveryUrlBox, serviceType));

    discoveryEnvironmentTextBox.clear();
    discoveryUrlTextBox.clear();
    serviceDiscoveryTypes.getSelectionModel().clearSelection();
  }

  @FXML
  public void addSecurityEnvironment() {
    String securityEnvironmentBox = securityEnvironmentTextBox.getText();
    String securityUrlBox = securityUrlTextBox.getText();
    var securityEnvironment = new SecurityEnvironment(securityEnvironmentBox, securityUrlBox);

    if (!validateSecurity(securityEnvironment, securityEnvironments.getItems())) {
      return;
    }

    securityEnvironments
        .getItems()
        .add(new SecurityEnvironment(securityEnvironmentBox, securityUrlBox));

    securityEnvironmentTextBox.clear();
    securityUrlTextBox.clear();
  }

  @FXML
  public void deleteSecurityEnvironment() {
    SecurityEnvironment selectedItem = securityEnvironments.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      securityEnvironments.getItems().remove(selectedItem);
    }
  }

  @FXML
  public void deleteDiscoveryEnvironment() {
    DiscoveryEnvironment selectedItem = discoveryEnvironments.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      discoveryEnvironments.getItems().remove(selectedItem);
    }
  }

  @FXML
  public void saveAction() throws IOException {

    configService.upsertConfig(
        new ArrayList<>(securityEnvironments.getItems()),
        new ArrayList<>(discoveryEnvironments.getItems()));

    stageManager.switchScene(ScreenStage.INITIAL_STAGE);
  }

  @FXML
  public void closeAction() throws IOException {
    stageManager.switchScene(ScreenStage.INITIAL_STAGE);
  }

  private void populateConfig(final Config config) {
    populateSecurityEnvironments(config);
    populateDiscoveryEnvironments(config);
  }

  private void populateDiscoveryEnvironments(final Config config) {
    config
        .discoveryServiceConnectionConfigs()
        .forEach(
            discoveryEnvironment ->
                discoveryEnvironments
                    .getItems()
                    .add(
                        new DiscoveryEnvironment(
                            discoveryEnvironment.environment(),
                            discoveryEnvironment.url(),
                            discoveryEnvironment.serviceType().name())));
  }

  private void populateSecurityEnvironments(final Config config) {
    config
        .securityServiceConnectionConfig()
        .forEach(
            securityEnvironment ->
                securityEnvironments
                    .getItems()
                    .add(
                        new SecurityEnvironment(
                            securityEnvironment.environment(), securityEnvironment.url())));
  }

  private void bindTables() {
    securityEnvironmentColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().environment()));
    securityUrlColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().url()));
    discoveryTypeColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().serviceType()));
    discoveryEnvironmentColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().environment()));
    discoveryUrlColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().url()));
  }
}
