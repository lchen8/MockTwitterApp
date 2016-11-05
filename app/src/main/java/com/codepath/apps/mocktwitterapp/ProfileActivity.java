package com.codepath.apps.mocktwitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codepath.apps.mocktwitterapp.fragments.UserProfileFragment;
import com.codepath.apps.mocktwitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.mocktwitterapp.models.Tweet;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 30;

    UserTimelineFragment userTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the screenName
        String screenName = getIntent().getStringExtra("screen_name");

        if (savedInstanceState == null) {
            //display profile fragment
            UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flProfileContainer, userProfileFragment);

            //display the user fragment
            userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            ft.replace(R.id.flTweetsContainer, userTimelineFragment);
            ft.commit();
        }

    }

    public void onComposeView(View view) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("text", "");
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            userTimelineFragment.addTweet(tweet);
        }
    }



}
