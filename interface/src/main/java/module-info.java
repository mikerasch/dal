module com.michael.dal {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.apache.commons.lang3;
  requires org.apache.commons.validator;
  requires com.google.guice;
  requires org.slf4j;
  requires com.fasterxml.jackson.databind;
  requires com.auth0.jwt;
  requires okhttp3;
  requires atlantafx.base;
  requires com.github.benmanes.caffeine;
  requires io.vertx.web;
  requires io.vertx.core;
  requires javafx.web;
  requires java.logging;
  requires server;
  requires annotations;
  requires utils;

  opens com.michael.dal to
      javafx.fxml;
  opens com.michael.dal.controllers.home to
      javafx.fxml;
  opens com.michael.dal.controllers.config to
      javafx.fxml;
  opens com.michael.dal.controllers.config.security to
      javafx.fxml;

  exports com.michael.dal.controllers.config;
  exports com.michael.dal.services.config;
  exports com.michael.dal.services.config.models;
  exports com.michael.dal.controllers.home;
  exports com.michael.dal.services.discovery.models;
  exports com.michael.dal.clients.models;
  exports com.michael.dal.clients;
  exports com.michael.dal.services.auth.models;
  exports com.michael.dal.services.auth;
  exports com.michael.dal.controllers.config.security;
  exports com.michael.dal.controllers.config.discovery;
  exports com.michael.dal.services.discovery;
  exports com.michael.dal.services.home;
  exports com.michael.dal.services.terminal;
  exports com.michael.dal.manager;
  exports com.michael.dal.controllers.home.tabs;

  opens com.michael.dal.controllers.home.tabs to
      javafx.fxml;
}
