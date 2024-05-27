package com.fithou.ecovn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.helper.CurrencyFormatter;
import com.fithou.ecovn.model.product.ProductsModel;
import com.google.android.material.card.MaterialCardView;

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
        holder.cost.setText(CurrencyFormatter.formatCurrency(productsModel.getCost()));

        holder.product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onProductClickListener != null) {
                    onProductClickListener.onProductClick(productsModel);
                }
            }
        });
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

        private CardView product_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_products);
            name = itemView.findViewById(R.id.name_products);
            cost = itemView.findViewById(R.id.cost_products);
            product_layout = itemView.findViewById(R.id.product_layout);
        }
    }
}
