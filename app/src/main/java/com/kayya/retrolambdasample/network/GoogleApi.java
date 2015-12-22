package com.kayya.retrolambdasample.network;

import com.kayya.retrolambdasample.model.SearchResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by mustafakaya on 22/12/15.
 */
public interface GoogleApi {

    @GET("customsearch/v1")
    Call<SearchResponse> search(@Query("key") String apiKey, @Query("cx") String cxKey, @Query("q") String searchText);

}
