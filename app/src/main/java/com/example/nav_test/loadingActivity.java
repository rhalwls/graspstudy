package com.example.nav_test;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class loadingActivity extends Activity {
    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);



        startLoading();
    }



    private void startLoading() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Elements dates = null;
                String myname = "";
                //기존 아이디 가져오기
                //개인 잔디는 앱 열 때마다 로드하기
                ReadMyName rmn = new ReadMyName(context);
                myname = rmn.getMyName();


                //Toast.makeText(this, readStr.substring(0, readStr.length()-1), Toast.LENGTH_SHORT).show();

                //않이 이미 매일 새로 받아서 저장하고 있는데
                Log.i("loadingActivity","done reading name again");
                github_parser date_number_parser = new github_parser(myname);//아이디 메인페이지 방문해 잔디 모판 색깔 확인
                try {
                    dates = date_number_parser.execute().get();


                    Log.i("loadingActivity","why perser.exe.get doesn't work??");
                    Log.i("loadingActivity","dates.text() : \n"+dates.text());
                    Log.i("loadingActivity","dates.html() : \n"+dates.html());

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("loadingActivity","done parsing dates");
                BufferedWriter bufferedWriter = null;
                try {
                    String path = getFilesDir().getPath();
                    Log.e("writing path",path);
                    bufferedWriter = new BufferedWriter(new FileWriter(path+"/myGrassData.txt")) ;//???뭘 저장하는 것이지?

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Element date : dates) {
                    String raw_date = date.attr("abs:data-date");
                    String raw_color = date.attr("abs:fill");
                    String raw_num_perday = date.attr("abs:data-count");
                    int color_idx = raw_color.indexOf("#");
                    //Log.i("mj loadingActivity", raw_date+" , "+raw_color+" , "+raw_num_perday);
                    String url_refined_date = raw_date.substring(19);//날짜 시작 위치 찾기 위해 2019년과는 관련 없음
                    String url_refined_color = raw_color.substring(color_idx);
                    String url_refined_num_perday = raw_num_perday.substring(19);
                    try {
                        bufferedWriter.write(url_refined_date);
                        bufferedWriter.newLine();
                        bufferedWriter.write(url_refined_color);
                        bufferedWriter.newLine();
                        bufferedWriter.write(url_refined_num_perday);
                        bufferedWriter.newLine();

                        Log.i("url_deleted_date",url_refined_date);
                        Log.i("url_deleted_color",url_refined_color);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myAlarm();

                Intent main = new Intent(loadingActivity.this,MainActivity.class);
                startActivity(main);
                finish();

            }
        },2000);//2초가 무슨의미..?
    }


    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Log.i("loading_activity","alarm time set currently : "+formatter.format(calendar.getTime()));
        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
            Log.i("loading_activity","alarm manager not null set repeat");
        }

    }


}
