package com.example.nav_test.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import java.util.LinkedList;

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

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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
        members = new ArrayList<String>();
        startDate = new Date();

        pictureUri = null;
    }
    public void setStartDate(Date d){
        startDate = d;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }
    public static final String genrPath(Context context, String user, String team_name){
        //String root = Environment.getExternalStorageDirectory().toString();
        File root= context.getCacheDir();
        //File teamFile = new File(root + "/username/"+user+"/teamname/"+team_name+".dat");

        return root + "/username/"+user+"/teamname/"+team_name+".dat";
        //return root+File.separator+team_name+".dat";
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
            File file =new File(path);
            /*
            if(!file.exists()){
                boolean res= file.mkdirs();
                Log.i("Team store","file.mkdirs result : "+res);
            }*/
            fos = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
            Log.i("Team","storing a team done");
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
        //String root = Environment.getExternalStorageDirectory().toString();
        File root= context.getCacheDir();
        String userPath = root+"/username/"+user+"/teamname";
        //String userPath =root.getPath();
        File serFile = new File(userPath);
        Log.i("Team","reading Team List, mom folder file is "+serFile.getPath());
        if (! serFile.isDirectory()){
            Log.i("Team","user and team name path wasn't directory");
            if (! serFile.mkdirs()){
                Log.i("Team","tried to make directory but failed");
            }
        }

        String[] files = serFile.list();

        if(files==null||files.length==0){
            Log.i("Team","팀원리스트가 비어있습니다");
        }
        else {
            //log
            for (int i = 0; i < files.length; i++) {
                Log.i("Team getTeamFileLists", "file number"+i+" : "+files[i]);
            }
        }
        return files;
    }
}
