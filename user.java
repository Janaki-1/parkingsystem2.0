package com.example.parking.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String passwordHash;

    private String fullName;

    private boolean admin = false;

    public User() {}

    public User(String username, String passwordHash, String fullName, boolean admin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.admin = admin;
    }

    // getters & setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String p) { this.passwordHash = p; }
    public String getFullName() { return fullName; }
    public void setFullName(String n) { this.fullName = n; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean a) { this.admin = a; }
}