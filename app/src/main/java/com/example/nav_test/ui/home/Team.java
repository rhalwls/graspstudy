package com.example.nav_test.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.nav_test.R;
import com.example.nav_test.ReadMyName;

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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
public class Team implements Serializable {

    public String team_name;
    public ArrayList<String> members;//멤버 별로 인덱스를 가짐
    public Date startDate;
    public Uri pictureUri;//사진 - 비트맵
    public Bitmap pictureBitmap;//비트맵 아직 넣는 기능 안 넣었어요!!!!!

    public Bitmap getPictureBitmap() {
        return pictureBitmap;
    }

    public void setPictureBitmap(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }

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
    public static final String genrPath(Context context, String user, String team_name){
        String path = context.getFilesDir().getPath()+"/useranem/"+user+"/teamname/"+team_name+".dat";
        return path;
    }

    public static final Team loadTeamFile(Context context,String user,String team_name){
        String path = Team.genrPath(context,user, team_name);

        try {
            FileInputStream fis = context.openFileInput(path);
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

    public boolean storeTeamFile(Context context,String user){
        //need to implement later
        String path = Team.genrPath(context,user,this.team_name);
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(path, Context.MODE_PRIVATE);
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

    public static final boolean deleteTeamFile(Context context,String user, String team_name){
        //errorneous
        String path = genrPath(context,user, team_name);
        Boolean ret = context.deleteFile(path);
        return ret;
    }

    public static final String[] getTeamFileLists(Context context,String user){
        String userPath =context.getFilesDir().getPath()+"/useranem/"+user+"/teamname";
        File serFile = new File(userPath);
        String[] files = serFile.list();
        //log
        for(int i =0;i<files.length;i++){
            Log.i("Team getTeamFileLists",files[i].split(".")[0]);
        }
        return files;
    }
}
