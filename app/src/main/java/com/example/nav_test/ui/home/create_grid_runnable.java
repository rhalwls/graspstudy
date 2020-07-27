package com.example.nav_test.ui.home;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nav_test.github_api_parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class create_grid_runnable extends AsyncTask<String, Void, ArrayList<Integer>> {


    private String str, receiveMsg;
    private String[] commits_week;
    private String[] commits_data;
    private int max_of_commit_array;
    ArrayList<String> name_array = new ArrayList<>();
    ArrayList<Integer> commit_array = new ArrayList<>();

    public create_grid_runnable(ArrayList<String> name_array_input) {

        name_array.addAll(name_array_input);
        for(int i=0;i<52*7;i++){
            commit_array.add(0);
        }
    }


    @Override
    protected ArrayList<Integer> doInBackground(String... params) {
        for (int i = 0; i < name_array.size(); i++) {

            github_api_parser user_parser = new github_api_parser("https://api.github.com/users/" + name_array.get(i) + "/repos");
            Log.e("new parser", "created");
            try {

                String receiveMsg = user_parser.get();


                JSONArray repos_array = new JSONArray(receiveMsg);
                String[] repos_url = new String[repos_array.length()];
                Log.e("repos_array.length()", Integer.toString(repos_array.length()));

                for (int j = 0; j < repos_url.length; j++) {
                    JSONObject each_repo = repos_array.getJSONObject(j);
                    repos_url[j] = each_repo.optString("url");
                    github_api_parser repos_parser = new github_api_parser(repos_url[j] + "/stats/commit_activity");

                    String commits_message = repos_parser.get();
                    if (commits_message != null) {
                        Log.e("commit_message", commits_message);

                        JSONArray commits_array = new JSONArray(commits_message);

                        //https://api.github.com/repos/YooJaeHong/android_grass/commits
                        commits_week = new String[commits_array.length()];
                        //commits_data = new int[commits_array.length()][7];

                        for (int k = 0; k < commits_week.length; k++) {
                            JSONObject each_week = commits_array.getJSONObject(k);//가장 바깥
                            //commits_week[k] = each_week.optString("week");//커밋시간 알아내기:->필요없겠다
                            //Log.e("week", commits_week[k]);

                            JSONArray days = each_week.getJSONArray("days");
                            for (int l = 0; l < 7; l++) {
                                //commits_data[k][l] = days.getInt(l);
                                int arrays_num = commit_array.get(l + k * 7);
                                arrays_num += days.getInt(l);
                                Log.e("arrays_num", Integer.toString(arrays_num));
                                commit_array.set(l + k * 7, arrays_num);
                                if (arrays_num > max_of_commit_array) {
                                    max_of_commit_array = arrays_num;
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
        return commit_array;
    }
}