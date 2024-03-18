package com.fithou.ecovn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.adapter.ViewPagerAdapter;
import com.fithou.ecovn.menu.AccountMenu;
import com.fithou.ecovn.menu.CartMenu;
import com.fithou.ecovn.menu.HomeMenu;
import com.fithou.ecovn.menu.NotificationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.fithou.ecovn.R.id;
public class MainActivity extends AppCompatActivity {
//    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    public static authModels CURRENT_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setUserInputEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        bottomNavigationView = findViewById(R.id.bottomNav);
        frameLayout = findViewById(R.id.frameLayout);
        CURRENT_USER = (authModels) getIntent().getSerializableExtra("user");
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager2.setVisibility(View.VISIBLE);
//                frameLayout.setVisibility(View.GONE);
//                viewPager2.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                viewPager2.setVisibility(View.VISIBLE);
//                frameLayout.setVisibility(View.GONE);
//            }
//        });

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                switch (position){
//                    case 0:
//                    case 1:
//                    case 2:
//                        tabLayout.getTabAt(position).select();
//                }
//                super.onPageSelected(position);
//            }
//        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager2.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                switch (item.getItemId()){
                    case id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeMenu()).commit();
                        return true;
                    case id.bottom_cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CartMenu()).commit();
                        return true;
                    case id.bottom_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NotificationMenu()).commit();
                        return true;
                    case id.bottom_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountMenu()).commit();
                        return true;
                }
                return false;
            }
        });
    }


}