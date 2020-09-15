package com.example.nav_test.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.nav_test.R;
import com.example.nav_test.github_parser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

// 각 팀 멤버의 잔디를 심어보자
public class FragTeamgrass extends FragGrass {
    Context mContext;
    String path;
    //String teamName;//???????이게 모람
    //ArrayList<String> memberNames;
    public Team team;
    public LinkedList<Integer>[] numMemberPerDay;

    public FragTeamgrass(){
        super("noname");
    }
    public FragTeamgrass(String myid){
        super(myid);
    }
    public FragTeamgrass(String myid, Team team){
        super(myid);
        this.team = team;

        Log.i("FragTeamgrass","team name : "+team.team_name+" , "+team.getMembers().get(0));
        numMemberPerDay = new LinkedList[team.getMembers().size()];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        Log.i("individual_teamgrass","doing onCreate");
        path = requireContext().getFilesDir().getPath()+File.separator+"teamname";
        //replace default

        parse_all();
        super.all_colors=createFrame();
        boolean allSameSize = true;
        int sz=numMemberPerDay[0].size();
        for(int i =0;i<numMemberPerDay.length;i++){
            if(numMemberPerDay[i].size()!=sz){
                allSameSize=false;
            }
        }


        Log.i("individual teamgrass","variable size : "+super.all_date.size()+" , "+super.all_colors.size()+" , other else "+allSameSize);


    } // 변경


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }



    @Override
    protected void initListener() {
        //오버라이드 해주세요
        //부모의 initListener와 다른 점은 여러명의 커밋 숫자를 띄워줘야한다는 점
        super.onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullDate = (String) view.getTag(R.string.rect_date);
                int idx = (int) view.getTag(R.string.rect_idx);
                String resStr = fullDate+"날의 우리 팀 잔디 기록\n";
                TextView dayDetail = (TextView) getActivity().findViewById(R.id.selectedRectDetail);
                for (int i = 0; i < team.getMembers().size(); i++) { // 멤버수대로 돌리고
                    int numPerDay = numMemberPerDay[i].get(numMemberPerDay[i].size()-1-idx);
                    // 각 멤버의 numPerDAY는 각자 INDEX접근해서 SELECT된 INDEX접근
                    Log.i("individual_teamgrass","idx : "+idx+" 00, allnumperday : "+numPerDay+" all_num_perday size : "+numMemberPerDay[0].size());
                    resStr += team.getMembers().get(i)+"님이 심은 잔디는 "+numPerDay+" 개입니다람쥐\n";
                }
                dayDetail.setText(resStr);
            }
        };
    }

    public String darkerColor(String color){//
        Log.i("FragTeamgrass","before darken"+color);
        color = color.split("#")[1];//#을 제외한 컬러코드만 나옴
        Log.i("FragTeamgrass","color sub 2 3 :"+color.substring(2,4));
        String rr = color.substring(2,4),gg=color.substring(4,6),bb =color.substring(6,8);
        int r=Integer.parseInt(rr,16), g=Integer.parseInt(gg,16), b=Integer.parseInt(bb,16);
        int R,G,B;


        // constant factors
        final double GS_RED   = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE  = 0.114;
        R = G = B = (int)(GS_RED * r + GS_GREEN * g + GS_BLUE * b);
        //colorLong-=0x00206020;//hex 001100
        String colorGray = color.substring(0,2)+String.format("%02x",R)+String.format("%02x",G)+String.format("%02x",B);
        if(colorGray.equals("ffffffff")){
            colorGray="eeeeeeee";
        }
        Log.i("FragTeamgrass","darkerColor : "+colorGray);
        return "#"+colorGray;
    }
    public LinkedList<String> createFrame(/*int commit_num, int max,String date*/) { // 여기서 team_grass의 색 list를 만들자. (파싱 안해옴)
        //색상 변경
        //일단 안 쓰이는 함수인 거 같은데 ...?
        //여튼 색상 변경은 각 사람을 1인분으로 여기고 커밋한 사람의 비율 따지는 걸로 바꿔주세요
        LinkedList<String> colorList = new LinkedList<String>();
        int totalCount = 0;
        for(int i = 0 ; i<numMemberPerDay[0].size() ; i++){ // 한칸마다
            for(int j = 0 ; j<team.getMembers().size() ;j++) { // 팀원들의 커밋횟수를 확인
            //멤버수대로 numPerday 접근해서 0이 아니면 팀커밋을 올려준다.
                if (numMemberPerDay[j].get(i) != 0){
                    totalCount += 1;
                }
            }
            float commitNum_devide_memberSize = (float)totalCount/(float)team.getMembers().size();
            Context context = getContext();
            String color = ColorP.fromRToString(context,R.color.colorWhite);
            if(commitNum_devide_memberSize == 0){
                color= ColorP.fromRToString(context,R.color.colorWhite);
            }
            else if(commitNum_devide_memberSize<0.25){
                color = ColorP.fromRToString(context,R.color.colorFourthDark); // 연녹색
            }
            else if(commitNum_devide_memberSize<0.5){
                color = ColorP.fromRToString(context,R.color.colorThirdDark); // 녹색
            }
            else if(commitNum_devide_memberSize<0.75){
                color = ColorP.fromRToString(context,R.color.colorSecondDark); // 진녹색
            }
            else if(commitNum_devide_memberSize<=1){
                color = ColorP.fromRToString(context,R.color.colorFirstDark); //찐녹색
            }
            else{
                Log.e("devide error!", String.valueOf(commitNum_devide_memberSize));
            }


            try {
                Date cur =git_hub_time_formatter.parse(this.all_date.get(i));
                if(cur.before(team.startDate)&&!cur.equals(team.startDate)){
                    color= darkerColor(color);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            colorList.add(color);

            totalCount = 0;
        }
        Log.i("individual_teamgrass","done genr color Linked List size : "+all_colors.size());
        return colorList; // 팀원잔디색 list 통째로 반환
    }

    private void gnerNumPerDay(Elements elements){
        for(Element team_date : elements){ // 팀원 한명의 data중에 숫자 데이터만!
            String team_raw_date = team_date.attr("abs:data-date");
            String team_url_refined_date = team_raw_date.substring(19);//??
            all_date.add(team_url_refined_date);
        } // 팀원 한명의 데이터의 요소 하나씩 linkedlist에 ad
    }

    private void parse_all(){
        Log.i("mj,individual_teamgrass","name_array.size() is "+ team.getMembers().size());

        for(int i = 0; i< team.getMembers().size(); i++){
            //각 멤버의 깃허브 프론트 페이지 파싱하기
            //github_parser이용하기 - LoadingActivity에서 사용법 참고
            //일단 저장은 하지 말고 Elements로 해주세요
            //question : 여러명이면 저장소를 어떻게 생성할까?? 사람수대로 동적생성필요

            numMemberPerDay[i] = new LinkedList<Integer>();

            Elements team_dates = null; // 각 멤버 파싱용 element(for문 돌리면서 삽입)
            github_parser team_date_number_parser = new github_parser(team.getMembers().get(i));
            try{
                team_dates = team_date_number_parser.execute().get(); // 개개인 파싱시작
                Log.i("individual_teamgrass", "individual parse working");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.i("individual teamgrass","done parsing dates");
            /*
            if(i==0){
                gnerNumPerDay(team_dates);
            }

             */
            for(Element team_date : team_dates){ // 팀원 한명의 data중에 숫자 데이터만!

                String team_raw_num_perday = team_date.attr("abs:data-count");

                String team_url_refined_num_perday = team_raw_num_perday.substring(19);

                numMemberPerDay[i].add(Integer.parseInt(team_url_refined_num_perday));

                Log.i("individual_teamgrass", "parsing"+team.getMembers().get(i)+ team_url_refined_num_perday);
            } // 팀원 한명의 데이터의 요소 하나씩 linkedlist에 add.

        }

        Date today = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    }
}
