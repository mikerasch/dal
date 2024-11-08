module server {
  requires io.vertx.web;
  requires io.vertx.core;
  requires com.google.guice;
  requires com.fasterxml.jackson.databind;
  requires utils;

  exports com.michael.dal.vertx;
  exports com.michael.dal.vertx.models;
}
