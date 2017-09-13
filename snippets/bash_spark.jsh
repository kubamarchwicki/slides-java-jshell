#!/opt/java/jdk-9/bin/jshell

/set editor vim
/env --class-path /home/kubam/.m2/repository/com/sparkjava/spark-core/2.5/spark-core-2.5.jar:/home/kubam/.m2/repository/org/slf4j/slf4j-api/1.7.24/slf4j-api-1.7.24.jar:/home/kubam/.m2/repository/org/slf4j/slf4j-simple/1.7.24/slf4j-simple-1.7.24.jar:/home/kubam/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/home/kubam/.m2/repository/org/eclipse/jetty/aggregate/jetty-all/9.3.6.v20151106/jetty-all-9.3.6.v20151106-uber.jar:/home/kubam/.m2/repository/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar

import spark.*;
Service http = Service.ignite();
http.port(8888)
http.staticFiles.externalLocation("/home/kubam/workspaces/slides/java9-jshell/ui")
http.init()

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

Queue<Todo> storage = new ConcurrentLinkedQueue<>()

import com.google.gson.*
Gson gson = new Gson()


http.get("/api/todos", (req, resp) -> new ArrayList<>(storage), gson::toJson)
http.delete("/api/todos/:id", (req, resp) -> {
    long id = Long.parseLong(req.params(":id"));
    boolean removed = storage.removeIf(t -> t.id == id);
    if (removed) http.halt(204);
    else http.halt(404);
    return null;
})


import java.util.concurrent.atomic.*
AtomicLong atomic = new AtomicLong()
http.post("/api/todos", (req, resp) -> {
  Todo data = gson.fromJson(req.body(), Todo.class).assignId(atomic.getAndIncrement());

  storage.add(data);
  resp.redirect("/api/todos/" + data.id, 201);
  return null;
})
