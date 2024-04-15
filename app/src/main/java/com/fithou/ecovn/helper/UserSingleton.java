package com.fithou.ecovn.helper;

import com.fithou.ecovn.model.authModels;

public class UserSingleton {
    private static UserSingleton instance;
    private authModels user;

    private UserSingleton() {
        // Khởi tạo đối tượng user ở đây
    }

    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public authModels getUser() {
        return user;
    }

    public void setUser(authModels user) {
        this.user = user;
    }
}
