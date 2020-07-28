package com.example.nav_test.ui.home;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.nav_test.R;
import com.example.nav_test.github_api_parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;




public class invidual_teamgrass extends Fragment {

    Context mContext;

    HashMap<String,Integer> date_user_contributions;
    HashMap<String, int[][]> user_commitList;

    HashMap<String, HashMap<String,Integer>>user_date_commitnum = new HashMap<>();
    HashMap<String,Integer>total_date_commitnum = new HashMap<>();


    String path;
    String which_block;


    String [] commits_week;
    int[][] commits_data;

    ArrayList<Integer> commit_for_recycler = new ArrayList<>();
    ArrayList<Integer> commit_array= new ArrayList<>();
    int max_of_commit_array = 0;
    int max_for_recycler = 0;
    ArrayList<String> name_array = new ArrayList<>();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat git_hub_time_formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월");
    SimpleDateFormat day_only = new SimpleDateFormat("dd");




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        which_block =getArguments().getString("selected_team_name");
        path = requireContext().getFilesDir().getPath()+File.separator+"teamname";


        for(int i=0;i<52*7;i++){
            commit_array.add(0);
        }
        //파일명 받아오기
        File file = new File(path+"/"+which_block+".txt");
        try {
            FileReader filereader = new FileReader(file);
            BufferedReader bufreader = new BufferedReader(filereader);
            String line = "";

            while((line = bufreader.readLine()) != null){
                name_array.add(line);

                Log.e("registered id", name_array.get(0));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //파일명 array에 맞춰 파싱
        parse_all();





    }


    @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.activity_invidual_teamgrass, container, false);





        /*Intent intent = getActivity().getIntent();
        String which_block = intent.getExtras().getString("selected_team_name");*/
        Log.e("received by Teamgrass",which_block);


        //Log.e("file_path", file.getPath());

        HashMap<String,Integer> date_name_contributions = new HashMap<>();
        HashMap<String,Integer[][]> uesr_commitList=  new HashMap<>();





        Log.e("registered id", name_array.get(0));

        TextView textView = new TextView(mContext);
        ArrayList<Integer> for_test = new ArrayList<>();

        for(int i=0;i<7*52;i++) {
            for_test.add(3);
        }

        int for_test_max = 5;

        textView.setText("1234");

        Log.e("text","passed");



