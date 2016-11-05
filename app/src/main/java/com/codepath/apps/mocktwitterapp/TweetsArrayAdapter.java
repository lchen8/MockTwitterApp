package com.codepath.apps.mocktwitterapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mocktwitterapp.models.Tweet;
import com.codepath.apps.mocktwitterapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by lily_chen on 10/31/16.
 */

public class TweetsArrayAdapter extends
        RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        @BindView(R.id.ivProfileImage) ImageView profileImage;
        @BindView(R.id.tvName) TextView name;
        @BindView(R.id.tvBody) TextView body;
        @BindView(R.id.tvScreenName) TextView screenName;
        @BindView(R.id.tvTimeAgo) TextView timeAgo;
        @BindView(R.id.tvRetweet) TextView retweetCount;
        @BindView(R.id.tvHeart) TextView favoriteCount;
        @BindView(R.id.ivRetweet) ImageView retweetIcon;
        @BindView(R.id.ivHeart) ImageView heartIcon;
        @BindView(R.id.ivReply) ImageView replyIcon;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View v) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(v);
            //set the views
            ButterKnife.bind(this, v);
        }
    }

    private List<Tweet> tweets;
    private Context context;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public TweetsArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TweetsArrayAdapter.ViewHolder vh, int position) {
        // Get the data model based on position
        Tweet tweet = tweets.get(position);
        User user = tweet.getUser();

        // Set item views based on your views and data model
        ImageView profileImage = vh.profileImage;
        TextView name = vh.name;
        TextView body = vh.body;
        TextView screenName = vh.screenName;
        TextView timeAgo = vh.timeAgo;
        TextView retweetCount = vh.retweetCount;
        TextView favoriteCount = vh.favoriteCount;
        ImageView retweetIcon = vh.retweetIcon;
        ImageView heartIcon = vh.heartIcon;

        name.setText(user.getName());
        body.setText(tweet.getBody());
        screenName.setText("@" + user.getScreenName());
        timeAgo.setText(tweet.getRelativeTimeAgo());
        retweetCount.setText(""+tweet.getRetweetCount());
        favoriteCount.setText(""+tweet.getFavouritesCount());

        // I think recycled views sometimes messes with the colors of these icons
        retweetIcon.setImageResource(R.drawable.icon_retweet);
        heartIcon.setImageResource(R.drawable.icon_heart);

        if(tweet.isRetweeted()) {
            retweetIcon.setImageResource(R.drawable.icon_retweet_green);
            retweetCount.setTextColor(getContext().getResources().getColor(R.color.green));
        }

        if(tweet.isFavorited()) {
            heartIcon.setImageResource(R.drawable.icon_heart_red);
            favoriteCount.setTextColor(getContext().getResources().getColor(R.color.red));
        }

        profileImage.setImageResource(0);

        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(5, 5))
                .into(profileImage);

        View.OnClickListener profileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileView(vh.screenName.getText().toString());
            }
        };

        //Clicking on any of the identifying views will launch the profile view
        vh.profileImage.setOnClickListener(profileClickListener);
        vh.screenName.setOnClickListener(profileClickListener);
        vh.name.setOnClickListener(profileClickListener);

        View.OnClickListener replyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComposeView(vh.screenName.getText().toString());
            }
        };

        View.OnClickListener retweetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComposeView("\"" + vh.screenName.getText().toString() + ": " + vh.body.getText().toString() +"\"");
            }
        };

        vh.replyIcon.setOnClickListener(replyListener);
        vh.retweetIcon.setOnClickListener(retweetListener);
    }

    public void onProfileView(String screenName) {
        Intent i = new Intent(this.getContext(), ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        this.getContext().startActivity(i);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return tweets.size();
    }


    public void onComposeView(String text) {
        Intent i = new Intent(this.getContext(), ComposeActivity.class);
        i.putExtra("text", text);
        this.getContext().startActivity(i);
    }

}
