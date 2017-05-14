import java.util.concurrent.atomic.*
AtomicLong atomic = new AtomicLong()
http.post("/api/todos", (req, resp) -> {
  Todo data = gson.fromJson(req.body(), Todo.class).assignId(atomic.getAndIncrement());

  storage.add(data);
  resp.redirect("/api/todos/" + data.id, 201);
  return null;
})
