package com.fithou.ecovn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fithou.ecovn.menu.AccountMenu;
import com.fithou.ecovn.menu.CartMenu;
import com.fithou.ecovn.menu.HomeMenu;
import com.fithou.ecovn.menu.NotificationMenu;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new HomeMenu();
            case 1: return new CartMenu();
            case 2: return new NotificationMenu();
            default: return new AccountMenu();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
