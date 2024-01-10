package com.matias;

import com.matias.api.router.APIV1Router;
import com.matias.api.responses.ErrorResponse;
import com.matias.api.handlers.SensorDataVerticle;
import com.matias.api.handlers.SensorVerticle;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import java.util.ArrayList;

public class MainVerticle extends BaseVerticle {
  private APIV1Router apiRouter;
  @Override
  public Completable rxStart() {
    return Completable.fromSingle(
      this.deployVerticles()
        .doOnSuccess(s ->
          this.registerRouter()
            .doOnSuccess(globalRouter ->
              vertx.createHttpServer()
                .requestHandler(globalRouter)
                .listen(8888)
                .doOnSuccess(http -> System.out.println("[HTTP] Server successfully started on 8888"))
                .doOnError(error -> System.out.println("[HTTP] Startup failure"))
                .subscribe()
            )
            .subscribe()
        ).doOnError(s ->
          System.out.println("ERROR LOADING Verticles")
        )
    );
  }

  private Single<Router> registerRouter() {
    var globalRouter = Router.router(vertx);

    this.apiRouter = new APIV1Router(vertx, "api.yaml");

    return apiRouter.createRouter()
      .map(subRouter -> {
          globalRouter.route("/v1/api/*").subRouter(subRouter);
          globalRouter.errorHandler(400, routingContext -> {
            this.registerErrorValidationHandler(routingContext);
          });
          return globalRouter;
        }
      ).map(router -> globalRouter);
  }

  private void registerErrorValidationHandler(RoutingContext routingContext) {
    ErrorResponse response = new ErrorResponse("unknown-error",  "Unknown error");

    if (routingContext.failure() instanceof BadRequestException) {
      if (routingContext.failure() instanceof ParameterProcessorException) {
        response = new ErrorResponse((ParameterProcessorException) routingContext.failure());
      } else if (routingContext.failure() instanceof BodyProcessorException) {
        response = new ErrorResponse((BodyProcessorException) routingContext.failure());
      } else if (routingContext.failure() instanceof RequestPredicateException) {
        response = new ErrorResponse((RequestPredicateException) routingContext.failure());
      }
    }

    routingContext
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .setStatusCode(400)
      .end(response.toString());
  }

  private Single<String> deployVerticles() {
    var singles = new ArrayList<SingleSource<String>>();

    singles.add(
      vertx.rxDeployVerticle(SensorVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(1))
        .doOnSuccess(success -> System.out.println("SensorVerticle deployed."))
        .doOnError(success -> System.out.println("SensorVerticle failed to deploy."))
    );

    singles.add(
      vertx.rxDeployVerticle(SensorDataVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(1))
        .doOnSuccess(success -> System.out.println("SensorDataVerticle deployed."))
        .doOnError(success -> System.out.println("SensorDataVerticle failed to deploy."))
    );

    return Single.zip(singles, f -> "done");
  }
}
