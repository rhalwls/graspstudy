package com.example.nav_test.ui.home;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Team implements Serializable {
    public String team_name;
    public ArrayList<String> members;//멤버 별로 인덱스를 가짐

    public Team(String tn){
        team_name= tn;

    }
    //파일에서 멤버들 읽어오기
    public ArrayList<String> loadTeamMembers(Context context){
        String path = context.getFilesDir().getPath() + File.separator + "teamname/"+team_name+".txt";
        String member;
        ArrayList<String> tempMembers = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            while((member = br.readLine())!=null){
                tempMembers.add(member);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        members = tempMembers;
        return members;
    }
    /*
    public boolean storeTeamFile(Context context){
        //need to implement later

        return true;
    }
    */
}
