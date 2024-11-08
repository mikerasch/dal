package com.michael.dal.vertx;

import com.google.inject.Singleton;
import com.michael.dal.vertx.addons.SwaggerVerticle;
import com.michael.dal.vertx.codec.CurlActivityCodec;
import com.michael.dal.vertx.models.CurlActivity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

@Singleton
public class VertxServer {
  private final Vertx vertx;

  public VertxServer() {
    this.vertx = Vertx.vertx();
    start();
    registerCodecs();
  }

  private void registerCodecs() {
    vertx.eventBus().registerDefaultCodec(CurlActivity.class, new CurlActivityCodec());
  }

  private void start() {
    AbstractVerticle swaggerVerticle = new SwaggerVerticle();
    vertx.deployVerticle(swaggerVerticle);
  }

  public Vertx getVertx() {
    return vertx;
  }
}
