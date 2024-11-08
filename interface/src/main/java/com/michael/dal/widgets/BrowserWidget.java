package com.michael.dal.widgets;

import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BrowserWidget extends Region {
  private final WebEngine webEngine;
  private final Double browserWidth;
  private final Double browserHeight;

  public BrowserWidget(final Double browserWidth, final Double browserHeight) {
    this.browserWidth = browserWidth;
    this.browserHeight = browserHeight;
    WebView webView = new WebView();
    webView.setMinHeight(browserHeight);
    webView.setMinWidth(browserWidth);
    webEngine = webView.getEngine();
    getChildren().add(webView);
  }

  public void loadURL(String url) {
    webEngine.load(url);
  }

  public Double getBrowserWidth() {
    return browserWidth;
  }

  public Double getBrowserHeight() {
    return browserHeight;
  }
}
