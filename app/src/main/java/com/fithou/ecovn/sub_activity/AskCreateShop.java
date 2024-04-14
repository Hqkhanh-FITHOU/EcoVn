package com.fithou.ecovn.sub_activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.fithou.ecovn.R;

public class AskCreateShop extends AppCompatActivity {
    private ImageView btn_back_active;
    private Button btn_active_shop;

    private ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if(data != null){
                        String shopID = data.getStringExtra("shop_id");
                        Intent intent = new Intent(AskCreateShop.this, MyStoreActivity.class);
                        intent.putExtra("shop_id", shopID);
                        startActivity(intent);
                        finish();
                    }else{
                        finish();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_create_shop);

        btn_back_active = findViewById(R.id.btn_back_active);
        btn_active_shop = findViewById(R.id.btn_active_shop);

        btn_back_active.setOnClickListener(view -> {
            finish();
        });

        btn_active_shop.setOnClickListener(view -> {
            Intent intent = new Intent(AskCreateShop.this, CreateShop.class);
            activityLauncher.launch(intent);
        });
    }
}