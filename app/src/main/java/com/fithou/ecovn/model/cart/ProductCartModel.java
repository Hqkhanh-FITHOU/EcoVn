package com.fithou.ecovn.model.cart;

public class ProductCartModel {
    private String product_id;
    private String quantity_order;

    public ProductCartModel(String product_id, String quantity_order) {
        this.product_id = product_id;
        this.quantity_order = quantity_order;
    }

    public ProductCartModel() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity_order() {
        return quantity_order;
    }

    public void setQuantity_order(String quantity_order) {
        this.quantity_order = quantity_order;
    }
}
