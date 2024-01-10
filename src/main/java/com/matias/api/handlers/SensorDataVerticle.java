package com.matias.api.handlers;

import com.matias.BaseVerticle;
import com.matias.api.responses.HttpResponse;
import com.matias.api.router.EndpointOperationId;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.eventbus.Message;

public class SensorDataVerticle extends BaseVerticle {
  public Completable rxStart() {
    this.connect();

    return Single.just(true).ignoreElement();
  }

  private void connect() {
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_DATA_CREATE.operationId).handler(this::create);
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_DATA_LIST.operationId).handler(this::list);
  }

  private void create(Message<JsonObject> message) {
    message.reply(new HttpResponse(200, new JsonObject()).toJson());
  }

  private void list(Message<JsonObject> message) {
    message.reply(new HttpResponse(200, new JsonObject()).toJson());
  }
}
