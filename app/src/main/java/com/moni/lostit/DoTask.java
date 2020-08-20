package com.moni.lostit;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.lang.ref.WeakReference;

public class DoTask extends AsyncTask<JobParameters,Void,JobParameters>
{
    private final JobService jobService;
    public static final String CHANNEL_ID = "ringIt";
    public static final String Channel_ID2 = "show number";
    public static final int NOTIF_ID=101;
    public DoTask(JobService jobService)
    {
        this.jobService=jobService;
    }

    @Override
    protected JobParameters doInBackground(JobParameters... jobParameters) {
        PersistableBundle bundle = jobParameters[0].getExtras();
        String msg ="";
        String num = bundle.getString("number");
        msg = bundle.getString("message").trim();
        boolean ringAllowed = bundle.getBoolean("ringerService");
        boolean dispAllowed = bundle.getBoolean("displayService");
        boolean contactAllowed = bundle.getBoolean("contactService");
        Log.d("number",num);
        Log.d("message",msg);
        if(msg.equalsIgnoreCase("play loud") && ringAllowed)
        {
            NotificationManager manager = jobService.getSystemService(NotificationManager.class);
            if(manager.isNotificationPolicyAccessGranted()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "SERVICE CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);

                    manager.createNotificationChannel(notificationChannel);
                }
                Intent intent = new Intent(jobService, RingService.class);
                intent.setAction("start service");
                ContextCompat.startForegroundService(jobService, intent);
            }

        }
        if(msg.substring(0,11).equalsIgnoreCase("get contact") && contactAllowed)
        {
            String name = msg.substring(12);
            if(ContextCompat.checkSelfPermission(jobService, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED) {
                WorkRequest workRequest = new OneTimeWorkRequest.Builder(SendContact.class)
                        .setInputData(
                                new Data.Builder()
                                        .putString("message", name)
                                        .putString("destination",num)
                                        .build()
                        )
                        .build();
                WorkManager.getInstance(jobService).enqueue(workRequest);
            }
        }
        Log.d("displayAllowed",Boolean.toString(dispAllowed));
        if(msg.substring(0,12).equalsIgnoreCase("show message") && dispAllowed)
        {
            String numb = msg.substring(13);
            NotificationManager manager = jobService.getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(Channel_ID2, "SERVICE CHANNEL", NotificationManager.IMPORTANCE_HIGH);

                manager.createNotificationChannel(notificationChannel);
            }
            Intent notifIntent = new Intent(Intent.ACTION_DIAL);
            notifIntent.setData(Uri.parse("tel:"+numb));
            notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(jobService,0,notifIntent,0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(jobService,Channel_ID2)
                    .setChannelId(Channel_ID2)
                    .setSmallIcon(R.drawable.ic_baseline_call_24)
                    .setContentTitle("Call "+numb)
                    .setContentText("If found please call the provided number")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(intent)
                    .setOngoing(true);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(jobService);
            notificationManagerCompat.notify(NOTIF_ID,builder.build());
        }
        return jobParameters[0];
    }


    @Override
    protected void onPostExecute(JobParameters s)
    {
        jobService.jobFinished(s,false);
        Log.d("postExe","jobfinished");
    }
}