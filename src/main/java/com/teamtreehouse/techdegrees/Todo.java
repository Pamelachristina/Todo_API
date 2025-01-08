package com.teamtreehouse.techdegrees;

import com.google.gson.annotations.SerializedName;

public class Todo {
    private int id;
    private String name;

    @SerializedName("is_completed") // Match the database column name
    private boolean isCompleted;  // Renamed to match database and Java conventions

    // Default no-args constructor needed by Gson
    public Todo() {
    }

    // Constructor
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

    // Use `getIsCompleted` to follow Java naming conventions and align with Gson serialization
    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
