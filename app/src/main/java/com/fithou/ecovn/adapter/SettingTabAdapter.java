package com.fithou.ecovn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fithou.ecovn.view.setting.InformationSettingFragment;
import com.fithou.ecovn.view.setting.SetupSettingFragment;

import java.util.ArrayList;
import java.util.List;

public class SettingTabAdapter extends FragmentStateAdapter {
    private List<Fragment> fragList;


    public SettingTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragList = new ArrayList<>();
        fragList.add(new InformationSettingFragment());
        fragList.add(new SetupSettingFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragList.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
