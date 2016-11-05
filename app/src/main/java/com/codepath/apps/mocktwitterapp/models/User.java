package com.codepath.apps.mocktwitterapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by lily_chen on 10/31/16.
 */

@Parcel(analyze = {User.class})
@Table(name="Users")
public class User extends Model{

    @Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String name;
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    long id;
    @Column(name = "screen_name")
    String screenName;
    @Column(name = "profile_image_url")
    String profileImageUrl;
    @Column(name = "header_image_url")
    String headerImageUrl;
    @Column(name = "followers")
    int followers;
    @Column(name = "following")
    int following;
    @Column(name = "description")
    String description;

    public String getName() {
        return name;
    }

    public long getUserId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public String getDescription() {
        return description;
    }

    public User() {}

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.id = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            // won't always have the profile banner entry
            try {
                user.headerImageUrl = jsonObject.getString("profile_banner_url");
            } catch (Exception e) {
                user.headerImageUrl = "";
            }
            user.followers = jsonObject.getInt("followers_count");
            user.following = jsonObject.getInt("friends_count");
            user.description = jsonObject.getString("description");
            user.save();
        } catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    // Finds existing user based on remoteId or creates new user and returns
    public static User findOrCreateFromJson(JSONObject json) {
        long rId = 0;
        try {
            rId = json.getLong("id"); // get just the remote id
        } catch (Exception e) {
            e.printStackTrace();
        }
        User existingUser =
                new Select().from(User.class).where("remote_id = ?", rId).executeSingle();
        if (existingUser != null) {
            // found and return existing
            return existingUser;
        } else {
            // create and return new user
            User user = User.fromJSON(json);
            user.save();
            return user;
        }
    }

}
