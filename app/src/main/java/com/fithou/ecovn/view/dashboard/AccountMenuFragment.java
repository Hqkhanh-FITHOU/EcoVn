package com.fithou.ecovn.view.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.view.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.AccountFeatureAdapter;
import com.fithou.ecovn.model.AccountFeatureViewModel;
import com.fithou.ecovn.view.auth.LoginActivity;
import com.fithou.ecovn.view.auth.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountMenuFragment extends Fragment {

    TextView account_name;
    CircleImageView user_img;
    Toolbar account_toobar;
    Button btn_login, btn_register;
    RelativeLayout authen_layout, user_infor_layout;
    public AccountMenuFragment() {}
    RecyclerView featureRecyclerView;
    AccountFeatureAdapter featureAdapter;

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == LoginActivity.LOGIN_OK){
            featureAdapter.setViewData(initData());
            featureAdapter.updateUser(MainActivity.CURRENT_USER);
            featureRecyclerView.setAdapter(featureAdapter);
            loadUserInfor();
        }
    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_menu, container, false);
        account_name = view.findViewById(R.id.user_name);
        authen_layout = view.findViewById(R.id.authen_layout);
        user_infor_layout = view.findViewById(R.id.user_infor_layout);
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
        user_img = view.findViewById(R.id.user_img);
        account_toobar = view.findViewById(R.id.account_toobar);
        loadUserInfor();



        featureAdapter = new AccountFeatureAdapter(MainActivity.CURRENT_USER);
        featureRecyclerView = view.findViewById(R.id.account_feature_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        featureRecyclerView.setLayoutManager(linearLayoutManager);

        featureAdapter.setViewData(initData());
        featureRecyclerView.setAdapter(featureAdapter);

        onClickLogin();
        onClickRegister();
        return view;
    }

    private void onClickLogin() {
        btn_login.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), LoginActivity.class);
            launcher.launch(i);
        });
    }

    private void onClickRegister() {
        btn_register.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), RegisterActivity.class);
            i.putExtra("login",3);
            launcher.launch(i);
        });
    }

    private void loadUserInfor() {
        if(MainActivity.CURRENT_USER != null){
            authen_layout.setVisibility(View.GONE);
            user_infor_layout.setVisibility(View.VISIBLE);
            account_toobar.getMenu().getItem(0).setVisible(true);
            account_toobar.getMenu().getItem(1).setVisible(true);
            account_name.setText(MainActivity.CURRENT_USER.getName());
            Glide.with(getActivity()).load(MainActivity.CURRENT_USER.getImage()).into(user_img);
        }else{
            account_toobar.getMenu().getItem(0).setVisible(false);
            account_toobar.getMenu().getItem(1).setVisible(false);
            authen_layout.setVisibility(View.VISIBLE);
            user_infor_layout.setVisibility(View.GONE);
        }
    }

    private List<AccountFeatureViewModel> initData(){
        List<AccountFeatureViewModel> list = new ArrayList<>();

        if(MainActivity.CURRENT_USER != null){
            list.add(new AccountFeatureViewModel(1,R.drawable.round_event_note_24, "Đơn mua"));
            list.add(new AccountFeatureViewModel(2,R.drawable.wallet, "Tiện ích"));
            list.add(new AccountFeatureViewModel(3,R.drawable.round_store_24, "Cửa hàng của tôi"));
            list.add(new AccountFeatureViewModel(4,R.drawable.round_star_24, "Đánh giá của tôi"));
            list.add(new AccountFeatureViewModel(5,R.drawable.round_settings_24, "Thiết lập tài khoản"));
            list.add(new AccountFeatureViewModel(6,R.drawable.round_help_24, "Trung tâm trợ giúp"));
            list.add(new AccountFeatureViewModel(7, R.drawable.baseline_logout_24, "Đăng xuất"));
        }
        else {
            list.add(new AccountFeatureViewModel(2,R.drawable.wallet, "Tiện ích"));
            list.add(new AccountFeatureViewModel(6,R.drawable.round_help_24, "Trung tâm trợ giúp"));
        }

        return list;
    }

}