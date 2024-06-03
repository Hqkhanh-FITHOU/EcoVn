package com.fithou.ecovn.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fithou.ecovn.view.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.view.auth.LoginActivity;
import com.fithou.ecovn.view.component.MyProgressDialog;
import com.fithou.ecovn.model.AccountFeatureViewModel;
import com.fithou.ecovn.model.user.authModels;
import com.fithou.ecovn.view.shop.AskCreateShop;
import com.fithou.ecovn.view.shop.MyShopActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AccountFeatureAdapter extends RecyclerView.Adapter<AccountFeatureAdapter.AccountFeatureViewHolder> {

    private List<AccountFeatureViewModel> mfeatures;

    private authModels user;

    FirebaseFirestore db;

    public AccountFeatureAdapter(authModels user) {
        this.user = user;
    }

    public void updateUser(authModels user){
        this.user = user;
    }

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


        holder.feature_layout.setOnClickListener(view -> {
            //Xử lý sự kiện click item
            switch (feature.getFeature_id()){
                case 1:

                    break;

                case 2:

                    break;

                case 3:
                    if(user.isShop()){
                        MyProgressDialog dialog = new MyProgressDialog(view.getContext());
                        dialog.setTitle("");
                        dialog.show();
//                        Toast.makeText(view.getContext(), feature.getFeature_name(), Toast.LENGTH_SHORT).show();
                        db = FirebaseFirestore.getInstance();
                        db.collection("shop").whereEqualTo("user_id", MainActivity.CURRENT_USER.getId())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            if(!task.getResult().isEmpty()){
                                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                                Intent intent = new Intent(view.getContext(), MyShopActivity.class);
                                                intent.putExtra("shop_id", doc.getId());

                                                view.getContext().startActivity(intent);
                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                });
                    }
                    else{
//                        Toast.makeText(view.getContext(), feature.getFeature_name()+" is shop: " + user.isShop() + " user:" + user.getName(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(view.getContext(), AskCreateShop.class);
                        view.getContext().startActivity(intent);
                    }
                    break;

                case 4:

                    break;

                case 5:

                    break;

                case 6:

                    break;

                case 7:
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    view.getContext().startActivity(intent);
                    MainActivity context = (MainActivity) view.getContext();
                    context.finish();
                    break;
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

        private CardView feature_layout;
        public AccountFeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_feature = itemView.findViewById(R.id.account_feature_icon);
            feature_name = itemView.findViewById(R.id.account_feature_name);
            feature_layout = itemView.findViewById(R.id.account_feature_layout);
        }
    }

}
