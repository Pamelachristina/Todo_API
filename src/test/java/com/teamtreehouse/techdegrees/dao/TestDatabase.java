package com.teamtreehouse.techdegrees.dao;

import org.sql2o.Sql2o;

public class TestDatabase {

    public static Sql2o getSql2o() {
        // Connection string to H2 in-memory database
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT FROM 'classpath:db/init.sql'";

        // Return a new Sql2o instance
        return new Sql2o(connectionString, "", "");
    }
}

