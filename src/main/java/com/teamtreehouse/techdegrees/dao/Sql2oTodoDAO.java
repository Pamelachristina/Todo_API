package com.teamtreehouse.techdegrees.dao;
import com.teamtreehouse.techdegrees.Todo;
import org.sql2o.Sql2o;
import org.sql2o.Connection;
import java.util.List;




public class Sql2oTodoDAO implements TodoDAO {
    private final Sql2o sql2o;

    public Sql2oTodoDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Todo findById(int id) {
        String sql = "SELECT * FROM todos WHERE id = :id"; // SQL query to fetch a todo by ID
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id) // Bind the ID parameter
                    .executeAndFetchFirst(Todo.class); // Fetch the first result
        }
    }

    @Override
    public List<Todo> findAll() {
        String sql = "SELECT * FROM todos";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql).executeAndFetch(Todo.class);
        }
    }

    @Override
    public void create(Todo todo) {
        String sql = "INSERT INTO todos (name, isCompleted) VALUES (:name, :isCompleted)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("name", todo.getName())
                    .addParameter("isCompleted", todo.isCompleted())
                    .executeUpdate();
        }
    }

    @Override
    public void update(Todo todo) {
        String sql = "UPDATE todos SET name = :name, isCompleted = :isCompleted WHERE id = :id";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id", todo.getId())
                    .addParameter("name", todo.getName())
                    .addParameter("isCompleted", todo.isCompleted())
                    .executeUpdate();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM todos WHERE id = :id";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id).executeUpdate();
        }
    }

    @Override
    public void createTodosTable() {
        String sql = "CREATE TABLE IF NOT EXISTS todos (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "isCompleted BOOLEAN DEFAULT FALSE" +
                ");";
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS todos").executeUpdate(); // Optional: Drop existing table
            conn.createQuery(sql).executeUpdate(); // Create the table
        }
    }
}
