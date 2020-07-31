package com.example.nav_test.ui.home;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

public class ParseResult {
    public String user;
    private LinkedList<String> all_date = new LinkedList<String>();//every date representing a small rect(about a half year)
    private LinkedList<String> all_colors = new LinkedList<String>();
    private LinkedList<Integer> all_num_perday = new LinkedList<Integer>();

    //date는 필요가 없을 수도 잇슴
    public ParseResult(String u, Elements rects){
        user= u;
        for (Element date : rects) {
            String raw_date = date.attr("abs:data-date");
            String raw_color = date.attr("abs:fill");
            String raw_num_perday = date.attr("abs:data-count");

            int color_idx = raw_color.indexOf("#");
            //Log.i("mj loadingActivity", raw_date+" , "+raw_color+" , "+raw_num_perday);
            String url_refined_date = raw_date.substring(19);//날짜 시작 위치 찾기 위해 2019년과는 관련 없음
            String url_refined_color = raw_color.substring(color_idx);
            String url_refined_num_perday = raw_num_perday.substring(19);

            all_date.add(url_refined_date);
            all_colors.add(url_refined_color);
            all_num_perday.add(Integer.parseInt(url_refined_num_perday));
        }
    }



    public LinkedList<String> getAll_date() {
        return all_date;
    }

    public void setAll_date(LinkedList<String> all_date) {
        this.all_date = all_date;
    }

    public LinkedList<String> getAll_colors() {
        return all_colors;
    }

    public void setAll_colors(LinkedList<String> all_colors) {
        this.all_colors = all_colors;
    }

    public LinkedList<Integer> getAll_num_perday() {
        return all_num_perday;
    }

    public void setAll_num_perday(LinkedList<Integer> all_num_perday) {
        this.all_num_perday = all_num_perday;
    }
}
