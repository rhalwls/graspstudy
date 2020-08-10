package com.example.nav_test.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.nav_test.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
public class Team implements Serializable {
    public String team_name;
    public ArrayList<String> members;//멤버 별로 인덱스를 가짐
    public Date startDate;
    public Uri pictureUri;

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public static final Uri getDefaultPictureUri(Context mContext){
        Resources resources = mContext.getResources();
        int resourceId= R.drawable.no_pic;
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceId))
                .appendPath(resources.getResourceTypeName(resourceId))
                .appendPath(resources.getResourceEntryName(resourceId))
                .build();
        return uri;
    }

    public Team(String tn){
        team_name= tn;
        pictureUri = null;
    }
    public void setStartDate(Date d){
        startDate = d;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
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
    public static final Team loadTeamFile(String team_name,Context context){
        String fileName = team_name+".dat";

        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = null;
            is = new ObjectInputStream(fis);
            Team readTeam = (Team) is.readObject();
            is.close();
            fis.close();
            return readTeam;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Team("empty_team");
    }

    public boolean storeTeamFile(Context context){
        //need to implement later
        String fileName = team_name+".dat";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
