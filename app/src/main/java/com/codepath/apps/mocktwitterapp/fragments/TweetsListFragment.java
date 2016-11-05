package com.codepath.apps.mocktwitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.codepath.apps.mocktwitterapp.R;
import com.codepath.apps.mocktwitterapp.TweetsArrayAdapter;
import com.codepath.apps.mocktwitterapp.TwitterClient;
import com.codepath.apps.mocktwitterapp.models.Tweet;
import com.codepath.apps.mocktwitterapp.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepath.apps.mocktwitterapp.TwitterClient.RELOAD;

/**
 * Created by lily_chen on 11/1/16.
 */

public abstract class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    protected RequestListener listener;

    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.clTweets)
    CoordinatorLayout clTweets;

    private EndlessRecyclerViewScrollListener scrollListener;


    public interface RequestListener {
        public void onRequestStarted();
        public void onRequestFinished();
    }

    public void setRequestListener(RequestListener rl) {
        this.listener = rl;
    }

    //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        // Lookup the recycler view in activity layout
        ButterKnife.bind(this, v);

        // Attach the adapter to the recycler view to populate items
        rvTweets.setAdapter(aTweets);
        // Set layout manager to position the items

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Long maxID = tweets.get(totalItemsCount-1).getTweetId();
                loadNextDataFromApi(maxID);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadNextDataFromApi(RELOAD);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        return v;
    }

    //creation life cycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> newTweets){
        tweets.addAll(newTweets);
        aTweets.notifyDataSetChanged();
    }

    public void addTweet(Tweet tweet) {
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();
        rvTweets.scrollToPosition(0);
    }

    public abstract void loadNextDataFromApi(long maxID);

    public void clear(){
        tweets.clear();
        aTweets.notifyDataSetChanged();
    }

    public void doneLoading() {
        swipeContainer.setRefreshing(false);
    }

    public void offlineAdd() {
        List<Tweet> savedTweets = new Select().from(Tweet.class).execute();
        tweets.addAll(savedTweets);
        aTweets.notifyDataSetChanged();
    }

    View.OnClickListener onRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadNextDataFromApi(TwitterClient.INITIAL_LOAD);
        }
    };

    public void showErrorSnackbar() {
        Log.d("debug", "Asdfoier");
        Snackbar.make(rvTweets, "Failed to load timeline", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", onRetryListener)
                .show(); // Don’t forget to show!
    }

}
