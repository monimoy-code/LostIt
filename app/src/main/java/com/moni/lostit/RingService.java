package com.moni.lostit;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class RingService extends Service {
    public static final String CHANNEL_ID = "ringIt";
    Ringtone ringtone;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    CountDownTimer timer;

    @Override
    public void onCreate() {
        powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"RINGSERVICE:LOG");
        wakeLock.acquire(10*60*1000);
        Log.d("ForeServ","Oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            Log.d("onStart","osCommand");
            if(intent.getAction().equalsIgnoreCase("start service")) {
                Intent intent1 = new Intent(this,RingService.class);
                intent1.setAction("stop service");
                PendingIntent pendingIntent = PendingIntent.getService(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Ringing your phone")
                        .setContentText("Click on button below to stop ringing")
                        .setSmallIcon(R.drawable.ic_volume_up_black_24dp)
                        .addAction(R.drawable.ic_volume_off_black_24dp,"Stop",pendingIntent)
                        .build();
                startForeground(1, notification);
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                Uri uriRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                ringtone = RingtoneManager.getRingtone(this, uriRing);
                ringtone.play();
                 timer = new CountDownTimer(2*60*1000,5000)
                {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(!(ringtone.isPlaying()))
                        {
                            ringtone.play();
                        }
                    }

                    @Override
                    public void onFinish() {
                        ringtone.stop();
                        stopForeground(true);
                        stopSelf();
                    }
                }.start();
            }
            else if(intent.getAction().equalsIgnoreCase("stop service")) {
                timer.cancel();
                ringtone.stop();
                stopForeground(true);
                stopSelf();
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
        Log.d("ForeService","Destroyed");
    }
}
