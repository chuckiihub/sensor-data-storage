package com.matias.api.responses;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class HttpResponse {
  public Integer statusCode;
  public String content;

  public HttpResponse(Integer statusCode, JsonObject content) {
    this.statusCode = statusCode;
    this.content = content.encode();
  }

  public HttpResponse(Integer statusCode, JsonArray content) {
    this.statusCode = statusCode;
    this.content = content.encode();
  }

  public HttpResponse(Integer statusCode, String content) {
    this.statusCode = statusCode;
    this.content = content;
  }


  public JsonObject toJson() {
    return new JsonObject()
      .put("statusCode", this.statusCode)
      .put("content", this.content);
  }

  public static HttpResponse fromJson(JsonObject json) {
    return new HttpResponse(
      json.getInteger("statusCode", 0),
      json.getString("content", new JsonObject().encode())
    );
  }
}
