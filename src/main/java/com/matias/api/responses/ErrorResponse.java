package com.matias.api.responses;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;

public class ErrorResponse extends HttpResponse {
  public String content;

  public ErrorResponse(Throwable internalError) {
    super(500, new JsonObject().put("type", "unknown-error").put("message", internalError.getMessage()));
  }

  public ErrorResponse(BodyProcessorException error) {
    super(
      400,
      new JsonObject()
        .put("type", "invalid-input")
        .put("message", error.getMessage()).encode()
    );
  }

  public ErrorResponse(ParameterProcessorException error) {
    super(
      400,
      new JsonObject()
        .put("type", "invalid-input")
        .put("message", error.getMessage()).encode()
    );
  }

  public ErrorResponse(RequestPredicateException error) {
    super(
      400,
      new JsonObject()
        .put("type", "invalid-input")
        .put("message", error.getMessage()).encode()
    );
  }

  public ErrorResponse(String type, String message) {
    super(
    400,
      new JsonObject()
        .put("type", type)
        .put("message", message
    ));
  }

  public ErrorResponse(Integer statusCode, String type, String message) {
    super(
      statusCode,
      new JsonObject()
        .put("type", "invalid-input")
        .put("message", message));
  }

  public String toString() {
    return this.content;
  }
}
