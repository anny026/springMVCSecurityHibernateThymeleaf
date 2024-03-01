package org.helpdesk.models;

public enum Urgency {

    CRITICAL (1, "Critical"),
    HIGH (2, "High"),
    AVERAGE (3, "Average"),
    LOW (4, "Low");

    private String name;
    private int id;
    Urgency(int id, String name) {
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
