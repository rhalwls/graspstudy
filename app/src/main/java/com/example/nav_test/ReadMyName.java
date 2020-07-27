package com.example.nav_test;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadMyName {
    final String myname;
    public ReadMyName(Context context) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(context.getCacheDir()+"/myname.txt"));
        String readStr = "";
        try {
            readStr = br.readLine();
            Log.i("loadingActivity ", "my name I read is "+readStr);
            br.close();
        }
        catch (IOException e){
            readStr = "ioexception";
        }
        myname = readStr;
    }
    public final String getMyName(){
        return myname;
    }
}
