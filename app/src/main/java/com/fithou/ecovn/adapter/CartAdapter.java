package com.fithou.ecovn.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.helper.CurrencyFormatter;
import com.fithou.ecovn.menu.CartMenu;
import com.fithou.ecovn.model.cart.CartModel;
import com.fithou.ecovn.model.cart.ExtendProductModel;
import com.fithou.ecovn.model.cart.ProductCartModel;
import com.fithou.ecovn.model.product.ProductsModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private Context context;
    private List<CartModel> cartModels;

    private List<ProductsModel> productsModels;

    private List<ExtendProductModel> extendProductModels;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private AppCompatActivity activity;
    private boolean allChecked = false;

    private CheckBox selectAllCheckbox;

    private TextView total;
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
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
        holder.checkboxItem.setChecked(extendProductModel.isChecked());
        holder.quantity.setText(String.valueOf(extendProductModel.getQuantity_order()));
        holder.checkboxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                extendProductModel.setChecked(isChecked);
                if (areAllItemsChecked()) {
                    allChecked = true;
                    selectAllCheckbox.setChecked(true);
                } else {
                    allChecked = false;
                    selectAllCheckbox.setChecked(false);
                }
               getTotalPrice();
            }
        });

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
    }
    public void setAllSelected(boolean selected) {
        allChecked = selected;
        if(selected){
            for (ExtendProductModel item : extendProductModels) {
                item.setChecked(selected);
            }
        }else{
            if(areAllItemsChecked()){
                for (ExtendProductModel item : extendProductModels) {
                    item.setChecked(false);
                }
            }else{
                return;
            }
        }
        getTotalPrice();
        notifyDataSetChanged();
    }

    public boolean areAllItemsChecked() {
        for (ExtendProductModel item : extendProductModels) {
            if (!item.isChecked()) {
                return false;
            }
        }
        return true;
    }


    public List<String> getSelectedItems() {

        List<String> selected = new ArrayList<>();
        for (int i = 0; i < extendProductModels.size(); i++) {
            if (selectedItems.get(i)) {
                selected.add(String.valueOf(extendProductModels.get(Integer.parseInt(String.valueOf(i)))));
            }
        }
        return selected;
    }

    public List<ExtendProductModel> filterCheckedItems() {
        List<ExtendProductModel> checkedItems = new ArrayList<>();
        for (ExtendProductModel model : extendProductModels) {
            if (model.isChecked()) {
                checkedItems.add(model);
            } else if(checkedItems.size() > 0){
                for(ExtendProductModel item : checkedItems){
                    if(item.getProduct_id().equals(model.getProduct_id())){
                        checkedItems.remove(item);
                    }
                }
            }
        }
        return checkedItems;
    }

    public void getTotalPrice(){
        double totals = 0;
        if(filterCheckedItems().size() < 0){
            return;
        }
        for (ExtendProductModel item : filterCheckedItems()){
            totals += item.getCost() * Integer.parseInt(item.getQuantity_order());
        }
        total.setText(CurrencyFormatter.formatCurrency(totals));

    }

    public CartAdapter(Context context, List<CartModel> cartModels, List<ExtendProductModel> extendProductModels, CheckBox selectAllCheckbox, TextView textView){
        this.context = context;
        this.cartModels = cartModels;
        this.extendProductModels = extendProductModels;
        this.selectAllCheckbox = selectAllCheckbox;
        this.total = textView;
    }
    public void setViewData(List<CartModel> mCartModels,List<ExtendProductModel> mExtendProductModels){
        cartModels = mCartModels;
        extendProductModels = mExtendProductModels;
    }

    @Override
    public int getItemCount() {
        if(extendProductModels == null || extendProductModels.isEmpty()){
            return 0;
        }
        return extendProductModels.size();
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
            checkboxItem = itemView.findViewById(R.id.checkbox_select);
            checkboxAll = itemView.findViewById(R.id.checkbox_select_all);
        }
    }
}
