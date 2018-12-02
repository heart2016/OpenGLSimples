package com.czf.demo01.one_hello_triangle;

import android.opengl.GLES30;
import android.util.Log;

/**
 * Created by Administrator on 2018/12/2.
 */

public class OpenGLUtil {

    private OpenGLUtil(){
        throw new RuntimeException();
    }

    public static int createShader(int type,String src){
        int shader = GLES30.glCreateShader(type);
        if (shader == 0){
            Log.e("TAG","==============>"+shader);
            return 0;
        }
        Log.e("TAG","=======111=======>"+shader);
        //把代码存入请求的地址
        GLES30.glShaderSource(shader,src);
        //编译的代码
        GLES30.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,compiled,0);

        if (compiled[0]==0){
            GLES30.glDeleteShader(shader);
        }
        return shader;
    }
}
