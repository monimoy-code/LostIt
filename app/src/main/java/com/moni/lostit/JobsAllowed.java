package com.moni.lostit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class JobsAllowed extends Application {
    private boolean jobContactAllowed;
    private boolean jobRingerAllowed;
    private boolean jobDisplayNumAllowed;
    public static final String PREFER = "myPreference";
    SharedPreferences preferences;

    public boolean isJobContactAllowed() {
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        jobContactAllowed = preferences.getBoolean("jobContact",false);
        return jobContactAllowed;
    }

    public boolean isJobRingerAllowed() {
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        jobRingerAllowed = preferences.getBoolean("jobRinger",false);
        return jobRingerAllowed;
    }

    public boolean isJobDisplayNumAllowed() {
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        jobDisplayNumAllowed = preferences.getBoolean("jobDisplay",false);
        return jobDisplayNumAllowed;
    }

    public void setJobContactAllowed(boolean jobContactAllowed) {
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("jobContact",jobContactAllowed);
        editor.apply();
        this.jobContactAllowed = jobContactAllowed;
    }

    public void setJobRingerAllowed(boolean jobRingerAllowed) {
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("jobRinger",jobRingerAllowed);
        editor.apply();
        this.jobRingerAllowed = jobRingerAllowed;
    }

    public void setJobDisplayNumAllowed(boolean jobDisplayNumAllowed) {
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("jobDisplay",jobDisplayNumAllowed);
        editor.apply();
        this.jobDisplayNumAllowed = jobDisplayNumAllowed;
    }
}
