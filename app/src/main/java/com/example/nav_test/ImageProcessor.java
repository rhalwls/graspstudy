package com.example.nav_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.nav_test.ui.home.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageProcessor {
    Context context;

    public ImageProcessor(Context mContext){
        context = mContext;
    }

    public void setTeamImg(Uri photoUri, ImageView image_added){ // Uri받아서 imageview에 set해주기. 없으면 default 메소드로 기본 이미지 set
    //uri를 bitmap으로 열어주는 것.
        try{
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(photoUri));
        image_added.setImageBitmap(bitmap);

    } catch (IOException ioe) {
        Log.e("error", "error while setting team picture");
        setTeamImgWithDefault(image_added);
    }
        catch(NullPointerException npe){
        setTeamImgWithDefault(image_added);
    }
}

    public void bitmapAdapter(View view){
        //bitmap이미지를 imageview에 넣어주는 것.
    }

    public void setTeamImgWithDefault(ImageView image_added){ // team에서 기본이미지 imageview에 set
        Uri photoUri= Team.getDefaultPictureUri(context);
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(photoUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        image_added.setImageBitmap(bitmap);
    }


    private void setImage(File temp_imageFile, ImageView image_added) {
        //ImageView imageView = findViewById(R.id.input_imageview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(temp_imageFile.getAbsolutePath(), options);
        image_added.setImageBitmap(originalBm);
    }

    public void saveBitmapTojpeg(Bitmap bitmap,String name){
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
    public File getCacheDir() {
        String path = "/data/data/com.example.nav_test/cache";
        File file = new File(path);
        return file;
    }

}
