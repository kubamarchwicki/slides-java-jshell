http.get("/api/todos", (req, resp) -> new ArrayList<>(storage), gson::toJson)