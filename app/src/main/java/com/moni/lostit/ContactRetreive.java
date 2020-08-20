package com.moni.lostit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ContactRetreive extends AppCompatActivity {
    public static final String PREFER = "myPreference";
    MaterialButton button;
    boolean retreive;
    JobsAllowed jobsAllowed;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_retreive);
        jobsAllowed = (JobsAllowed) getApplicationContext();
        preferences = getSharedPreferences(PREFER,Context.MODE_PRIVATE);
        button = findViewById(R.id.ButtonRetreive);
        button.setChecked(preferences.getBoolean("retreive",false));
        if (button.isChecked()) {
            retreive= true;
            button.setBackgroundColor(Color.BLUE);
            button.setText("Enabled");
            jobsAllowed.setJobContactAllowed(retreive);
        } else {
            button.setBackgroundColor(Color.parseColor("#7B1FA2"));
            button.setText("Enable");
            jobsAllowed.setJobContactAllowed(retreive);
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
                jobsAllowed.setJobContactAllowed(isChecked);
            }
        });
    }
}
