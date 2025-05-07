package com.example.myapplication.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.BookAllListActivity;
import com.example.myapplication.vo.InterParkBookVo;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * 베스트 셀러, 카테고리 탭에서 사용하는 클래스
 */
public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<BookListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<InterParkBookVo> itemList;
    InterParkBookVo bookVo = new InterParkBookVo();
    private Context context;

    public BookListRecyclerViewAdapter(Context context, ArrayList<InterParkBookVo> data) {
        this.context = context;
        this.itemList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bookVo = itemList.get(position);

        // 타이틀 문자 설정
        holder.title.setText(URLDecoder.decode(bookVo.getSearchCategoryName().replaceAll("국내도서>","")));
        // categoryId 값 설정
        holder.catagoryId.setText(bookVo.getSearchCategoryId());

        // 이미지를 표시할 가로 recyclerView 설정
        HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter(this.context, bookVo.getItems());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView catagoryId;
        public TextView all;
        protected RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewTitle);
            catagoryId = itemView.findViewById(R.id.textViewCatagoryId);
            all = itemView.findViewById(R.id.textViewAll);
            recyclerView = itemView.findViewById(R.id.listview);

            // 카테고리별 전체보기 클릭 이벤트
            all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    InterParkBookVo vo = itemList.get(pos);
                    Log.i("title : " , "" + title.getText());
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, BookAllListActivity.class);
                        intent.putExtra("categoryId", vo.getSearchCategoryId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}


