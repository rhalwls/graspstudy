package com.example.nav_test.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_test.AdapterForGithubIDInput;
import com.example.nav_test.ImageProcessor;
import com.example.nav_test.LoginActivity;
import com.example.nav_test.R;
import com.example.nav_test.ReadMyName;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;


public class ActivityInputTeam extends Activity {

    private static final String TAG = "beomgeun";
    private Boolean isPermission = true;
    String user;
    String formerTeamName = "NOT_EXIST";//NOT_EXIST가 아니면 기존 파일을 지우는 작업도 필요함
    private static final String NOT_EXIST ="NOT_EXIST";
    File temp_imageFile;

    EditText ed = null;
    //List<EditText> TeamMemberETs = new ArrayList<EditText>();
    LinearLayout outer;
    EditText input_teamname;
    Button member_plus_button;//remains, needs to control the member adapter
    ArrayList<String> members = new ArrayList<String>();//give this reference to Adapter
    RecyclerView recyclerInputTeamMembers;
    AdapterForGithubIDInput adapterForGithubIDInput;
    Button confirm;
    Button image_add_button;
    ImageView image_added;
    DatePicker datePickerStart;

    ImageProcessor imageProcessor;


    Activity root = null;

    int num_of_text = 1;
    public void addTeamMemberET(String memberName){//if memberName is null it adds a new member input
        members.add(memberName);
        Log.i("ActivityInputTeam","this member size : "+members.size()+" , adapter member size : "+adapterForGithubIDInput.getItemCount());

        adapterForGithubIDInput.notifyDataSetChanged();
        //일단 임시방편으로 edittext done 때 검사하는 것 말고 추가할 때 넣는 걸로 함
        for(int i =0;i<members.size();i++){
            Log.i("ActivityInputTeam","team member added and full mem #"+members.get(i));
        }

    }
    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tedPermission();
        setContentView(R.layout.activity_input_team);
        onCreateView(getLayoutInflater());
        user = new ReadMyName(this).getMyName();
        Log.i("ActivityInputTeam","onCreate()");

    }

    public void onCreateView(LayoutInflater inflater) {


        // Inflate the layout for this fragment
        root = this;
        //map the views to java variables
        outer = root.findViewById(R.id.text_container);
        input_teamname = root.findViewById(R.id.input_teamname_text);
        member_plus_button = root.findViewById(R.id.member_plus_btn);
        confirm = root.findViewById(R.id.create_btn);
        image_add_button = root.findViewById(R.id.input_image_button);
        image_added = root.findViewById(R.id.team_edit_imageview);
        datePickerStart =(DatePicker) findViewById(R.id.team_start_date);
        recyclerInputTeamMembers =(RecyclerView) findViewById(R.id.recycler_team_members);
        adapterForGithubIDInput = new AdapterForGithubIDInput(this.members);
        Log.i("ActivityInputTeam","adapter team # member :"+adapterForGithubIDInput.getItemCount());
        recyclerInputTeamMembers.setLayoutManager(new LinearLayoutManager(this));//call this first and set adapter later
        recyclerInputTeamMembers.setAdapter(adapterForGithubIDInput);
        /*
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        recyclerInputTeamMembers.setLayoutParams(lp);
        */

        //set adapter after checking team null

        Intent intent = getIntent();

        imageProcessor = new ImageProcessor(getApplicationContext());

        Team team = (Team) intent.getSerializableExtra("teamObj");
        if(team !=null){// 실험

            formerTeamName = team.team_name;

            //members에 넣어야댐
            members.addAll(team.getMembers());
            adapterForGithubIDInput.notifyDataSetChanged();

            mappingUI(team); //edit team
            //need to erase the former team and store the edited info as a new team(for time saving)
            //erase when the user REALLY WANTS TO UPDATE TEAM INFO


            Log.i("ActivityInputTeam","team is not null");
        }
        else{
            //empty team
            //input new team
            addTeamMemberET("");//empty edit text
            Log.i("ActivityInputTeam","team is null");
        }


        image_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermission) goToAlbum();
            }
        });

        member_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeamMemberET("");
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {//store the input and finish activity
            @Override
            public void onClick(View v) {
                //시간이 되면 입력 양식이 올바른지 검사하는 코드도 만들 것
                Team team = mappingTeam();
                String teamname = team.team_name;
                if(ActivityInputTeam.this.formerTeamName!=NOT_EXIST){
                    //formerTeamName이 이전 파일 이름
                    boolean ret = Team.deleteTeamFile(ActivityInputTeam.this, user, formerTeamName);
                    Log.i("ActivityInputTeam","trying to remove former team "+formerTeamName+" success ? "+ret);
                }

                if(teamname == ""||team.getMembers().size()==0){
                    //입력 양식이 올바른지 검사
                    //시간이 된다면 유효한 깃허브 아이디인지도 확인하기(아직 안함!!!!)
                    Toast toast = Toast.makeText(ActivityInputTeam.this, "팀 이름이 비었거나 팀 멤버가 없습니다", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Log.i("ActivityInputTeam","팀을 생성중입니다.");
                    team.storeTeamFile(ActivityInputTeam.this, user);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ActivityInputTeam.this.getCurrentFocus().getWindowToken(), 0);


                    if (image_added.getDrawable() != null) {
                        image_added.buildDrawingCache();
                        Bitmap bitmap = image_added.getDrawingCache();
                        imageProcessor.saveBitmapTojpeg(bitmap, teamname);
                    }

                    //ActivityInputTeam.this.finish();//이렇게 하지 말고 다시 잔디 다람쥐 보여주는 게 더 맞는듯

                    Intent intent=new Intent(ActivityInputTeam.this, LoginActivity.class);
                    startActivity(intent);

                }
            }

        });
        setDateStartRange(datePickerStart);
    }


    public void mappingUI(Team team) { // 아마 기존 팀정보 불러오는 것(팀 수정시 사용되는거같음)
        input_teamname.setText(team.team_name);
        imageProcessor.setTeamImg(team.pictureUri, image_added);//if new team, need to initialize adapter with empty list
        //else if existing team, need to init adapter with existing list
        imageProcessor.setTeamImg(team.pictureUri, image_added);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public Team mappingTeam(){//from ui to team
        //if return is null it fails to generate a team
        String teamname = input_teamname.getText().toString();
        Team team = new Team(teamname);
        ArrayList<String> membersFromAdapter = adapterForGithubIDInput.getRefinedMembers();
        if(membersFromAdapter !=null) {
            team.setMembers(membersFromAdapter);
        }
        else{
            return null;//fail
        }

        Log.i("ActivityInputTeam","Members EditText size : "+team.getMembers().size());

        Date startDate = getDateFromDatePicker(datePickerStart);
        team.setStartDate(startDate);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ActivityInputTeam.this.getCurrentFocus().getWindowToken(),0);

        if (image_added.getDrawable()!=null){
            image_added.buildDrawingCache();
            Bitmap bitmap = image_added.getDrawingCache();
            imageProcessor.saveBitmapTojpeg(bitmap,teamname);
        }
        return team;
    }

    public void setDateStartRange(DatePicker datePicker){
        //현재로부터 5개월 전까지만 스터디 기간으로 잡는다
        Date curdate = new Date();
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH,-150);
        datePicker.setMinDate(minDate.getTimeInMillis());
        datePicker.setMaxDate(curdate.getTime());
    }

    private static final int PICK_FROM_ALBUM = 1;

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    } // intent를 통해 앨범화면으로 넘어간다. startActivitForResult에
    //PICK_FROM_ALBUM 변수 넣어줌.
    //onActivityResult에서 requestCode로 반환되는 값
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        image_added = findViewById(R.id.team_edit_imageview);

        if (requestCode == PICK_FROM_ALBUM) { // requestCode가 pickfromalbum이면 정상실행
            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);
            imageProcessor.setTeamImg(photoUri, image_added);

        }
    }
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

}
