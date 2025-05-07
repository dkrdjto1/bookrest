package com.example.myapplication.adapter.recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.BestSellerAndReCommendAllListActivity;
import com.example.myapplication.activity.BookAllListActivity;
import com.example.myapplication.activity.BookSearchListActivity;
import com.example.myapplication.activity.DetailActivity;
import com.example.myapplication.adapter.BaseViewHolder;
import com.example.myapplication.vo.InterParkItemVo;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 베스트셀러, 카테고리 탭에서 전체보기 클릭 또는 검색 시 BestSellerAndReCommendListActivity, BookAllListActivity, BookSearchListActivity 에서 사용하는 클래스
 */
public class BookAllListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private List<InterParkItemVo> itemList;
    Context  mContext;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, InterParkItemVo interParkItemVo);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public BookAllListRecyclerViewAdapter(Context context, List<InterParkItemVo> interParkItemVos) {
        this.mContext = context;
        this.itemList = interParkItemVos;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_all_list, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == itemList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }
    public void addItems(List<InterParkItemVo> interParkItemVos) {
        itemList.addAll(interParkItemVos);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        itemList.add(new InterParkItemVo());
        notifyItemInserted(itemList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = itemList.size() - 1;
        InterParkItemVo item = getItem(position);
        if (item != null) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    InterParkItemVo getItem(int position) {
        return itemList.get(position);
    }

    public class ViewHolder extends BaseViewHolder implements View.OnCreateContextMenuListener{

        public TextView title;
        public TextView author;
        public ImageView imageView;
        public TextView publisher;
        public TextView libraryAddButton;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            publisher = itemView.findViewById(R.id.publisher);
            imageView = itemView.findViewById(R.id.image);
            libraryAddButton = itemView.findViewById(R.id.libraryAddButton);
            // Context menu
            itemView.setOnCreateContextMenuListener(this);
            // 상세보기
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

        protected void clear() { }

        /**
         * 각각의 리스트에 해당 항목 설정
         * @param position
         */
        public void onBind(int position) {
            super.onBind(position);
            InterParkItemVo item = itemList.get(position);
            title.setText(item.getTitle());
            author.setText(item.getAuthor());
            publisher.setText(item.getPublisher());
            Glide.with(this.itemView.getContext()).load(item.getCoverSmallUrl()).into(this.imageView);

            // 서재 TABLE 에 이미 존재할 경우 서재 추가 버튼 활성화/비활성화 처리
            InterParkItemVo tmpVo = MainActivity.sQLiteHelper.selectColumn(item.getItemId());
            if(tmpVo.getItemId() != null){
                libraryAddButton.setVisibility(View.GONE);
            } else{
                libraryAddButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem add = contextMenu.add(Menu.NONE, R.id.libraryAddButton, 1, "서재에 추가");
            add.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.libraryAddButton:
                        InterParkItemVo vo = itemList.get(getAdapterPosition());
                        InterParkItemVo tmpVo = MainActivity.sQLiteHelper.selectColumn(vo.getItemId());

                        if(vo.getItemId() != null && tmpVo.getItemId() == null) {
                            long result = MainActivity.sQLiteHelper.insertColumn(vo.getItemId(), vo.getTitle(), vo.getDescription(), vo.getCoverSmallUrl(), vo.getCoverLargeUrl(), vo.getPublisher(), vo.getAuthor(), vo.getLink(), vo.getIsbn());
                            Toast.makeText(mContext, "서재에 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                            MainActivity.tabPagerAdapter.notifyDataSetChanged();
                            // 서재에 추가 후 서재 추가 버튼 비활성화
                            libraryAddButton.setVisibility(View.GONE);
                        } else{
                            Toast.makeText(mContext, "서재에 존재합니다.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        };
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @Override
        protected void clear() {
        }
    }
}