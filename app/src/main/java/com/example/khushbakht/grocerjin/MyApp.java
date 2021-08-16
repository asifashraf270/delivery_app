package com.example.khushbakht.grocerjin;

import android.app.Application;

/**
 * Created by khush on 11/29/2017.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/OpenSans-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}
