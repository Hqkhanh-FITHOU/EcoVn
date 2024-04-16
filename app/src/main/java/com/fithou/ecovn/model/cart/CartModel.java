package com.fithou.ecovn.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fithou.ecovn.model.product.ProductsModel;

import java.util.List;

public class CartModel implements Parcelable {
    private String cart_id;
    private String user_id;
    private String total;
    private List<ProductCartModel> productCartModel;

    private List<ProductsModel> productsModels;

    public CartModel(String cart_id, String user_id, String total, List<ProductCartModel> productCartModel,List<ProductsModel> productsModels) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.total = total;
        this.productCartModel = productCartModel;
        this.productsModels = productsModels;
    }

    public CartModel() {
    }

    protected CartModel(Parcel in) {
        cart_id = in.readString();
        user_id = in.readString();
        total = in.readString();
        productsModels = in.createTypedArrayList(ProductsModel.CREATOR);
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ProductCartModel> getProductCartModel() {
        return productCartModel;
    }


    public void setProductCartModel(List<ProductCartModel> productCartModel) {
        this.productCartModel = productCartModel;
    }

    public List<ProductsModel> getProductsModels() {
        return productsModels;
    }

    public void setProductsModels(List<ProductsModel> productsModels) {
        this.productsModels = productsModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(cart_id);
        parcel.writeString(user_id);
        parcel.writeString(total);
        parcel.writeTypedList(productsModels);
    }
}
