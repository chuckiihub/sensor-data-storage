package com.matias.persistence.sensor;

import com.matias.dto.Sensor;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import java.util.List;

public class MongoSensorPersistence implements ISensorPersistence {
  final private Vertx vertx;
  final private JsonObject config;
  final private String collection;

  private MongoClient client;

  public MongoSensorPersistence(Vertx vertx, JsonObject config, String collection) {
    this.vertx = vertx;
    this.config = config;
    this.collection = collection;
  }

  public MongoSensorPersistence connect() {
    this.client = MongoClient.createShared(this.vertx, config);
    return this;
  }

  public Single<Sensor> create(Sensor createRequest) {
    return this.client.rxInsert(this.collection, createRequest.toJson())
      .doOnSuccess(createRequest::setId)
      .ignoreElement()
      .toSingleDefault(createRequest);
  }

  public void delete(String id) {
    this.client.rxRemoveDocument(this.collection, new JsonObject().put("_id", id)).subscribe();
  }

  public Maybe<Sensor> get(String id) {
    var query = new JsonObject().put("_id", id);

    return this.client.rxFindOne(this.collection, query, null)
      .map(Sensor::fromJson)
      .map(sensor -> {
        sensor.setId(id);
        return sensor;
      });
  }

  public Single<List<Sensor>> list() {
    return this.client.rxFind(this.collection, new JsonObject())
      .map(list -> list.stream().map(Sensor::fromJson).toList());
  }


  static public MongoSensorPersistence forNowInit(Vertx vertx) {
    return new MongoSensorPersistence(
      vertx,
      new JsonObject()
        .put("connection_string", "mongodb://localhost.noenv.com:27017/sensor_date")
        .put("useObjectId", true)
        .put("ssl", true)
        .put("authSource", "$external")
        .put("authMechanism", "MONGODB-X509")
        .put("caPath", "ca.pem")
        .put("keyPath", "peer.key.pem")
        .put("certPath", "mongo.cert.pem"),
      "sensor"
    );
  }
}
