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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;



public class github_parser extends AsyncTask<Void, Void, Elements> {


    List<String> all_date = new ArrayList<>();
    List<String> all_colors = new ArrayList<>();

    String URL = "";
    String name = "";

    Document doc = null;
    Elements dates = null;

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
        dates = doc.select("rect[class=day]");
        Log.i("github_parser","github_parser dates : "+dates.text()+ " , # "+dates.size());

        return dates;
    }

    protected Element getToday(){
        return dates.last();
    }



}
