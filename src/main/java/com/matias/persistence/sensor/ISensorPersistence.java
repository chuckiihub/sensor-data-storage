package com.matias.persistence.sensor;

import com.matias.dto.Sensor;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.ext.mongo.MongoClient;

import java.util.List;

public interface ISensorPersistence {
  Single<Sensor> create(Sensor createRequest);
  void delete(String id);
  Maybe<Sensor> get(String id);
  Single<List<Sensor>> list();
}
