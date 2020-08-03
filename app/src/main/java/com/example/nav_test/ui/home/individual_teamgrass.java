package com.example.nav_test.ui.home;

import androidx.fragment.app.Fragment;


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

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

// 각 팀 멤버의 잔디를 심어보자
public class individual_teamgrass extends frag_mygrass {
    Context mContext;
    String path;
    String which_block;//???????이게 모람
    ArrayList<String> memberNames;

    public LinkedList<Integer>[] numMemberPerDay;


    public individual_teamgrass(String myid){
        super(myid);


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        which_block =getArguments().getString("selected_team_name");
        path = requireContext().getFilesDir().getPath()+File.separator+"teamname";


    } // 변경


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_grass, container, false);






        return root;
    }

    @Override
    protected void initListener() {
        //오버라이드 해주세요
        //부모의 initListener와 다른 점은 여러명의 커밋 숫자를 띄워줘야한다는 점
        super.initListener();
        onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullDate = (String) view.getTag(R.string.rect_date);
                int idx = (int) view.getTag(R.string.rect_idx);
                for (int i = 0; i < memberNames.size(); i++) { // 멤버수대로 돌리고
                    int numPerDay = numMemberPerDay[i].get(numMemberPerDay[i].size()-1-idx);
                    // 각 멤버의 numPerDAY는 각자 INDEX접근해서 SELECT된 INDEX접근
                    TextView dayDetail = (TextView) getActivity().findViewById(R.id.selectedRectDetail);
                    Log.i("individual_teamgrass","idx : "+idx+" 00, allnumperday : "+numPerDay+" all_num_perday size : "+numMemberPerDay[0].size());
                    dayDetail.setText(fullDate+" 날에 심은 잔디는 "+numPerDay+" 개 입니다.\n");
                }
            }
        };
    }


    public ArrayList createFrame(/*int commit_num, int max,String date*/) { // 여기서 team_grass의 색 list를 만들자. (파싱 안해옴)
        //색상 변경
        //일단 안 쓰이는 함수인 거 같은데 ...?
        //여튼 색상 변경은 각 사람을 1인분으로 여기고 커밋한 사람의 비율 따지는 걸로 바꿔주세요
        ArrayList<String> colorList = new ArrayList<>();
        int totalCount = 0;
        for(int i = 0 ; i<numMemberPerDay[0].size() ; i++){ // 한칸마다
            for(int j = 0 ; j<memberNames.size() ;j++) { // 팀원들의 커밋횟수를 확인
            //멤버수대로 numPerday 접근해서 0이 아니면 팀커밋을 올려준다.
                if (numMemberPerDay[j].get(i) != 0){
                    totalCount += 1;
            }
        }
            float commitNum_devide_memberSize = (float)totalCount/(float)memberNames.size();
            if(commitNum_devide_memberSize == 0){
                colorList.add("#ebedf0"); // 회색
            }
            else if(commitNum_devide_memberSize<0.25){
                colorList.add("#c6e48b"); // 연녹색
            }
            else if(commitNum_devide_memberSize<0.5){
                colorList.add("#7bc96f"); // 녹색
            }
            else if(commitNum_devide_memberSize<0.75){
                colorList.add("#239a3b"); // 진녹색
            }
            else if(commitNum_devide_memberSize<=1){
                colorList.add("#196127"); //찐녹색
            }
            else{
                Log.e("devide error!", String.valueOf(commitNum_devide_memberSize));
            }
        }
        return colorList; // 팀원잔디색 list 통째로 반환
    }



    private void parse_all(){
        Log.i("mj,individual_teamgrass","name_array.size() is "+ memberNames.size());
        for(int i = 0; i< memberNames.size(); i++){
            //각 멤버의 깃허브 프론트 페이지 파싱하기
            //github_parser이용하기 - LoadingActivity에서 사용법 참고
            //일단 저장은 하지 말고 Elements로 해주세요
            //question : 여러명이면 저장소를 어떻게 생성할까?? 사람수대로 동적생성필요
            Elements team_dates = null; // 각 멤버 파싱용 element(for문 돌리면서 삽입)
            github_parser team_date_number_parser = new github_parser(memberNames.get(i));
            try{
                team_dates = team_date_number_parser.execute().get(); // 개개인 파싱시작
                Log.i("individual_teamgrass", "individual parse working");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.i("individual teamgrass","done parsing dates");

            ///////////////////////////////////////////////////
//            BufferedWriter bufferedWriter = null;
//            try{
//                String path = getFilesDir().getPath();
//                Log.e("writing path",path);
//                bufferedWriter = new BufferedWriter(new FileWriter(path + "/myGrassData.txt"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //////////////////////////////////////////////////////
            for(Element team_date : team_dates){ // 팀원 한명의 data
                String team_raw_date = team_date.attr("abs:data-date");
                //String team_raw_color = team_date.attr("abs:fill");
                String team_raw_num_perday = team_date.attr("abs:data-count");
                //int color_idx = team_raw_color.indexOf("#");
                String team_url_refined_date = team_raw_date.substring(19);//???
                //String team_url_refined_color = team_raw_color.substring(color_idx);
                String team_url_refined_num_perday = team_raw_num_perday.substring(19);

                all_date.add(team_url_refined_date);
                //all_colors.add(team_url_refined_color);
                numMemberPerDay[i].add(Integer.parseInt(team_url_refined_num_perday));
            } // 팀원 한명의 데이터의 요소 하나씩 linkedlist에 add.

        }

        Date today = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    }
}
