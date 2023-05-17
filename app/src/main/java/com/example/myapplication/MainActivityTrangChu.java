package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivityTrangChu extends AppCompatActivity {
  //  ViewPager viewPager;
    ViewPager2 viewPager;
    TabLayout tablayout;
    TabViewPagerAdapterHome tabViewPagerAdapter;
    ImageView ivTimKiem, ivSetting;
    BottomNavigationView bottom_nav;
    static int check = 0;
    ArrayList<Truyen> data_temp = new ArrayList<>();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trangchu);
        context = this;
        setControl();
        setEvent();
    }

    private void setEvent() {
        setUpViewPager();
        ivTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityTrangChu.this, SearchHome.class);
                if (check == 0){
                    data_temp.clear();
                    data_temp.addAll(KeSachFragment.data_Truyen);
                    intent.putExtra("data_Truyen",data_temp);
                }
                else{
                    data_temp.clear();
                    data_temp.addAll(LichSuFragment.data_Truyen);
                    intent.putExtra("data_Truyen",data_temp);
                }

                startActivity(intent);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRemove();
            }
        });

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_kham_pha:
                        Intent intent = new Intent(MainActivityTrangChu.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_ca_nhan:
                        Intent intent2 = new Intent(MainActivityTrangChu.this, UserActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.action_cong_dong:
                        Intent intent3 = new Intent(MainActivityTrangChu.this, ChatActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
    private void DialogRemove() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_remove);
        dialog.setCanceledOnTouchOutside(false);

        TextView textView = dialog.findViewById(R.id.tvTitle);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        if (check == 0){
            textView.setText("Bạn muốn xóa tất cả truyện trong kệ sách?");
        }
        else
            textView.setText("Bạn muốn xóa tất cả truyện trong lịch sử?");
        dialog.show();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 0){
                    if (KeSachFragment.data_Truyen.size() > 0){
                        KeSachFragment.data_Truyen.clear();
                        KeSachFragment.adapter_Truyen.notifyDataSetChanged();
                        KeSachFragment.saveKeSach(context);
                        Toast.makeText(MainActivityTrangChu.this, "Đã xóa truyện", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(MainActivityTrangChu.this, "Không có truyện để xóa", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (LichSuFragment.data_Truyen.size() > 0){
                        LichSuFragment.data_Truyen.clear();
                        LichSuFragment.adapter_Truyen.notifyDataSetChanged();
                        LichSuFragment.saveLichSu(context);
                        Toast.makeText(MainActivityTrangChu.this, "Đã xóa truyện", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(MainActivityTrangChu.this, "Không có truyện để xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void setUpViewPager() {
        tabViewPagerAdapter = new TabViewPagerAdapterHome(this);

        viewPager.setAdapter(tabViewPagerAdapter);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                check = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tablayout.getTabAt(position).select();
            }
        });
    }
    private void setControl() {
        ivTimKiem = findViewById(R.id.ivTimKiem);
        ivSetting = findViewById(R.id.ivSetting);
        bottom_nav = findViewById(R.id.bottom_nav);
        tablayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPagerTab1);
    }
}