package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import java.util.List;
import java.util.Map;

public class Sql2oTodoDAO implements TodoDAO {

    private final Sql2o sql2o;

    public Sql2oTodoDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
        createTodosTable();
    }

    private void createTodosTable() {
        String sql = "CREATE TABLE IF NOT EXISTS todos (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "ISCOMPLETED BOOLEAN DEFAULT FALSE" +  // Keep the original column name
                ");";
        try (Connection conn = sql2o.open()) {
            // First drop the table to ensure clean state
            conn.createQuery("DROP TABLE IF EXISTS todos").executeUpdate();
            // Then create it with our schema
            conn.createQuery(sql).executeUpdate();
            System.out.println("Table created successfully");
        }
    }

    @Override
    public void create(Todo todo) {
        String sql = "INSERT INTO todos (name, is_completed) VALUES (:name, :isCompleted)";
        try (Connection conn = sql2o.open()) {
            int id = (int) conn.createQuery(sql, true)
                    .addParameter("name", todo.getName())
                    .addParameter("isCompleted", todo.getIsCompleted())
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        }
    }



    @Override
    public List<Todo> findAll() {
        try (Connection conn = sql2o.open()) {
            // First, let's check what columns actually exist
            List<Map<String, Object>> columns = conn.createQuery(
                    "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'TODOS'"
            ).executeAndFetchTable().asList();
            System.out.println("Table columns: " + columns);

            // Now try to select with the correct column name
            String sql = "SELECT id, name, \"ISCOMPLETED\" as \"isCompleted\" FROM todos";
            return conn.createQuery(sql)
                    .executeAndFetch(Todo.class);
        }
    }
    @Override
    public void update(Todo todo) {
        String sql = "UPDATE todos SET name = :name, is_Completed = :isCompleted WHERE id = :id;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id", todo.getId())
                    .addParameter("name", todo.getName())
                    .addParameter("isCompleted", todo.getIsCompleted())
                    .executeUpdate();
        }
    }

    @Override
    public Todo findById(int id) {
        String sql = "SELECT * FROM todos WHERE id = :id";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Todo.class);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM todos WHERE id = :id;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}
