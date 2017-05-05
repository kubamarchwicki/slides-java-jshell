package example.spark;

import java.util.List;

public interface Storage {

    List<Todo> getAll();
    boolean save(Todo todo);
    boolean delete(long id);

}
