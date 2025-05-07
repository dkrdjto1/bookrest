package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.BookSearchListActivity;
import com.example.myapplication.adapter.recycler.BookListRecyclerViewAdapter;
import com.example.myapplication.api.InterParkRestAPI;
import com.example.myapplication.vo.InterParkBookVo;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment {

    // 인터파크 API
    InterParkRestAPI interParkRestAPI;
    String INTERPARK_BASE_URL = "http://book.interpark.com/";
    String INTERPARK_KEY = "2C63E2D3201272C4309D7385E7169A9D3CFB38F7FB37B90F4F21AC8651AB0FF0";

    // 요청변수
    String queryType = "title";
    String searchTarget= "book";
    String start = "1";
    String maxResults = "10";
    String sort = "accuracy";
    String output = "json";
    String inputEncoding ="utf-8";
    String callback = "";
    String adultImageExposure = "n";
    String soldOut = "y";
    static ArrayList<InterParkBookVo> list;

    // 카테고리별 API 호출을 위한 categoryId
    String [] searchList = {"101", "102", "103", "104", "105", "107", "108", "109", "110", "111", "116", "117", "118", "119", "120", "122", "123", "124", "125", "126", "128"};
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        list = new ArrayList<>();
        // 인터파크 API 초기화
        initInterParkRestAPI();
        // 카테고리별 API 호출을 위한 categoryId 수 만큼 호출
        interParkBookSearchAll();

        TextView editTextSearch = (TextView) view.findViewById(R.id.editTextSearch);
        // 검색 액션 설정
        editTextSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        // 검색 버튼 이벤트
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Intent intent = new Intent(getActivity(), BookSearchListActivity.class);
                        Log.i("Query : " , editTextSearch.getText().toString());
                        intent.putExtra("query", editTextSearch.getText().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        return view;
    }

    /**
     * 카테고리 별 API 호출을 위한 categoryId 수 만큼 호출
     */
    public void interParkBookSearchAll(){
        String query = "-";
        for(String str : searchList) {
            interParkSearchApi(INTERPARK_KEY, "-"
                    , queryType
                    , searchTarget
                    , start
                    , maxResults
                    , sort
                    , str
                    , output
                    , inputEncoding
                    , callback
                    , adultImageExposure
                    , soldOut);
        }
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
     */
    public void interParkSearchApi(String key
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
                    InterParkBookVo result = response.body();
                    list.add(result);

                    // 검색 요청한 전체 목록을 API 로 모두 가져온 후 RecyclerView 설정
                    if(list.size() > searchList.length-1){
                        RecyclerView recyclerListview = (RecyclerView) view.findViewById(R.id.listview);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerListview.setLayoutManager(layoutManager);

                        BookListRecyclerViewAdapter adapter = new BookListRecyclerViewAdapter(getActivity(), list);
                        recyclerListview.setAdapter(adapter);
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
}
