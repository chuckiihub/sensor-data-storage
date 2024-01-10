package com.matias.api.handlers;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

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

  public static HandlerRequest fromJson(JsonObject json) {
    var pathParams = new HashMap<String, String>();
    var pathParamsObject = json.getJsonObject("path", new JsonObject());

    pathParamsObject.fieldNames().stream().forEach(
      field -> {
        pathParams.put(field, pathParamsObject.getString(field, ""));
      }
    );

    return new HandlerRequest(pathParams, json.getJsonObject("body", new JsonObject()));
  }
}
