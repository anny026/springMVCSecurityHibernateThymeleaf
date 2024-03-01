package org.helpdesk.models;

public enum Role {
    ROLE_EMPLOYEE (1, "Employee"),
    ROLE_MANAGER (2, "Manager"),
    ROLE_ENGINEER (3, "Engineer");

    private int id;
    private String name;

    Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
