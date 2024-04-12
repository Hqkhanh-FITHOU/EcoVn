package com.fithou.ecovn.shop_tabfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.ProductsAdapter;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.sub_activity.ProductDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ShopProductsFragment extends Fragment {
    private RecyclerView shop_products_list;
    private ProductsAdapter productsAdapter;
    private List<ProductsModel> productsModelListData;

    FirebaseFirestore db;
    String shopID;

    public ShopProductsFragment(String shopID) {
        this.shopID = shopID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_products, container, false);
        shop_products_list = view.findViewById(R.id.shop_products_list);

        productsModelListData = new ArrayList<>();
        productsAdapter = new ProductsAdapter(getContext(), productsModelListData);
        GridLayoutManager gridLayoutProductManager = new GridLayoutManager(getContext(), 2);
        shop_products_list.setLayoutManager(gridLayoutProductManager);
        shop_products_list.setAdapter(productsAdapter);
        loadProductFromFirebase(shopID);
        onClickProduct();
        return view;
    }


    private void onClickProduct(){
        productsAdapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", (Serializable) product);
            // Gửi các thông tin khác của sản phẩm nếu cần
            startActivity(intent);
        });
    }



    private void loadProductFromFirebase(String shopID){
        db = FirebaseFirestore.getInstance();
        db.collection("product")
                .whereEqualTo("FK_shop_id", shopID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        productsModelListData.clear();
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
                            productsModelListData.add(item);
                        }
                        productsAdapter.setViewData(productsModelListData);
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