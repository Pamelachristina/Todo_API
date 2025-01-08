package com.teamtreehouse.techdegrees.dao;

import java.util.List;
import com.teamtreehouse.techdegrees.Todo;


public interface TodoDAO {
    /**
     * Create a new Todo item.
     *
     * @param todo the Todo item to create
     */
    void create(Todo todo);

    /**
     * Retrieve all Todo items.
     *
     * @return a list of all Todo items
     */
    List<Todo> findAll();

    /**
     * Update an existing Todo item.
     *
     * @param todo the Todo item with updated values
     */
    void update(Todo todo);

    /**
     * Delete a Todo item by its ID.
     *
     * @param id the ID of the Todo item to delete
     */
    void delete(int id);

    /**
     * Find a Todo item by its ID.
     *
     * @param id the ID of the Todo item to find
     * @return the Todo item, or null if not found
     */

     Todo findById(int id);
}

