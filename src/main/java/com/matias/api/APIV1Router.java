package com.matias.api;

import com.matias.api.eventbus.HandlerRequest;
import com.matias.api.eventbus.HandlerResponse;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.Arrays;

public class APIV1Router {
  private String openAPIDefinitionPath;
  public APIV1Router(String openAPIDefinitionPath) {
    this.openAPIDefinitionPath = openAPIDefinitionPath;
  }

  public Future<Router> registerOperations(Vertx vertx) {
    return RouterBuilder.create(vertx, this.openAPIDefinitionPath)
      .onSuccess(routerBuilder ->
        Arrays.stream(SwaggerOperationId.values())
          .forEach(apiEndpointDefinition ->
            this.registerRouterOperation(vertx, routerBuilder, apiEndpointDefinition.operationId)
          )
      ).map(builder -> builder.createRouter());
  }

  private void registerRouterOperation(Vertx vertx, RouterBuilder routerBuilder, String operation) {
    routerBuilder.operation(operation).handler(routingContext -> {
      var handlerRequest = new HandlerRequest(routingContext.pathParams(), routingContext.body().asJsonObject());

      vertx.eventBus().<HandlerResponse>request(operation, handlerRequest.toJson())
        .onComplete(promised -> {
          if (promised.succeeded()) {
            this.successResponse(routingContext, promised.result().body());
          } else {
            this.failureResponse(routingContext, promised.result().body());
          }
        });

    });
  }

  private void successResponse(RoutingContext context, HandlerResponse handlerResponse) {
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .setStatusCode(handlerResponse.statusCode)
      .end(Json.encodeToBuffer(handlerResponse.content));
  }

  private void failureResponse(RoutingContext context, HandlerResponse handlerResponse) {
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .setStatusCode(400)
      .end(Json.encodeToBuffer("what an error"));
  }
}
