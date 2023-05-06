package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabViewPagerAdapterHome extends FragmentStateAdapter {

    public TabViewPagerAdapterHome(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new KeSachFragment();
            case 1:
                return new LichSuFragment();
            default:
                return new KeSachFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
