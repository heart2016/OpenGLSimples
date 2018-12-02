package com.czf.demo01.one_hello_triangle;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/12/2.
 */

public class ResourceUtil {
    private ResourceUtil(){
        throw new NullPointerException();
    }

    public static String loadRawToString(@NonNull Context context,  int resID){
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(resID);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String buffer = bufferedReader.readLine();
            StringBuilder builder = new StringBuilder();
            while (!TextUtils.isEmpty(buffer)){
                builder.append(buffer);
                builder.append("\n");
                buffer = bufferedReader.readLine();
            }
            return builder.toString();
        }catch (IOException e){

        }finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";

    }
}
