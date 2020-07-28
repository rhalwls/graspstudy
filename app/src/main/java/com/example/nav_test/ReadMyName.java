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
        BufferedReader br = new BufferedReader(new FileReader(context.getCacheDir()+"/myname.txt")); // buffer입출력 통해서 txt에서 이름 받아옴.
        String readStr = "";
        try {
            readStr = br.readLine(); // readStr에 버퍼값 넣는다.
            Log.i("loadingActivity ", "my name I read is "+readStr);
            br.close();
        }
        catch (IOException e){
            readStr = "ioexception";
        }
        myname = readStr; // myname = 버퍼 통해서 txt에서 읽어온 내 이름.
    }
    public final String getMyName(){
        return myname;
    } // getMyName함수로 내 이름 반환
}
