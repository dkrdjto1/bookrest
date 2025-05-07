package com.example.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.recycler.MemoRecyclerViewAdapter;
import com.example.myapplication.vo.InterParkItemVo;
import com.example.myapplication.vo.ReadingVo;

import java.util.ArrayList;

public class ReadingRecdActivity extends AppCompatActivity {

    String itemId;
    String itemPosition;
    String titleStr;
    InterParkItemVo interParkItemVo;
    Toolbar toolbar;

    TextView title;
    TextView author;
    TextView publisher;
    ImageView image;
    ImageView menu;
    RecyclerView recyclerListview;
    MemoRecyclerViewAdapter adapter;
    ArrayList<ReadingVo> items;
    LinearLayout linearLayoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_recd);

        // Toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        publisher = (TextView) findViewById(R.id.publisher);
        image = (ImageView) findViewById(R.id.image);
        menu = (ImageView) findViewById(R.id.menu);
        linearLayoutEmpty = (LinearLayout) findViewById(R.id.linearLayoutEmpty);

        Intent intent = getIntent();
        // Get the result of title
        itemId = intent.getStringExtra("itemId");
        itemPosition = intent.getStringExtra("position");

        interParkItemVo = MainActivity.sQLiteHelper.selectColumn(itemId);
        titleStr = interParkItemVo.getTitle();

        // Set results to the TextViews
        title.setText(interParkItemVo.getTitle());
        author.setText(interParkItemVo.getAuthor());
        publisher.setText(interParkItemVo.getPublisher());
        Glide.with(this).load(interParkItemVo.getCoverLargeUrl()).into(image);

        // 메모 목록
        items =  MainActivity.sQLiteHelper.selectReadingColumns(itemId);
        recyclerListview = (RecyclerView) findViewById(R.id.listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerListview.setLayoutManager(layoutManager);

        items =  MainActivity.sQLiteHelper.selectReadingColumns(itemId);
        adapter = new MemoRecyclerViewAdapter(this, items);
        recyclerListview.setAdapter(adapter);

        if(items.size() > 0) {
            recyclerListview.setVisibility(View.VISIBLE);
            linearLayoutEmpty.setVisibility(View.GONE);
        } else{
            recyclerListview.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.VISIBLE);
        }

        // RecyclerView 클릭 이벤트
        adapter.setOnItemClickListener(new MemoRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<ReadingVo> itemList) {

                TextView addMemo = view.findViewById(R.id.addMemo);
                // 점 세 개 이미지(more) 클릭 시 option menu 사용하기
                addMemo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 옵션 메뉴 생성
                        openOptionMenu(view, position, itemList.get(position));
                    }
                });
            }
        });

        // + 클릭 이벤트
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Object 를 리턴하기 위한 Intent
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("itemId", itemId);
                intent.putExtra("title", titleStr);
                intent.putExtra("idx", "");
                startActivityForResult(intent , MainActivity.REQUEST_CODE);
            }
        });
    }

    /**
     * more 에 옵션 메뉴 생성
     * @param v
     * @param position
     * @param vo
     */
    public void openOptionMenu(View v,final int position, ReadingVo vo){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.context_readingrecord_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                switch (item.getItemId()) {
                    case R.id.edit:
                        // 아이템 수정하기
                        intent.putExtra("position", String.valueOf(position));
                        intent.putExtra("idx", vo.getIdx());
                        intent.putExtra("title", titleStr);
                        intent.putExtra("itemId", vo.getItemId());
                        intent.putExtra("readingpagenumber", vo.getReadingpagenumber());
                        intent.putExtra("memo", vo.getMemo());
                        intent.putExtra("alarm", vo.getAlarm());
                        startActivityForResult(intent , MainActivity.REQUEST_CODE);
                        return true;
                    case R.id.delete:
                        // 아이템 삭제하기
                        MainActivity.sQLiteHelper.deleteReadingColumn(vo.getIdx(), vo.getItemId());
                        Toast.makeText(getApplicationContext(), "기록을 삭제 하였습니다.", Toast.LENGTH_SHORT).show();

                        adapter.removeItem(position);
                        // 독서기록 탭 갱신
                        MainActivity.tabPagerAdapter.notifyDataSetChanged();
                        // 메모 목록 갱신
                        adapter.notifyDataSetChanged();
                        // 메모 목록이 없을 경우
                        if(adapter.getItemCount() < 1){
                            recyclerListview.setVisibility(View.GONE);
                            linearLayoutEmpty.setVisibility(View.VISIBLE);
                        }

                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    // Activity 결과 전달 받음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MainActivity.REQUEST_CODE) {

            // intent 가 null 아닐 경우만
            if(data != null) {
                // 추가
                if ("I".equals(data.getStringExtra("FLAG"))) { // INSERT(새롭게 메모 추가)
                    // 메모 목록이 없을 경우 (상세보기에서 서재 추가 후 독서기록할 경우)
                    if(items.size() < 1) {
                        recyclerListview.setVisibility(View.VISIBLE);
                        linearLayoutEmpty.setVisibility(View.GONE);
                    }
                    // 메모 목록에 추가
                    adapter.addItem((ReadingVo) data.getSerializableExtra("vo"));
                } else {
                    // 수정
                    adapter.updateItem(Integer.parseInt(data.getStringExtra("position"))
                            , (ReadingVo) data.getSerializableExtra("vo"));
                }
                // 메모 목록 갱신
                adapter.notifyDataSetChanged();
                // 독서기록 탭 갱신
                MainActivity.tabPagerAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // toolbar 의 back 키 눌렀을 때 동작
            case android.R.id.home:{
                if(itemPosition != "") {
                    Intent intent = new Intent();
                    intent.putExtra("position", itemPosition);
                    setResult(MainActivity.REQUEST_CODE, intent);
                }
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}