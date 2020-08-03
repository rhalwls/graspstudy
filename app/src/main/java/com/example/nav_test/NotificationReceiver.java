package com.example.nav_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NotificationReceiver extends BroadcastReceiver {
    boolean needNotification(Context context){//returns true when no jandie
        ReadMyName rmn = new ReadMyName(context);
        String myname = rmn.getMyName();

        github_parser gp = new github_parser(myname);

        return (Integer.parseInt(gp.getToday().attr("abs:data-count"))==0);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(needNotification(context)) {
            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.createNotification();
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            Log.i("NotificationReceiver","have to notify and current time is "+df.format(dateobj));
        }

        else{
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();

            Log.i("NotificationReceiver","don't have to notify and current time is "+df.format(dateobj));
        }
    }
}