package com.example.nav_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import org.jsoup.select.Elements;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class NotificationReceiver extends BroadcastReceiver {
    boolean needNotification(Context context){//returns true when no jandie
        ReadMyName rmn = new ReadMyName(context);
        String myname = rmn.getMyName();
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        Log.i("NotificationReceiver","right before parsing current time : "+df.format(dateobj)+ " , name is"+myname);
        github_parser gp = new github_parser(myname);
        try{
            Elements es = gp.execute().get();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (Integer.parseInt(gp.getToday().attr("abs:data-count").substring(19))==0);
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if(needNotification(context)) {
            Log.i("NotificationReceiver","need to show alarm");
            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.createNotification();

            //Log.i("NotificationReceiver","have to notify and current time is "+df.format(dateobj));
        }

        else{
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();

            Log.i("NotificationReceiver","don't have to notify and current time is "+df.format(dateobj));
        }
    }
}