package foo;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class SpringWeb {

    public static void main(String[] args) {

        Flux<String> colors = Flux.just("blue", "green", "orange");
        HandlerFunction<ServerResponse> helloWorld =
                request -> ServerResponse.ok().body(colors, String.class);

        RouterFunction<?> route =
                route(path("/hello-world"),
                        request -> ServerResponse.ok().body(Mono.just("Hello World"), String.class))
                        .and(route(path("/the-answer"),
                                request -> ServerResponse.ok().body(Mono.just("42"), String.class)));

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        ReactorHttpHandlerAdapter adapter =
                new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create("localhost", 8080);
        server.startAndAwait(adapter, blockingNettyContext -> {});
    }

}
