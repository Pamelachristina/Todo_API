package com.teamtreehouse.techdegrees;


import com.google.gson.annotations.SerializedName;

public class Todo {
    private int id;
    private String name;
    @SerializedName("is_completed")
    private boolean isCompleted;


    // Constructors
    public Todo() {}
    public Todo(int id, String name, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}

