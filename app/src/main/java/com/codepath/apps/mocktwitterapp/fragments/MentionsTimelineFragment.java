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

public class MentionsTimelineFragment extends TweetsListFragment{

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    // Send API request to get timeline json
    // then fill the list view
    private void populateTimeline() {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", "got here!");
                addAll(Tweet.fromJSONArray(response));
                // deserialize json, make objects, and load into listview
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
