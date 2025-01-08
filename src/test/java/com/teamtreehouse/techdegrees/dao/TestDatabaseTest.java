package com.teamtreehouse.techdegrees.dao;

import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDatabaseTest {

    @Test
    public void testGetSql2o() {
        // Get an instance of Sql2o
        Sql2o sql2o = TestDatabase.getSql2o();

        // Try opening a connection and ensure it does not throw exceptions
        try (Connection conn = sql2o.open()) {
            assertNotNull(conn, "The connection should not be null");
        }
    }
}


