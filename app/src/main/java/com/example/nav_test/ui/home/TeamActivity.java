package com.example.nav_test.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import com.example.nav_test.R;



public class TeamActivity extends AppCompatActivity {
    Team team;
    TextView teamInfoView;
    LinearLayout teamLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);  // layout xml 과 자바파일을 연결
        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("teamObj");
        //fetch information and match between data and ui
        teamInfoView =(TextView) findViewById(R.id.teaminfo_temp);
        teamInfoView.setText("팀 이름 : "+team.team_name+"\n팀장 명 : "+team.members.get(0));
        teamLayout = (LinearLayout) findViewById(R.id.TeamLayout);

        Bundle args = new Bundle();

        args.putString("selected_team_name", team.team_name);

        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction trans;
        trans = manager.beginTransaction();
        Fragment it = (Fragment) new individual_teamgrass(team.team_name,team);
        it.setArguments(args);
        trans.add(teamLayout.getId(), it , "fragment_grass");
        trans.commit();




    } // end onCreate()
}
