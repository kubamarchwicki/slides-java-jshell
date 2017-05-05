import spark.*
Service ws = Service.ignite()
ws.port(35729)
ws.webSocket("/livereload", LiveReloadWebSocket.class);
ws.init()