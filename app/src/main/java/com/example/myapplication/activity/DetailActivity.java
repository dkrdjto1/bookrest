package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.recycler.BookAllListRecyclerViewAdapter;
import com.example.myapplication.vo.InterParkItemVo;

public class DetailActivity extends AppCompatActivity {

    String title;
    String link;
    String image;
    String author;
    String description;
    String publisher;
    String coverSmallUrl;
    String coverLargeUrl;
    String isbn;
    String itemId;
    Toolbar toolbar;
    String itemPosition;
    InterParkItemVo interParkItemVo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        // Get the result of title
        title = intent.getStringExtra("title");
        // Get the result of link
        link = intent.getStringExtra("link");
        // Get the result of image
        image = intent.getStringExtra("image");
        // Get the result of author
        author = intent.getStringExtra("author");
        // Get the result of description
        description = intent.getStringExtra("description");
        // Get the result of publisher
        publisher = intent.getStringExtra("publisher");
        coverSmallUrl = intent.getStringExtra("coverSmallUrl");
        coverLargeUrl = intent.getStringExtra("coverLargeUrl");
        isbn = intent.getStringExtra("isbn");
        itemId = intent.getStringExtra("itemId");
        itemPosition = intent.getStringExtra("position");

        // Toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView txttitle = (TextView) findViewById(R.id.title);
        TextView txtauthor = (TextView) findViewById(R.id.author);
        TextView txtdescription = (TextView) findViewById(R.id.description);
        TextView txtpublisher = (TextView) findViewById(R.id.publisher);
        TextView bookshoplink = (TextView) findViewById(R.id.bookshop_link);
        TextView addBookcase = (TextView) findViewById(R.id.addBookcase);
        TextView addReadingRecord = (TextView) findViewById(R.id.addReadingRecord);

        ImageView imageView = (ImageView) findViewById(R.id.image);

        // Set results to the TextViews
        txttitle.setText(title);
        txtauthor.setText(author);
        txtdescription.setText(description);
        txtpublisher.setText(publisher);

        Glide.with(this).load(image).into(imageView);


        // 서재 TABLE 에 이미 존재할 경우 서재 추가 버튼 활성화/비활성화 처리
        interParkItemVo = MainActivity.sQLiteHelper.selectColumn(itemId);
        if (interParkItemVo.getItemId() != null) {
            addBookcase.setVisibility(View.GONE);
            addReadingRecord.setVisibility(View.VISIBLE);
        } else {
            addBookcase.setVisibility(View.VISIBLE);
            addReadingRecord.setVisibility(View.GONE);
        }

        // 기록하기 버튼
       addReadingRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadingRecdActivity.class);
                intent.putExtra("itemId",itemId);
                intent.putExtra("position", String.valueOf(itemPosition));
                startActivity(intent);
            }
       });

        // 서재 추가 버튼
        addBookcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterParkItemVo vo = MainActivity.sQLiteHelper.selectColumn(itemId);

                if(itemId != null && vo.getItemId() == null) {
                    long result = MainActivity.sQLiteHelper.insertColumn(itemId, title, description, coverSmallUrl, coverLargeUrl, publisher, author, link, isbn);
                    Toast.makeText(getApplicationContext(), "서재에 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                    MainActivity.tabPagerAdapter.notifyDataSetChanged();
                    addBookcase.setVisibility(View.GONE);
                    addReadingRecord.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "서재에 존재합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 도서 구매 버튼
        bookshoplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(itemPosition != "") {
                    Intent intent = new Intent();
                    intent.putExtra("position", itemPosition);
                    setResult(MainActivity.REQUEST_CODE, intent);
                    finish();
                }
                return true;
        }
        return false;
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
