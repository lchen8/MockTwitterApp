package com.codepath.apps.mocktwitterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mocktwitterapp.models.Tweet;
import com.codepath.apps.mocktwitterapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by lily_chen on 10/31/16.
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // tweet objects --> views

    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        User user = tweet.getUser();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView profileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView name = (TextView) convertView.findViewById(R.id.tvName);
        TextView body = (TextView) convertView.findViewById(R.id.tvBody);
        TextView screenName = (TextView) convertView.findViewById(R.id.tvScreenName);

        name.setText(user.getName());
        body.setText(tweet.getBody());
        screenName.setText("@" + user.getScreenName());

        profileImage.setImageResource(0);

        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(5, 5))
                .into(profileImage);

        return convertView;
    }

}
