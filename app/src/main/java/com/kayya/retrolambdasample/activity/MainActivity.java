package com.kayya.retrolambdasample.activity;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.kayya.retrolambdasample.communication.WordsAdapterListener;
import com.kayya.retrolambdasample.network.GoogleApi;
import com.kayya.retrolambdasample.R;
import com.kayya.retrolambdasample.SampleApplication;
import com.kayya.retrolambdasample.adapter.WordsAdapter;
import com.kayya.retrolambdasample.model.SearchResponse;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements WordsAdapterListener{

    final String TAG = "WordFinder";

    WordsAdapter adapter;
    Subscription listObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setProgressBar((ContentLoadingProgressBar) findViewById(R.id.progressBar));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new WordsAdapter(new ArrayList<>(),this);

        recyclerView.setAdapter(adapter);


    }


    private SearchView mSearchView;
    private MenuItem searchMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);
        return true;
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            adapter.clearList();
            showProgressBar();
            if (listObs != null && !listObs.isUnsubscribed()) {
                listObs.unsubscribe();
            }

            listObs = fetchWords(newText).onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(100).subscribe(fetchObserver);

            return false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!listObs.isUnsubscribed())
            listObs.unsubscribe();
    }

    Observer<String> fetchObserver = new Observer<String>() {

        @Override
        public void onCompleted() {
            Log.i(TAG, "Observer Completed");
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "Observer Error Received");
        }

        @Override
        public void onNext(String s) {
            hideProgressBar();
            Log.i(TAG, "Observer onNext :" + s);
            adapter.addWord(s);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
        }
    };

    private Observable<String> fetchWords(String t) {
        return Observable.create(subscriber -> {

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("british-english-insane.txt")));
                Log.i(TAG, "Reading started");
                String line = reader.readLine();
                while (line != null && reader != null) {
                    if (subscriber.isUnsubscribed())
                        return;
                    if (line.startsWith(t))
                        subscriber.onNext(line);
                    line = reader.readLine();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } finally {
                Log.i(TAG, "Reading stop");
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            if (!subscriber.isUnsubscribed())
                subscriber.onCompleted();

        });
    }



    @Override
    public void onWordSelected(String word) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Bundle b = new Bundle();
        b.putString(DetailActivity.SearchTextKey, word);
        intent.putExtras(b);
        startActivity(intent);

    }
}
