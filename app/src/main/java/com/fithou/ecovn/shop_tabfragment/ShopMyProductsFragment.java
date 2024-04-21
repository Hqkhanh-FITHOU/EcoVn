package com.fithou.ecovn.shop_tabfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.MyProductsAdapter;
import com.fithou.ecovn.adapter.ProductsAdapter;
import com.fithou.ecovn.custom_view.MyProgressDialog;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.sub_activity.EditProduct;
import com.fithou.ecovn.sub_activity.MyStoreActivity;
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


public class ShopMyProductsFragment extends Fragment {
    private RecyclerView shop_myproducts_list;
    private MyProductsAdapter productsAdapter;
    private List<ProductsModel> productsModelListData;
    private TextView no_product;

    FirebaseFirestore db;
    String shopID;

    private ActivityResultLauncher<Intent> launcher  = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    ReloadData(MyStoreActivity.SHOP_ID);
                }
            });

    public ShopMyProductsFragment(String shopID) {
        this.shopID = shopID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_my_products, container, false);
        // Inflate the layout for this fragment
        shop_myproducts_list = view.findViewById(R.id.shop_myproducts_list);
        no_product = view.findViewById(R.id.no_product);

        productsModelListData = new ArrayList<>();
        productsAdapter = new MyProductsAdapter(getContext(), productsModelListData);
        GridLayoutManager gridLayoutProductManager = new GridLayoutManager(getContext(), 2);
        shop_myproducts_list.setLayoutManager(gridLayoutProductManager);
        shop_myproducts_list.setAdapter(productsAdapter);
        loadProductFromFirebase(shopID);
        onClickEditProduct();
        return view;
    }

    private void onClickEditProduct(){
        productsAdapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(getContext(), EditProduct.class);
            intent.putExtra("productID", product.getProduct_id());
            launcher.launch(intent);
        });
    }

    private void loadProductFromFirebase(String shopID){
        MyProgressDialog dialog = new MyProgressDialog(this.getContext());
        dialog.setTitle("");
        dialog.show();
        db = FirebaseFirestore.getInstance();
        db.collection("product")
                .whereEqualTo("fk_shop_id", shopID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        productsModelListData.clear();
                        if(!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                ProductsModel item = documentSnapshot.toObject(ProductsModel.class);
                                productsModelListData.add(item);
                            }
                            productsAdapter.setViewData(productsModelListData);
                            productsAdapter.notifyDataSetChanged();
                            no_product.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }else {
                            no_product.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting data", e);
                    }
                });
    }

    public void ReloadData(String shopID){
        loadProductFromFirebase(shopID);
    }
}