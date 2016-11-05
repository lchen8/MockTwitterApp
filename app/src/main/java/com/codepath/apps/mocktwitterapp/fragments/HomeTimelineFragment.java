package com.codepath.apps.mocktwitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mocktwitterapp.TwitterApplication;
import com.codepath.apps.mocktwitterapp.TwitterClient;
import com.codepath.apps.mocktwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lily_chen on 11/1/16.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        loadNextDataFromApi(TwitterClient.INITIAL_LOAD);
}

    public void loadNextDataFromApi(final long maxID) {
        if (listener != null) listener.onRequestStarted();
        client.getHomeTimeline(maxID, new JsonHttpResponseHandler() {

            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", "got here!");
                if (maxID == TwitterClient.RELOAD) clear();
                addAll(Tweet.fromJSONArray(response));
                if (listener != null) listener.onRequestFinished();
                doneLoading();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "got here instead");
                // only pull from database if this is first time loading
                if (maxID == TwitterClient.INITIAL_LOAD) {
                    offlineAdd();
                }
                showErrorSnackbar();
                if (listener != null) listener.onRequestFinished();
                doneLoading();
            }
        });
    }
}
