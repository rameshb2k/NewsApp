package com.example.android.newsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    //Test URL for News data for Gaurdian News  api
    private static final String NEWS_REQUEST_URL =
    "http://content.guardianapis.com/search?q=US%20election%20emails&api-key=test";
    StringBuilder urlString = new StringBuilder();
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsArray;
    private ArrayList<News> savedNews = new ArrayList<News>();

    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //  Create a new loader for the given URL
        return new NewsLoader(MainActivity.this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsArray) {

        // Clear the adapter of previous news data
        newsAdapter.clear();
        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArray != null && !newsArray.isEmpty()) {
            newsAdapter.addAll(newsArray);
            savedNews = (ArrayList)newsArray;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //  Loader reset, so we can clear out our existing data.
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    //Save News data when activity is destroyed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save books
        savedInstanceState.putParcelableArrayList("key", savedNews);
        super.onSaveInstanceState(savedInstanceState);
        newsAdapter.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check for Internet Connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setEmptyView(findViewById(R.id.empty_view));
        final TextView emptyView = (TextView) findViewById(R.id.empty_view);

        //grab the search term when the search button is clicked
       // final Button searchButton = (Button) findViewById(R.id.search_button_view);

        //Restore Contents of Adapter if Instance is saved before Activity is destroyed
        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            Log.v("savedInstanceState", " false");
            // Create a new {@link ArrayAdapter} of books
            newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        } else {
            Log.v("savedInstanceState", "true");
            savedNews = savedInstanceState.getParcelableArrayList("key");
            newsAdapter = new NewsAdapter(this, savedNews);
        }

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(newsAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String newsUrl = savedNews.get(position).getUrl();
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(Uri.parse(newsUrl));
                startActivity(urlIntent);
            }
        });

        //get data from GuardianAPI every 30 seconds

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // code to run
                if (isConnected) {
                    getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
                } else {
                    emptyView.setText("No Internet Connection");
                }
            }
        }, 0, 30, TimeUnit.SECONDS);

    }//onCreate

}//MainActivity