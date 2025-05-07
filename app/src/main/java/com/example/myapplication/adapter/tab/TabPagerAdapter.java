package com.example.myapplication.adapter.tab;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.R;
import com.example.myapplication.fragment.LibraryFragment;

import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList;

    public TabPagerAdapter(FragmentManager manager, List<Fragment> fragmentList){
        super(manager);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {

        return fragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //return POSITION_NONE;

        if (object.getClass().getName().trim().equals("com.example.myapplication.fragment.LibraryFragment")
                ||object.getClass().getName().trim().equals("com.example.myapplication.fragment.ReadingRecordFragment")){
            return POSITION_NONE;
        }else {
            return super.getItemPosition(object);
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        /*
        if (object.getClass().getName().trim().equals("com.example.myapplication.fragment.LibraryFragment")
                ||object.getClass().getName().trim().equals("com.example.myapplication.fragment.ReadingRecordFragment")) {
            super.destroyItem(container, position, object);
        }
        */
        super.destroyItem(container, position, object);
    }
}
