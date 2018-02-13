package determinista;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class DeterministES extends AbstractVerticle {

    private static API api;
    public static void main(String[] args) {
       api = new API();

        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily("Funciona"))
        );
        router.route("/reglas").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(api.getAllRules())));

        router.route("/hechos").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(api.getAllHechos())));

        router.post("/addRegla").handler(DeterministES::addRegla);
        router.post("/addHecho").handler(DeterministES::addHecho);

        router.route("/indices").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(api.getIndex())));

        vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);
    }

    private static void addHecho(RoutingContext routingContext) {
        api.addHecho(routingContext.getBodyAsString());
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(api.getAllHechos()));
    }

    private static void addRegla(RoutingContext routingContext) {
        if(api.addRegla(routingContext.getBodyAsString()))
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(api.getAllRules()));
        else
            routingContext.response()
                    .setStatusCode(404)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end("error");

    }


}