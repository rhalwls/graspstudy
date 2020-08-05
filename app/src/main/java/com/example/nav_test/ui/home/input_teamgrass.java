package com.example.nav_test.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.nav_test.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class input_teamgrass extends Fragment{
    Context context = getContext();

    private static final String TAG = "beomgeun";
    private Boolean isPermission = true;

    File temp_imageFile;

    EditText ed = null;
    final List<EditText> allEds = new ArrayList<EditText>();

    Fragment thisfrag = this;

    View root = null;

    int num_of_text = 1;


    public input_teamgrass() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tedPermission();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_input_teamgrass, container, false);

        final LinearLayoutCompat outer = root.findViewById(R.id.text_container);
        final EditText input_teamname = root.findViewById(R.id.input_teamname_text);
        Button member_plus_button = root.findViewById(R.id.member_plus_btn);
        Button confirm = root.findViewById(R.id.create_btn);
        Button image_add_button = root.findViewById(R.id.input_image_button);
        ImageView image_added = root.findViewById(R.id.input_imageview);

        EditText ed = new EditText(requireContext());
        ed.setHint("팀원 아이디");
        ed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        allEds.add(ed);
        outer.addView(ed);

        image_add_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isPermission) goToAlbum();
            }
        });

        member_plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = new EditText(requireContext());
                ed.setHint("팀원 아이디");


                ed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                allEds.add(ed);
                outer.addView(ed);


            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "";
                String teamname = input_teamname.getText().toString();
                try {
                    path = requireContext().getFilesDir().getPath() + File.separator + "teamname";
                    File teamname_dir = new File(path);
                    if (!teamname_dir.exists())
                        teamname_dir.mkdirs();

                    //Todo : 팀원아이디 입력 부분 파일 저장 파트
                    Log.e("output_file_path", path + "/" + teamname + ".txt");
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + "/" + teamname + ".txt"));
                    // output2 = new FileOutputStream(path+"/team3.txt");
                    for (int i = 0; i < allEds.size(); i++) {
                        String member = allEds.get(i).getText().toString();
                        Log.e("writed id", member);
                        bufferedWriter.write(member);
                        bufferedWriter.newLine();
                        //bufferedWriter.newLine();
                    }
                    bufferedWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                View view = getActivity().getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);



                if (image_added.getDrawable()!=null){
                    image_added.buildDrawingCache();
                    Bitmap bitmap = image_added.getDrawingCache();
                    saveBitmapTojpeg(bitmap,teamname);
                }

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans;
                trans = manager.beginTransaction();
                trans.remove(thisfrag);
                trans.commit();
            }
        });
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        ImageView imageView = getView().findViewById(R.id.input_imageview);

        if (requestCode == PICK_FROM_ALBUM) { // requestCode가 pickfromalbum이면 정상실행
            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(photoUri));


                imageView.setImageBitmap(bitmap);
                //messageText.setText("Uploading file path:" +imagepath);

            } catch (IOException ie) {
                Log.e("error", "error");
                //messageText.setText("Error");
            }
        }
    }

    private void setImage() {

        ImageView imageView = getView().findViewById(R.id.input_imageview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(temp_imageFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
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

        TedPermission.with(requireContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    private void saveBitmapTojpeg(Bitmap bitmap,String name){
        File storage = getCacheDir();
        String fileName = name+".jpg";
        File tempFile = new File(storage,fileName);

        try{

            tempFile.createNewFile();

            FileOutputStream out = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File getCacheDir() {
        String path = "/data/data/com.example.nav_test/cache";
        File file = new File(path);
        return file;
    }

}
