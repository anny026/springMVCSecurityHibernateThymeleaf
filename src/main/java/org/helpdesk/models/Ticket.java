package org.helpdesk.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max= 100, message = "Name should be 2-30 characters")
    @Column(name = "name_ticket")
    private String name_ticket;

    @Column(name = "description")
    private String description;
    @Column(name = "created_on")
    private java.util.Date created_on;
    @Column(name = "desired_resolution_date")
    private Date desired_resolution_date;
    @Column(name = "assignee_id")
    private int assignee_id;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=true)
    public User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "state_id")
    private int state_id;
    @Column(name = "category_id")
    private int category_id;
    @Column(name = "urgency_id")
    private int urgency_id;
    @Column(name = "approver_id")
    private int approver_id;


    public Ticket() {};

    public Ticket(int id, String name_ticket, String description, java.sql.Date created_on, java.sql.Date desired_resolution_date, int assignee_id, int owner_id, int state_id, int category_id, int urgency_id, int approver_id) {
        this.id = id;
        this.name_ticket = name_ticket;
        this.description = description;
        this.created_on = created_on;
        this.desired_resolution_date = desired_resolution_date;
        this.assignee_id = assignee_id;
//        this.owner_id = owner_id;
        this.state_id = state_id;
        this.category_id = category_id;
        this.urgency_id = urgency_id;
        this.approver_id = approver_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_ticket() {
        return name_ticket;
    }

    public void setName_ticket(String name_ticket) {
        this.name_ticket = name_ticket;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(java.util.Date created_on) {
        this.created_on = created_on;
    }

    public Date getDesired_resolution_date() {
        return desired_resolution_date;
    }

    public void setDesired_resolution_date(Date desired_resolution_date) {
        this.desired_resolution_date = desired_resolution_date;
    }

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getUrgency_id() {
        return urgency_id;
    }

    public void setUrgency_id(int urgency_id) {
        this.urgency_id = urgency_id;
    }

    public int getApprover_id() {
        return approver_id;
    }

    public void setApprover_id(int approver_id) {
        this.approver_id = approver_id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name_ticket='" + name_ticket + '\'' +
                ", description='" + description + '\'' +
                ", created_on='" + created_on + '\'' +
                ", desired_resolution_date='" + desired_resolution_date + '\'' +
                ", assignee_id=" + assignee_id +
//                ", owner_id=" + owner_id +    //для маппинга
                ", state_id=" + state_id +
                ", category_id=" + category_id +
                ", urgency_id=" + urgency_id +
                ", approver_id=" + approver_id +
                '}';
    }


}
