package com.fithou.ecovn.view.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.ShopTabAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShopActivity extends AppCompatActivity {
    private ViewPager2 shop_viewpager;
    private TabLayout shop_tablauout;
    private Toolbar shop_toolbar;
    private ImageView shop_img_certificate_business, shop_img, btn_back_shop;
    private TextView shop_name, phone_number;
    private Button btn_follow;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ShopTabAdapter tabAdapter;


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        initViewComponent();

        Intent intent = getIntent();

        tabAdapter = new ShopTabAdapter(this, intent.getStringExtra("shop_id"));
        shop_viewpager.setAdapter(tabAdapter);

        new TabLayoutMediator(shop_tablauout, shop_viewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Giới thiệu");
                    break;
                case 1:
                    tab.setText("Sản phẩm");
                    break;
            }
        }).attach();
        loadViewData(intent.getStringExtra("shop_id"));

        clickBackButton();


        setAppbarAnimation();
    }

    private void setAppbarAnimation() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_login);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(ShopActivity.this,R.color.white ));
                collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(ShopActivity.this,R.color.Sauvignon_red));
            }
        });
    }

    private void loadViewData(String shop_id) {
        db = FirebaseFirestore.getInstance();

        db.collection("shop").document(shop_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            shop_name.setText((String) doc.get("name"));
                            phone_number.setText((String) doc.get("phone_number"));

                            Glide.with(ShopActivity.this)
                                    .load(doc.get("img_shop") == "" ? R.mipmap.ic_launcher : doc.get("img_shop"))
                                    .into(shop_img);
                        }
                    }
                });


        btn_follow.setOnClickListener(view -> {
            Toast.makeText(this, "Theo dõi", Toast.LENGTH_SHORT).show();
        });
    }

    private void clickBackButton() {
        btn_back_shop.setOnClickListener(view -> {
            finish();
        });
    }

    private void initViewComponent() {
        shop_toolbar = findViewById(R.id.shop_toolbar);
        shop_viewpager = findViewById(R.id.shop_viewpager);
        shop_tablauout = findViewById(R.id.shop_tablayout);
        shop_toolbar = findViewById(R.id.shop_toolbar);
        shop_img_certificate_business = findViewById(R.id.shop_img_certificate_business);
        shop_img = findViewById(R.id.shop_img);
        shop_name = findViewById(R.id.shop_name);
        phone_number = findViewById(R.id.phone_number);
        btn_follow = findViewById(R.id.btn_follow);
        btn_back_shop = findViewById(R.id.btn_back_shop);
        collapsingToolbarLayout = findViewById(R.id.shop_collapsingtoobarlayout);
    }


}