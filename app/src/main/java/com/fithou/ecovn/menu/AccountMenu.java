package com.fithou.ecovn.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fithou.ecovn.MainActivity;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.AccountFeatureAdapter;
import com.fithou.ecovn.model.AccountFeatureViewModel;

import java.util.ArrayList;
import java.util.List;


public class AccountMenu extends Fragment {

    TextView account_name;
    public AccountMenu() {}
    RecyclerView featureRecyclerView;
    AccountFeatureAdapter featureAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_menu, container, false);
        account_name = view.findViewById(R.id.user_name);

        account_name.setText(MainActivity.CURRENT_USER.getName());

        featureAdapter = new AccountFeatureAdapter(MainActivity.CURRENT_USER);
        featureRecyclerView = view.findViewById(R.id.account_feature_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        featureRecyclerView.setLayoutManager(linearLayoutManager);

        featureAdapter.setViewData(initData());
        featureRecyclerView.setAdapter(featureAdapter);


        return view;
    }

    private List<AccountFeatureViewModel> initData(){
        List<AccountFeatureViewModel> list = new ArrayList<>();
        list.add(new AccountFeatureViewModel(1,R.drawable.round_event_note_24, "Đơn mua"));
        list.add(new AccountFeatureViewModel(2,R.drawable.wallet, "Tiện ích"));
        list.add(new AccountFeatureViewModel(3,R.drawable.round_store_24, "Cửa hàng của tôi"));
        list.add(new AccountFeatureViewModel(4,R.drawable.round_star_24, "Đánh giá của tôi"));
        list.add(new AccountFeatureViewModel(5,R.drawable.round_settings_24, "Thiết lập tài khoản"));
        list.add(new AccountFeatureViewModel(6,R.drawable.round_help_24, "Trung tâm trợ giúp"));
        list.add(new AccountFeatureViewModel(7, R.drawable.baseline_logout_24, "Đăng xuất"));
        return list;
    }

}