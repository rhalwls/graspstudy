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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
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

public class FragGrass extends Fragment {

    int col_num = 6;
    protected LinkedList<String> all_date = new LinkedList<String>();//every date representing a small rect(about a half year) // github의 잔디날짜를 파싱
    protected LinkedList<String> all_colors = new LinkedList<String>(); // rgb color
    private LinkedList<Integer> all_num_perday = new LinkedList<Integer>(); // commit 횟수
    protected Context mContext = null; // activity 상황
    protected String myID = "noname";
    private String htmlPageUrl = "https://github.com/" + myID;
    public int ctr = 0;
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
    public FragGrass(){

    }
    public FragGrass(String myId) {
        myID = myId;
    }
    protected static final int numCols = 25; // 한 row에 25개 1 2 3 4~25


    protected void initListener(){ // 잔디 누르면 밑에 text 뜨는 리스너
        onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullDate = (String) view.getTag(R.string.rect_date); // rect_date 키를 통해 sd 반환(selected date)
                int idx = (int) view.getTag(R.string.rect_idx); // tag 검색해서 알아보기 -> rect_idx로 cnt 반환
                int numPerDay = all_num_perday.get(all_num_perday.size()-1-idx);
                TextView dayDetail = (TextView) getActivity().findViewById(R.id.selectedRectDetail);
                Log.i("frag_mygrass","idx : "+idx+" 00, allnumperday : "+numPerDay+" all_num_perday size : "+all_num_perday.size());
                dayDetail.setText(fullDate+" 일에 심은 잔디는 "+numPerDay+" 개 입니다.");
            }
        };
    }
    public void dealWithRects(){
        for(int i=2;i<numCols;i++){//왜 i가 2부터 시작하나요...... // 인덱스1은 이번주(처리해야할게 있음)
            for(int j=1;j<=7;j++){
                //String color_temp = all_colors.pollLast();
                addDayUI(i,j);
            }
        }
    }
    public Button getRectByColRow(int c, int r){ // colrow를 파라미터로 Button colrow 가져오기.
        try {
            int colrowid = R.id.class.getField("col" + c + "row" + r).getInt(0); // getField 찾아보기
            Button colrow =(Button)root.findViewById(colrowid); // R.ajtllsdjl fjao
            return colrow;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected int getRectDD(Button button){
        return Integer.parseInt(button.getText().toString()); // 버튼에 써져있는 일자 가져오기
    }
    protected int getRectMM(Button button){
        String date = button.getTag(R.string.rect_date).toString(); // 메타데이터에서 월 뽑아오기
        String[] num3 = date.split("-");
        return Integer.parseInt(num3[1]);
    }
    public String MonthArr[] = {"Non","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEPT","OCT","NOV","DEC"};
    public String DayArr[] = {"MON","TUE","WED","THU","FRI","SAT","SUN"};//length 7
    public void addDayUI(int i, int j){//왜 근데 지금 day가 하나씩 차이 나는 잔디모가 있는지 모르겠다
        //아터님의 경우 18일에 5갠데 6개로 나옴
        try {
            Log.i("FragGrass","all_colors size "+all_colors.size() );
            String color_temp = all_colors.pollLast(); // 링크드리스트에서 마지막요소를 반환하면서 제거.
            String sd = all_date.pollLast(); // selected date
            Date selected_date = git_hub_time_formatter.parse(sd); // yyyy-mm-dd
            String day = day_only.format(selected_date);//selected_dates에서 day만 뽑아오기.
            int colrowid = 0;

            colrowid = R.id.class.getField("col"+(numCols-i)+"row"+(8-j)).getInt(0);
            Log.i("frag_mygrass","adding col"+(numCols-i)+"row"+(8-j));
            Button colrow = (Button)root.findViewById(colrowid);
            //숫자 관련 처리 해주기!!!
            colrow.setTag(R.string.rect_date,sd);//store metadata to buttons // colrow에 R.string.rect_date라는 항목을 만들고 그것의 value는 sd이다.
            colrow.setTag(R.string.rect_idx, ctr);//to access additional data(separate from ui)
            colrow.setOnClickListener(onClickListener);
            Log.i("FragGrass","color_temp : "+color_temp);
            colrow.setBackgroundColor(Color.parseColor(color_temp));
            colrow.setText(day);
            if(day.equals("01")){
                colrow.setTextColor(getResources().getColor(R.color.colorIndianRed));
            }
            ctr++;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }



    public void addMonthUI(){ // 1

        TableRow monthtr = root.findViewById(R.id.monthTR);
        Log.i("grass","const col_num = "+col_num);
        for(int i =1;i<numCols;i++){
            TextView tv = new TextView(requireContext());
            Button rect = getRectByColRow(i,1);
            int repday = getRectDD(rect);
            monthtr.addView(tv);
            Log.i("grass","adding a month view(empty or valid) and repday : " +repday);
            if(repday<=7||i ==1){ //7보다 낮은 날짜면 월이 바뀜
                int repmonth = getRectMM(rect);
                tv.setText(" "+MonthArr[repmonth]);
                tv.setHeight(50);
                Log.i("grass","intend to add month mark");
            }

        }

    }

    public void dealWithRestUI(){
        tv = new FrameLayout[numCols];


    }

    public void dealWithCurrentWeek(){
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK);//일요일이 1, 토요일이 7
        //파싱한 데이터를 기반으로 색상변경

        cal.add(Calendar.DATE,7-week);

        for(int j=1;j<=7-week;j++){//이번주에 대해서 아직 안 온 날들인가?
            try {
                String day = day_only.format(cal.getTime());
                int colrowid = R.id.class.getField("col24row"+(8-j)).getInt(0);//7~8까지 가능해보이는데?
                Button colrow =(Button)root.findViewById(colrowid);
                colrow.setText(day);
                colrow.setBackgroundColor(getResources().getColor(R.color.colorStrongGrayBlue));
                cal.add(Calendar.DATE,-1);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        //이번주 아직 안온날들 제외
        boolean today = true;


        for(int j=8-week;j<=7;j++){//이번주 이미 지나간 날들 오늘 포함
            addDayUI(1,j);
            try{
                if(today ==true){
                    int colrowid = R.id.class.getField("col24row"+(8-j)).getInt(0);
                    View border = (View)root.findViewById(colrowid).getParent();
                    border.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    //Log.i("border sz",border.getWidth()+" , "+border.getHeight());
                    today = false;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
    public void loadPersonalData(){


        String path = requireContext().getFilesDir().getPath();
        Log.e("receive path",path);
        File file = new File(path+"/myGrassData.txt"); //읽어오고잇슴

        long start = System.currentTimeMillis();
        initListener();


        try {
            FileReader fileReader = new FileReader(file);
            //Log.e("save","1");
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            String line= "";
            //Log.e("save","3");
            while((line = bufferedReader.readLine())!=null) { //왜 읽는 것일까
                Log.i("grass", "line read from myGrassData.txt : "+line);
                all_date.add(line);
                all_colors.add(bufferedReader.readLine());
                int num = Integer.parseInt(bufferedReader.readLine());
                if(num!= 0){
                    Log.i("grass","0이 아닌 숫자를 받음");
                }
                all_num_perday.add(num);

            }
            Log.i("grass","line read done");
            Log.i("grass","myid i got passed "+myID);
            //Log.e("save","4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Log.d("timecheck","oncreate"+(end-start));
        Log.i("frag_mygrass","# of 3 components : "+all_date.size()+" , "+ all_colors.size()+" , "+all_num_perday.size());

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        loadPersonalData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        long push_block_color_start = System.currentTimeMillis();
        // Inflate the layout for this fragment
        Log.i("for git test","hello world! 1114");
        root = inflater.inflate(R.layout.grass, container, false);
        Log.e("grass","created");

        final TextView top_date = (TextView) root.findViewById(R.id.selected_date);

        //상단 스크롤바 버튼
        //col 주소를 tv에 저장
        tv = new FrameLayout[numCols];
        Log.i("grass","row index start from 1 to 7");
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

        Date curDate = new Date();

        horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollX = horizontalScrollView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                //Log.i("grass","scroll value : "+scrollX);



                Calendar cal = Calendar.getInstance();
                cal.setTime(curDate);
                if(scrollX>0) {
                    cal.add(Calendar.MONTH, (scrollX / 340) - 5);//야매
                    String scroll_starter_date = formatter.format(cal.getTime());
                    top_date.setText(scroll_starter_date);
                }

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
        dealWithCurrentWeek();



        //나머지요일
        dealWithRects();
        addMonthUI();

        long push_block_color_end = System.currentTimeMillis();

        Log.d("timecheck","time:" +(push_block_color_end-push_block_color_start));
        Log.i("frag_mygrass","idx 13 // 7/18 : "+all_num_perday.get(all_num_perday.size()-1-13));

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
