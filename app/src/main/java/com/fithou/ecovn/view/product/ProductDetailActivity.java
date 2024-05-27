package com.fithou.ecovn.view.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.view.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.CommentAdapter;
import com.fithou.ecovn.helper.UserSingleton;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.model.cart.CartModel;
import com.fithou.ecovn.model.cart.ExtendProductModel;
import com.fithou.ecovn.model.cart.ProductCartModel;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.view.payment.PaymentActivity;
import com.fithou.ecovn.view.shop.ShopActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView img_product_detail;

    TextView price_product_detail, name_product_detail, description_product_detail;
    TextView quantity_product_detail, unit_product_detail, container_type_product_detail;

    RatingBar rating_product_detail;

    TextView btn_add_to_cart, btn_buy_now;
    TextView none_comment, name_shop_product_detail;
    Button btn_see_shop;
    ImageView btn_back_product_detail, share_btn;
    CircleImageView img_shop_product_detail;

    RecyclerView comment_recycleview;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments;

    FirebaseFirestore db;
    ProductsModel product;
    private FirebaseFirestore firestore;

    private authModels authModels;


    ProgressDialog progressDialog;

    private Boolean checkExistsCart;

    private String cartIds = "";

    private String currentUser = "";
    public ProductDetailActivity() {
    }

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
        authModels = UserSingleton.getInstance().getUser();
        firestore = FirebaseFirestore.getInstance();
        name_product_detail.setText(product.getName());
        price_product_detail.setText(formatCurrency(product.getCost()));
        quantity_product_detail.setText(product.getQuantity()+"");
        unit_product_detail.setText(product.getUnit() + " /");
        container_type_product_detail.setText(" "+product.getContainer_type());
        description_product_detail.setText(product.getDes());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Thêm vào giỏ hàng");
        progressDialog.setMessage("Đang thực hiện !");

        checkExistsCart = false;
        loadShopInfor(product.getFK_shop_id());
        clickButtonSeeShop(product.getFK_shop_id());
        getComments(product.getProduct_id());
        onAddToCart();
        onBuyNow();
        if(comments == null || comments.size() == 0){}
        if(authModels != null){
            currentUser = authModels.getId();
        }else{
            currentUser = "";
        }
        //Log.d("Current user ", MainActivity.CURRENT_USER.getId());
        }




    private void clickButtonSeeShop(String fk_shop_id) {
        btn_see_shop.setOnClickListener(view -> {
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("shop_id", fk_shop_id);
            startActivity(intent);
        });
    }

    private void loadShopInfor(String shopId) {
        db = FirebaseFirestore.getInstance();
        db.collection("shop").document(shopId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            Glide.with(ProductDetailActivity.this)
                                    .load(doc.get("img_shop") == "" ? R.mipmap.ic_launcher : doc.get("img_shop"))
                                    .into(img_shop_product_detail);
                            name_shop_product_detail.setText((String) doc.get("name"));
                        }
                    }
                });
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


    private void onAddToCart(){
        final String[] cartId = {""};
        ArrayList<ProductCartModel> cartData = new ArrayList<>();
        if(MainActivity.CURRENT_USER != null){
            firestore.collection("cart")
                    .whereEqualTo("user_id", MainActivity.CURRENT_USER.getId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                CartModel item = documentSnapshot.toObject(CartModel.class);

                                cartId[0] = item.getCart_id();
                                List<HashMap<String, Object>> productDataList = (List<HashMap<String, Object>>) documentSnapshot.get("product");
                                if (productDataList != null) {
                                    List<ProductCartModel> productCartModels = new ArrayList<>();
                                    for (HashMap<String, Object> productData : productDataList) {
                                        String product_id = (String) productData.get("product_id");
                                        String quantity_order = (String) productData.get("quantity_order");
                                        ProductCartModel productCartModel = new ProductCartModel(product_id, quantity_order);
                                        productCartModels.add(productCartModel);
                                        cartData.add(productCartModel);
                                    }
                                    item.setProductCartModel(productCartModels);
                                }
                            }

                            cartIds = cartId[0];

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firestore", "Error getting data", e);
                        }
                    });
        }
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST", cartId[0]);
                Log.d("TEST", MainActivity.CURRENT_USER.getId());
                progressDialog.show();
                ProductCartModel productCartModel = new ProductCartModel(product.getProduct_id(), "1");
                for (ProductCartModel item: cartData){
                    if(item.getProduct_id().equals(product.getProduct_id())){
                        checkExistsCart = true;
                    }
                }
                if(!checkExistsCart){
                    cartData.add(productCartModel);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetailActivity.this, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("product", cartData);

                DocumentReference documentReference = firestore.collection("cart").document(cartId[0]);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        documentReference.update(updateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại ... ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void onBuyNow(){
        ExtendProductModel model = new ExtendProductModel();
        model.setQuantity_order("1");
        model.setQuantity(product.getQuantity());
        model.setProduct_id(product.getProduct_id());
        model.setCost(product.getCost());
        model.setDes(product.getDes());
        model.setComment(product.getComment());
        model.setContainer_type(product.getContainer_type());
        model.setFK_category_id(product.getFK_category_id());
        model.setFK_shop_id(product.getFK_shop_id());
        model.setImg(product.getImg());
        model.setName(product.getName());
        model.setUnit(product.getUnit());

        ArrayList<ExtendProductModel> extendProductModels = new ArrayList<>();
        extendProductModels.add(model);

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, PaymentActivity.class);
                intent.putParcelableArrayListExtra("extendProductModels",  extendProductModels);
                startActivity(intent);
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
        btn_see_shop = findViewById(R.id.btn_see_shop);
        name_shop_product_detail = findViewById(R.id.name_shop_product_detail);
        img_shop_product_detail = findViewById(R.id.img_shop_product_detail);
    }

    @NonNull
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