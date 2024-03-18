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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fithou.ecovn.R;
import com.fithou.ecovn.model.CategoryModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private Context context;
    private List<CategoryModel> mCategoryModel;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list,parent,false);
        return new ViewHolder(view);
    }

    public CategoryAdapter(Context context, List<CategoryModel> mCategoryModel) {
        this.context = context;
        this.mCategoryModel = mCategoryModel;
    }

    public void setViewData(List<CategoryModel> categoryModels){
        mCategoryModel = categoryModels;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryModel categoryModel = mCategoryModel.get(position);
        if(categoryModel == null){
            return;
        }
        if (context == null) {
            Log.e("Glide Error", "Context is null");
        } else {
//            int radius = 8; // Radius of the rounded corners
//            RequestOptions requestOptions = RequestOptions
//                    .bitmapTransform(new RoundedCorners(radius))
//                    .override(300, 300); // Optional: set the size of the image
            // Load image with Glide
            Glide.with(context)
                    .load(categoryModel.getImg())
//                    .apply(requestOptions)
                    .placeholder(R.drawable.logo)
                    .centerCrop()
                    .error(R.drawable.baseline_error_outline_24)
                    .into(holder.imageView);
        }

        Log.d("img", categoryModel.getImg());
        holder.textView.setText(categoryModel.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mCategoryModel == null || mCategoryModel.isEmpty()){
            return 0;
        }
        return mCategoryModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_img);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
