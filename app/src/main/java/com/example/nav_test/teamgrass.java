package com.example.nav_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.nav_test.ui.home.individual_teamgrass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class teamgrass extends Fragment {
    //팀 목록 보여주는 듯
    Context mContext;

    String text = "";
    String myLine = null;
    String path = null;
    Intent toTeamgrass = null;
    teamgrass(Context mContext){
        this.mContext = mContext;
        try {
            path = mContext.getFilesDir().getPath();
            Log.e("output_file_path",path+"/team2.txt");
            OutputStream output = new FileOutputStream(path+"/team2.txt");
            OutputStream output2 = new FileOutputStream(path+"/team3.txt");
            String text ="temp";
            byte[] by = text.getBytes();
            Log.e("output_file_path",path+"/team2.txt");
            output.write(by);
            output2.write(by);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        toTeamgrass = new Intent(this.mContext, individual_teamgrass.class);


        loadAllFile(path);
    }


    //ScrollView teamgrass_scrollview = (ScrollView)findViewById(R.id.팀잔디밭_scrollview);



    public void createlayout(String teamname){



        final LinearLayout teamgrass_block = new LinearLayout(mContext);
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,mContext.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);

        //teamgrass_block.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        teamgrass_block.setGravity(Gravity.CENTER);

        teamgrass_block.setLayoutParams(params);


        final TextView Team_title = new TextView(mContext.getApplicationContext());

        Team_title.setGravity(Gravity.CENTER|Gravity.CENTER);
        Team_title.setText(teamname);
        //Team_title.setBackgroundResource(R.color.colorPrimaryDark);

        teamgrass_block.addView(Team_title);

        final TextView team_detail = new TextView(mContext.getApplicationContext());
        String path = requireContext().getFilesDir().getPath() + File.separator + "teamname";
        File file = new File(path) ;
        FileReader fr = null ;
        BufferedReader bufrd = null ;
        String line = null;
        try {
            // open file.
            fr = new FileReader(file) ;
            bufrd = new BufferedReader(fr) ;

            while ((line=bufrd.readLine())!=null) {
                team_detail.setText(line);
                team_detail.setText("\n");
            }

            // close file.
            bufrd.close() ;
            fr.close() ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }





        teamgrass_block.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.e("teamTitle.getText()",Team_title.getText().toString());
                toTeamgrass.putExtra("selected_team_name",Team_title.getText().toString());
                mContext.startActivity(toTeamgrass);
            }
        });




       Activity teamlist_view = (Activity)mContext;

      // final LinearLayout teamgrass_layout = teamlist_view.findViewById(R.id.팀잔디밭_innerlayout);
       //teamgrass_layout.addView(teamgrass_block);
    }
    public void loadAllFile(String path){
        File file = new File(path);
        Log.e("file_path",path);

        File[] fileArray = file.listFiles();
        Log.e("file_num",Integer.toString(fileArray.length));
        if(fileArray != null) {

            for (int i = 0; i < fileArray.length; i++) {
                Log.e("filename",fileArray[i].getName());

                createlayout(fileArray[i].getName());
            }
        }
    }
}
