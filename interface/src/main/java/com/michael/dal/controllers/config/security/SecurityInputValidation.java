package com.michael.dal.controllers.config.security;

import com.michael.dal.utils.SequentialBooleanChecker;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class SecurityInputValidation {
  private SecurityInputValidation() {}

  public static boolean validateSecurity(
      final SecurityEnvironment securityEnvironment,
      final ObservableList<SecurityEnvironment> items) {
    SequentialBooleanChecker sequentialBooleanChecker =
        new SequentialBooleanChecker(
            List.of(
                () -> validateNoneEmpty(securityEnvironment),
                () -> validateIsUrl(securityEnvironment),
                () -> validateDuplicateSecurityEnvironment(securityEnvironment, items),
                () -> validateDuplicateSecurityUrl(securityEnvironment, items)));
    return sequentialBooleanChecker.check();
  }

  private static boolean validateDuplicateSecurityUrl(
      final SecurityEnvironment securityEnvironment,
      final ObservableList<SecurityEnvironment> items) {
    boolean securityUrlExists =
        items.stream()
            .anyMatch(item -> StringUtils.equalsIgnoreCase(item.url(), securityEnvironment.url()));
    if (securityUrlExists) {
      alert("Security URL already exists.");
    }
    return !securityUrlExists;
  }

  private static boolean validateDuplicateSecurityEnvironment(
      final SecurityEnvironment securityEnvironment,
      final ObservableList<SecurityEnvironment> items) {
    boolean securityEnvironmentExists =
        items.stream()
            .anyMatch(
                item ->
                    StringUtils.equalsIgnoreCase(
                        item.environment(), securityEnvironment.environment()));
    if (securityEnvironmentExists) {
      alert("Security environment already exists.");
    }
    return !securityEnvironmentExists;
  }

  private static boolean validateIsUrl(final SecurityEnvironment securityEnvironment) {
    var validator = new UrlValidator();

    boolean isValidUrl = validator.isValid(securityEnvironment.url());
    if (!isValidUrl) {
      alert("Invalid URL format. Are you missing the protocol?");
    }
    return isValidUrl;
  }

  private static boolean validateNoneEmpty(final SecurityEnvironment securityEnvironment) {
    if (StringUtils.isAnyEmpty(securityEnvironment.environment(), securityEnvironment.url())) {
      alert("Both security environment and URL must be provided.");
      return false;
    }
    return true;
  }

  private static void alert(final String contextText) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Input Error");
    alert.setHeaderText("Invalid Input");
    alert.setContentText(contextText);
    alert.showAndWait();
  }
}
