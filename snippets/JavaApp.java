import spark.*;
import com.google.gson.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

class Todo{
   public long id;
   public String title;
   public long order;
   public boolean completed;

   public Todo(long id, String title, long order, boolean completed) {
       this.id = id;
       this.title = title;
       this.order = order;
       this.completed = completed;
   }

   public Todo assignId(long id) {
      return new Todo(id, this.title, this.order, this.completed);
   }

    public String toString() {
      return String.format("Todo{id=%d, title=%s, order=%d, completed=%b}", id, title, order, completed);
    }
 }

public class JavaApp {

  public Queue<Todo> storage = new ConcurrentLinkedQueue<>();

  public static void main(String... args) {
    new JavaApp().run();
  }

  public void run() {
      Service http = Service.ignite();
      http.port(8888);
      http.staticFiles.externalLocation("/home/kubam/workspaces/slides/java9-jshell/ui");
      http.init();

      Gson gson = new Gson();

      http.get("/api/todos", (req, resp) -> new ArrayList<>(storage), gson::toJson);
      http.delete("/api/todos/:id", (req, resp) -> {
          long id = Long.parseLong(req.params(":id"));
          boolean removed = storage.removeIf(t -> t.id == id);
          if (removed) http.halt(204);
          else http.halt(404);
          return null;
      });

      AtomicLong atomic = new AtomicLong();
      http.post("/api/todos", (req, resp) -> {
        Todo data = gson.fromJson(req.body(), Todo.class).assignId(atomic.getAndIncrement());

        storage.add(data);
        resp.redirect("/api/todos/" + data.id, 201);
        return null;
      });

  }

}
