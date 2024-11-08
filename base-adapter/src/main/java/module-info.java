module base.adapter {
  requires com.google.guice;
  requires com.michael.dal;
  requires com.fasterxml.jackson.databind;
  requires org.slf4j;
  requires okhttp3;
  requires com.auth0.jwt;
  requires org.apache.commons.lang3;
  requires com.github.benmanes.caffeine;
  requires server;
  requires io.vertx.core;
  requires annotations;
  requires javafx.graphics;
  requires database;
  requires atlantafx.base;
  requires javafx.fxml;
  requires utils;
  requires org.jooq;
  requires java.sql;

  exports com.michael.dal.base.adapter to
      javafx.graphics;
  exports com.michael.dal.base.adapter.services.config.impl to
      com.google.guice;
  exports com.michael.dal.base.adapter.providers to
      com.google.guice;
  exports com.michael.dal.base.adapter.services.auth.impl to
      com.google.guice;
  exports com.michael.dal.base.adapter.clients.impl to
      com.google.guice;
  exports com.michael.dal.base.adapter.services.auth.factory to
      com.google.guice;
  exports com.michael.dal.base.adapter.services.discovery.impl to
      com.google.guice;
  exports com.michael.dal.base.adapter.services.home.impl to
      com.google.guice;
}
