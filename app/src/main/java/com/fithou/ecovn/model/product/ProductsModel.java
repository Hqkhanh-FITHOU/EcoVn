package com.fithou.ecovn.model.product;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class ProductsModel implements Parcelable {
    private String product_id;
    private String name;
    private String des;
    private Double cost;

    private  String img;
    private String FK_title_product_id;
    private String FK_shop_id;

    private List<Comment> commentList;

    public ProductsModel() {
    }

    public ProductsModel(String product_id, String name, String des, Double cost, String img, String FK_title_product_id, String FK_shop_id, List<Comment> comment) {
        this.product_id = product_id;
        this.name = name;
        this.des = des;
        this.cost = cost;
        this.img = img;
        this.FK_title_product_id = FK_title_product_id;
        this.FK_shop_id = FK_shop_id;
        this.commentList = comment;
    }


    protected ProductsModel(Parcel in) {
        product_id = in.readString();
        name = in.readString();
        des = in.readString();
        if (in.readByte() == 0) {
            cost = null;
        } else {
            cost = in.readDouble();
        }
        img = in.readString();
        FK_title_product_id = in.readString();
        FK_shop_id = in.readString();
    }

    public static final Creator<ProductsModel> CREATOR = new Creator<ProductsModel>() {
        @Override
        public ProductsModel createFromParcel(Parcel in) {
            return new ProductsModel(in);
        }

        @Override
        public ProductsModel[] newArray(int size) {
            return new ProductsModel[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFK_title_product_id() {
        return FK_title_product_id;
    }

    public void setFK_title_product_id(String FK_title_product_id) {
        this.FK_title_product_id = FK_title_product_id;
    }

    public String getFK_shop_id() {
        return FK_shop_id;
    }

    public void setFK_shop_id(String FK_shop_id) {
        this.FK_shop_id = FK_shop_id;
    }

    public List<Comment> getComment() {
        return commentList;
    }

    public void setComment(List<Comment> comment) {
        this.commentList = comment;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(product_id);
        parcel.writeString(name);
        parcel.writeString(des);
        if (cost == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cost);
        }
        parcel.writeString(img);
        parcel.writeString(FK_title_product_id);
        parcel.writeString(FK_shop_id);
    }
}