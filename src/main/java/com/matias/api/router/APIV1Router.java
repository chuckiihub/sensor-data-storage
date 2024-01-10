package com.matias.api.router;

import com.matias.api.handlers.HandlerRequest;
import com.matias.api.responses.HttpResponse;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.rxjava3.impl.AsyncResultSingle;

public class APIV1Router {
  private final Vertx vertx;
  private final String openAPIDefinitionPath;

  public APIV1Router(
    Vertx vertx,
    String openAPIDefinitionPath
  ) {
    this.vertx = vertx;
    this.openAPIDefinitionPath = openAPIDefinitionPath;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.rxjava3.ext.web.Router> createRouter() {
    return AsyncResultSingle.toSingle(registerOperations(), io.vertx.rxjava3.ext.web.Router::newInstance);
  }

  public Future<Router> registerOperations() {
    return RouterBuilder.create(vertx.getDelegate(), this.openAPIDefinitionPath)
      .map(builder -> {
          builder.operations().forEach(operation -> {
              System.out.printf("Operation registered for %s%n", operation.getOperationId());
              registerRouterOperation(builder, operation.getOperationId());
            }
          );

          return builder.createRouter();
        });
  }

  private void registerRouterOperation(RouterBuilder routerBuilder, String operation) {
    System.out.printf("[ROUTER] registering operation %s%n", operation);

    routerBuilder.operation(operation)
      .handler(routingContext -> {
        var handlerRequest = new HandlerRequest(routingContext.pathParams(), routingContext.body().asJsonObject());

        System.out.printf("[REQUEST] requesting operation %s%n", operation);

        vertx.eventBus().<JsonObject>request(operation, handlerRequest.toJson())
          .doOnSuccess(promised -> {
            var response = HttpResponse.fromJson(promised.body());
            this.successResponse(routingContext, response);
          })
          .doOnError(error -> this.failureResponse(routingContext))
          .subscribe();
      })
    ;
  }

  private void successResponse(RoutingContext context, HttpResponse handlerResponse) {
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .setStatusCode(handlerResponse.statusCode)
      .end(handlerResponse.content);
  }

  private void failureResponse(RoutingContext context) {
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .setStatusCode(500)
      .end("what an error");
  }
}
