package com.example.nav_test;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class BackgroundAlarmService extends Service {







    private IBinder mIBinder = new MyBinder();

    public int var = 777;
    class MyBinder extends Binder {
        BackgroundAlarmService getService(){
            return BackgroundAlarmService.this;
        }
    }
    public BackgroundAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.e("onbind","true");
        return mIBinder;
        // TODO: Return the communication channel to the service.

    }

    @Override
    public void onCreate() {



        Log.e("backgroundAlarmService","true");

        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26 ) {

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);



            Log.e("build_ver",Build.VERSION.SDK);
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "잔디 안채워졌을때 알림",
                   NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager noti_manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            noti_manager.createNotificationChannel(channel);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent (this,loadingActivity.class),0);



            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("아직 오늘의 잔디밭을 채우지 못하셨습니다!")
                    .setContentText("오늘의 잔디를 심어주세요!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setDefaults(Notification.DEFAULT_VIBRATE).setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .build();
            startForeground(1,notification);
            noti_manager.notify(1234,notification);
            Log.e("startForeground","true");


        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind","true");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy","true");
        super.onDestroy();
    }
}