        RecyclerView recyclerView = root.findViewById(R.id.teamgrass_calendar);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext,7,GridLayoutManager.HORIZONTAL,false));

        recycler_view_adapter adapter = new recycler_view_adapter(commit_for_recycler,max_for_recycler);

        recyclerView.setAdapter(adapter);




        recyclerView.getAdapter().notifyDataSetChanged();

        return root;
    }


    int col_num=3;
    int row_num=3;



    public void createFrame(int commit_num, int max/*,String date*/) {

        ArrayList<String> colorList = new ArrayList<>();
        float commitNum_devide_max = (float)commit_num/(float)max;
        if(commitNum_devide_max == 0){
            colorList.add("#ebedf0");
        }
        else if(commitNum_devide_max<0.25){
            colorList.add("#c6e48b");
        }
        else if(commitNum_devide_max<0.5){
            colorList.add("#7bc96f");
        }
        else if(commitNum_devide_max<0.75){
            colorList.add("#239a3b");
        }
        else if(commitNum_devide_max<=1){
            colorList.add("#196127");
        }
        else{
            Log.e("devide error!", String.valueOf(commitNum_devide_max));
        }
    }



    private ArrayList<Integer> parse_all(){
        ArrayList<String> keyList = new ArrayList<>();
        Log.i("mj,individual_teamgrass","name_array.size() is "+name_array.size());
        for(int i=0;i<name_array.size();i++) {

            github_api_parser user_parser = new github_api_parser("https://api.github.com/users/" + name_array.get(i) + "/subscriptions");
            Log.e("new parser", "created");
            try {

                String receiveMsg = user_parser.execute().get();
                if(receiveMsg!=null){
                    Log.i("mj,indvidual teamgrass",receiveMsg);
                }
                else{
                    Log.i("mj,indvidual teamgrass","mesage is null");
                }

                JSONArray repos_array = new JSONArray(receiveMsg);
                
                String[] repos_url = new String[repos_array.length()];
                Log.e("repos_array.length()", Integer.toString(repos_array.length()));

                for (int j = 0; j < repos_url.length; j++) {
                    JSONObject each_repo = repos_array.getJSONObject(j);
                    repos_url[j] = each_repo.optString("url");
                    github_api_parser repos_parser = new github_api_parser(repos_url[j] + "/commits");

                    String commits_message = repos_parser.execute().get();
                    if (commits_message != null) {
                        Log.e("commit_message", commits_message);

                        //String crappyPrefix ="";

                        JSONArray commits_array = new JSONArray(commits_message);

                        //https://api.github.com/repos/YooJaeHong/android_grass/commits
                        //commits_week = new String[commits_array.length()];
                        //commits_data = new int[commits_array.length()][7];
                        String last_date = "empty_now";

                        for (int k = 0; k < commits_array.length(); k++) {
                            JSONObject each_commit = commits_array.getJSONObject(k);//가장 바깥
                            //commits_week[k] = each_week.optString("week");//커밋시간 알아내기:->필요없겠다
                            //Log.e("week", commits_week[k]);

                            if(each_commit.has("author")&&!each_commit.isNull("author")) {

                                JSONObject days = each_commit.getJSONObject("commit");
                                Log.e("author", "before");

                                JSONObject author = each_commit.getJSONObject("author");
                                Log.e("author", "after");
                                JSONObject commit_author = days.getJSONObject("author");


                                String name = author.optString("login");
                                String date = commit_author.optString("date");
                                String date_split = date.split("T", 2)[0];
                                Log.e("name", name);
                                Log.e("name_arrays",name_array.get(i));
                                Log.e("date", date);
                                Log.e("date_split",date_split);


                                if (name_array.get(i).equals(name)) {
                                    if(!user_date_commitnum.containsKey(name)){

                                        HashMap<String,Integer> newMap = new HashMap<>();

                                        user_date_commitnum.put(name,newMap);

                                    }

                                    Log.e("getnull", "before");
                                    if (user_date_commitnum.get(name).containsKey(date_split)) {
                                        Log.e("notempty", "user");
                                        int now_num = user_date_commitnum.get(name).get(date_split);
                                        user_date_commitnum.get(name).put(date_split, now_num + 1);
                                        int now_total_num = total_date_commitnum.get(date_split);
                                        total_date_commitnum.put(date_split, now_total_num + 1);
                                    }
                                    else if (!user_date_commitnum.get(name).containsKey(date_split)) {
                                        Log.e("empty","user");
                                        user_date_commitnum.get(name).put(date_split, 1);
                                        if (!total_date_commitnum.containsKey(date_split)) {
                                            Log.e("empty","total");
                                            total_date_commitnum.put(date_split, 1);
                                            keyList.add(date_split);
                                        }
                                        else if (total_date_commitnum.containsKey(date_split)) {
                                            Log.e("notempty","total");
                                            int now_total_num = total_date_commitnum.get(date_split);
                                            Log.e("now_total_num",Integer.toString(now_total_num));

                                            total_date_commitnum.put(date_split, now_total_num + 1);
                                            Log.e("dupkey","ok");

                                            Log.e("date_split", date_split);
                                        }
                                        else{
                                            Log.e("total_date_commitnum","contain not true and false");
                                        }
                                    }
                                    else{
                                        Log.e("user_date_commitnum","contain not true and false");
                                    }
                                }
                            }
                        }
                    }
                    //user_commitList.put(name_array.get(i),commits_data);//유저이름과 커밋리스트 연동
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Date today = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");



        for(int i=0;i<keyList.size();i++){
            try {
                int commit_num = total_date_commitnum.get(keyList.get(i));
                if(max_of_commit_array < commit_num){
                    max_of_commit_array = commit_num;
                    Log.e("max",Integer.toString(max_of_commit_array));
                }
                Date commit_date = format.parse(keyList.get(i));
                Log.e("commit_date",keyList.get(i));
                long diff = today.getTime()-commit_date.getTime();
                int diffday = (int)(diff/(24*60*60*1000));
                Calendar cal = Calendar.getInstance();
                cal.setTime(today);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                if(((52*7) -8+ dayOfWeek - diffday)>=0) {
                    commit_array.set((52 * 7) - 8 + dayOfWeek - diffday, commit_num);//-7+요일
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        commit_for_recycler.addAll(commit_array);
        max_for_recycler = max_of_commit_array;
        return commit_array;
    }


}
