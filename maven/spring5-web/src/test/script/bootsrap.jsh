Flux<String> colors =
        Flux.just("blue", "green", "orange").delayElements(Duration.ofSeconds(1))

HandlerFunction<ServerResponse> helloColors =
        request -> ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(colors, String.class)

RouterFunction<?> route = route(path("/colors"), helloColors)
