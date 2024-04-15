package com.fithou.ecovn.helper;

import com.fithou.ecovn.model.cart.CartModel;

public class CartSingleton {
    private static CartSingleton instance;
    private CartModel cartModel;

    public CartSingleton() {
    }

    public static synchronized CartSingleton getInstance() {
        if (instance == null) {
            instance = new CartSingleton();
        }
        return instance;
    }

    public CartModel getCartModel() {
        return cartModel;
    }

    public void setCartModel(CartModel cartModel) {
        this.cartModel = cartModel;
    }
}
