package com.example.nav_test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.nav_test.ui.github_parser_imageURL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Context context;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;


    private AppBarConfiguration mAppBarConfiguration;

    private static final String TAG_PARENT = "TAG_PARENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("manage github");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

//alarm<- 왜 알람 기능을 주석처리 한 것이지?
       /* this.context = this;

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent alarm_reciver_Intent = new Intent(this.context,Alarm_Reciver.class);



        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,alarm_reciver_Intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+(10000),pendingIntent);
        Toast.makeText(this,"alarm set after"+1+"second",Toast.LENGTH_LONG).show();
        */

//alarm end
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab); // 우측하단 동그라미 버튼


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });//버블

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View nav_header_view = navigationView.getHeaderView(0);

        String myname = "";
        ReadMyName rmn = new ReadMyName(context);
        myname = rmn.getMyName();
        Log.d("Main : getMyName", myname);

        TextView userID = nav_header_view.findViewById(R.id.nav_userID);
        userID.setText(myname);

        TextView userAddress = nav_header_view.findViewById(R.id.nav_userAddress);
        userAddress.setText("https://github.com/"+myname);

        ImageView userImage = nav_header_view.findViewById(R.id.nav_userImage);

        github_parser_imageURL gitURL = new github_parser_imageURL(myname);
        String imageURL = null;
        try {
            imageURL = gitURL.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Glide.with(getApplicationContext()).load(imageURL).into(userImage); // nav_header에 parsing한 이미지 url 전달


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_logout, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); //Menu 리소스 ID, main 객체
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment parentFragment = fragmentManager.findFragmentByTag(TAG_PARENT);
        if (parentFragment != null && parentFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            parentFragment.getChildFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
