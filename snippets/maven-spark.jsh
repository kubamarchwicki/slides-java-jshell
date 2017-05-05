import example.spark.*;
Queue<Todo> store = new ConcurrentLinkedQueue<>();
App app = App.build().withPort(4567).withStorage(new InMemoryStorage(store)).build();
app.run()
store.add(Todo.create(2, "Another todo", 2,true))
Todo.create(1, "Sample todo", 0, false)
store.add($6)
store