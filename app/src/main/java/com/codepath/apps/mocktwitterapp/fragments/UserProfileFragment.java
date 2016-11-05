package com.codepath.apps.mocktwitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mocktwitterapp.R;
import com.codepath.apps.mocktwitterapp.TwitterApplication;
import com.codepath.apps.mocktwitterapp.TwitterClient;
import com.codepath.apps.mocktwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by lily_chen on 11/2/16.
 */

public class UserProfileFragment extends Fragment {

    private String screenName;

    private TwitterClient client;

    @BindView(R.id.ivMainProfImage) ImageView ivImage;
    @BindView(R.id.ivHeader) ImageView ivHeader;
    @BindView(R.id.tvMainUserName) TextView tvName;
    @BindView(R.id.tvMainScreenName) TextView tvScreenName;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;

    public static UserProfileFragment newInstance(String screenName) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userProfileFragment.setArguments(args);
        return userProfileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateUserProfile();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, parent, false);
        ButterKnife.bind(this, v);
        return v;
    }

    // Send API request to get timeline json
    // then fill the list view
    private void populateUserProfile() {
        screenName = getArguments().getString("screen_name");
        // if we are searching with null screenName, i.e. viewing out own profile,
        // we need to get our screenName so that we can GET users/show.json
        if (screenName == null) {
            client.getScreenName(new JsonHttpResponseHandler() {
                // SUCCESS
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        User user = User.findOrCreateFromJson(response);
                        getUserProfile(user.getScreenName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // FAILURE
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                }
            });

        } else {
            getUserProfile(screenName);
        }
    }



    private void getUserProfile(String screenName) {
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.findOrCreateFromJson(response);
                super.onSuccess(statusCode, headers, response);
                tvName.setText(user.getName());
                tvScreenName.setText("@" + user.getScreenName());
                tvDescription.setText(user.getDescription());
                tvFollowers.setText(user.getFollowers() + " followers");
                tvFollowing.setText(user.getFollowing() + " following");

                ivImage.setImageResource(0);
                ivHeader.setImageResource(0);

                Picasso.with(getContext())
                        .load(user.getHeaderImageUrl())
                        .fit()
                        .into(ivHeader);

                Picasso.with(getContext())
                        .load(user.getProfileImageUrl())
                        .into(ivImage);
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}
