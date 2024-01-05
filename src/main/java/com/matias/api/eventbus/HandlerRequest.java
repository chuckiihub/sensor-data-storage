package com.matias.api.eventbus;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

@DataObject()
public record HandlerRequest(Map<String, String> pathParams, JsonObject body) {
  public JsonObject toJson() {
    var params = pathParams.keySet().stream().collect(
      JsonObject::new,
      (result, key) -> result.put(key, pathParams.get(key)),
      JsonObject::mergeIn
    );

    return new JsonObject()
      .put("body", body)
      .put("path", params);
  }
}
