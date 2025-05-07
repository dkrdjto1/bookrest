package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.DetailActivity;
import com.example.myapplication.activity.ReadingRecdActivity;
import com.example.myapplication.adapter.recycler.BookAllListRecyclerViewAdapter;
import com.example.myapplication.adapter.recycler.BookListRecyclerViewAdapter;
import com.example.myapplication.adapter.recycler.ReadingRecordRecyclerViewAdapter;
import com.example.myapplication.vo.InterParkItemVo;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReadingRecordFragment extends Fragment {

    View view;
    static ArrayList<InterParkItemVo> items;
    ReadingRecordRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reading_record, container, false);

        RecyclerView recyclerListview = (RecyclerView) view.findViewById(R.id.listview);
        LinearLayout linearLayoutEmpty = (LinearLayout) view.findViewById(R.id.linearLayoutEmpty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerListview.setLayoutManager(layoutManager);

        items =  MainActivity.sQLiteHelper.selectReadingRecordColumns();
        adapter = new ReadingRecordRecyclerViewAdapter(getActivity(), items);
        recyclerListview.setAdapter(adapter);

        if(items.size() > 0){ // 서재에 도서 아이템이 있으면
            // 도서 목록 RecyclerView 활성화
            recyclerListview.setVisibility(View.VISIBLE);
            linearLayoutEmpty.setVisibility(View.GONE);
        } else{
            // 도서 목록 RecyclerView 비활성화
            recyclerListview.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.VISIBLE);
        }

        adapter.setOnItemClickListener(new ReadingRecordRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, InterParkItemVo vo) {
                Intent intent = new Intent(getActivity(), ReadingRecdActivity.class);
                intent.putExtra("title", vo.getTitle());
                intent.putExtra("link", vo.getLink());
                intent.putExtra("author",vo.getAuthor());
                intent.putExtra("description",vo.getDescription());
                intent.putExtra("publisher",vo.getPublisher());
                intent.putExtra("image", vo.getCoverLargeUrl());
                intent.putExtra("coverSmallUrl", vo.getCoverSmallUrl());
                intent.putExtra("coverLargeUrl", vo.getCoverLargeUrl());
                intent.putExtra("isbn", vo.getIsbn());
                intent.putExtra("itemId", vo.getItemId());
                intent.putExtra("position", String.valueOf(position));
                startActivityForResult(intent, MainActivity.REQUEST_CODE);
            }
        });

        return view;
    }
}
