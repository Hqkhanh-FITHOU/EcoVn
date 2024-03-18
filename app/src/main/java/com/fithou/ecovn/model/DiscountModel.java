package com.fithou.ecovn.model;

public class DiscountModel {
    private int id;
    private String title;
    private String des;

    public DiscountModel() {
    }

    public DiscountModel(int id, String title, String des) {
        this.id = id;
        this.title = title;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
