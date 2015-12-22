package com.kayya.retrolambdasample.activity;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.kayya.retrolambdasample.R;
import com.kayya.retrolambdasample.adapter.WordsAdapter;
import com.kayya.retrolambdasample.model.SearchResponse;
import com.kayya.retrolambdasample.network.GoogleApi;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mustafakaya on 22/12/15.
 */
public class DetailActivity extends BaseActivity  {

    public static final String SearchTextKey ="searchtext";
    WordsAdapter adapter;
    final String APIKEY = "AIzaSyC1RLa-r1pc6RG2nSAlSxJtYBlOOTsyMg8";
    final String CXKEY = "011553412646652451558:yhcjyo9fjaa";

    String searchText;

    GoogleApi googleApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setProgressBar((ContentLoadingProgressBar) findViewById(R.id.progressBar));
        Bundle b = getIntent().getExtras();
        searchText = b.getString(SearchTextKey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new WordsAdapter(new ArrayList<>(),null);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        googleApiService = getRetrofit().create(GoogleApi.class);
        showProgressBar();
        googleApiService.search(APIKEY,CXKEY,searchText).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                if( response.isSuccess()){
                    Observable.from(response.body().getItems()).onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe( item -> {
                                 hideProgressBar();
                                 adapter.addWord(item.getTitle());
                                 adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }



}
