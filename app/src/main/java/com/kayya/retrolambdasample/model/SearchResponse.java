package com.kayya.retrolambdasample.model;

import com.kayya.retrolambdasample.model.SearchItem;

import java.util.List;

/**
 * Created by mustafakaya on 22/12/15.
 */
public class SearchResponse {

    List<SearchItem> items;

    public SearchResponse() {
    }

    public List<SearchItem> getItems() {
        return items;
    }

    public void setItems(List<SearchItem> items) {
        this.items = items;
    }
}
