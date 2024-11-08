package com.michael.dal.widgets;

import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class TrapDialogWidget extends Dialog<Pair<String, String>> {
  public TrapDialogWidget() {
    trapUntilValidInput();
  }

  /**
   * Traps the dialog from being removed from the user. This not only prevents the X button from
   * closing the dialog, but also prevents the ESC key from closing the dialog. It's a trap!
   */
  private void trapUntilValidInput() {
    initStyle(StageStyle.UNDECORATED);
    setOnShown(
        event ->
            getDialogPane()
                .getScene()
                .addEventFilter(
                    KeyEvent.KEY_PRESSED,
                    keyEvent -> {
                      if (keyEvent.getCode() == KeyCode.ESCAPE) {
                        keyEvent.consume();
                      }
                    }));
  }
}
