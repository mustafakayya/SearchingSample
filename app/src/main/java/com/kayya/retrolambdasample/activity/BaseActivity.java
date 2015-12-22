package com.kayya.retrolambdasample.activity;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kayya.retrolambdasample.SampleApplication;

import retrofit.Retrofit;

/**
 * Created by mustafakaya on 23/12/15.
 */
public class BaseActivity extends AppCompatActivity {

    ContentLoadingProgressBar progressBar;


    public void setProgressBar(ContentLoadingProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void showProgressBar(){
        if(progressBar!=null)
            progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        if(progressBar!=null)
            progressBar.setVisibility(View.GONE);
    }

    public Retrofit getRetrofit(){
        return ((SampleApplication)getApplication()).getRetrofit();
    }
}
