package example.spark;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class LiveReloadWebSocket {

    private static final Protocol protocol = new Protocol(new Gson());
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private static final Logger log = LoggerFactory.getLogger(LiveReloadWebSocket.class);

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        log.info("Received message={}", message);
        if (protocol.isHello(message)) {
            String msg = protocol.hello();
            session.getRemote().sendString(msg);
            log.info("Sent: {}", msg);
        }
    }

    public static void broadcast() throws IOException {
        for (Session s: sessions) {
            s.getRemote().sendString(protocol.notification());
        }
    }

    static class Protocol {

        private final Gson gson;

        Protocol(Gson gson) {
            this.gson = gson;
        }

        public String notification() {
            Map<String, Object> obj = new HashMap<>();
            obj.put("command","reload");
            obj.put("path", "index.html");
            obj.put("liveCSS", true);

            return gson.toJson(obj);
        }

        public String hello() {
            Map<String, Object> map = new HashMap<>();
            map.put("command", "hello");
            map.put("protocols", Arrays.asList("http://livereload.com/protocols/official-7"));
            map.put("serverName", "spark");
            return gson.toJson(map);
        }

        public boolean isHello(String data) {
            Map<String, String> map = gson.fromJson(data, Map.class);
            return "hello".equals(map.get("command"));
        }

    }

}
