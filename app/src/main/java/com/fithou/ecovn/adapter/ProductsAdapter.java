package com.fithou.ecovn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.model.product.ProductsModel;

import java.text.DecimalFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    private Context context;
    private List<ProductsModel> productsModels;
    private ProductsModel.OnProductClickListener onProductClickListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_list_item,parent,false);
        return new ViewHolder(view);
    }

    public ProductsAdapter(Context context, List<ProductsModel> productsModels) {
        this.context = context;
        this.productsModels = productsModels;
    }

    public void setViewData(List<ProductsModel> mProductsModels){
        productsModels = mProductsModels;
    }

    public void setOnProductClickListener(ProductsModel.OnProductClickListener listener) {
        onProductClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        if(productsModels == null || productsModels.isEmpty()){
            return 0;
        }
        return productsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_product;
        private TextView name;
        private TextView cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_products);
            name = itemView.findViewById(R.id.name_products);
            cost = itemView.findViewById(R.id.cost_products);

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
}
