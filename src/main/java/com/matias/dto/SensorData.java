package com.matias.dto;

import io.vertx.codegen.annotations.DataObject;
import java.util.Date;

@DataObject()
public class SensorData {
  public String sensorId;
  public String value;
  public Date createdAt;
}
