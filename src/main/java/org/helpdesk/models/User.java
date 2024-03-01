package org.helpdesk.models;

//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "users" )
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    //    @NotEmpty(message = "Name shouldn't be empty")
//    @Size(min = 2, max= 30, message = "Name should be 2-30 characters")
    private String firstName;

    @Column(name = "last_name")
    //    @NotEmpty(message = "Name shouldn't be empty")
//    @Size(min = 2, max= 30, message = "Name should be 2-30 characters")
    private String lastName;
//    @Min(value = 0, message = "Age should be >0 ")


    @Column(name = "role_id")
    private int role_id;

    @NotEmpty(message = "Email shouldn't be empty")
    @Email(message = "Email should be valid ")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Email shouldn't be empty")
    private String password;

    public User() {
    }

    public User(int id, String firstName, String lastName, int role_Id, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role_id = role_Id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role_id=" + role_id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    /*
    @OneToMany(mappedBy="user")
    private List<Ticket> tickets;

     */

/*

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "ticket",
            joinColumns = { @JoinColumn(name = "id") }
//           inverseJoinColumns = { @JoinColumn(name = "project_id")         }
    )
private List<Ticket> tickets;

 */
}

