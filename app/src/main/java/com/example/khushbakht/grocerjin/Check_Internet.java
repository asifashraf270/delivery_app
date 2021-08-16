package com.example.khushbakht.grocerjin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Hafiz Haseeem on 8/21/2017.
 */

public class Check_Internet {
    private static Check_Internet instance = new Check_Internet();
    static Context context;
    boolean connected = false;

    public static Check_Internet getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
              ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
              NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
              return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
               Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
