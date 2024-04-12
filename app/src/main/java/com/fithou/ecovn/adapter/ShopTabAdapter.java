package com.fithou.ecovn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fithou.ecovn.shop_tabfragment.ShopIntroductionFragment;
import com.fithou.ecovn.shop_tabfragment.ShopProductsFragment;

public class ShopTabAdapter extends FragmentStateAdapter {

    String shopID;

    public ShopTabAdapter(@NonNull FragmentActivity fragmentActivity, String shopID) {
        super(fragmentActivity);

        this.shopID = shopID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ShopIntroductionFragment(shopID);
            default: return new ShopProductsFragment(shopID);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
