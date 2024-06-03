package com.fithou.ecovn.view.setting;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.SettingTabAdapter;
import com.fithou.ecovn.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SettingActivity extends AppCompatActivity {
    ViewPager2 setting_viewPager;
    SettingTabAdapter tabAdapter;
    TabLayout setting_tablayout;
    Toolbar setting_toolbar;

    ImageView setting_back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        setting_viewPager = findViewById(R.id.setting_view_pager);
        setting_tablayout = findViewById(R.id.setting_tabLayout);
        setting_toolbar = findViewById(R.id.setting_toolbar);
        setting_back_btn = findViewById(R.id.setting_back_btn);

        tabAdapter = new SettingTabAdapter(this);
        setting_viewPager.setAdapter(tabAdapter);

        new TabLayoutMediator(setting_tablayout, setting_viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Thông tin");
                    break;
                case 1:
                    tab.setText("Thiết lâp");
                    break;
            }
        }).attach();

        setting_back_btn.setOnClickListener(v -> {
            finish();
        });
    }
}