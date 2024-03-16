package com.fithou.ecovn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.AccountFeatureViewModel;

import java.util.List;

public class AccountFeatureAdapter extends RecyclerView.Adapter<AccountFeatureAdapter.AccountFeatureViewHolder> {
    private Context context;
    private List<AccountFeatureViewModel> mfeatures;

    @NonNull
    @Override
    public AccountFeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item,parent,false);
        return new AccountFeatureViewHolder(view);
    }

    public void setViewData(List<AccountFeatureViewModel> features){
        mfeatures = features;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountFeatureViewHolder holder, int position) {
        AccountFeatureViewModel feature = mfeatures.get(position);
        if(feature == null){
            return;
        }
        holder.icon_feature.setImageResource(feature.getFeature_icon());
        holder.feature_name.setText(feature.getFeature_name());


        holder.feature_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Xử lý sự kiện click item
                Toast.makeText(view.getContext(), holder.feature_name.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mfeatures == null || mfeatures.isEmpty()){
            return 0;
        }
        return mfeatures.size();
    }

    public class AccountFeatureViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon_feature;
        private TextView feature_name;

        private RelativeLayout feature_layout;
        public AccountFeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_feature = itemView.findViewById(R.id.account_feature_icon);
            feature_name = itemView.findViewById(R.id.account_feature_name);
            feature_layout = itemView.findViewById(R.id.account_feature_layout);
        }



    }

}
