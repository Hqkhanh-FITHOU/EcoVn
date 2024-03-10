package com.fithou.ecovn.model;

import java.io.Serializable;

public class authModels implements Serializable {
    String Id;
    String name;
    String email;
    String password;
    public authModels(String Id, String name, String email, String password) {
        this.Id = Id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public authModels() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
}
