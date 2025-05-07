package com.example.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.recycler.BookAllListRecyclerViewAdapter;
import com.example.myapplication.adapter.recycler.HorizontalRecyclerViewAdapter;
import com.example.myapplication.api.InterParkRestAPI;
import com.example.myapplication.listener.PaginationListener;
import com.example.myapplication.vo.InterParkBookVo;
import com.example.myapplication.vo.InterParkItemVo;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.listener.PaginationListener.PAGE_START;

/**
 * 베스트셀러 탭에서 전체보기 및 더보기 클릭 시 사용하는 클래스
 */
public class BestSellerAndReCommendAllListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // 요청변수
    String start = "1";
    String maxResults = "100"; // 한 페이지에 표시할 도서 개수
    String categoryId = "100";
    String output = "json";
    String gubun = "1";

    // 인터파크 API
    InterParkRestAPI interParkRestAPI;
    String INTERPARK_BASE_URL = "http://book.interpark.com/";
    String INTERPARK_KEY = "2C63E2D3201272C4309D7385E7169A9D3CFB38F7FB37B90F4F21AC8651AB0FF0";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private BookAllListRecyclerViewAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 100;
    private boolean isLoading = false;
    int itemCount = 0;
    TextView viewText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller_and_re_commend_list);

        ButterKnife.bind(this);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookAllListRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 메인 페이지에서 전체보기 클릭 시 검색 categoryId 을 얻어 옴
        Intent intent = new Intent(this.getIntent());
        gubun = intent.getStringExtra("gubun");

        // 검색 결과가 없을 시 표시
        viewText = (TextView) findViewById(R.id.viewText);

        // 인터파크 Retrofit 초기화
        initInterParkRestAPI();
        interParkApiCall(gubun, currentPage);

        // 페이징 처리
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                interParkApiCall(gubun, currentPage);
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        adapter.setOnItemClickListener(new BookAllListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, InterParkItemVo vo) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
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
                intent.putExtra("position", String.valueOf( position ) );
                startActivityForResult(intent, MainActivity.REQUEST_CODE);
            }
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        interParkApiCall(gubun, currentPage);
    }

    // 상세보기 화면에서 뒤로 가기 했을 때 adapter 의 position 에 해당하는 more 아이콘 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MainActivity.REQUEST_CODE) {
            if(data.getStringExtra("position") != "") {
                int pos = Integer.parseInt(data.getStringExtra("position"));
                adapter.notifyItemChanged(pos);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 페이징 처리
     * @param gubun
     * @param startPage
     * @param startPage
     */
    private void interParkApiCall(String gubun, int startPage) {
        final ArrayList<InterParkItemVo> items  = new ArrayList<>();

        start = String.valueOf(startPage);

        // 인터파크 추천도서 조회(국내도서)
        ArrayList<InterParkItemVo> list;

        if("1".equals(gubun)) {
            // 베스트셀러 목록
            list = InterParkBestSellerApi(INTERPARK_KEY, categoryId, output);
        } else {
            // 추천 도서 목록
            list = InterParkRecommendApi(INTERPARK_KEY, categoryId, output);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(InterParkItemVo vo : list){
                    itemCount++;
                    items.add(vo);
                }

                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START) adapter.removeLoading();
                adapter.addItems(items);
                swipeRefresh.setRefreshing(false);
                // check weather is last page or not
                if (currentPage < totalPage) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
            }
        }, 1000);
    }

    /**
     * 인터파크 베스트셀러
     * @param key
     * @param categoryId
     * @param output
     */
    public ArrayList InterParkBestSellerApi(String key, String categoryId, String output){

        final ArrayList<InterParkItemVo> list = new ArrayList<>();

        Call<InterParkBookVo> getCall = interParkRestAPI.getBestSellerApi(key, categoryId, output);

        getCall.enqueue(new Callback<InterParkBookVo>() {
            @Override
            public void onResponse(Call<InterParkBookVo> call, Response<InterParkBookVo> response) {
                if (response.isSuccessful()) {
                    ArrayList<InterParkItemVo> items = response.body().getItems();
                    String totalResults = response.body().getTotalResults();

                    // 전체 페이지
                    totalPage = Integer.parseInt(totalResults) / Integer.parseInt(maxResults);

                    if (Integer.parseInt(totalResults) < 1) {
                        swipeRefresh.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        viewText.setVisibility(View.VISIBLE);
                        viewText.setText("검색결과가 없습니다.");
                    } else {
                        swipeRefresh.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        viewText.setVisibility(View.GONE);
                    }

                    for(InterParkItemVo vo : items) {
                        list.add(vo);
                    }

                } else {
                    Log.i("TAG", "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<InterParkBookVo> call, Throwable t) {
                Log.i("TAG", "Status Code : " + t.getMessage());
            }
        });

        return list;
    }


    /**
     * 인터파크 추천도서
     * @param key
     * @param categoryId
     * @param output
     */
    public ArrayList InterParkRecommendApi(String key, String categoryId, String output){

        final ArrayList<InterParkItemVo> list = new ArrayList<>();

        Call<InterParkBookVo> getCall = interParkRestAPI.getRecommendApi(key, categoryId, output);

        getCall.enqueue(new Callback<InterParkBookVo>() {
            @Override
            public void onResponse(Call<InterParkBookVo> call, Response<InterParkBookVo> response) {
                if (response.isSuccessful()) {
                    ArrayList<InterParkItemVo> items = response.body().getItems();
                    String totalResults = response.body().getTotalResults();

                    // 전체 페이지
                    totalPage = Integer.parseInt(totalResults) / Integer.parseInt(maxResults);

                    if (Integer.parseInt(totalResults) < 1) {
                        swipeRefresh.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        viewText.setVisibility(View.VISIBLE);
                        viewText.setText("검색결과가 없습니다.");
                    } else {
                        swipeRefresh.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        viewText.setVisibility(View.GONE);
                    }

                    for (InterParkItemVo vo : items) {
                        list.add(vo);
                    }

                } else {
                    Log.i("TAG", "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<InterParkBookVo> call, Throwable t) {
                Log.i("TAG", "Status Code : " + t.getMessage());
            }
        });

        return list;
    }

    /**
     * 인터파크 API 초기화
     */
    private void initInterParkRestAPI(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(INTERPARK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interParkRestAPI = retrofit.create(InterParkRestAPI.class);
    }

    /**
     * API 통신 로그
     * @return
     */
    private HttpLoggingInterceptor httpLoggingInterceptor(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("API :", message + "");
            }
        });

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // toolbar 의 back 키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}