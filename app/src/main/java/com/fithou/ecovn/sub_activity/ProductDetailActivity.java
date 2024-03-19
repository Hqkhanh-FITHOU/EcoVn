package com.fithou.ecovn.sub_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.model.product.ProductsModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView img_product_detail;

    TextView price_product_detail, name_product_detail, description_product_detail;
    TextView quantity_product_detail, unit_product_detail, container_type_product_detail;

    RatingBar rating_product_detail;

    TextView btn_add_to_cart, btn_buy_now;
    TextView none_comment;
    ImageView btn_back_product_detail, share_btn;

    RecyclerView comment_recycleview;


    FirebaseFirestore db;
    ProductsModel product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent  = getIntent();
        initializeViewComponent();
        onClickBack();
        product = (ProductsModel) intent.getSerializableExtra("PRODUCT_ID");
        loadContentView(product);
    }

    private void loadContentView(ProductsModel product) {
        Glide.with(this)
                .load(product.getImg())
                .into(img_product_detail);

        name_product_detail.setText(product.getName());
        price_product_detail.setText(formatCurrency(product.getCost()));
        quantity_product_detail.setText(product.getQuantity()+"");
        unit_product_detail.setText(product.getUnit() + " /");
        container_type_product_detail.setText(" "+product.getContainer_type());
        description_product_detail.setText(product.getDes());
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

    private String formatCurrency(double c) {
        DecimalFormat decimalFormat = null;
        if(c >= 1000){
            decimalFormat = new DecimalFormat("#,###");
        }
        if(c >= 10000){
            decimalFormat = new DecimalFormat("##,###");
        }
        if(c >= 100000){
            decimalFormat = new DecimalFormat("###,###");
        }
        if(c >= 1000000){
            decimalFormat = new DecimalFormat("#,###,###");
        }
        if(c >= 1000000){
            decimalFormat = new DecimalFormat("#,###,###");
        }
        if(c >= 10000000){
            decimalFormat = new DecimalFormat("##,###,###");
        }
        if(c >= 100000000){
            decimalFormat = new DecimalFormat("###,###,###");
        }

        if(decimalFormat == null){
            return c+"";
        }

        return decimalFormat.format(c);
    }
}