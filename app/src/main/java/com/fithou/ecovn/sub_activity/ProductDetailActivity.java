package com.fithou.ecovn.sub_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.CommentAdapter;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView img_product_detail;

    TextView price_product_detail, name_product_detail, description_product_detail;
    TextView quantity_product_detail, unit_product_detail, container_type_product_detail;

    RatingBar rating_product_detail;

    TextView btn_add_to_cart, btn_buy_now;
    TextView none_comment;
    ImageView btn_back_product_detail, share_btn;

    RecyclerView comment_recycleview;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments;

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

        getComments(product.getProduct_id());
        if(comments == null || comments.size() == 0){

        }else {

        }
    }

    private void getComments(String pid){
        Log.d("ProductDetailActivity.pid", pid);
        db = FirebaseFirestore.getInstance();
        db.collection("product")
                .document(pid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
//                            Log.d("ProductDetail.db.comments",doc.get("comment").toString());
                            List<HashMap<String, Object>> commentDataList = (List<HashMap<String, Object>>) doc.get("comment"); //tham chiếu theo key-value
                            if (commentDataList != null) {
                                //duyệt lấy danh sách comment của product
                                comments = new ArrayList<>();
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
                                        Log.d("ProductDetail.local.comment",comment.toString());
                                        comments.add(comment);
                                    }
                                }
                                if(comments.toString().equalsIgnoreCase( "[]") || comments == null || comments.size() == 0){
                                    none_comment.setVisibility(View.VISIBLE);
                                    Log.d("Comments", "Null or empty");
                                }else {
                                    Log.d("ProductDetail.local.comments",comments.toString());
                                    none_comment.setVisibility(View.GONE);
                                    Log.d("Comments", comments.toString());
                                    commentAdapter = new CommentAdapter(comments, ProductDetailActivity.this);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                                    comment_recycleview.setLayoutManager(linearLayoutManager);
                                    comment_recycleview.setAdapter(commentAdapter);
                                }
                            }
                            else {
                                none_comment.setVisibility(View.VISIBLE);
                                Log.d("Comments", "Null or empty");
                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        none_comment.setVisibility(View.VISIBLE);
                        Log.d("Comments", "Null or empty");
                    }
                });
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