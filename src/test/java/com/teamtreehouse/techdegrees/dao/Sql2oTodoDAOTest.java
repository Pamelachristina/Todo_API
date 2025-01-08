package com.teamtreehouse.techdegrees.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import com.teamtreehouse.techdegrees.Todo;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDAO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Sql2oTodoDAOTest {
    private Sql2oTodoDAO dao;

    @BeforeEach
    public void setUp() {
        Sql2o sql2o = TestDatabase.getSql2o();
        dao = new Sql2oTodoDAO(sql2o);
    }

    @Test
    public void addingTodoSetsId() {
        Todo todo = new Todo(0, "Test Todo", false);
        dao.create(todo);

        List<Todo> todos = dao.findAll();
        assertEquals(1, todos.size());
        assertEquals(1, todos.get(0).getId());
    }

    @Test
    public void findAllReturnsAllTodos() {
        Todo todo1 = new Todo(0, "Todo 1", false);
        Todo todo2 = new Todo(0, "Todo 2", true);

        dao.create(todo1);
        dao.create(todo2);

        List<Todo> todos = dao.findAll();
        assertEquals(2, todos.size());
    }

    @Test
    public void findAllReturnsEmptyListWhenNoTodos() {
        List<Todo> todos = dao.findAll();
        assertTrue(todos.isEmpty());
    }

    @Test
    public void updateTodoChangesItsProperties() {
        Todo todo = new Todo(0, "Initial Todo", false);
        dao.create(todo);

        todo.setName("Updated Todo");
        todo.setCompleted(true);
        dao.update(todo);

        Todo updatedTodo = dao.findAll().get(0);
        assertEquals("Updated Todo", updatedTodo.getName());
        assertTrue(updatedTodo.isCompleted());
    }

    @Test
    public void deleteTodoRemovesIt() {
        Todo todo = new Todo(0, "Delete Me", false);
        dao.create(todo);

        assertEquals(1, dao.findAll().size());

        dao.delete(todo.getId());
        assertTrue(dao.findAll().isEmpty());
    }
}
