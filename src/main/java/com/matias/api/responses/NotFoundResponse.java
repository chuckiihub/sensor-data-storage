package com.matias.api.responses;


import io.vertx.core.json.JsonObject;

public class NotFoundResponse extends ErrorResponse {
  public NotFoundResponse() {
    super(404, "not-found", new JsonObject().encode());
  }
}
