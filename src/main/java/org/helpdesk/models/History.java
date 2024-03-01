package org.helpdesk.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "history")
public class History {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ticket_id")
    private int ticket_id;
    @Column(name = "date_action")
    private Date date;
    @Column(name = "action")
    private String action;
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public History() {}

    public History(int id, int ticket_id, Date date, String action, int user_id, String description) {
        this.id = id;
        this.ticket_id = ticket_id;
        this.date = date;
        this.action = action;
        this.user_id = user_id;
        this.description = description;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", ticket_id=" + ticket_id +
                ", date=" + date +
                ", action='" + action + '\'' +
                ", user_id=" + user_id +
                ", description='" + description + '\'' +
                '}';
    }
}

