package com.fithou.ecovn.view.dashboard;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.fithou.ecovn.adapter.CategoryAdapter;
import com.fithou.ecovn.adapter.DiscountAdapter;
import com.fithou.ecovn.adapter.ProductsAdapter;
import com.fithou.ecovn.view.component.MyProgressDialog;
import com.fithou.ecovn.model.CategoryModel;
import com.fithou.ecovn.model.DiscountModel;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.view.product.ProductDetailActivity;
import com.fithou.ecovn.view.search.SearchActivity;
import com.fithou.ecovn.view.category.SeeMoreCategory;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.compose.animation.Scale;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.fithou.ecovn.R;

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

public class HomeMenuFragment extends Fragment {

    Toolbar home_toolbar;
    ImageSlider home_img_slider;
    RecyclerView categoryRecyclerView, discountRecyclerView, productRecyclerView;
    CategoryAdapter categoryAdapter;
    DiscountAdapter discountAdapter;

    ProductsAdapter productsAdapter;
    private FirebaseFirestore firestore;
    ArrayList<CategoryModel> categoryModelList;
    ArrayList<DiscountModel> discountModelList;

    ArrayList<ProductsModel> productsModelList, listTemp;

    TextView tvSeeMore;

    private ScrollView scrollView;


    public HomeMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);
        firestore = FirebaseFirestore.getInstance();

        tvSeeMore = view.findViewById(R.id.tv_seemore);
        scrollView = view.findViewById(R.id.scroll_view);
        home_toolbar = view.findViewById(R.id.home_toolbar);
        home_img_slider = view.findViewById(R.id.home_img_slider);

        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);
        categoryRecyclerView = view.findViewById(R.id.category_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);

        discountModelList = new ArrayList<>();
        discountAdapter = new DiscountAdapter(getContext(),discountModelList);
        discountRecyclerView = view.findViewById(R.id.discount_list);
        LinearLayoutManager discountLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(discountLayoutManager);
        discountRecyclerView.setAdapter(discountAdapter);

        productsModelList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(getContext(),productsModelList);
        productRecyclerView = view.findViewById(R.id.products_list);
        GridLayoutManager gridLayoutProductManager = new GridLayoutManager(getContext(), 3);
        productRecyclerView.setLayoutManager(gridLayoutProductManager);
        productRecyclerView.setAdapter(productsAdapter);

        loadImageSlider();
        loadCategoryFromFirebase();
        loadDiscountFromFirebase();
        loadProductFromFirebase();
        onClickSeeMore();
        onClickProduct();
        onClickCategory();
        onClickMenuItem();

        return view;
    }

    private void loadImageSlider() {
        List<SlideModel> imgList = new ArrayList<>();
        imgList.add(new SlideModel(R.drawable.imgslide1, ScaleTypes.FIT));
        imgList.add(new SlideModel(R.drawable.imgslide2, ScaleTypes.FIT));
        imgList.add(new SlideModel(R.drawable.imgslide3, ScaleTypes.FIT));
        imgList.add(new SlideModel(R.drawable.imgslide4, ScaleTypes.FIT));
        home_img_slider.setImageList(imgList, ScaleTypes.FIT);
    }

    private void onClickMenuItem() {
        home_toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.search_menu_item_toolbar: {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("search", 1);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), Pair.create(home_toolbar,"searchView"));
                    startActivity(intent, options.toBundle());
                }
                case R.id.chat_menu_item_toolbar: {

                }
                case R.id.QR_menu_item_toolbar:{

                }
            }
            return false;
        });
    }

    private void onClickSeeMore(){
        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SeeMoreCategory.class);
                intent.putParcelableArrayListExtra("data", (ArrayList<? extends android.os.Parcelable>) categoryModelList);
                startActivity(intent);
            }
        });
    }

    private void onClickCategory(){
        categoryAdapter.setOnCategoryClickListener(category -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("CATEGORY_ID", (Serializable) category);
            // Gửi các thông tin khác của sản phẩm nếu cần
            startActivity(intent);
        });
    }



    private void onClickProduct(){
        productsAdapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", (Serializable) product);
            // Gửi các thông tin khác của sản phẩm nếu cần
            startActivity(intent);
        });
    }


    private void searchProduct(String name){
        listTemp = new ArrayList<>();
        for(ProductsModel productsModel: productsModelList){
            if(productsModel.getName().toLowerCase().contains(name.toLowerCase())){
                listTemp.add(productsModel);
            }
        }

        if(listTemp.size() != 0){
            productsAdapter.setViewData(listTemp);
            productsAdapter.notifyDataSetChanged();
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    private void loadCategoryFromFirebase() {
        firestore.collection("category")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CategoryModel item = documentSnapshot.toObject(CategoryModel.class);
                            categoryModelList.add(item);
                        }
                        categoryAdapter.setViewData(categoryModelList);
                        categoryAdapter.notifyDataSetChanged();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting data", e);
                    }
                });
    }

    private void loadDiscountFromFirebase(){

        firestore.collection("discount")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DiscountModel item = documentSnapshot.toObject(DiscountModel.class);
                            discountModelList.add(item);
                        }
                        discountAdapter.setViewData(discountModelList);
                        discountAdapter.notifyDataSetChanged();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting data", e);
                    }
                });
    }

    private void loadProductFromFirebase(){
        MyProgressDialog progressDialog = new MyProgressDialog(this.getActivity());
        progressDialog.setTitle("");
        progressDialog.show();
        firestore.collection("product")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        productsModelList.clear();
                        progressDialog.setTitle("");
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
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting data", e);
                    }
                });
    }

}