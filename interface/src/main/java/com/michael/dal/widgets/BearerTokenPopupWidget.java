package com.michael.dal.widgets;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

/**
 * I don't think I used my brain when I wrote this class. I'm sorry. I should revisit this class and
 * fix it when I am not tired.
 */
public class BearerTokenPopupWidget extends TrapDialogWidget {
  private BearerTokenPopupWidget(final String errorMessage) {
    displayWidget(errorMessage);
  }

  public static Dialog<Pair<String, String>> create(String errorMessage) {
    return new BearerTokenPopupWidget(errorMessage);
  }

  private void displayWidget(final String errorMessage) {
    setTitle("Bearer Token Login");
    setHeaderText("Please enter your username and password");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField usernameField = new TextField();
    usernameField.setPromptText("Username");
    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Password");

    createDialogContent(grid, usernameField, passwordField);
    displayPotentialErrorMessage(errorMessage, grid);

    ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);

    getDialogPane().getButtonTypes().addAll(loginButtonType);

    // Let's disable the login button until we have a username and password woo!!
    Button loginButton = (Button) getDialogPane().lookupButton(loginButtonType);
    loginButton.setDisable(true);

    addTextFieldValidationListeners(usernameField, passwordField, loginButton);

    setResultConverter(buttonType -> new Pair<>(usernameField.getText(), passwordField.getText()));
  }

  private void displayPotentialErrorMessage(final String errorMessage, final GridPane gridPane) {
    if (StringUtils.isBlank(errorMessage)) {
      return;
    }
    Label errorMessageLabel = new Label();
    if (StringUtils.isNotBlank(errorMessage)) {
      errorMessageLabel.setText(errorMessage);
      errorMessageLabel.setStyle("-fx-text-fill: red;");
    }
    gridPane.add(errorMessageLabel, 0, 2, 2, 1);
  }

  private void createDialogContent(
      final GridPane grid, final TextField usernameField, final PasswordField passwordField) {
    grid.add(new Label("Username:"), 0, 0);
    grid.add(usernameField, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(passwordField, 1, 1);

    getDialogPane().setContent(grid);
  }

  private void addTextFieldValidationListeners(
      final TextField usernameField, final PasswordField passwordField, final Button loginButton) {
    addUserNameListener(usernameField, passwordField, loginButton);
    addPasswordListener(passwordField, usernameField, loginButton);
  }

  private void addUserNameListener(
      final TextField usernameField, final PasswordField passwordField, final Button loginButton) {
    usernameField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                loginButton.setDisable(
                    !areInputsValid(usernameField.getText(), passwordField.getText())));
  }

  private void addPasswordListener(
      final PasswordField passwordField, final TextField usernameField, final Button loginButton) {
    passwordField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                loginButton.setDisable(
                    !areInputsValid(usernameField.getText(), passwordField.getText())));
  }

  /** Both fields must be non-empty for the login button to be enabled. */
  private boolean areInputsValid(final String first, final String second) {
    return StringUtils.isNotBlank(first) && StringUtils.isNotBlank(second);
  }
}
