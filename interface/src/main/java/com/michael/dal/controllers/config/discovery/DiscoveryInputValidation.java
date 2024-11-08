package com.michael.dal.controllers.config.discovery;

import com.michael.dal.utils.SequentialBooleanChecker;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.commons.validator.routines.UrlValidator;

public class DiscoveryInputValidation {
  private DiscoveryInputValidation() {}

  public static boolean validateDiscovery(
      final DiscoveryEnvironment discoveryEnvironment,
      final ObservableList<DiscoveryEnvironment> items) {
    // TODO LOOK INTO THIS COMMENT. http://dc2-svddev01:8500 IS A VALID URL BUT THE VALIDATOR
    // DOESN'T LIKE IT
    SequentialBooleanChecker sequentialBooleanChecker =
        new SequentialBooleanChecker(
            List.of(
                () -> validateNoneEmpty(discoveryEnvironment),
                // () -> validateIsUrl(discoveryEnvironment),
                () -> validateDuplicateDiscoveryEnvironment(discoveryEnvironment, items),
                () -> validateDuplicateDiscoveryUrl(discoveryEnvironment, items)));
    return sequentialBooleanChecker.check();
  }

  private static boolean validateDuplicateDiscoveryUrl(
      final DiscoveryEnvironment discoveryEnvironment,
      final ObservableList<DiscoveryEnvironment> items) {
    boolean discoveryUrlExists =
        items.stream().anyMatch(item -> item.url().equalsIgnoreCase(discoveryEnvironment.url()));
    if (discoveryUrlExists) {
      alert("Discovery URL already exists.");
    }
    return !discoveryUrlExists;
  }

  private static boolean validateDuplicateDiscoveryEnvironment(
      final DiscoveryEnvironment discoveryEnvironment,
      final ObservableList<DiscoveryEnvironment> items) {
    boolean discoveryEnvironmentExists =
        items.stream()
            .anyMatch(
                item -> item.environment().equalsIgnoreCase(discoveryEnvironment.environment()));
    if (discoveryEnvironmentExists) {
      alert("Discovery environment already exists.");
    }
    return !discoveryEnvironmentExists;
  }

  private static boolean validateIsUrl(final DiscoveryEnvironment discoveryEnvironment) {
    var validator = new UrlValidator();

    boolean isValidUrl = validator.isValid(discoveryEnvironment.url());

    if (!isValidUrl) {
      alert("Invalid URL format. Are you missing the protocol?");
    }

    return isValidUrl;
  }

  private static boolean validateNoneEmpty(final DiscoveryEnvironment discoveryEnvironment) {
    if (discoveryEnvironment.environment().isEmpty()
        || discoveryEnvironment.url().isEmpty()
        || discoveryEnvironment.serviceType().isEmpty()) {
      alert("Environment and URL must not be empty.");
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
