package com.moni.lostit;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.util.List;

public class SmsReceiver extends BroadcastReceiver {
    public static final String pdu_type = "pdus";
    public static final int JOB_CONTACTS = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle bundle = intent.getExtras();
        ComponentName componentName = new ComponentName(context,SmsJobService.class);
        SmsMessage[] msgs;
        String num="";
        String msgBody="";
        String format = bundle.getString("format");
        Object[] pdus = (Object[])bundle.get(pdu_type);
        if(pdus!=null)
        {
            msgs = new SmsMessage[pdus.length];
            for(int i=0;i<msgs.length;i++)
            {
                msgs[i]=SmsMessage.createFromPdu((byte[])pdus[i],format);
                num=msgs[i].getOriginatingAddress();
                msgBody=msgs[i].getMessageBody();
                PersistableBundle persistableBundle = new PersistableBundle();
                persistableBundle.putString("number",num);
                persistableBundle.putString("message",msgBody);
                JobsAllowed jobsAllowed = (JobsAllowed) context.getApplicationContext();
                boolean ring = jobsAllowed.isJobRingerAllowed();
                boolean disp = jobsAllowed.isJobDisplayNumAllowed();
                boolean contact = jobsAllowed.isJobContactAllowed();
                persistableBundle.putBoolean("ringerService",ring);
                persistableBundle.putBoolean("displayService",disp);
                persistableBundle.putBoolean("contactService",contact);
                JobInfo info = new JobInfo.Builder(JOB_CONTACTS,componentName)
                        .setOverrideDeadline(10*1000)
                        .setExtras(persistableBundle)
                        .build();
                JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                scheduler.schedule(info);
            }
        }
        Log.d("In Broadcast Lostit","SMS received yayyyy!!!!!");
    }
}
