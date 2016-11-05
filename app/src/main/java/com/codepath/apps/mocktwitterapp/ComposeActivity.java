package com.codepath.apps.mocktwitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mocktwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by lily_chen on 11/3/16.
 */

public class ComposeActivity extends AppCompatActivity{

    MenuItem miActionProgressItem;

    private TwitterClient client;

    @BindView(R.id.etTweet) EditText etTweet;
    @BindView(R.id.tvCharCount) TextView tvCharCount;
    @BindView(R.id.btTweet) Button btTweet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        String text = getIntent().getExtras().getString("text");
        client = TwitterApplication.getRestClient();
        ButterKnife.bind(this);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tvCharCount.setText("140");
        etTweet.setText(text);
        etTweet.setSelection(etTweet.getText().length());

        btTweet.setOnClickListener(
            new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onTweet();
               }
            }
        );

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharCount.setText(""+(140-count));
                if(count < 1 || count > 140) {
                    btTweet.setEnabled(false);
                    if(count > 140) {
                        tvCharCount.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                } else {
                    btTweet.setEnabled(true);
                    tvCharCount.setTextColor(getResources().getColor(R.color.lightGrey));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void onTweet() {
        showProgressBar();
        String status = etTweet.getText().toString();
        client.postNewTweet(status, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", "got here!");
                Tweet newTweet = Tweet.fromJSON(response);
                Intent data = new Intent();
                // Pass relevant data back as a result
                // data.putExtra ......
                data.putExtra("tweet", Parcels.wrap(newTweet));
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
                hideProgressBar();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                hideProgressBar();
                Toast.makeText(getBaseContext(), "Failed to upload Tweet", Toast.LENGTH_LONG);
            }
        });
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}

