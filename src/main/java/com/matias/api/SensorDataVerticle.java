package com.matias.api;

import com.matias.BaseVerticle;
import com.matias.api.eventbus.HandlerResponse;
import io.vertx.core.json.JsonObject;

public class SensorDataVerticle extends BaseVerticle {
  private void create() {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_DATA_CREATE.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }

  private void list() {
    this.vertx.eventBus().consumer(SwaggerOperationId.SENSOR_DATA_CREATE.operationId).handler(message ->
      new HandlerResponse(200, new JsonObject().put("test", "test"))
    );
  }
}
