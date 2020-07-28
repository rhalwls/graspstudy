package com.example.nav_test;

import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class github_api_parser extends AsyncTask<String, Void, String> {

    String url_str = null;

    String id = "";

    public github_api_parser(){};

    public github_api_parser(String url){
        url_str = url;
    }

    String [] commit_url;

    String [] repos_url;
    int stat_added = 0;
    int stat_deleted = 0;
    String name="";
    String date ="";



    public String[] get_repos_url(){
        return repos_url;
    }
    public int get_repos_url_size(){
        return repos_url.length;
    }


    public void set_commit_URL(){
        try {
            JSONArray jsonArray = new JSONArray(receiveMsg);
            commit_url = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++){
                JSONObject each_repo = jsonArray.getJSONObject(i);

                commit_url[i] =each_repo.optString("url")+"/commits";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String[] get_commit_URL(){
        return commit_url;
    }
    public int get_comment_url_size(){
        return commit_url.length;
    }

    public void set_addDel_date_committer(){
        try {
            JSONObject commit_sha = new JSONObject(receiveMsg);

            JSONObject stats = commit_sha.getJSONObject("stats");
            JSONObject committer =  commit_sha.getJSONObject("committer");
            JSONObject commit = commit_sha.getJSONObject("commit");
            JSONObject commit_commiter = commit.getJSONObject("committer");

            stat_added = Integer.parseInt(stats.optString("additions"));
            stat_deleted = Integer.parseInt(stats.optString("deletions"));
            name =  committer.optString("login");
            date = commit_commiter.optString("date");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return;
    }

    public int get_add(){
        return stat_added;
    }
    public int get_delete(){
        return stat_deleted;
    }
    public String get_name(){
        return name;
    }
    public String get_Date(){
        return date;
    }
    public void setId (String id){
        this.id = id;
    }





    String clientKey = "#########################";;
    private String str, receiveMsg;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... params) {
        URL url = null;

        try {
            Log.e("url_str",url_str);
            url = new URL(url_str);
            Log.e("url",url.toString());


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestProperty("Authorization", "Basic " + Base64.encodeToString("usernameUsed:passwordUsed".getBytes(), Base64.NO_WRAP));
            Log.e("html","connected");

            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Authorization","token 23a1f34ac96ed5e1c7bae06fc2895bad0508f426");//재홍샘코드
            conn.setRequestProperty("Authorization","23a1f34ac96ed5e1c7bae06fc2895bad0508f426");
            conn.setRequestProperty("Accept", "application/json");
            //conn.setRequestProperty("Password","c9e8c039d6392b935460de28731ab6baa2308e7d");

            if (conn.getResponseCode() == conn.HTTP_OK) {
                Log.e("html","connected_ok");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                    Log.e("str" , str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러"+conn.getPermission().getName());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }


}