package com.michael.dal.controllers.home.tabs;

import com.michael.dal.annotations.Controller;
import com.michael.dal.services.home.HomeService;
import com.michael.dal.vertx.models.CurlActivity;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;

@Controller
public class HistoryTabController {
  @FXML private TableView<CurlActivity> historyTable;
  @FXML private TextArea historyCurlCommand;
  @FXML private TableColumn<String, String> createdColumn;
  @FXML private TableColumn<String, String> commandColumn;
  @FXML private TableColumn<String, String> serviceColumn;
  @FXML private TableColumn<String, String> envColumn;

  private final HomeService homeService;
  private TabPane tabPane;

  public HistoryTabController(final HomeService homeService) {
    this.homeService = homeService;
  }

  @FXML
  public void initialize() {
    initializeHistory();
    initializeHistoryObserver();
  }

  @FXML
  public void onSendHistoryCurlClicked() {
    String historyCurlCommandText = historyCurlCommand.getText();
    if (StringUtils.isEmpty(historyCurlCommandText)) {
      return;
    }
    String environment = historyTable.getSelectionModel().getSelectedItem().getEnvironment();
    String serviceName = historyTable.getSelectionModel().getSelectedItem().getServiceName();
    String result =
        homeService.executeCurlCommand(historyCurlCommandText, environment, serviceName);
    homeService.getEventBus().publish("curl-result", result);
    switchToHomeTab();
  }

  private void initializeHistoryObserver() {
    homeService
        .getEventBus()
        .consumer(
            "curl-history",
            message -> {
              if (message.body() instanceof CurlActivity activity) {
                ObservableList<CurlActivity> observableList = historyTable.getItems();
                observableList.add(activity);
                historyTable.setItems(observableList);
              }
            });
  }

  @FXML
  private void initializeHistory() {
    List<CurlActivity> curlActivities = homeService.fetchAllCurlActivities();
    ObservableList<CurlActivity> observableList = FXCollections.observableArrayList();
    observableList.addAll(curlActivities);
    historyTable.setItems(observableList);

    commandColumn.setCellValueFactory(new PropertyValueFactory<>("body"));
    envColumn.setCellValueFactory(new PropertyValueFactory<>("environment"));
    serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
    createdColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

    // Add a listener to the table
    historyTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                historyCurlCommand.setText(newValue.getBody());
              }
            });
  }

  private void switchToHomeTab() {
    tabPane.getSelectionModel().select(0);
  }

  public void setTabPane(final TabPane tabPane) {
    this.tabPane = tabPane;
  }
}
