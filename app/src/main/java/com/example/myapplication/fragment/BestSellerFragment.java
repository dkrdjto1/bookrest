package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.BestSellerAndReCommendAllListActivity;
import com.example.myapplication.activity.DetailActivity;
import com.example.myapplication.adapter.recycler.HorizontalRecyclerViewAdapter;
import com.example.myapplication.api.InterParkRestAPI;
import com.example.myapplication.vo.InterParkBookVo;
import com.example.myapplication.vo.InterParkItemVo;

import java.util.ArrayList;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BestSellerFragment extends Fragment {

    // 인터파크 API
    InterParkRestAPI interParkRestAPI;
    String INTERPARK_BASE_URL = "http://book.interpark.com/";
    String INTERPARK_KEY = "2C63E2D3201272C4309D7385E7169A9D3CFB38F7FB37B90F4F21AC8651AB0FF0";

    // 요청변수
    String categoryId = "100"; // 국내도서
    String output = "json";
    static ArrayList<InterParkBookVo> bestSellerList;
    ImageView todaybookImageView;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_best_seller, container, false);

        bestSellerList = new ArrayList<>();
        // 인터파크 API 초기화
        initInterParkRestAPI();
        // 베스트 셀러 목록
        InterParkBestSellerApi(INTERPARK_KEY, categoryId, output);
        // 추천 도서 목록
        InterParkRecommendApi(INTERPARK_KEY, categoryId, output);


        // 베스트셀러 전체보기
        TextView textViewViewall = (TextView) view.findViewById(R.id.textViewViewall);
        textViewViewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BestSellerAndReCommendAllListActivity.class);
                intent.putExtra("gubun", "1");
                startActivity(intent);
            }
        });

        // 추천 도서 전체보기
        TextView textViewViewmore = (TextView) view.findViewById(R.id.textViewViewmore);
        textViewViewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BestSellerAndReCommendAllListActivity.class);
                intent.putExtra("gubun", "2");
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 인터파크 베스트셀러
     * @param key
     * @param categoryId
     * @param output
     */
    public void InterParkBestSellerApi(String key, String categoryId, String output){

        Call<InterParkBookVo> getCall = interParkRestAPI.getBestSellerApi(key, categoryId, output);

        getCall.enqueue(new Callback<InterParkBookVo>() {
            @Override
            public void onResponse(Call<InterParkBookVo> call, Response<InterParkBookVo> response) {
                if (response.isSuccessful()) {
                    InterParkBookVo result = response.body();
                    bestSellerList.add(result);
                    ArrayList<InterParkItemVo> items = result.getItems();
                    RecyclerView recyclerListview = (RecyclerView) view.findViewById(R.id.listview);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerListview.setLayoutManager(layoutManager);

                    HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter(getActivity(), items);
                    recyclerListview.setAdapter(adapter);
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
     * 인터파크 추천도서
     * @param key
     * @param categoryId
     * @param output
     */
    public void InterParkRecommendApi(String key, String categoryId, String output){

        Call<InterParkBookVo> getCall = interParkRestAPI.getRecommendApi(key, categoryId, output);

        getCall.enqueue(new Callback<InterParkBookVo>() {
            @Override
            public void onResponse(Call<InterParkBookVo> call, Response<InterParkBookVo> response) {
                if (response.isSuccessful()) {
                    InterParkBookVo result = response.body();
                    bestSellerList.add(result);

                    todaybookImageView = (ImageView) view.findViewById(R.id.imageViewTodaybook);
                    // 추천 도서 목록
                    ArrayList<InterParkItemVo> items = result.getItems();
                    Random random = new Random();
                    int randomPos = (int) (random.nextInt(items.size()) *  1);
                    InterParkItemVo todayVo = items.get(randomPos);
                    Glide.with(getActivity()).load(todayVo.getCoverLargeUrl()).into(todaybookImageView);

                    todaybookImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("title", todayVo.getTitle());
                            intent.putExtra("link", todayVo.getLink());
                            intent.putExtra("author", todayVo.getAuthor());
                            intent.putExtra("description", todayVo.getDescription());
                            intent.putExtra("publisher", todayVo.getPublisher());
                            intent.putExtra("image", todayVo.getCoverSmallUrl());
                            intent.putExtra("coverSmallUrl", todayVo.getCoverSmallUrl());
                            intent.putExtra("coverLargeUrl", todayVo.getCoverLargeUrl());
                            intent.putExtra("isbn", todayVo.getIsbn());
                            intent.putExtra("itemId", todayVo.getItemId());
                            intent.putExtra("position", "");
                            startActivity(intent);
                        }
                    });
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
