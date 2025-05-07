package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.DetailActivity;
import com.example.myapplication.activity.ReadingRecdActivity;
import com.example.myapplication.adapter.LibraryAdapter;
import com.example.myapplication.adapter.recycler.HorizontalRecyclerViewAdapter;
import com.example.myapplication.api.InterParkRestAPI;
import com.example.myapplication.vo.InterParkBookVo;
import com.example.myapplication.vo.InterParkItemVo;
import com.example.myapplication.vo.ReadingVo;

import java.util.ArrayList;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryFragment extends Fragment {

    //요청변수
    static ArrayList<InterParkItemVo> items;

    View view;
    GridView gridView;
    LinearLayout linearLayoutEmpty;
    InterParkItemVo vo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library, container, false);

        // 전체삭제
        TextView textViewAllDelete = (TextView) view.findViewById(R.id.textViewAllDelete);

        items =  MainActivity.sQLiteHelper.selectColumns();

        gridView = (GridView) view.findViewById(R.id.gridView);
        LibraryAdapter libraryAdapter = new LibraryAdapter(getActivity(), R.layout.item_book_large_image, items);
        gridView.setAdapter(libraryAdapter);
        linearLayoutEmpty = (LinearLayout) view.findViewById(R.id.linearLayoutEmpty);
        // 검색 결과가 없으면
        if(items.size() < 1){
            // 그리드뷰 비활성화
            gridView.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            textViewAllDelete.setVisibility(View.GONE);
        } else {
            // 결과 값이 존재하면 linearLayoutEmpty 비활성화
            linearLayoutEmpty.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            textViewAllDelete.setVisibility(View.VISIBLE);

            // 그리드뷰에서 이미지 클릭 시
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    vo = items.get(position);

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("title", vo.getTitle());
                    intent.putExtra("link", vo.getLink());
                    intent.putExtra("author", vo.getAuthor());
                    intent.putExtra("description", vo.getDescription());
                    intent.putExtra("publisher", vo.getPublisher());
                    intent.putExtra("image", vo.getCoverLargeUrl());
                    intent.putExtra("coverSmallUrl", vo.getCoverSmallUrl());
                    intent.putExtra("coverLargeUrl", vo.getCoverLargeUrl());
                    intent.putExtra("isbn", vo.getIsbn());
                    intent.putExtra("itemId", vo.getItemId());
                    intent.putExtra("position", "");
                    getActivity().startActivity(intent);
                }
            });

            // 그리드뷰에서 이미지 길게 누르고 있을 시
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    vo = items.get(position);
                    // 그리드뷰에서 이미지 길게 누르고 있을 시 context menu 사용
                    registerForContextMenu(gridView);

                    return false;
                }
            });

            // 전체삭제 클릭 시
            textViewAllDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("서재 전체삭제")
                            .setMessage("전체 삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인 시 처리 로직
                                    MainActivity.sQLiteHelper.deleteColumns();
                                    MainActivity.sQLiteHelper.deleteReadingColumns();
                                    Toast.makeText(getActivity(), "서재에서 전체 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                    MainActivity.tabPagerAdapter.notifyDataSetChanged();
                                }})
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소 시 처리 로직
                                    Toast.makeText(getActivity(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }})
                            .show();
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_library_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.regster: // 독서기록하기

                Intent intent = new Intent(getActivity(), ReadingRecdActivity.class);
                intent.putExtra("itemId", vo.getItemId());
                intent.putExtra("position", "");
                getActivity().startActivity(intent);

                return true;
            case R.id.delete: // 서재에서 삭제하기

                ArrayList<ReadingVo> readingVoList = MainActivity.sQLiteHelper.selectReadingColumns(vo.getItemId());
                if(readingVoList.size() > 0){ // 해당 도서에 기존에 작성한 메모가 존재할 경우
                    new AlertDialog.Builder(getActivity())
                            .setTitle("서재 및 메모 전체삭제")
                            .setMessage("삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    MainActivity.sQLiteHelper.deleteReadingItemsColumn(vo.getItemId());
                                    MainActivity.sQLiteHelper.deleteColumn(vo.getItemId());
                                    Toast.makeText(getActivity(), "서재 및 메모가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                    MainActivity.tabPagerAdapter.notifyDataSetChanged();
                                }})
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소시 처리 로직
                                    Toast.makeText(getActivity(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }})
                            .show();
                }else {
                    MainActivity.sQLiteHelper.deleteColumn(vo.getItemId());
                    Toast.makeText(getActivity(), "서재에서 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    MainActivity.tabPagerAdapter.notifyDataSetChanged();
                }
                return true;
        }

        return super.onContextItemSelected(item);
    }
}