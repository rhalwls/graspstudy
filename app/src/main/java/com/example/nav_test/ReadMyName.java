package com.example.nav_test;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadMyName {
    public String myname;
    public ReadMyName(Context context)  {

        try {
            BufferedReader br = new BufferedReader(new FileReader(context.getCacheDir()+"/myname.txt"));
            String readStr = "";
            readStr = br.readLine();
            Log.i("loadingActivity ", "my name I read is "+readStr);
            br.close();
            myname=readStr;
        }
        catch (IOException e){
            myname = "ioexception";
        }
    }
    public String getMyName(){
        return myname;
    }
}
