package com.skytreasure.firebasemultiaccountlogin.github;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by haseeb on 7/1/17.
 */
public class GithubSession {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public static final String SHARED = "Github_Preferences";
    public static final String API_USERNAME = "username";
    public static final String API_ID = "id";
    public static final String API_ACCESS_TOKEN = "access_token";
    public static final String API_REPO = "repo_url";

    public GithubSession(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    /**
     *
     * @param accessToken
     */
    public void storeAccessToken(String accessToken , String data) {
        JSONObject newData = null;
        try {
            newData = new JSONObject(data);
            editor.putString(API_ID, String.valueOf(newData.optInt("id")));
            editor.putString(API_ACCESS_TOKEN, accessToken);
            editor.putString(API_USERNAME, newData.optString("login"));
            editor.putString(API_REPO, newData.optString("repos_url"));
            editor.commit();
        }
        catch (Exception e){

        }


    }

    public void storeAccessToken(String accessToken) {
        editor.putString(API_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    /**
     * Reset access token and user name
     */
    public void resetAccessToken() {
        editor.putString(API_ID, null);
        editor.putString(API_ACCESS_TOKEN, null);
        editor.putString(API_USERNAME, null);
        editor.putString(API_REPO, null);
        editor.commit();
    }

    /**
     * Get user name
     *
     * @return User name
     */
    public String getUsername() {
        return sharedPref.getString(API_USERNAME, null);
    }

    /**
     * Get repo url
     *
     * @return Repo url
     */
    public String getRepUrl() {
        return sharedPref.getString(API_REPO, null);
    }

    /**
     * Get access token
     *
     * @return Access token
     */
    public String getAccessToken() {
        return sharedPref.getString(API_ID, null);
    }
}
