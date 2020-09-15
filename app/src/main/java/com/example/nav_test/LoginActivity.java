package com.example.nav_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {
    public String user_file = "nofile";
    public String user_name = "noname";
    boolean need_login(){
        return !new File(user_file).exists();
    }
    void doLogin(){
        EditText myname = findViewById(R.id.input_myname);

        Button login = findViewById(R.id.login);

        Log.i("LoginActivity","doLogin()");
        login.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                File file = new File(user_file) ;
                file.delete();
                FileWriter fw = null ;
                String text = myname.getText().toString();
                //check whether a valid github id
                GithubIDValidator validator = new GithubIDValidator(text);
                try {
                    Boolean isValidUsername = validator.execute().get();
                    if(!isValidUsername){
                        Log.i("LoginActivity","invalid user: "+text);
                        Toast toast = Toast.makeText(getApplicationContext(), "아이디 "+text+"는 유효한 깃허브 아이디가 아닙니다. 확인 후 입력해주세요.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    // open file.
                    fw = new FileWriter(file) ;

                    // write file.
                    fw.write(text) ;
                    user_name = text;
                } catch (Exception e) {
                    e.printStackTrace() ;
                }

                // close file.
                if (fw != null) {
                    // catch Exception here or throw.
                    try {
                        fw.close() ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                afterLogin();
            }
        });
    }
    public String loadLogin(){
        File file = new File(user_file);
        String data;
        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine(); //only the first line contains the id
            return line;
        }
        catch (Exception e){
            e.printStackTrace();
            return "readfilefail_id";
        }

    }
    void afterLogin(){
        Intent loading = new Intent(LoginActivity.this, LoadingActivity.class);
        startActivity(loading);
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_file = getCacheDir()+"/myname.txt";
        boolean nl = need_login();
        if(nl){
            Log.i("Login activity","no login data remians");
            doLogin();
        }
        else{
            Log.i("Login activity","login data remians");
            user_name = loadLogin();
            Log.i("Login activity","read user_name = "+user_name);
            afterLogin();
        }

    }
}
