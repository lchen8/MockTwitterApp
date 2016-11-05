package com.codepath.apps.mocktwitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mocktwitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.mocktwitterapp.fragments.MentionsTimelineFragment;
import com.codepath.apps.mocktwitterapp.fragments.TweetsListFragment;
import com.codepath.apps.mocktwitterapp.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {

    ProgressBar progressBarFooter;

    MentionsTimelineFragment mentionsTimeline;
    HomeTimelineFragment homeTimeline;

    MenuItem miActionProgressItem;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        showProgressBar();
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    //return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = { "Home", "@Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    mentionsTimeline = new MentionsTimelineFragment();
                    mentionsTimeline.setRequestListener(rl);
                    return mentionsTimeline;
                default:
                    homeTimeline = new HomeTimelineFragment();
                    homeTimeline.setRequestListener(rl);
                    return homeTimeline;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
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
            homeTimeline.addTweet(tweet);
        }
    }

    public void showProgressBar() {
        // Show progress item
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    public void hideProgressBar() {
        // Hide progress item
        if (miActionProgressItem != null) miActionProgressItem.setVisible(false);
    }


    public void onProfileView(MenuItem mi) {
        // Launch Profile View
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    TweetsListFragment.RequestListener rl = new TweetsListFragment.RequestListener(
    ) {
        @Override
        public void onRequestStarted() {
            showProgressBar();
        }

        @Override
        public void onRequestFinished() {
            hideProgressBar();
        }
    };
}

