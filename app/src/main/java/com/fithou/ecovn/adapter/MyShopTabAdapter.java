package com.fithou.ecovn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fithou.ecovn.view.shop.ShopIntroductionFragment;
import com.fithou.ecovn.view.shop.ShopMyProductsFragment;

import java.util.ArrayList;
import java.util.List;

public class MyShopTabAdapter extends FragmentStateAdapter {
    String shopID;
    private List<Fragment> fragmentList;

    public MyShopTabAdapter(@NonNull FragmentActivity fragmentActivity, String shopID) {
        super(fragmentActivity);
        this.shopID = shopID;
        fragmentList = new ArrayList<>();
        fragmentList.add(new ShopIntroductionFragment(shopID));
        fragmentList.add(new ShopMyProductsFragment(shopID));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getItemCount() {
        return 2;
    }


    public void reloadFragmentView(int position) {
        Fragment fragment = fragmentList.get(position);
        if (fragment instanceof ShopMyProductsFragment) {
            ((ShopMyProductsFragment) fragment).ReloadData(shopID);
        }
    }
}
