package com.fithou.ecovn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fithou.ecovn.view.dashboard.AccountMenuFragment;
import com.fithou.ecovn.view.dashboard.CartMenuFagment;
import com.fithou.ecovn.view.dashboard.HomeMenuFragment;
import com.fithou.ecovn.view.dashboard.NotificationMenuFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new HomeMenuFragment();
            case 1: return new CartMenuFagment();
            case 2: return new NotificationMenuFragment();
            default: return new AccountMenuFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
