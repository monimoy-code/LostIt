package com.moni.lostit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class Ringer extends AppCompatActivity {
    public static final String PREFER = "myPreference";
    MaterialButton button;
    boolean ring;
    JobsAllowed jobsAllowed;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringer);
        jobsAllowed = (JobsAllowed) getApplicationContext();
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        button = findViewById(R.id.ButtonRinger);
        button.setChecked(preferences.getBoolean("ring",false));
        if (button.isChecked()) {
            ring = true;
            button.setBackgroundColor(Color.BLUE);
            button.setText("Enabled");
            jobsAllowed.setJobRingerAllowed(ring);
        } else {
            button.setBackgroundColor(Color.parseColor("#7B1FA2"));
            button.setText("Enable");
            jobsAllowed.setJobRingerAllowed(ring);
        }
        button.addOnCheckedChangeListener(new MaterialButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialButton button, boolean isChecked) {
                if (isChecked) {
                    button.setBackgroundColor(Color.BLUE);
                    button.setText("Enabled");
                }
                else
                {
                    button.setBackgroundColor(Color.parseColor("#7B1FA2"));
                    button.setText("Enable");
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("ring",isChecked);
                editor.apply();
                jobsAllowed.setJobRingerAllowed(isChecked);
            }
        });
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager.isNotificationPolicyAccessGranted()) {

        } else {
            startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS), 12);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

