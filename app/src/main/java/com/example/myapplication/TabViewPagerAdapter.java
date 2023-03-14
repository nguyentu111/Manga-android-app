package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.tabs.tab1;
import com.example.myapplication.tabs.tab2;
import com.example.myapplication.tabs.tab3;
import com.example.myapplication.tabs.tab4;

public class TabViewPagerAdapter extends FragmentStateAdapter {

    public TabViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new tab1();
            case 1:
                return  new tab2();
            case 2:
                return new tab3();
            default:
                return new tab4();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
