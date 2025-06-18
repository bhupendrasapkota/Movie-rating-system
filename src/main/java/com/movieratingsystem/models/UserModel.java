package com.movieratingsystem.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private byte[] image;
    private Role role;

    public enum Role {
        user,
        admin
    }

    public UserModel() {
    }

    public UserModel(int id, String name, String email, String password, byte[] image, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role=role;
    }
}
