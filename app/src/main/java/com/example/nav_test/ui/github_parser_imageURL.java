package com.example.nav_test.ui;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class github_parser_imageURL extends AsyncTask<String, Void, String> {
    String myURL;
    String URL = "";
    String name = "";
    Document doc = null;

    public github_parser_imageURL(){
        name = "bbeomgeun";
        URL = "https://github.com/"+name;
    }


    public github_parser_imageURL(String id){
        name = id;
        URL = "https://github.com/"+id;
    }

    protected  void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";

        try {
            //Log.e("파싱한하기전:","abce");
            Log.i("github_parser_imageURL", "the url : "+URL);
            doc = Jsoup.connect(URL).userAgent(useragent).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        myURL = doc.select("img[class=avatar avatar-user width-full border bg-white]").attr("src");
        Log.i("github_parser","github_parser image_url : "+myURL);

        return myURL;
    }
}
