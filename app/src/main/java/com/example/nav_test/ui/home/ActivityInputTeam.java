package com.example.nav_test.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.nav_test.ImageProcessor;
import com.example.nav_test.R;
import com.example.nav_test.ReadMyName;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class ActivityInputTeam extends Activity {

    private static final String TAG = "beomgeun";
    private Boolean isPermission = true;
    String formerTeamName = "NOT_EXIST";//NOT_EXIST가 아니면 기존 파일을 지우는 작업도 필요함
    File temp_imageFile;

    EditText ed = null;
    final List<EditText> TeamMemberETs = new ArrayList<EditText>();
    LinearLayout outer;
    EditText input_teamname;
    Button member_plus_button;
    LinearLayout layoutInputTeamMembers;
    Button confirm;
    Button image_add_button;
    ImageView image_added;
    DatePicker datePickerStart;

    ImageProcessor imageProcessor;


    Activity root = null;

    int num_of_text = 1;
    public EditText addTeamMemberET(String memberName){
        EditText ed = new EditText(ActivityInputTeam.this);
        ed.setWidth(50);
        ed.setHint("팀원 username");
        ed.setText(memberName);
        TeamMemberETs.add(ed);
        layoutInputTeamMembers.addView(ed);
        return ed;
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

        Log.i("ActivityInputTeam","onCreate()");

    }

    public void onCreateView(LayoutInflater inflater) {


        // Inflate the layout for this fragment
        root = this;

        outer = root.findViewById(R.id.text_container);
        input_teamname = root.findViewById(R.id.input_teamname_text);
        member_plus_button = root.findViewById(R.id.member_plus_btn);
        confirm = root.findViewById(R.id.create_btn);
        image_add_button = root.findViewById(R.id.input_image_button);
        image_added = root.findViewById(R.id.team_edit_imageview);
        datePickerStart =(DatePicker) findViewById(R.id.team_start_date);
        layoutInputTeamMembers =(LinearLayout) findViewById(R.id.layout_input_team_members);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutInputTeamMembers.setLayoutParams(lp);
        Intent intent = getIntent();

        imageProcessor = new ImageProcessor(getApplicationContext());

        Team team = (Team) intent.getSerializableExtra("teamObj");
        if(team !=null){
            mappingUI(team); //edit team
            formerTeamName = team.team_name;
            //need to erase the former team and store the edited info as a new team(for time saving)
            Log.i("ActivityInputTeam","team is not null");
        }
        else{
            //empty team
            //input new team
            Log.i("ActivityInputTeam","team is null");
        }

        addTeamMemberET("");//empty edit text


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
                String path = "";
                String teamname = input_teamname.getText().toString();
                try {
                    path = getFilesDir().getPath() + File.separator + "teamname";
                    File teamname_dir = new File(path);
                    if (!teamname_dir.exists()) // teamname_dir이 존재하지않는다면 만든다.
                        teamname_dir.mkdirs();

                    //Todo : 팀원아이디 입력 부분 파일 저장 파트
                    Log.e("output_file_path", path + "/" + teamname + ".txt");
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + "/" + teamname + ".txt"));
                    // output2 = new FileOutputStream(path+"/team3.txt");
                    for (int i = 0; i < TeamMemberETs.size(); i++) {

                        String member = TeamMemberETs.get(i).getText().toString();
                        if(member!="") {//시간이 되면 존재하는 아이디인지 검사도 할 것
                            Log.e("writed id", member);
                            bufferedWriter.write(member);
                            bufferedWriter.newLine();
                        }
                    }
                    bufferedWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ActivityInputTeam.this.getCurrentFocus().getWindowToken(),0);



                if (image_added.getDrawable()!=null){
                    image_added.buildDrawingCache();
                    Bitmap bitmap = image_added.getDrawingCache();
                    imageProcessor.saveBitmapTojpeg(bitmap,teamname);
                }


                ActivityInputTeam.this.finish();
            }

        });
        setDateStartRange(datePickerStart);
    }


    public void mappingUI(Team team) { // 아마 기존 팀정보 불러오는 것(팀 수정시 사용되는거같음)
        input_teamname.setText(team.team_name);
        imageProcessor.setTeamImg(team.pictureUri, image_added);
        ArrayList<String> teamMembers = team.getMembers();
        for(int i=0;i<team.getMembers().size();i++){
            addTeamMemberET(teamMembers.get(i));
        }
        imageProcessor.setTeamImg(team.pictureUri, image_added);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public Team mappingTeam(){
        String teamname = input_teamname.getText().toString();
        Team team = new Team(teamname);
        for (int i = 0; i < TeamMemberETs.size(); i++) {//convert team member edittexts to LinkedList
            String member = TeamMemberETs.get(i).getText().toString();
            Log.e("writed id", member);
            team.members.add(member);
        }
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
