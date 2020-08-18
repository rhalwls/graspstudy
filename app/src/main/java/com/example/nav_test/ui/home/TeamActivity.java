package com.example.nav_test.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nav_test.MainActivity;
import com.example.nav_test.R;



public class TeamActivity extends AppCompatActivity {
    Team team;
    TextView teamNameView;
    LinearLayout teamLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TeamActivity","onCreate()");
        setContentView(R.layout.activity_team);  // layout xml 과 자바파일을 연결
        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("teamObj");
        //fetch information and match between data and ui
        teamNameView =(TextView) findViewById(R.id.team_name);
        teamNameView.setText("팀 이름 : "+team.team_name);
        teamNameView.setTextSize(20);
        teamLayout = (LinearLayout) findViewById(R.id.TeamLayout);

        //team edit button
        Button buttonOne = (Button) findViewById(R.id.btn_edit_team);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.i("TeamActivity","btn_team_edit button on click");
                Intent i = new Intent(TeamActivity.this, ActivityInputTeam.class);
                i.putExtra("teamObj",TeamActivity.this.team);
                startActivity(i);
            }
        });




        //to send team infomation to fragment
        Bundle args = new Bundle();
        args.putString("selected_team_name", team.team_name);
        //add fragment to this activity
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction trans;
        trans = manager.beginTransaction();
        Log.i("TeamActivity","team name : "+team.team_name+" , team members : "+team.getMembers().size());
        Fragment it = (Fragment) new FragTeamgrass(team.team_name,team);
        it.setArguments(args);
        trans.add(teamLayout.getId(), it , "fragment_grass");
        trans.commit();

    } // end onCreate()


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
