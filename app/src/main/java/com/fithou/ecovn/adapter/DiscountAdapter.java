package com.fithou.ecovn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.DiscountModel;


import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder>{
    private Context context;
    private List<DiscountModel> mDiscount;
    public DiscountAdapter(Context context, List<DiscountModel> mDiscount) {
        this.context = context;
        this.mDiscount = mDiscount;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.ViewHolder holder, int position) {
        DiscountModel discountModel = mDiscount.get(position);
        if(discountModel == null){
            return;
        }

        holder.title.setText(discountModel.getTitle());
        holder.des.setText(discountModel.getDes());
    }
    public void setViewData(List<DiscountModel> discountModels){
        mDiscount = discountModels;
    }
    @Override
    public int getItemCount() {
        if(mDiscount == null || mDiscount.isEmpty()){
            return 0;
        }
        return mDiscount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, des;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_discount);
            des = itemView.findViewById(R.id.des_discount);
        }
    }
}
