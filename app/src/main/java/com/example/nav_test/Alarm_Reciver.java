package com.example.nav_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

public class Alarm_Reciver extends BroadcastReceiver {
    //Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("onReceived","correct");


        github_parser date_color_parser = new github_parser();
        Intent service_intent = new Intent(context,BackgroundAlarmService.class);
        Elements dates = null;
        try {
            dates = date_color_parser.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Element today_original = date_color_parser.getToday();
        Element today_original = dates.last();

        String raw_color = today_original.attr("abs:fill");


        int color_idx = raw_color.indexOf("#");
        String url_deleted_color = raw_color.substring(color_idx);
        Log.e("url_deleted_color",url_deleted_color);


// 회색이면 startService를 한다. -> 액티비티가 꺼져도 계속 실행되는 이유
       if(url_deleted_color.equals("#ebedf0")) { // ebedf0 == 회색 -> 즉 commit하지 않았더라면

           // TODO: This method is called when the BroadcastReceiver is receiving
           // an Intent broadcast.
           Log.e("onReceived send","correct");
           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               context.startForegroundService(service_intent);
           } else {
               context.startService(service_intent); //
           }
       }
    }
}
