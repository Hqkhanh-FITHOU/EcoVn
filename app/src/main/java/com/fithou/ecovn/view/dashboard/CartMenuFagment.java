package com.fithou.ecovn.view.dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fithou.ecovn.view.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.CartAdapter;
import com.fithou.ecovn.view.auth.LoginActivity;
import com.fithou.ecovn.view.component.MyProgressDialog;
import com.fithou.ecovn.helper.CartSingleton;
import com.fithou.ecovn.helper.UserSingleton;
import com.fithou.ecovn.model.payment.PaymentModel;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.model.cart.CartModel;
import com.fithou.ecovn.model.cart.ExtendProductModel;
import com.fithou.ecovn.model.cart.ProductCartModel;
import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.view.payment.PaymentActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CartMenuFagment extends Fragment {
    RecyclerView cartRecyclerView;
    CartAdapter cartAdapter;
    private FirebaseFirestore firestore;
    private ArrayList<ProductsModel> productsModelList;
    private ArrayList<CartModel> cartModels;
    private ArrayList<ExtendProductModel> extendProductModels;
    private PaymentModel paymentModels;
    private ArrayList<String> productIdsList = new ArrayList<>();
    private CheckBox checkAllSelect;
    private TextView total, txt_no_product;
    private RelativeLayout frame_total;
    private ImageView img_delete;
    ProgressDialog progressDialog;

    private Button btn_save, btn_login_to_user_cart;

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == LoginActivity.LOGIN_OK){
            loadCartFromFirebase();
        }
    });

    public CartMenuFagment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart_menu, container, false);

        firestore = FirebaseFirestore.getInstance();
        cartModels = new ArrayList<>();
        extendProductModels = new ArrayList<>();
        productsModelList = new ArrayList<>();
        paymentModels = new PaymentModel();
        checkAllSelect = view.findViewById(R.id.checkbox_select_all);
        total = view.findViewById(R.id.tv_total_price);
        img_delete = view.findViewById(R.id.btn_delete);
        //btn_back = view.findViewById(R.id.btn_back);
        btn_save = view.findViewById(R.id.btn_save);
        txt_no_product = view.findViewById(R.id.txt_no_product);
        frame_total = view.findViewById(R.id.frame_total);
        btn_login_to_user_cart = view.findViewById(R.id.btn_login_to_user_cart);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Xóa sản phẩm khỏi giỏ hàng");
        progressDialog.setMessage("Đang thực hiện !");


        cartAdapter = new CartAdapter(getContext(),cartModels, extendProductModels,checkAllSelect,total );
        cartRecyclerView = view.findViewById(R.id.cart_list);

        LinearLayoutManager cartLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        cartRecyclerView.setLayoutManager(cartLayoutManager);
        cartRecyclerView.setAdapter(cartAdapter);
        if (MainActivity.CURRENT_USER != null){
            loadCartFromFirebase();
        }else{
            btn_login_to_user_cart.setVisibility(View.VISIBLE);
            txt_no_product.setVisibility(View.VISIBLE);
            txt_no_product.setText("Đăng nhập để mua sắm ngay");
            frame_total.setVisibility(View.GONE);
        }

        checkAllSelected();
        onClickPayment();
        onClickDeleteCart();
        onClickLogin();
        return view;
    }

    private void onClickLogin() {
        btn_login_to_user_cart.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), LoginActivity.class);
            launcher.launch(i);
        });
    }

    private void checkAllSelected(){
        checkAllSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    cartAdapter.setAllSelected(b);
            }
        });
    }

    private void onClickPayment(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                ArrayList<ExtendProductModel> model = new ArrayList<>();
                paymentModels.setProductCartModel(onGetCartChecked("payment"));

                for (ExtendProductModel ex : extendProductModels){
                    for (ProductCartModel paymentModel : paymentModels.getProductCartModel()){
                       if(ex.getProduct_id().equals(paymentModel.getProduct_id())){
                           model.add(ex);
                       }
                    }
                }

                intent.putParcelableArrayListExtra("extendProductModels",  model);
                startActivity(intent);
            }
        });
    }


    private List<ProductCartModel> onGetCartChecked(String type){
        String typeForPayment = "payment"; // Get list product to payment
        String typeForDelete = "delete"; // Get list product to delete
        ArrayList<ProductCartModel> productCartModels = new ArrayList<>();
        List<ExtendProductModel> checkedItems = new ArrayList<>(cartAdapter.filterCheckedItems());
        if(type.equals(typeForPayment)){
            for(CartModel cartModel : cartModels){
                for(ProductCartModel productCartModel : cartModel.getProductCartModel()){
                    for (ExtendProductModel extendProductModel : checkedItems){
                        if(productCartModel.getProduct_id().equals(extendProductModel.getProduct_id())){
                            productCartModels.add(productCartModel);
                        }
                    }
                }
            }
        }else if (type.equals(typeForDelete)){
            for(CartModel cartModel : cartModels){
                for(ProductCartModel productCartModel : cartModel.getProductCartModel()){
                    Boolean flag = false;
                    for (ExtendProductModel extendProductModel : checkedItems){
                        if(productCartModel.getProduct_id().equals(extendProductModel.getProduct_id())){
                            flag = true;
                        }
                    }
                    if(flag == false){
                        productCartModels.add(productCartModel);
                    }
                }
            }
        }
        return productCartModels;
    }

    private void loadCartFromFirebase() {
        firestore.collection("cart")
                .whereEqualTo("user_id", MainActivity.CURRENT_USER.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CartModel item = documentSnapshot.toObject(CartModel.class);
                        List<HashMap<String, Object>> productDataList = (List<HashMap<String, Object>>) documentSnapshot.get("product");
                        if (productDataList != null) {
                            List<ProductCartModel> productCartModels = new ArrayList<>();
                            for (HashMap<String, Object> productData : productDataList) {
                                String product_id = (String) productData.get("product_id");
                                String quantity_order = (String) productData.get("quantity_order");
                                ProductCartModel productCartModel = new ProductCartModel(product_id, quantity_order);
                                productCartModels.add(productCartModel);
                            }
                            item.setProductCartModel(productCartModels);
                        }
                        cartModels.add(item);
                        CartSingleton.getInstance().setCartModel(cartModels.get(0));
                        getProductIds();
                    }
                }).addOnFailureListener(e -> Log.e("Firestore", "Error getting data", e));
    }

    private void loadProductFromFirebase(){
        MyProgressDialog progressDialog = new MyProgressDialog(getContext());
        progressDialog.setTitle("");
        progressDialog.show();
        firestore.collection("product")
                .whereIn("product_id",productIdsList)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            btn_login_to_user_cart.setVisibility(View.GONE);
                            txt_no_product.setVisibility(View.GONE);
                            frame_total.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                ExtendProductModel item = documentSnapshot.toObject(ExtendProductModel.class);
                                productsModelList.add(item);

                                for (CartModel cartModel: cartModels){
                                    for (ProductCartModel productCartModel : cartModel.getProductCartModel())
                                        if(productCartModel.getProduct_id().equals(item.getProduct_id())){
                                            item.setQuantity_order(productCartModel.getQuantity_order());
                                        }
                                }
//                            List<HashMap<String, Object>> commentDataList = (List<HashMap<String, Object>>) documentSnapshot.get("comment");
//                            if (commentDataList != null) {
//                                List<Comment> commentList = new ArrayList<>();
//                                for (HashMap<String, Object> commentData : commentDataList) {
//                                    String userId = (String) commentData.get("user_id");
//                                    String content = (String) commentData.get("content");
//                                    int star = ((Long) commentData.get("star")).intValue();
//                                    String dateTimeString = (String) commentData.get("date_time");
//
//                                    // Chuyển đổi giá trị String sang kiểu DateTime
//                                    if (dateTimeString != null && !dateTimeString.isEmpty()){
//                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        Date dateTime = null;
//                                        try {
//                                            dateTime = dateFormat.parse(dateTimeString);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Comment comment = new Comment(userId, content, star, dateTime);
//                                        commentList.add(comment);
//                                    }
//                                }
//                                item.setComment(commentList);
//                            }
                                extendProductModels.add(item);
                            }
                            cartAdapter.setViewData(cartModels,extendProductModels);
                            cartAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }else{
                            btn_login_to_user_cart.setVisibility(View.GONE);
                            txt_no_product.setText("Chưa có sản phẩm trong giỏ");
                            txt_no_product.setVisibility(View.VISIBLE);
                            frame_total.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting data", e);
                    }
                });

    }

    private void onClickDeleteCart(){
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //xử lý khi nhấn xóa phải đảm bảo có sản phẩm được chọn thì mới xóa
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa những mục đã chọn?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện xóa các mục đã chọn từ Firebase
                deleteItemsFromFirebase();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItemsFromFirebase() {
        progressDialog.show();

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("product", onGetCartChecked("delete"));

        DocumentReference documentReference = firestore.collection("cart").document(cartModels.get(0).getCart_id());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                documentReference.update(updateData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Iterator<ExtendProductModel> iterator = extendProductModels.iterator();
                                while (iterator.hasNext()) {
                                    ExtendProductModel extendProductModel = iterator.next();
                                    for (ProductCartModel productCartModel : onGetCartChecked("payment")) {
                                        if (extendProductModel.getProduct_id().equals(productCartModel.getProduct_id())) {
                                            iterator.remove(); // Xóa phần tử từ Iterator, không gây ra lỗi ConcurrentModificationException
                                            break; // Đảm bảo chỉ xóa một phần tử duy nhất trong mỗi vòng lặp của for-each
                                        }
                                    }
                                }

                                if(extendProductModels.size() == 0){
                                    checkAllSelect.setChecked(false);
                                    btn_login_to_user_cart.setVisibility(View.GONE);
                                    txt_no_product.setText("Chưa có sản phẩm trong giỏ");
                                    txt_no_product.setVisibility(View.VISIBLE);
                                    frame_total.setVisibility(View.GONE);
                                }
                                cartAdapter = new CartAdapter(getContext(),cartModels, extendProductModels,checkAllSelect, total);

                                LinearLayoutManager cartLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                cartRecyclerView.setLayoutManager(cartLayoutManager);
                                cartRecyclerView.setAdapter(cartAdapter);

                                cartAdapter.getTotalPrice();
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Xóa thất bại ... ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getProductIds() {
        for (CartModel cartModel : cartModels) {
            // Lấy product_id từ mỗi ProductCartModel và thêm vào danh sách productIdsList
           for (ProductCartModel productCartModel : cartModel.getProductCartModel()){
               if (productCartModel != null) {
                   productIdsList.add(productCartModel.getProduct_id());
               }
           }
        }
        if(!productIdsList.isEmpty()){
            loadProductFromFirebase();
        }
    }
}