package com.example.nav_test;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GithubIDValidator extends AsyncTask<Void, Void, Boolean> {//



    String myURL;

    String URL = "";
    String name = "";

    Document doc = null;
    Elements dates = null;
    Elements imageURL = null;

    public GithubIDValidator(){
        name = "YooJaeHong";
        URL = "https://github.com/"+name;
    }


    public GithubIDValidator(String id){
        name = id;
        URL = "https://github.com/"+id;
    }




    protected  void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {//only discriminates validity!!

        String useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";
        try {
            //Log.e("파싱한하기전:","abce");
            Log.i("mj, github_parser", "the url : "+URL);
            doc = Jsoup.connect(URL).userAgent(useragent).get();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        //Log.e("파싱후:","abce");
       // Log.e("파싱한 값 전체:",doc.html());



        return doc!=null;
    }

    protected Element getToday(){
        return dates.last();
    }




}
