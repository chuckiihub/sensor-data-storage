package com.matias.dto;

import io.vertx.codegen.annotations.DataObject;
import java.util.List;

@DataObject()
public class Sensor {
  public String id;
  public String name;
  public String dataType;
  public int inactiveAlertTime;
  public String belongsTo;
  public List<String> tags;
}
