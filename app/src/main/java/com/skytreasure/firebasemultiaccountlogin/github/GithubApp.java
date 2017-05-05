package com.skytreasure.firebasemultiaccountlogin.github;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by haseeb on 7/1/17.
 */

public class GithubApp {
    private GithubSession mSession;
    private GithubDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;

    /**
     * Callback url, as set in 'Manage OAuth Costumers' page
     * (https://developer.github.com/)
     */

    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize?";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token?";
    public static final String API_URL = "https://api.github.com";

    private static final String TAG = "GitHubAPI";

    public GithubApp(Context context, String clientId, String clientSecret,
                     String callbackUrl) {
        mSession = new GithubSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
        mTokenUrl = TOKEN_URL + "client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl;
        mAuthUrl = AUTH_URL+ "scope=user:email&" + "client_id=" + clientId + "&redirect_uri="
                + mCallbackUrl;

        GithubDialog.OAuthDialogListener listener = new GithubDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new GithubDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = 0;

                try {
                    URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.i(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    Log.i(TAG, "response " + response);
                    mAccessToken = response.substring(
                            response.indexOf("access_token=") + 13,
                            response.indexOf("&token_type"));
                    Log.i(TAG, "Got access token: " + mAccessToken);
                } catch (Exception ex) {
                    what = 1;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    private void fetchUserName() {
        mProgress.setMessage("Finalizing ...");

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = 0;

                try{
//                    String url = "http://www.google.com/search?q=mkyong";

//                    URL obj = new URL(url);
                    URL obj = new URL(API_URL + "/user?access_token="
                            + mAccessToken);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
//                    con.setRequestProperty("User-Agent", USER_AGENT);

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + obj);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    mSession.storeAccessToken(mAccessToken, response.toString());
                    //print result
                    System.out.println("RESPPP : "+response.toString());


                }
                catch (Exception e){

                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                if (msg.what == 0) {
                    fetchUserName();
                } else {
                    mProgress.dismiss();
                    mListener.onFail("Failed to get access token");
                }
            } else {
                mProgress.dismiss();
                mListener.onSuccess();
            }
        }
    };

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public String getAccessToken(){
        return mAccessToken;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getUserName() {
        return mSession.getUsername();
    }
    public String getRepoUrl() {
        return mSession.getRepUrl();
    }

    public void authorize() {
        mDialog.show();
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}
