package com.kayya.retrolambdasample;

import android.app.Application;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by mustafakaya on 22/12/15.
 */
public class SampleApplication extends Application {

    Retrofit retrofit;


    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/").addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
