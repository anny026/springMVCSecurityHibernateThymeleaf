package org.helpdesk.models;

public enum State {
    DRAFT (1, "Draft"),
    NEW (2, "New"),
    APPROVED (3, "Approved"),
    DECLINED (4, "Declined"),
    IN_PROGRESS (5, "In_Progress"),
    DONE (6, "Done"),
    CANCELED (7, "Canceled");

    private String name;
    private int id;

    State(int id, String name) {
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
