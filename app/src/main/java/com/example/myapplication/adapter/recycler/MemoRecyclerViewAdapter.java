package com.example.myapplication.adapter.recycler;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.vo.ReadingVo;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MemoRecyclerViewAdapter extends RecyclerView.Adapter<MemoRecyclerViewAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    private ArrayList<ReadingVo> itemList;
    ReadingVo itemVo = new ReadingVo();
    private Context context;
    GestureDetector mGestureDetector;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ArrayList<ReadingVo> itemList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public MemoRecyclerViewAdapter(Context context, ArrayList<ReadingVo> itemList) {
        this.context = context;
        this.itemList = itemList;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && onItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            onItemClickListener.onItemClick(childView, view.getChildAdapterPosition(childView), itemList);
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_reading_record_memo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    // 메모 추가시 RecyclerView 에 추가
    public void addItem(ReadingVo vo) {
        itemList.add(vo);
        notifyItemInserted(itemList.size() - 1);
    }

    // 메모 수정시 RecyclerView 에 수정
    public void updateItem(int position, ReadingVo vo) {
        itemList.set(position, vo);
        notifyItemChanged(position);
    }

    // 메모 삭제시 RecyclerView 에 삭제
    public void removeItem(int position) {
        ReadingVo item = getItem(position);
        if (item != null) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    ReadingVo getItem(int position) {
        return itemList.get(position);
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView readingpagenumber;
        public TextView addMemo;
        public TextView upddate;
        public TextView memo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // 화면에 표시될 View(Layout 이 inflate 된)로부터 위젯에 대한 참조 획득
            readingpagenumber = (TextView) itemView.findViewById(R.id.readingpagenumber) ;
            upddate = (TextView) itemView.findViewById(R.id.upddate) ;
            memo = (TextView) itemView.findViewById(R.id.memo) ;
            addMemo = (TextView) itemView.findViewById(R.id.addMemo) ;

            // 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ReadingVo vo = itemList.get(position);

                    if (position != RecyclerView.NO_POSITION) {
                        Log.e("메모 :" , "리사이클" + view.getTag());
                        onItemClickListener.onItemClick(view, position, itemList);
                        Log.e("메모 :" , "리사이클");
                    }
                }
            });
        }

        public void onBind(int position) {
            itemVo = itemList.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            readingpagenumber.setText("P." + itemVo.getReadingpagenumber());
            upddate.setText(itemVo.getUpdDate());
            memo.setText(itemVo.getMemo());
        }

        public void onBind(int position, Object item) {
            itemVo = itemList.get(position);
            ReadingVo tmpVo = (ReadingVo)item;
            // 아이템 내 각 위젯에 데이터 반영
            readingpagenumber.setText("P." + itemVo.getReadingpagenumber());
            upddate.setText(itemVo.getUpdDate());
            memo.setText(itemVo.getMemo());
        }
    }
}



