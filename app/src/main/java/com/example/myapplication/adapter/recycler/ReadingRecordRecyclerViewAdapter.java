package com.example.myapplication.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.BookAllListActivity;
import com.example.myapplication.activity.DetailActivity;
import com.example.myapplication.vo.InterParkBookVo;
import com.example.myapplication.vo.InterParkItemVo;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ReadingRecordRecyclerViewAdapter extends RecyclerView.Adapter<ReadingRecordRecyclerViewAdapter.ViewHolder> {

    private List<InterParkItemVo> itemList;
    InterParkItemVo itemVo = new InterParkItemVo();
    private Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, InterParkItemVo interParkItemVo);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ReadingRecordRecyclerViewAdapter(Context context, ArrayList<InterParkItemVo> data) {
        this.context = context;
        this.itemList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_reading_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemVo = itemList.get(position);

        InterParkItemVo item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        holder.publisher.setText(item.getPublisher());
        String memoCount = item.getMemoCount();
        if("0".equals(memoCount)){
            memoCount = "-";
        }
        holder.memoCount.setText(memoCount);
        Glide.with(holder.itemView.getContext()).load(item.getCoverSmallUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public ImageView imageView;
        public TextView publisher;
        public TextView memoCount;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            publisher = itemView.findViewById(R.id.publisher);
            memoCount = itemView.findViewById(R.id.memoCount);
            imageView = itemView.findViewById(R.id.image);

            // 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    InterParkItemVo vo = itemList.get(position);

                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(view, position, vo);
                    }
                }
            });
        }
    }
}



