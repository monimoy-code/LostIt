package com.moni.lostit;

import android.app.job.JobParameters;
import android.app.job.JobService;

import android.os.PersistableBundle;


public class SmsJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        PersistableBundle bundle = params.getExtras();
        String num = bundle.getString("number");
        String message = bundle.getString("message");
        //Log.d("number",num);
        //Log.d("message",message);
        new DoTask(this).execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
