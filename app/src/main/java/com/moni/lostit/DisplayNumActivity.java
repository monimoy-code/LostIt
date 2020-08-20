package com.moni.lostit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

public class DisplayNumActivity extends AppCompatActivity {
    public static final int NOTIF_ID=101;
    public static final String PREFER = "myPreference";
    MaterialButton button;
    boolean disp;
    JobsAllowed jobsAllowed;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_num);
        jobsAllowed = (JobsAllowed) getApplicationContext();
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        button = findViewById(R.id.ButtonDisplayNum);
        button.setChecked(preferences.getBoolean("disp",false));
        if (button.isChecked()) {
            disp = true;
            button.setBackgroundColor(Color.BLUE);
            button.setText("Enabled");
            jobsAllowed.setJobDisplayNumAllowed(disp);
        } else {
            button.setBackgroundColor(Color.parseColor("#7B1FA2"));
            button.setText("Enable");
            jobsAllowed.setJobDisplayNumAllowed(disp);
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
                editor.putBoolean("disp",isChecked);
                editor.apply();
                jobsAllowed.setJobDisplayNumAllowed(isChecked);
            }
        });
        NotificationManager notificationManagerCompat = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManagerCompat.cancel(NOTIF_ID);
    }
}