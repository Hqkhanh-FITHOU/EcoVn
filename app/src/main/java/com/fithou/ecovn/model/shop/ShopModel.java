package com.fithou.ecovn.model.shop;

import com.google.type.DateTime;

public class ShopModel {
    private String shop_id;
    private String user_id;
    private String img_certificate_business;
    private String img_shop;
    private String name;
    private String phone_number;
    private String address;
    private String des;

    private DateTime joining_date;

    public ShopModel() {
    }

    public ShopModel(String shop_id, String user_id, String img_certificate_business, String img_shop, String name, String phone_number, String address, String des, DateTime joining_date) {
        this.shop_id = shop_id;
        this.user_id = user_id;
        this.img_certificate_business = img_certificate_business;
        this.img_shop = img_shop;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
        this.des = des;
        this.joining_date = joining_date;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImg_certificate_business() {
        return img_certificate_business;
    }

    public void setImg_certificate_business(String img_certificate_business) {
        this.img_certificate_business = img_certificate_business;
    }

    public String getImg_shop() {
        return img_shop;
    }

    public void setImg_shop(String img_shop) {
        this.img_shop = img_shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public DateTime getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(DateTime joining_date) {
        this.joining_date = joining_date;
    }
}
