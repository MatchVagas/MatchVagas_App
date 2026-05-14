package com.edu.matchvagasapp;

import android.app.Application;
import android.content.Context;

public class MatchVagasApp extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
