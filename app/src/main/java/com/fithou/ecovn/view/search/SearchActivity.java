package com.fithou.ecovn.view.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.ProductsAdapter;
import com.fithou.ecovn.model.product.CategoryModel;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.view.product.ProductDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.search.SearchBar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ImageView btn_back_to_home;
    SearchBar search_activity_search_bar;

    ArrayList<ProductsModel> productsModelList, listTemp;

    RecyclerView productRecyclerView;
    private FirebaseFirestore firestore;
    ProductsAdapter productsAdapter;
    CategoryModel categoryModel;



    com.google.android.material.search.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btn_back_to_home = findViewById(R.id.btn_back_to_home);
        searchView = findViewById(R.id.search_view_search_screen);
        search_activity_search_bar = findViewById(R.id.search_activity_search_bar);

        Intent intent  = getIntent();

        firestore = FirebaseFirestore.getInstance();
        categoryModel = (CategoryModel) intent.getSerializableExtra("CATEGORY_ID");

        productsModelList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(this,productsModelList);
        productRecyclerView = findViewById(R.id.products_list);
        productRecyclerView.setAdapter(productsAdapter);

        onClickBack();
        onClickProduct();
        onSearch();
        if(categoryModel != null){
            loadProductsFromFirebase(categoryModel.getId());
        }

    };

//    private void onSearch() {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                    searchProduct(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                productsAdapter.setViewData(productsModelList);
//                productsAdapter.notifyDataSetChanged();
//                return false;
//            }
//        });
//    }

    private void onSearch() {
        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            search_activity_search_bar.setText(searchView.getText());
            searchView.hide();
            return false;
        });

        
    }

    private void onClickBack() {
        btn_back_to_home.setOnClickListener(view -> {
            finish();
        });
    }

    private void onClickProduct(){
        productsAdapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", (Serializable) product);
            // Gửi các thông tin khác của sản phẩm nếu cần
            startActivity(intent);
        });
    }

    private void searchProduct(String name) {
        listTemp = new ArrayList<>();
        for(ProductsModel productsModel: productsModelList){
            if(productsModel.getName().toLowerCase().contains(name.toLowerCase())){
                listTemp.add(productsModel);
            }
        }

        if(listTemp.size() != 0){
            productsAdapter.setViewData(listTemp);
            productsAdapter.notifyDataSetChanged();
        }
    }

    private void loadProductsFromFirebase(String category_id) {
        firestore.collection("product")
                .whereEqualTo("fk_category_id",category_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        productsModelList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ProductsModel item = documentSnapshot.toObject(ProductsModel.class);
                            List<HashMap<String, Object>> commentDataList = (List<HashMap<String, Object>>) documentSnapshot.get("comment");
                            if (commentDataList != null) {
                                List<Comment> commentList = new ArrayList<>();
                                for (HashMap<String, Object> commentData : commentDataList) {
                                    String userId = (String) commentData.get("user_id");
                                    String content = (String) commentData.get("content");
                                    int star = ((Long) commentData.get("star")).intValue();
                                    String dateTimeString = (String) commentData.get("date_time");

                                    // Chuyển đổi giá trị String sang kiểu DateTime
                                    if (dateTimeString != null && !dateTimeString.isEmpty()){
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date dateTime = null;
                                        try {
                                            dateTime = dateFormat.parse(dateTimeString);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Comment comment = new Comment(userId, content, star, dateTime);
                                        commentList.add(comment);
                                    }
                                }
                                item.setComment(commentList);
                            }
                            productsModelList.add(item);
                        }
                        productsAdapter.setViewData(productsModelList);
                        productsAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting data", e);
                    }
                });
    }
}