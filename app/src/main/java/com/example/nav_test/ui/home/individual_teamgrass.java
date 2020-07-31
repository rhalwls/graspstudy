package com.example.nav_test.ui.home;

import androidx.fragment.app.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nav_test.R;
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;


public class individual_teamgrass extends frag_mygrass {
    Context mContext;
    String path;
    String which_block;//???????이게 모람
    ArrayList<String> memberNames = new ArrayList<>();

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


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.activity_invidual_teamgrass, container, false);






        return root;
    }

    @Override
    protected void initListener() {
        //오버라이드 해주세요
        //부모의 initListener와 다른 점은 여러명의 커밋 숫자를 띄워줘야한다는 점
    }

    public void createFrame(int commit_num, int max/*,String date*/) {
        //색상 변경
        //일단 안 쓰이는 함수인 거 같은데 ...?
        //여튼 색상 변경은 각 사람을 1인분으로 여기고 커밋한 사람의 비율 따지는 걸로 바꿔주세요
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



    private void parse_all(){
        Log.i("mj,individual_teamgrass","name_array.size() is "+ memberNames.size());
        for(int i = 0; i< memberNames.size(); i++) {
            //각 멤버의 깃허브 프론트 페이지 파싱하기
            //github_parser이용하기 - LoadingActivity에서 사용법 참고
            //일단 저장은 하지 말고 Elements로 해주세요


        }

        Date today = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    }


}
