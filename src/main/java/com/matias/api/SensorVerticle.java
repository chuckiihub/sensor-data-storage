package com.matias.api;

import com.matias.BaseVerticle;
import com.matias.api.eventbus.HandlerRequest;
import com.matias.api.eventbus.HandlerResponse;
import io.vertx.core.json.JsonObject;

public class SensorVerticle extends BaseVerticle {
  private void create() {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_CREATE.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }

  private void get() {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_GET.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }

  private void list(HandlerRequest request) {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_LIST.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }

  private void delete() {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_DELETE.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }

  private void health() {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_HEALTH.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }
}
