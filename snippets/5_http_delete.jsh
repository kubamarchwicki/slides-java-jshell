http.delete("/api/todos/:id", (req, resp) -> {
    long id = Long.parseLong(req.params(":id"));
    boolean removed = storage.removeIf(t -> t.id == id);
    if (removed) http.halt(204);
    else http.halt(404);
    return null;
})