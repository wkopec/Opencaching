package com.example.opencaching.app;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Volfram on 15.07.2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

}
