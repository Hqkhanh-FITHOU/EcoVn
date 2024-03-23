package com.fithou.ecovn.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fithou.ecovn.model.product.ProductsModel;

import java.io.Serializable;

public class CategoryModel implements Parcelable, Serializable {

    private String id;
    private String img;
    private String title;


    public CategoryModel() {
    }

    public CategoryModel(String id, String img, String title) {
        this.id = id;
        this.img = img;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected CategoryModel(Parcel in) {
        id = in.readString();
        img = in.readString();
        title = in.readString();
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(img);
        parcel.writeString(title);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryModel category);
    }
}
