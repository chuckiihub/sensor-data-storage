package com.matias.dto;

import io.vertx.core.json.JsonObject;
import java.util.Date;

public class SensorData {
  public String sensorId;
  public String value;
  public Date createdAt;

  public JsonObject toJson() {
    return new JsonObject();
  }
}
