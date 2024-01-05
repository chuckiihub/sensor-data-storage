package com.matias.api.eventbus;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject()
public class HandlerResponse {
  public Integer statusCode;
  public JsonObject content;

  public HandlerResponse(Integer statusCode, JsonObject content) {
    this.statusCode = statusCode;
    this.content = content;
  }
}
