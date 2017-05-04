package com.skytreasure.firebasemultiaccountlogin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by akash on 4/5/17.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication sArboApplication;

    public static synchronized MyApplication getInstance() {
        return sArboApplication;
    }

    /**
     * check if the internet is connected or not
     *
     * @return
     */
    public static boolean isInternetConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) sArboApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        MultiDex.install(this);
        sArboApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}
