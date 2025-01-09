package com.teamtreehouse.techdegrees.dao;

import java.util.List;
import com.teamtreehouse.techdegrees.Todo;


public interface TodoDAO {
    List<Todo> findAll(); // Fetch all todos
    Todo findById(int id); // Fetch a single todo by ID
    void create(Todo todo); // Create a new todo
    void update(Todo todo); // Update an existing todo
    void delete(int id); // Delete a todo by ID
    void createTodosTable(); // Create the todos table
}
