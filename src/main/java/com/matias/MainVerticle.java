package com.matias;

import com.matias.api.APIV1Router;
import com.matias.api.SensorDataVerticle;
import com.matias.api.SensorVerticle;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class MainVerticle extends BaseVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    var globalRouter = Router.router(vertx);
    var apiRouter = new APIV1Router("api.yaml");

    apiRouter.registerOperations(vertx)
      .onFailure(error -> startPromise.fail(error))
      .onSuccess(router -> {
        this.deployVerticles();
        globalRouter.route("/v1/api/*").subRouter(router);
        vertx.createHttpServer().requestHandler(globalRouter).listen(8888, http -> {
          if (http.succeeded()) {
            startPromise.complete();
            System.out.println("HTTP server started on port 8888");
          } else {
            startPromise.fail(http.cause());
          }
        });
      });
  }

  public void deployVerticles() {
    vertx.deployVerticle(SensorVerticle.class, new DeploymentOptions().setInstances(1))
      .onSuccess(success -> System.out.println("SensorVerticle deployed."))
      .onFailure(success -> System.out.println("SensorVerticle failed to deploy."));
    vertx.deployVerticle(SensorDataVerticle.class, new DeploymentOptions().setInstances(1))
      .onSuccess(success -> System.out.println("SensorDataVerticle deployed."))
      .onFailure(success -> System.out.println("SensorDataVerticle failed to deploy."));
  }
}
