package com.example.myapplication.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.DetailActivity;
import com.example.myapplication.vo.InterParkItemVo;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 베스트 셀러, 카테고리 탭에서 가로 이미지를 표시하기 위한 클래스
 */
public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder> {

    private ArrayList<InterParkItemVo> itemList;
    InterParkItemVo bookVo = new InterParkItemVo();
    private Context context;

    public HorizontalRecyclerViewAdapter(Context context, ArrayList<InterParkItemVo> data) {
        this.context = context;
        this.itemList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_recycler_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bookVo = itemList.get(position);
        Glide.with(holder.itemView.getContext()).load(bookVo.getCoverSmallUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);

            // 상세보기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    InterParkItemVo vo = itemList.get(pos);

                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailActivity.class);
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
                        intent.putExtra("position", "");
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}


