package com.matias.api.handlers;

import com.matias.BaseVerticle;
import com.matias.api.responses.ErrorResponse;
import com.matias.api.responses.HttpResponse;
import com.matias.api.responses.NotFoundResponse;
import com.matias.api.router.EndpointOperationId;
import com.matias.dto.Sensor;
import com.matias.persistence.sensor.MongoSensorPersistence;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.eventbus.Message;

public class SensorVerticle extends BaseVerticle {
  MongoSensorPersistence persistence;

  @Override
  public Completable rxStart() {
    this.persistence = MongoSensorPersistence.forNowInit(this.vertx).connect();
    this.connect();

    return Completable.fromSingle(Single.just("ASDASD"));
  }

  private void connect() {
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_CREATE.operationId).handler(this::create);
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_HEALTH.operationId).handler(this::health);
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_GET.operationId).handler(this::get);
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_LIST.operationId).handler(this::list);
    this.vertx.eventBus().<JsonObject>consumer(EndpointOperationId.SENSOR_DELETE.operationId).handler(this::delete);
  }

  private void create(Message<JsonObject> message) {
    var sensor = Sensor.fromHandlerRequest(HandlerRequest.fromJson(message.body()));

    this.persistence.create(sensor)
      .map(savedSensor -> sensor.toJson())
      .doOnSuccess(savedSensor -> message.reply(new HttpResponse(200, savedSensor).toJson()))
      .doOnError(error -> message.reply(new ErrorResponse(error).toJson()))
      .subscribe();
  }

  private void get(Message<JsonObject> message) {
    var request = HandlerRequest.fromJson(message.body());

    this.persistence.get(request.pathParams().get("id"))
      .map(sensor -> sensor.toJson())
      .defaultIfEmpty(new JsonObject())
      .doOnSuccess(savedSensor -> {
        if (savedSensor.getString("id") == null) {
          message.reply(new NotFoundResponse().toJson());
        } else {
          message.reply(new HttpResponse(200, savedSensor).toJson());
        }
      })
      .doOnError(error -> message.reply(new ErrorResponse(error).toJson()))
      .subscribe();
  }

  private void list(Message<JsonObject> message) {
    this.persistence.list()
      .map(list -> list.stream().map(s -> s.toJson())
        .collect(JsonArray::new, (result, entry) -> result.add(entry), JsonArray::addAll))
      .doOnSuccess(list -> message.reply(new HttpResponse(200, list).toJson()))
      .doOnError(error -> message.reply(new ErrorResponse(error).toJson()))
      .subscribe();
  }

  private void delete(Message<JsonObject> message) {
    var id = HandlerRequest.fromJson(message.body()).pathParams().get("id");

    this.persistence.delete(id);

    message.reply(new HttpResponse(204, new JsonObject()).toJson());
  }

  private void health(Message<JsonObject> message) {
    message.reply(new HttpResponse(200, new JsonObject()).toJson());
  }
}
