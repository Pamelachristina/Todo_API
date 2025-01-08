package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDAO;
import com.teamtreehouse.techdegrees.dao.TodoDAO;
import com.teamtreehouse.techdegrees.Todo;
import org.sql2o.Sql2o;

import java.util.List;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");

            // Static file location must be first
            staticFileLocation("/public");
            System.out.println("Static file location set");

            // Set port
            port(8081);
            System.out.println("Port set to 8081");

            // Configure CORS
            before((request, response) -> {
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Content-Length, Accept, Origin");
            });


            options("/*", (request, response) -> {
                String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                if (accessControlRequestHeaders != null) {
                    response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                }

                String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                if (accessControlRequestMethod != null) {
                    response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                }

                return "OK";
            });

            // Initialize SQL2o and DAO
            TodoDAO todoDAO;
            Sql2o sql2o;

            try {
                System.out.println("Initializing database connection...");
                sql2o = new Sql2o("jdbc:h2:mem:todoapp;INIT=RUNSCRIPT FROM 'classpath:db/init.sql'", "", "");
                todoDAO = new Sql2oTodoDAO(sql2o);
                System.out.println("Database connection established");
            } catch (Exception e) {
                System.err.println("Database initialization failed: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            // Initialize Gson for JSON handling
            Gson gson = new Gson();

            // API Routes
            path("/api/v1", () -> {
                get("/todos", (req, res) -> {
                    try {
                        res.type("application/json");
                        List<Todo> todos = todoDAO.findAll();
                        System.out.println("Found todos: " + todos);
                        return todos;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error getting todos: " + e.getMessage());
                        throw e;
                    }
                }, gson::toJson);

                get("/todos/:id", (req, res) -> {
                    int id = Integer.parseInt(req.params("id"));
                    Todo todo = todoDAO.findById(id);
                    if (todo == null) {
                        res.status(404);
                        return new ErrorResponse("Todo not found");
                    }
                    res.type("application/json");
                    return todo;
                }, gson::toJson);

                post("/todos", (req, res) -> {
                    try {
                        System.out.println("Received POST request with body: " + req.body());
                        Todo todo = gson.fromJson(req.body(), Todo.class);
                        System.out.println("Created Todo object: " + todo);
                        todoDAO.create(todo);
                        res.status(201);
                        res.type("application/json");
                        return todo;
                    } catch (Exception e) {
                        e.printStackTrace();
                        String errorMessage = "Error creating todo: " + e.getMessage() +
                                "\nCause: " + (e.getCause() != null ? e.getCause().getMessage() : "unknown");
                        System.err.println(errorMessage);
                        throw e;
                    }
                }, gson::toJson);

                put("/todos/:id", (req, res) -> {
                    int id = Integer.parseInt(req.params("id"));
                    Todo todoToUpdate = todoDAO.findById(id);
                    if (todoToUpdate == null) {
                        res.status(404);
                        return new ErrorResponse("Todo not found");
                    }
                    Todo updatedTodo = gson.fromJson(req.body(), Todo.class);
                    updatedTodo.setId(id);
                    todoDAO.update(updatedTodo);
                    res.type("application/json");
                    return updatedTodo;
                }, gson::toJson);

                delete("/todos/:id", (req, res) -> {
                    int id = Integer.parseInt(req.params("id"));
                    Todo todoToDelete = todoDAO.findById(id);
                    if (todoToDelete == null) {
                        res.status(404);
                        return new ErrorResponse("Todo not found");
                    }
                    todoDAO.delete(id);
                    res.status(204);
                    return "";
                });
            });

            // Exception Handling
            exception(Exception.class, (e, req, res) -> {
                res.status(500);
                res.type("application/json");
                res.body(gson.toJson(new ErrorResponse("An error occurred: " + e.getMessage())));
            });

            // Not Found Handling
            notFound((req, res) -> {
                res.type("application/json");
                return gson.toJson(new ErrorResponse("Route not found"));
            });

            System.out.println("Application started successfully!");
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Error Response Helper Class
    static class ErrorResponse {
        private final String message;

        ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}