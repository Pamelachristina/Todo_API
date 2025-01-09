package com.teamtreehouse.techdegrees;

import static spark.Spark.*;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDAO;
import com.teamtreehouse.techdegrees.dao.TodoDAO;
import com.google.gson.Gson;
import org.sql2o.Sql2o;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Static file location must be first
        staticFileLocation("/public");
        System.out.println("Static file location set");

        // Set port
        port(8081);
        System.out.println("Port set to 8081");
        Sql2o sql2o = new Sql2o("jdbc:h2:~/test", "user", "password");

        // Initialize DAO implementation
        TodoDAO todoDAO = new Sql2oTodoDAO(sql2o); // Ensure the correct implementation is used
        Gson gson = new Gson();

        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((req, res) -> res.header("Access-Control-Allow-Origin", "*"));





        // Ensure the todos table is created
        todoDAO.createTodosTable();

        // Base API path
        path("/api/v1", () -> {
            // POST /todos - Create a new todo
            post("/todos", (req, res) -> {
                res.type("application/json");
                Todo todo = gson.fromJson(req.body(), Todo.class);
                todoDAO.create(todo);
                res.status(201); // Created
                return todo;
            }, gson::toJson);

            // GET /todos - Fetch all todos
            get("/todos", (req, res) -> {
                res.type("application/json");
                List<Todo> todos = todoDAO.findAll();
                return todos;
            }, gson::toJson);

            // GET /todos/:id - Fetch a single todo by ID
            get("/todos/:id", (req, res) -> {
                res.type("application/json");
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    Todo todo = todoDAO.findById(id);
                    if (todo == null) {
                        res.status(404); // Not Found
                        return new ErrorMessage("Todo not found");
                    }
                    return todo;
                } catch (NumberFormatException e) {
                    res.status(400); // Bad Request
                    return new ErrorMessage("Invalid ID format");
                }
            }, gson::toJson);

            // PUT /todos/:id - Update an existing todo
            put("/todos/:id", (req, res) -> {
                res.type("application/json");
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    Todo existingTodo = todoDAO.findById(id);
                    if (existingTodo == null) {
                        res.status(404); // Not Found
                        return new ErrorMessage("Todo not found");
                    }
                    Todo updatedTodo = gson.fromJson(req.body(), Todo.class);
                    updatedTodo.setId(id); // Ensure ID is set
                    todoDAO.update(updatedTodo);
                    return updatedTodo;
                } catch (NumberFormatException e) {
                    res.status(400); // Bad Request
                    return new ErrorMessage("Invalid ID format");
                }
            }, gson::toJson);

            // DELETE /todos/:id - Delete a todo by ID
            delete("/todos/:id", (req, res) -> {
                res.type("application/json");
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    Todo todo = todoDAO.findById(id);
                    if (todo == null) {
                        res.status(404); // Not Found
                        return new ErrorMessage("Todo not found");
                    }
                    todoDAO.delete(id);
                    res.status(204); // No Content
                    return "";
                } catch (NumberFormatException e) {
                    res.status(400); // Bad Request
                    return new ErrorMessage("Invalid ID format");
                }
            });
        });

        // Exception Handling
        exception(Exception.class, (e, req, res) -> {
            res.type("application/json");
            res.status(500); // Internal Server Error
            res.body(gson.toJson(new ErrorMessage(e.getMessage())));
        });

        // 404 Not Found
        notFound((req, res) -> {
            res.type("application/json");
            return gson.toJson(new ErrorMessage("Route not found"));
        });

        // After-filters for consistent JSON response
        after((req, res) -> {
            res.type("application/json");
        });
    }
}
