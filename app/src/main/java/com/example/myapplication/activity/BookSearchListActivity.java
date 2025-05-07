package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.recycler.BookAllListRecyclerViewAdapter;
import com.example.myapplication.api.InterParkRestAPI;
import com.example.myapplication.listener.PaginationListener;
import com.example.myapplication.vo.InterParkBookVo;
import com.example.myapplication.vo.InterParkItemVo;

import java.util.ArrayList;

import butterknife.BindView;
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
 * 카테고리 탭에서 검색 시 사용하는 클래스
 */
public class BookSearchListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //요청변수
    String queryType = "all";
    String searchTarget= "book";
    String start = "1";
    String maxResults = "20"; // 한 페이지에 표시할 도서 개수
    String sort = "accuracy";
    String categoryId = "100"; // 국내도서
    String output = "json";
    String inputEncoding ="utf-8";
    String callback = "";
    String adultImageExposure = "n";
    String soldOut = "y";
    String query = "";

    // 인터파크 API
    InterParkRestAPI interParkRestAPI;
    String INTERPARK_BASE_URL = "http://book.interpark.com/";
    String INTERPARK_KEY = "2C63E2D3201272C4309D7385E7169A9D3CFB38F7FB37B90F4F21AC8651AB0FF0";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private BookAllListRecyclerViewAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 20;
    private boolean isLoading = false;
    int itemCount = 0;
    TextView viewText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

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

        // 메인 페이지에서 검색 클릭시 검색 query 을 얻어 옴
        Intent intent = new Intent(this.getIntent());
        query = intent.getStringExtra("query");
        // 검색창에 query 값 설정
        TextView editTextSearch = (TextView) findViewById(R.id.editTextSearch);
        editTextSearch.setText(query);
        // 검색 결과가 없을 시 표시
        viewText = (TextView) findViewById(R.id.viewText);
        
        // 인터파크 Retrofit 초기화
        initInterParkRestAPI();
        // query 검색
        interParkSearchApiCall(query, categoryId, 1);

        // 검색
        editTextSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        query = editTextSearch.getText().toString();
                        onRefresh();
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        // 페이징 처리
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                interParkSearchApiCall(query, categoryId, currentPage);
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
                intent.putExtra("position", String.valueOf(position));
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
        interParkSearchApiCall(query, categoryId, currentPage);
    }

    // 상세보기 화면에서 뒤로 가기 했을때 adapter 의 position 에 해당하는 more 아이콘 처리
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
     * 인터파크 검색 API 호출 페이징 처리
     * @param query
     * @param categoryId
     * @param startPage
     */
    private void interParkSearchApiCall(String query, String categoryId, int startPage) {
        final ArrayList<InterParkItemVo> items  = new ArrayList<>();

        start = String.valueOf(startPage);

        // 인터파크 추천 도서 조회(국내도서)
        ArrayList<InterParkItemVo> list = interParkSearchApi(INTERPARK_KEY,  query
                , queryType
                , searchTarget
                , start
                , maxResults
                , sort
                , categoryId
                , output
                , inputEncoding
                , callback
                , adultImageExposure
                , soldOut);;
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

                if(totalPage == 0 && items.size() > 0)adapter.removeLoading();
            }
        }, 1000);
    }

    /**
     * 인터파크 검색 API 호출
     * @param key
     * @param query
     * @param queryType
     * @param searchTarget
     * @param start
     * @param maxResults
     * @param sort
     * @param categoryId
     * @param output
     * @param inputEncoding
     * @param callback
     * @param adultImageExposure
     * @param soldOut
     * @return
     */
    public ArrayList interParkSearchApi(String key
            , String query
            , String queryType
            , String searchTarget
            , String start
            , String maxResults
            , String sort
            , String categoryId
            , String output
            , String inputEncoding
            , String callback
            , String adultImageExposure
            , String soldOut){

        final ArrayList<InterParkItemVo> list = new ArrayList<>();

        Call<InterParkBookVo> getCall = interParkRestAPI.getSearchApi(key
                , query
                , queryType
                , searchTarget
                , start
                , maxResults
                , sort
                , categoryId
                , output
                , inputEncoding
                , callback
                , adultImageExposure
                , soldOut);

        getCall.enqueue(new Callback<InterParkBookVo>() {
            @Override
            public void onResponse(Call<InterParkBookVo> call, Response<InterParkBookVo> response) {
                if (response.isSuccessful()) {
                    ArrayList<InterParkItemVo> items = response.body().getItems();
                    String totalResults = response.body().getTotalResults();

                    // 전체 페이지
                    int tmp = (int) Math.ceil(Integer.parseInt(totalResults) / Integer.parseInt(maxResults)) ;
                    totalPage = Integer.parseInt( String.valueOf( tmp ) );

                    if(Integer.parseInt(totalResults) < 1) {
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
