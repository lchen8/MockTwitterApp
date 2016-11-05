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

public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        loadNextDataFromApi(TwitterClient.INITIAL_LOAD);
    }


    public void loadNextDataFromApi(final long maxID) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(maxID, screenName, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (maxID == TwitterClient.RELOAD) clear();
                addAll(Tweet.fromJSONArray(response));
                doneLoading();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                // only pull from database if this is first time loading
                if (maxID == TwitterClient.INITIAL_LOAD) {
                    offlineAdd();
                }
                showErrorSnackbar();
                doneLoading();
            }
        });
    }
}
