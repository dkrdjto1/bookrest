package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.vo.InterParkItemVo;

import java.util.ArrayList;

/**
 * 서재 탭에서 그리드뷰에 이미지를 표시하기 위한 클래스
 */
public class LibraryAdapter extends BaseAdapter {
    Context context;
    int layout;
    LayoutInflater inflater;
    ArrayList<InterParkItemVo> itemList;
    InterParkItemVo bookVo = new InterParkItemVo();

    public LibraryAdapter(Context context, int layout, ArrayList<InterParkItemVo> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public InterParkItemVo getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inflater.inflate(layout, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        Glide.with(context).load(itemList.get(position).getCoverLargeUrl()).into(imageView);

        return convertView;
    }
}
