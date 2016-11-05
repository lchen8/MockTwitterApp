package com.codepath.apps.mocktwitterapp;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.net.URLEncoder;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "PEIR7qw0iqGPXtVtGjVNFFB1G";       // Change this
	public static final String REST_CONSUMER_SECRET = "GdS80c2RdG0ou3D80xCWZKawnnl97G7pGODJdbsoXZhPYNPOMB"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpmocktwitterapp"; // Change this (here and in manifest)


    public static final long INITIAL_LOAD = -1;
    public static final long RELOAD = -2;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}


	public void getHomeTimeline(long maxID, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 40);
		params.put("since_id", 1);
        if(maxID > 0) {
            params.put("max_id", maxID - 1);
        }
		client.get(apiUrl, params, handler);
	}

    public void getMentionsTimeline(long maxID, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 40);
        if(maxID > 0) {
            params.put("max_id", maxID - 1);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(long maxID, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count", 25);
        params.put("since_id", 1);
        if(maxID > 0) {
            params.put("max_id", maxID - 1);
        }
        client.get(apiUrl, params, handler);
    }

    public void getScreenName(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);
    }

    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    public void postNewTweet(String text, AsyncHttpResponseHandler handler) {
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            RequestParams params = new RequestParams();
            params.put("status", encodedText);
            String apiUrl = getApiUrl("statuses/update.json");
            client.post(apiUrl, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
