package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;

import com.example.myapplication.adapter.tab.TabPagerAdapter;
import com.example.myapplication.db.sqllite.SQLiteHelper;
import com.example.myapplication.fragment.BestSellerFragment;
import com.example.myapplication.fragment.CategoryFragment;
import com.example.myapplication.fragment.LibraryFragment;
import com.example.myapplication.fragment.ReadingRecordFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10101";
    public static SQLiteHelper sQLiteHelper;
    TabLayout tabLayout;
    ViewPager viewPager;
    public static TabPagerAdapter tabPagerAdapter;
    public static int REQUEST_CODE = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SQLite DB 연결 시작
        sQLiteHelper = new SQLiteHelper(this);
        sQLiteHelper.open();
        sQLiteHelper.create();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 각 TAB 에 들어갈 Fragment 설정
        List<Fragment> fragmentList = new ArrayList<>();
        // 베스트셀러
        fragmentList.add(new BestSellerFragment());
        // 카테고리
        fragmentList.add(new CategoryFragment());
        // 서재
        fragmentList.add(new LibraryFragment());
        // 독서기록
        fragmentList.add(new ReadingRecordFragment());

        // TabPagerAdapter 생성 후 ViewPager 에 설정
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(tabPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // TabLayout 에 TAB 추가
        tabLayout.addTab(tabLayout.newTab().setText("베스트셀러"));
        tabLayout.addTab(tabLayout.newTab().setText("카테고리"));
        tabLayout.addTab(tabLayout.newTab().setText("서재"));
        tabLayout.addTab(tabLayout.newTab().setText("독서기록"));
        tabLayout.setTabTextColors(Color.LTGRAY, 0xFFFF7E4A);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        // 베스트셀러 TAB
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 1:
                        // 카테고리 TAB
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 2:
                        // 서재 TAB
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 3:
                        // 독서기록 TAB
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    protected void onDestroy() {
        // SQLite DB 연결 종료
        sQLiteHelper.close();
        super.onDestroy();
    }
}