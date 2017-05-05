package example.spark;

import com.google.gson.Gson;
import spark.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class App {

    final int port;
    final AtomicLong atomic = new AtomicLong();
    final Gson gson = new Gson();
    final Storage storage;

    public App(Storage storage) {
        this.port = 8888;
        this.storage = storage;
    }

    public App(AppBuilder builder) {
        this.port = builder.port;
        this.storage = builder.storage;
    }

    public static AppBuilder build() {
        return new AppBuilder();
    }

    public static void main(String... args) {

        Storage storage = new InMemoryStorage(new ConcurrentLinkedQueue<>());
        App app = new App(storage);
        app.run();
    }

    public void run() {
        Service http = Service.ignite();
        http.port(this.port);
        http.staticFiles.externalLocation("/home/kubam/workspaces/slides/java9-jshell/ui");

        http.get("/api/todos", (req, resp) -> storage.getAll(), gson::toJson);

        http.delete("/api/todos/:id", (req, resp) -> {
            long id = Long.parseLong(req.params(":id"));
            boolean removed = storage.delete(id);
            if (removed) http.halt(204);
            else http.halt(404);
            return null;
        });

        http.post("/api/todos", (req, resp) -> {
            Todo data = Todo.withNewId(atomic.getAndIncrement(), new Gson().fromJson(req.body(), Todo.class));

            boolean saved = storage.save(data);
            if (saved) resp.redirect("/api/todos/" + data.id, 201);
            else http.halt(500);
            return null;
        });
    }


    public static class AppBuilder {
        int port = 8888;
        Storage storage;

        public AppBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public AppBuilder withStorage(Storage storage) {
            this.storage = storage;
            return this;
        }

        public App build() {
            return new App(this);
        }
    }

}
