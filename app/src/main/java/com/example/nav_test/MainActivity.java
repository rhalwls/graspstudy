package com.example.nav_test;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.nav_test.ui.github_parser_imageURL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import android.text.TextUtils;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
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

import java.io.File;
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

        Log.i("MainActivity","right before searching toolbar");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send) // getgraph
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

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                //다운로드 파일은 지우지 않도록 설정
                if(!s.equals("lib") && !s.equals("files"))
                    deleteDir(new File(appDir, s));
                Log.d("test", "File /data/data/"+context.getPackageName()+"/" + s + " DELETED");
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                new AlertDialog.Builder(this/* 해당 액티비티를 가르킴 */)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                clearApplicationData(getApplicationContext());
                                Intent i = new Intent(MainActivity.this/*현재 액티비티 위치*/ , LoginActivity.class/*이동 액티비티 위치*/);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}