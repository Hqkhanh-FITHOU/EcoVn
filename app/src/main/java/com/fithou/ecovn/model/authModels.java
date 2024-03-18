package com.fithou.ecovn.model;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.io.Serializable;

public class authModels implements Serializable {
    String Id;
    String name;
    String email;
    String password;

    boolean gender = false;

    String phone = "";

    String address = "";

    String date_of_birth = "";

    String image = "";

    boolean is_shop = false;

    public authModels(String Id, String name, String email, String password) {
        this.Id = Id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public authModels() {
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isShop() {
        return is_shop;
    }

    public void setShop(boolean shop) {
        is_shop = shop;
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
