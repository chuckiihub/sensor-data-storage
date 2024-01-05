package com.matias.api;

public enum SwaggerOperationId {
  SENSOR_CREATE("sensorCreate"),
  SENSOR_LIST("sensorList"),
  SENSOR_GET("sensorGet"),
  SENSOR_HEALTH("sensorHealth"),
  SENSOR_DELETE("sensorDelete"),
  SENSOR_DATA_CREATE("sensorDataCreate"),
  SENSOR_DATA_LIST("sensorDataList");

  public final String operationId;

  SwaggerOperationId(final String operationId) {
    this.operationId = operationId;
  }

  @Override
  public String toString() {
    return operationId;
  }
}
