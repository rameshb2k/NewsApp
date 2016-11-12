package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;
/**
 * Created by Ramesh on 10/28/2016.
 */

public class NewsLoader extends android.support.v4.content.AsyncTaskLoader<List<News>> {
    private String mUrl;
    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }
        return NewsQueryUtils.fetchBookData(mUrl);
    }
}