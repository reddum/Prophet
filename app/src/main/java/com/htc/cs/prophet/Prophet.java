package com.htc.cs.prophet;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Ct_Huang on 6/22/15. 
 */

public class Prophet extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {

        analytics = GoogleAnalytics.getInstance(this.getApplicationContext());
        analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        analytics.setLocalDispatchPeriod(3);
        //analytics.setDryRun(true);
        analytics.dispatchLocalHits();

        tracker = analytics.newTracker("UA-64364277-1"); // Replace with actual tracker/property Id

        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    public static Tracker getTracker() {
        return tracker;
    }
}