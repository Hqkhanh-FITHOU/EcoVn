package com.fithou.ecovn.model.payment;

import com.fithou.ecovn.model.cart.CartModel;
import com.fithou.ecovn.model.cart.ProductCartModel;

import java.util.List;

public class PaymentModel {
    private String typePayment;
    private String address;
    private String cart_id;
    private String user_id;
    private String total;
    private List<ProductCartModel> productCartModel;

    public PaymentModel() {
    }

    public PaymentModel(String typePayment, String address, String cart_id, String user_id, String total, List<ProductCartModel> productCartModel) {
        this.typePayment = typePayment;
        this.address = address;
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.total = total;
        this.productCartModel = productCartModel;
    }

    public String getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
}
