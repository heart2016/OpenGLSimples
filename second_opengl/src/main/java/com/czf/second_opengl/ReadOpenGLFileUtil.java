package com.czf.second_opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/7/14.
 */

public class ReadOpenGLFileUtil {
    //读取res/raw 文件夹下的文件
    public static String readStringFromRawResource(Context context,int resource){
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resource);
            BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream));
            String nextLine;
            while ((nextLine=reader.readLine())!=null){
                sb.append(nextLine);
                //添加换行符
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
