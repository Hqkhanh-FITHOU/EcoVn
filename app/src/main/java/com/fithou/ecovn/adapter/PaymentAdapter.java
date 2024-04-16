package com.fithou.ecovn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.helper.CurrencyFormatter;
import com.fithou.ecovn.model.payment.PaymentModel;
import com.fithou.ecovn.model.cart.ExtendProductModel;
import com.fithou.ecovn.model.product.ProductsModel;

import java.util.List;

public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{
    private Context context;
    private PaymentModel paymentModel;

    private List<ProductsModel> productsModels;

    private List<ExtendProductModel> extendProductModels;

    private TextView total;
    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        ExtendProductModel extendProductModel = extendProductModels.get(position);
        if(extendProductModel == null){
            return;
        }
        if (context == null) {
            Log.e("Glide Error", "Context is null");
        } else {

            // Load image with Glide
            Glide.with(context)
                    .load(extendProductModel.getImg())
                    .placeholder(R.drawable.logo)
                    .centerCrop()
                    .error(R.drawable.baseline_error_outline_24)
                    .into(holder.img_product);
        }

        holder.name.setText(extendProductModel.getName());
        if(extendProductModel.getQuantity_order() != null){
            holder.cost.setText(CurrencyFormatter.formatCurrency(extendProductModel.getCost() * Integer.parseInt(extendProductModel.getQuantity_order())));
        }else{
            holder.cost.setText(CurrencyFormatter.formatCurrency(extendProductModel.getCost()));
        }

        holder.quantity.setText(String.valueOf(extendProductModel.getQuantity_order()));


        holder.minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity =Integer.parseInt(extendProductModel.getQuantity_order());
                String quantity_string = String.valueOf(quantity -1);
                if (quantity > 0) {
                    extendProductModel.setQuantity_order(quantity_string);
                    holder.quantity.setText(quantity_string);
                    holder.cost.setText(CurrencyFormatter.formatCurrency( extendProductModel.getCost() * Integer.parseInt(extendProductModel.getQuantity_order())));
                    getTotalPrice();
                }
            }
        });

        holder.plus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity =Integer.parseInt(extendProductModel.getQuantity_order());
                String quantity_string = String.valueOf(quantity +1);
                extendProductModel.setQuantity_order(quantity_string);
                holder.quantity.setText(quantity_string);
                holder.cost.setText(CurrencyFormatter.formatCurrency( extendProductModel.getCost() * Integer.parseInt(extendProductModel.getQuantity_order())));
                getTotalPrice();
            }
        });

        getTotalPrice();
    }

    @Override
    public int getItemCount() {
        if(extendProductModels == null || extendProductModels.isEmpty()){
            return 0;
        }
        return extendProductModels.size();
    }

    public PaymentAdapter(Context context, PaymentModel paymentModel, List<ExtendProductModel> extendProductModels, TextView total) {
        this.context = context;
        this.paymentModel = paymentModel;
        this.extendProductModels = extendProductModels;
        this.total = total;
    }

    public void setViewData(PaymentModel mPaymentModel,List<ExtendProductModel> mExtendProductModels){
        paymentModel = mPaymentModel;
        extendProductModels = mExtendProductModels;
    }

    public void getTotalPrice(){
        double totals = 0;
        if(extendProductModels.size() < 0){
            return;
        }
        for (ExtendProductModel item : extendProductModels){
            totals += item.getCost() * Integer.parseInt(item.getQuantity_order());
        }
        total.setText(CurrencyFormatter.formatCurrency(totals));

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_product;
        private TextView name;
        private TextView cost;

        private TextView minus_quantity;
        private TextView quantity;
        private TextView plus_quantity;

        private CheckBox checkboxItem, checkboxAll;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_products);
            name = itemView.findViewById(R.id.name_products);
            cost = itemView.findViewById(R.id.tv_price);
            minus_quantity = itemView.findViewById(R.id.tv_minus_quantity);
            quantity = itemView.findViewById(R.id.tv_quantity);
            plus_quantity = itemView.findViewById(R.id.tv_plus_quantity);
        }
    }
}
