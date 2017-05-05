package example.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InMemoryStorage implements Storage {

    private final Queue<Todo> store;

    public InMemoryStorage(Queue<Todo> store) {
        this.store = store;
    }

    @Override
    public List<Todo> getAll() {
        return new ArrayList<>(store);
    }

    @Override
    public boolean save(Todo todo) {
        return store.add(todo);
    }

    @Override
    public boolean delete(long id) {
        return store.removeIf(t -> t.id == id);
    }
}
