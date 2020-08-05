package com.example.nav_test;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class github_parser extends AsyncTask<Void, Void, Elements> {


    List<String> all_date = new ArrayList<>();
    List<String> all_colors = new ArrayList<>();

    String myURL;

    String URL = "";
    String name = "";

    Document doc = null;
    Elements dates = null;
    Elements imageURL = null;

    public github_parser(){
        name = "YooJaeHong";
        URL = "https://github.com/"+name;
    }


    public github_parser(String id){
        name = id;
        URL = "https://github.com/"+id;
    }




    protected  void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Elements doInBackground(Void... params) {

            String useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";

            try {
                //Log.e("파싱한하기전:","abce");
                Log.i("mj, github_parser", "the url : "+URL);
                doc = Jsoup.connect(URL).userAgent(useragent).get();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //Log.e("파싱후:","abce");
           // Log.e("파싱한 값 전체:",doc.html());

        Log.i("github_parser","query list"+doc.title());
        dates = doc.select("rect[class=day]"); // <rect class="day"
        Log.i("github_parser","github_parser dates : "+dates.text()+ " , # "+dates.size());
        myURL = doc.select("img[class=avatar avatar-user width-full border bg-white]").attr("src");
        Log.i("github_parser","github_parser image_url : "+myURL);


        return dates;
    }

    protected Element getToday(){
        return dates.last();
    }





    //function below hasn't implemented yet
    /*
    public LinkedList<Integer> retLinkedListNum(){// returns only a linked list of numbers per day
        LinkedList<Integer> numPerDay;
        for (Element date : dates) {
            String raw_date = date.attr("abs:data-date");
            String raw_color = date.attr("abs:fill");
            String raw_num_perday = date.attr("abs:data-count");
            int color_idx = raw_color.indexOf("#");
            //Log.i("mj loadingActivity", raw_date+" , "+raw_color+" , "+raw_num_perday);
            String url_refined_date = raw_date.substring(19);//날짜 시작 위치 찾기 위해 2019년과는 관련 없음
            String url_refined_color = raw_color.substring(color_idx);
            String url_refined_num_perday = raw_num_perday.substring(19);
            numPerDay.add(r)
        }

        }
    */

}
