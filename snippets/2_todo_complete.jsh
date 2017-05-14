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
