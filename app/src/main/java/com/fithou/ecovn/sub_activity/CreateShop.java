package com.fithou.ecovn.sub_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fithou.ecovn.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.custom_view.MyProgressDialog;
import com.fithou.ecovn.model.ShopModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateShop extends AppCompatActivity {
    private EditText create_shop_name, create_shop_phone, create_shop_address, create_shop_description;
    private Button btn_create_shop;

    private ImageView btn_back_create_shop;

    public static final int CREATE_OK = 1;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        intitViewComponent();

        btn_back_create_shop.setOnClickListener(view -> {
            finish();
        });

        btn_create_shop.setOnClickListener(view -> {
            clickFinish();
        });
    }

    private void clickFinish() {
        if(create_shop_name.getText().toString().equalsIgnoreCase("")){
            create_shop_name.setError("Nhập tên shop của bạn");
        }
        if(create_shop_phone.getText().toString().equalsIgnoreCase("")){
            create_shop_phone.setError("Nhập điện thoại liên hệ của shop");
        }
        if(create_shop_address.getText().toString().equalsIgnoreCase("")){
            create_shop_address.setError("Nhập địa chỉ của shop");
        }
        if(create_shop_description.getText().toString().equalsIgnoreCase("")){
            create_shop_description.setError("Nhập giới thiệu về shop");
        }
        else{
            MyProgressDialog progressDialog = new MyProgressDialog(CreateShop.this);
            progressDialog.setTitle("Đang hoàn tất");
            progressDialog.show();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> shopData = new HashMap<>();
            shopData.put("user_id",MainActivity.CURRENT_USER.getId());
            shopData.put("name",create_shop_name.getText().toString());
            shopData.put("img_shop","");
            shopData.put("img_certificate_business","");
            shopData.put("des",create_shop_description.getText().toString());
            shopData.put("phone_number",create_shop_phone.getText().toString());
            shopData.put("address",create_shop_address.getText().toString());
            shopData.put("shop_id","");
            shopData.put("joining_date",dateFormat.format(new Date()));


            db = FirebaseFirestore.getInstance();
            db.collection("shop").add(shopData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    documentReference.update("shop_id", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent resultIntent = new Intent(CreateShop.this, AskCreateShop.class);
                                            resultIntent.putExtra("shop_id", documentReference.getId());
                                            setResult(Activity.RESULT_OK, resultIntent);
                                            updateIsShopUserProfile();
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                            });
        }

    }

    private void updateIsShopUserProfile(){
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(MainActivity.CURRENT_USER.getId())
                .update("shop",true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        MainActivity.CURRENT_USER.setShop(true);
                        Toast.makeText(CreateShop.this, "Hoàn tất", Toast.LENGTH_SHORT).show();
                    }
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