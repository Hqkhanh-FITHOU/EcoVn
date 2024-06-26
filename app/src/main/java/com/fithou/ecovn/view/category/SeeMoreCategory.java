package com.fithou.ecovn.view.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.SeeMoreCategoryAdapter;
import com.fithou.ecovn.model.product.CategoryModel;
import com.fithou.ecovn.view.search.SearchActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class SeeMoreCategory extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SeeMoreCategoryAdapter mAdapter;

    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more_category);

        btnBack = findViewById(R.id.backButton);
        Intent intent = getIntent();
        ArrayList<CategoryModel> data = intent.getParcelableArrayListExtra("data");

        mAdapter = new SeeMoreCategoryAdapter(this, data);
        mRecyclerView = findViewById(R.id.seemore_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        mRecyclerView.setAdapter(mAdapter);

        onBackScreen();
        onClickCategory();
    }

    private void onBackScreen(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void onClickCategory(){
        mAdapter.setOnCategoryClickListener(category -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("CATEGORY_ID", (Serializable) category);
            // Gửi các thông tin khác của sản phẩm nếu cần
            startActivity(intent);
        });
    }
}