package com.moni.lostit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.aware.SubscribeConfig;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialCardView contactCard,ringCard,numCard;
    public static final int SMS_RECEIVED_PER=1;
    public static final int SMS_SEND_PER=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactCard = findViewById(R.id.CardContacts);
        ringCard = findViewById(R.id.CardRing);
        numCard = findViewById(R.id.cardDisplayNum);
        checkforsmsPermission();
        contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,ContactRetreive.class);
                startActivity(intent1);
            }
        });
        ringCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,Ringer.class);
                startActivity(intent2);
            }
        });
        numCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this,DisplayNumActivity.class);
                startActivity(intent3);
            }
        });
        List<SubscriptionInfo> list = SubscriptionManager.from(this).getActiveSubscriptionInfoList();
        for(int i=0;i<list.size();i++)
        {
            SubscriptionInfo s = list.get(i);
            if(s!=null)
            {
                Log.d("from list    ",s.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==SMS_RECEIVED_PER)
        {
            if((permissions[0].equalsIgnoreCase(Manifest.permission.RECEIVE_SMS))&&(grantResults[0]==PackageManager.PERMISSION_GRANTED))
            {

            }
            else
            {
                Toast.makeText(this, "Receive sms permission not granted", Toast.LENGTH_SHORT).show();
            }
            if((permissions[1].equalsIgnoreCase(Manifest.permission.SEND_SMS))&&(grantResults[1]==PackageManager.PERMISSION_GRANTED))
            {

            }
            else
            {
                Toast.makeText(this, "send sms permission not granted", Toast.LENGTH_SHORT).show();
            }
            if((permissions[2].equalsIgnoreCase(Manifest.permission.READ_SMS)) && (grantResults[2]==PackageManager.PERMISSION_GRANTED))
            {

            }
            else
            {
                Toast.makeText(this, "Read sms permission not granted", Toast.LENGTH_SHORT).show();
            }
            if((permissions[3].equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)) && (grantResults[2]==PackageManager.PERMISSION_GRANTED))
            {

            }
            else
            {
                Toast.makeText(this, "cannot check for sim cards", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkforsmsPermission()
    {
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CONTACTS}, SMS_RECEIVED_PER);
        }
    }
}
