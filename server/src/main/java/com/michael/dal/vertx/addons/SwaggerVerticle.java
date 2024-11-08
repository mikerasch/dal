package com.michael.dal.vertx.addons;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class SwaggerVerticle extends AbstractVerticle {
  @Override
  public void start() {
    startVerticle();
  }

  private void startVerticle() {
    Router router = Router.router(vertx);
    HttpServer server = vertx.createHttpServer().requestHandler(router);

    routeSwagger(router);
    routeCopyToClipboard(router);

    server.listen(8080);
  }

  private void routeCopyToClipboard(final Router router) {
    router.route().handler(BodyHandler.create());
    router
        .route("/swagger/curl/copy-to-clipboard")
        .handler(
            ctx -> {
              String body = ctx.body().asString();
              vertx.eventBus().publish("curl", body);
              ctx.response().end();
            });
  }

  private void routeSwagger(Router router) {
    router.route("/swagger").handler(StaticHandler.create("index.html"));
    router.route("/swagger-ui").handler(StaticHandler.create("swagger-ui.css"));
    router.route("/swagger-ui-bundle.js").handler(StaticHandler.create("swagger-ui-bundle.js"));
    router
        .route("/swagger-ui-standalone-preset.js")
        .handler(StaticHandler.create("swagger-ui-standalone-preset.js"));
    router.route("/swagger-ui.css").handler(StaticHandler.create("swagger-ui.css"));
  }
}
