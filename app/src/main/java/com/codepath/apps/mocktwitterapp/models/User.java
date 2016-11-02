package com.codepath.apps.mocktwitterapp.models;

import org.json.JSONObject;

/**
 * Created by lily_chen on 10/31/16.
 */

public class User {

    private String name;

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    private long id;
    private String screenName;
    private String profileImageUrl;

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.id = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

}
