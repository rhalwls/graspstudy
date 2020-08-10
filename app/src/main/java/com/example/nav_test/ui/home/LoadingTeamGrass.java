package com.example.nav_test.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.nav_test.LoadingActivity;
import com.example.nav_test.LoginActivity;
import com.example.nav_test.MainActivity;
import com.example.nav_test.R;

public class LoadingTeamGrass extends Activity {
    Team team;
    private Runnable task = new Runnable() {
        public void run() {
            Log.i("LoadingTeamGrass","runnable task run");
            Toast.makeText(getApplicationContext(), "Spinner complete!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(LoadingTeamGrass.this, TeamActivity.class);
            i.putExtra("teamObj",LoadingTeamGrass.this.team);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("teamObj");

        Handler handler = new Handler();
        handler.postDelayed(task, 5000);//5초
    }

}
