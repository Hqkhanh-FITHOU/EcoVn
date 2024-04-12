package com.fithou.ecovn.sub_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fithou.ecovn.R;

public class CreateShop extends AppCompatActivity {
    private EditText create_shop_name, create_shop_phone, create_shop_address, create_shop_description;
    private Button btn_create_shop;

    private ImageView btn_back_create_shop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        intitViewComponent();

        btn_back_create_shop.setOnClickListener(view -> {
            finish();
        });
    }

    private void intitViewComponent() {
        create_shop_name = findViewById(R.id.create_shop_name);
        create_shop_phone = findViewById(R.id.create_shop_phone);
        create_shop_address = findViewById(R.id.create_shop_address);
        create_shop_description = findViewById(R.id.create_shop_description);
        btn_create_shop = findViewById(R.id.btn_create_shop);
        btn_back_create_shop = findViewById(R.id.btn_back_create_shop);
    }
}