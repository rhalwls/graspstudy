package com.example.nav_test.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nav_test.R;

import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class page_mygrass extends Fragment {

    int col_num = 6;
    LinkedList<String> all_date = new LinkedList<String>();//every date representing a small rect(about a half year)
    LinkedList<String> all_colors = new LinkedList<String>();
    LinkedList<Integer> all_num_perday = new LinkedList<Integer>();

    private Context mContext = null;
    private String myID = "noname";
    private String htmlPageUrl = "https://github.com/" + myID;

    private Button textviewHtmlDocument;

    private String htmlContentInStringFormat;
    //Button top_date = (Button)findViewById(R.id.selected_date);//이 코드 실행하면 터짐(?)

    SimpleDateFormat git_hub_time_formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월");
    SimpleDateFormat day_only = new SimpleDateFormat("dd");
    SimpleDateFormat monthOnly = new SimpleDateFormat("mm");

    // TODO: Rename and change types of parameters
    View root;
    Button.OnClickListener onClickListener;
    FrameLayout[] tv;
    public page_mygrass(String myId) {
        myID = myId;
    }
    private static final int numCols = 25;


    public void initListener(){
        onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullDate = (String) view.getTag(R.string.rect_date);
                int numPerDay = (int) view.getTag(R.string.rect_num);
                TextView dayDetail = (TextView) getActivity().findViewById(R.id.selectedRectDetail);
                dayDetail.setText(fullDate+" 날에 심은 잔디는 "+numPerDay+" 개 입니다.");
            }
        } ;

    }
    public void dealWithRects(){

        for(int i=2;i<numCols;i++){//왜 i가 2부터 시작하나요......
            for(int j=1;j<=7;j++){
                String color_temp = all_colors.pollLast();
                try {

                    String sd = all_date.pollLast();
                    Date selected_date = git_hub_time_formatter.parse(sd);
                    int numPerDay = all_num_perday.pollLast();
                    Log.i("page_mygrass","numperday : "+numPerDay);
                    String day = day_only.format(selected_date);
                    int colrowid = R.id.class.getField("col"+(numCols-i)+"row"+(8-j)).getInt(0);
                    Button colrow = (Button)root.findViewById(colrowid);
                    //숫자 관련 처리 해주기!!!
                    colrow.setTag(R.string.rect_date,sd);//store metadata to buttons
                    colrow.setTag(R.string.rect_num, numPerDay);
                    colrow.setOnClickListener(onClickListener);
                    colrow.setBackgroundColor(Color.parseColor(color_temp));
                    colrow.setText(day);
                    if(day.equals("01")){
                        colrow.setTextColor(Color.RED);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Button getRectByColRow(int c, int r){
        try {
            int colrowid = R.id.class.getField("col" + c + "row" + r).getInt(0);
            Button colrow =(Button)root.findViewById(colrowid);
            return colrow;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getRectDD(Button button){
        return Integer.parseInt(button.getText().toString());
    }
    public int getRectMM(Button button){
        String date = button.getTag(R.string.rect_date).toString();
        String[] num3 = date.split("-");
        return Integer.parseInt(num3[1]);
    }

    public String MonthArr[] = {"Non","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEPT","OCT","NOV","DEC"};


    public void addMonthUI(){
        /*
        tv[3].setBackgroundColor(getResources().getColor(R.color.colorBlue));
        tv[5].setBackgroundColor(getResources().getColor(R.color.colorBlue));

        */
        TableRow monthtr = root.findViewById(R.id.monthTR);

        for(int i =1;i<col_num;i++){
            TextView tv = new TextView(requireContext());
            Button rect = getRectByColRow(i,1);
            int repday = getRectDD(rect);
            monthtr.addView(tv);
            if(repday<=7||i ==1){
                int repmonth = getRectMM(rect);
                tv.setText(MonthArr[repmonth]);

            }

        }
        /*



        TextView monthtv;
        Button colrow =getRectByColRow(1,1);
        int mm = getRectMM(colrow);
        monthtv = new TextView(requireContext());
        monthtv.setText(MonthArr[mm]);
        monthtr.addView(monthtv);
        int ctr = 1;
        for(int i =2;i<numCols;i++){//앞에서부터 봐야댐
            int colrowid = 0;
            colrow = getRectByColRow(i,1);
            Log.i("page_mygrass","i = "+i+" , "+Integer.parseInt(colrow.getText().toString()));
            if(Integer.parseInt(colrow.getText().toString())<=7){//1-7월이 바뀌는 숫자이면 위에 달아준다
                Log.i("page_mygrass","새로운 월을 추가하고 있습니다.");
                monthtv.setWidth(40*ctr);
                mm = getRectMM(colrow);
                monthtv = new TextView(requireContext());
                monthtv.setText(MonthArr[mm]);
                monthtr.addView(monthtv);
                ctr= 1;
            }
            ctr++;
        }

         */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        tv = new FrameLayout[numCols];

        String path = requireContext().getFilesDir().getPath();
        Log.e("receive path",path);
        File file = new File(path+"/myGrassData.txt");

        long start = System.currentTimeMillis();
        initListener();


        try {
            FileReader fileReader = new FileReader(file);
            //Log.e("save","1");
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            //Log.e("save","2");
            String line= "";
            //Log.e("save","3");
            while((line = bufferedReader.readLine())!=null) { //왜 읽는 것일까
                Log.i("page_mygrass", "line read from myGrassData.txt : "+line);
                all_date.add(line);
                all_colors.add(bufferedReader.readLine());
                int num = Integer.parseInt(bufferedReader.readLine());
                if(num!= 0){
                    Log.i("page_mygrass","0이 아닌 숫자를 받음");
                }
                all_num_perday.add(num);

            }
            Log.i("page_mygrass","line read done");
            Log.i("page_mygrass","myid i got passed "+myID);
            //Log.e("save","4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Log.d("timecheck","oncreate"+(end-start));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.page_mygrass, container, false);
        Log.e("page_mygrass","created");

        final TextView top_date = (TextView) root.findViewById(R.id.selected_date);

        //상단 스크롤바 버튼
        //col 주소를 tv에 저장
        tv = new FrameLayout[numCols];
        Log.i("page_mygrass","row index start from 1 to 7");
        for(int i=1;i<=6;i++){//1~6까지 가로로 나눠먹은 듯
            int col_num = i*4-2;//이게 뭔데
            try {
                int id = R.id.class.getField("col"+col_num+"row1_background_Frame").getInt(0);
                tv[i] = (FrameLayout)root.findViewById(id);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        //버튼조작

        ImageButton left_btn = (ImageButton)root.findViewById(R.id.left_btn);
        ImageButton right_btn = (ImageButton)root.findViewById(R.id.right_btn);

        DisplayMetrics display = this.getResources().getDisplayMetrics();

        int display_width = display.widthPixels;

        final HorizontalScrollView horizontalScrollView= (HorizontalScrollView)root.findViewById(R.id.내잔디밭_scrollView);
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
        final int halfScreenWidth = (int)(display_width*0.5f);
        Log.e("halfscreen = ",Integer.toString(halfScreenWidth));
        Log.e("tv[col_num].getLeft",Integer.toString(tv[col_num].getLeft()));
        left_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                if(col_num>1){
                    col_num--;
                    horizontalScrollView.smoothScrollTo(tv[col_num].getLeft() - halfScreenWidth,0);//스크롤이동


                    //날짜변환
                    try {
                        Date original_date = formatter.parse(top_date.getText().toString());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(original_date);
                        cal.add(Calendar.MONTH,-1);


                        String scroll_starter_date = formatter.format(cal.getTime());
                        top_date.setText(scroll_starter_date);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }



            }
        });
        right_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                if(col_num<6){
                    col_num++;
                    horizontalScrollView.smoothScrollTo(tv[col_num].getLeft() - halfScreenWidth,0);//스크롤이동
                    try {
                        Date original_date = formatter.parse(top_date.getText().toString());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(original_date);
                        cal.add(Calendar.MONTH,1);


                        String scroll_starter_date = formatter.format(cal.getTime());
                        top_date.setText(scroll_starter_date);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        Log.e("try 이전:","abce");
        //Document doc = null;
        Elements dates = null;


        //loading Activity 에서 입력한 목록 입력






        //파싱한 데이터로 초기 날짜 설정
        Calendar cal = Calendar.getInstance();
        Date original_date = null;

        try {

            Log.e("num of date : ",Integer.toString(all_date.size()));
            Log.e("num of color : ",Integer.toString(all_colors.size()));
            if(all_date.size()!=0) {
                original_date = git_hub_time_formatter.parse(all_date.get(all_date.size()-1));
            }
            //요일계산용 마지막 날짜 저장

            cal.setTime(original_date);


            String scroll_starter_date = formatter.format(original_date);
            top_date.setText(scroll_starter_date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        int week = cal.get(Calendar.DAY_OF_WEEK);//일요일이 1, 토요일이 7
        //파싱한 데이터를 기반으로 색상변경

        cal.add(Calendar.DATE,7-week);
        long push_block_color_start = System.currentTimeMillis();
        for(int j=1;j<=7-week;j++){//이번주에 대해서 아직 안 온 날들인가?
            try {

                String day = day_only.format(cal.getTime());
                int colrowid = R.id.class.getField("col24row"+(8-j)).getInt(0);//7~8까지 가능해보이는데?
                Button colrow =(Button)root.findViewById(colrowid);
                colrow.setText(day);
                cal.add(Calendar.DATE,-1);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        //이번주 아직 안온날들 제외
        boolean today = true;

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullDate = (String) view.getTag(R.string.rect_date);
                int numPerDay = (int) view.getTag(R.string.rect_num);
                TextView dayDetail = (TextView) getActivity().findViewById(R.id.selectedRectDetail);
                dayDetail.setText(fullDate+" 날에 심은 잔디는 "+numPerDay+" 개 입니다.");
            }
        } ;
        for(int j=8-week;j<=7;j++){
            String color_temp = all_colors.pollLast();
            try {
                String sd = all_date.pollLast();
                Date selected_date = git_hub_time_formatter.parse(sd);
                String day = day_only.format(selected_date);
                int numPerDay = all_num_perday.pollLast();
                int colrowid = R.id.class.getField("col24row"+(8-j)).getInt(0);

                Button colrow = (Button)root.findViewById(colrowid);
                colrow.setTag(R.string.rect_date,sd);
                colrow.setTag(R.string.rect_num,numPerDay);
                colrow.setOnClickListener(onClickListener);
                colrow.setBackgroundColor(Color.parseColor(color_temp));
                colrow.setText(day);
                if(day.equals("01")){
                    colrow.setTextColor(Color.RED);
                }
                if(today ==true){
                    View border = (View)root.findViewById(colrowid).getParent();
                    border.setBackgroundColor(Color.BLUE);
                    today = false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }


        //나머지요일
        dealWithRects();
        addMonthUI();

        long push_block_color_end = System.currentTimeMillis();

        Log.d("timecheck","time:" +(push_block_color_end-push_block_color_start));


        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
