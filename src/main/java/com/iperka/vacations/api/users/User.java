package com.iperka.vacations.api.users;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The {@link com.iperka.vacations.api.users.User} class defines the structure
 * of a basic user. This includes numerous attributes used for authentication,
 * authorization and specification of user details and states.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-07-05
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Unique identifier for {@link com.iperka.vacations.api.users.User} model. Uses
     * the built in {@link java.util.UUID} object to generate UUIDs. In the database
     * schema the field is of the type {@code BINARY(16) NOT NULL}. This field must
     * not be changed after creation.
     */
    @Id
    @GeneratedValue
    @NotNull
    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID uuid;

    /**
     * Unique string used for authentication and better user experience when logging
     * in or displaying user accounts.
     */
    @Column(unique = true, nullable = false, length = 100)
    @Min(4)
    @Max(100)
    @NotNull
    private String username;

    /**
     * Stores the hashed password for user account. This fields should NOT be
     * serialized and sent to client as response.
     */
    @Column(nullable = false)
    @NotNull
    @JsonIgnore
    private String password;

    /**
     * Stores the date time when the user object has been created.
     */
    @Column(nullable = false)
    @NotNull
    private Date created = new Date();

    /**
     * Stores the date time when the user object has been updated.
     */
    @Column(nullable = false)
    @NotNull
    private Date updated = new Date();

    /**
     * Set updated field to current date. This method gets triggered every time the
     * user object changes.
     */
    @PreUpdate
    public void setLastUpdate() {
        this.updated = new Date();
    }

    /**
     * Empty constructor for Java Hibernate.
     */
    public User() {
    }

    /**
     * User Constructor to create a new User Object. Never commit changes directly
     * to the user model, always use a DTO (Data Transfer Object) class to create or
     * modify an user.
     * 
     * @param username Users username.
     * @param password Users password. Should already be hashed.
     */
    public User(@Min(4) @Max(100) @NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
    }
}