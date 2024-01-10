package com.matias.dto;

import com.matias.api.handlers.HandlerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Sensor {
  public String id;
  public String name;
  public String dataType;
  public int inactiveAlertTime;
  public List<String> tags;

  public Sensor setId(String id) {
    this.id = id;

    return this;
  }

  public JsonObject toJson() {
    var document = new JsonObject()
      .put("name", this.name)
      .put("dataType", this.dataType)
      .put("tags", new JsonArray(this.tags))
      .put("inactiveAlertTime", this.inactiveAlertTime);

    if (this.id != null) {
      document.put("id", this.id);
    }

    return document;
  }

  static public Sensor fromJson(JsonObject json) {
    var instance = new Sensor();
    var jsonTags = json.getJsonArray("tags", new JsonArray());
    var tags = new ArrayList<String>();

    for (var i=0; i<jsonTags.size();i++) {
      tags.add(jsonTags.getString(i));
    }

    instance.id = json.getString("_id", "");
    instance.name = json.getString("name", "");
    instance.dataType = json.getString("dataType", "");
    instance.tags = tags;
    instance.inactiveAlertTime = json.getInteger("inactiveAlertTime", 0);

    return instance;
  }

  static public Sensor fromHandlerRequest(HandlerRequest request) {
    var id = request.body().getString("id");
    if (request.pathParams().containsKey("id")) {
      id = request.body().getString("id");
    }

    var data = new JsonObject();
    data.mergeIn(request.body());

    if (id != null) {
      data.put("id", id);
    }

    return fromJson(request.body());
  }
}
