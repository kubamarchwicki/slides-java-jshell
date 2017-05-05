package example.spark;

public class Todo {
   public final long id;
   public final String title;
   public final long order;
   public final boolean completed;

   private Todo(long id, String title, long order, boolean completed) {
       this.id = id;
       this.title = title;
       this.order = order;
       this.completed = completed;
   }

   public static Todo create(long id, String title, long order, boolean completed) {
     return new Todo(id, title, order, completed);
   }

   public static Todo withNewId(long id, Todo t) {
     return new Todo(id, t.title, t.order, t.completed);
   }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", completed=" + completed +
                '}';
    }

}
