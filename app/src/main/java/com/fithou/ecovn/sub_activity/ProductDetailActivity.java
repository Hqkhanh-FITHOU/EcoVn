package com.fithou.ecovn.sub_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fithou.ecovn.R;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView img_product_detail;

    TextView price_product_detail, name_product_detail, description_product_detail;
    TextView quantity_product_detail, unit_product_detail, container_type_product_detail;

    RatingBar rating_product_detail;

    TextView btn_add_to_cart, btn_buy_now;
    TextView none_comment;
    ImageView btn_back_product_detail, share_btn;

    RecyclerView comment_recycleview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViewComponent();
        onClickBack();
        loadComments();
    }

    private void loadComments() {

    }

    private void onClickBack() {
        btn_back_product_detail.setOnClickListener(view -> {
            finish();
        });
    }

    private void initializeViewComponent() {
        img_product_detail = findViewById(R.id.img_product_detail);
        price_product_detail = findViewById(R.id.price_product_detail);
        name_product_detail = findViewById(R.id.name_product_detail);
        description_product_detail = findViewById(R.id.description_product_detail);
        quantity_product_detail = findViewById(R.id.quantity_product_detail);
        unit_product_detail = findViewById(R.id.unit_product_detail);
        container_type_product_detail = findViewById(R.id.container_type_product_detail);
        rating_product_detail = findViewById(R.id.rating_product_detail);
        share_btn = findViewById(R.id.share_btn);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        btn_buy_now = findViewById(R.id.btn_buy_now);
        btn_back_product_detail = findViewById(R.id.btn_back_product_detail);
        none_comment = findViewById(R.id.none_comment);
        comment_recycleview = findViewById(R.id.comment_recycleview);
    }


}