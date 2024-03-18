package com.fithou.ecovn.model.product;

import com.google.type.DateTime;

import java.util.Date;

public class Comment {
    private String user_id;
    private String content;
    private int star;
    private Date date_time;

    public Comment() {
    }

    public Comment(String user_id, String content, int star, Date dateTime) {
        this.user_id = user_id;
        this.content = content;
        this.star = star;
        this.date_time = dateTime;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public Date getDateTime() {
        return date_time;
    }

    public void setDateTime(Date dateTime) {
        this.date_time = dateTime;
    }
}
