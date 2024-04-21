package com.fithou.ecovn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.custom_view.MyProgressDialog;
import com.fithou.ecovn.model.product.ProductsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder>{
    private Context context;
    private List<ProductsModel> productsModels;

    private FirebaseFirestore db;

    private ProductsModel.OnProductClickListener onProductClickListener;


    public MyProductsAdapter(Context context, List<ProductsModel> productsModels) {
        this.context = context;
        this.productsModels = productsModels;
    }

    public void setViewData(List<ProductsModel> mProductsModels){
        productsModels = mProductsModels;
    }

    public void setOnProductClickListener(ProductsModel.OnProductClickListener listener) {
        onProductClickListener = listener;
    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_list_item_myshop,parent,false);
        return new MyProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsAdapter.ViewHolder holder, int position) {
        ProductsModel productsModel = productsModels.get(position);
        if(productsModel == null){
            return;
        }
        if (context == null) {
            Log.e("Glide Error", "Context is null");
        } else {

            // Load image with Glide
            Glide.with(context)
                    .load(productsModel.getImg())
                    .placeholder(R.drawable.logo)
                    .centerCrop()
                    .error(R.drawable.baseline_error_outline_24)
                    .into(holder.img_product);
        }

        holder.name.setText(productsModel.getName());
        holder.cost.setText(formatCurrency(productsModel.getCost()));




        holder.btn_edit_product.setOnClickListener(view -> {
            onProductClickListener.onProductClick(productsModel);
        });


        holder.btn_delete_product.setOnClickListener(view -> {
            deleteMyProduct(productsModel.getProduct_id(), position, view.getContext());
        });
    }

    @Override
    public int getItemCount() {
        if(productsModels == null || productsModels.isEmpty()){
            return 0;
        }
        return productsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_product;
        private TextView name;
        private TextView cost;

        private CardView product_layout;

        private Button btn_edit_product, btn_delete_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_products);
            name = itemView.findViewById(R.id.name_products);
            cost = itemView.findViewById(R.id.cost_products);
            product_layout = itemView.findViewById(R.id.product_layout);
            btn_edit_product = itemView.findViewById(R.id.btn_edit_product);
            btn_delete_product = itemView.findViewById(R.id.btn_delete_product);
        }
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
            return c+"đ";
        }

        return decimalFormat.format(c) + "đ";
    }

    private void deleteMyProduct(String productID, int position, Context context){
        MyProgressDialog progressDialog = new MyProgressDialog(context);
        progressDialog.setTitle("");
        progressDialog.show();
        db = FirebaseFirestore.getInstance();
        db.collection("product").document(productID)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        notifyItemRemoved(position);
                        productsModels.remove(position);
                        progressDialog.dismiss();
                        Log.d("Delete Product", "DocumentSnapshot successfully deleted!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete Product","Error deleting document", e);
                    }
                });
    }
}
