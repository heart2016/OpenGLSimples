package com.czf.first_opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class FirstRender implements GLSurfaceView.Renderer {
    private final String TAG = getClass().getSimpleName();
    //只有在创建的之后才会调用
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG,"==onSurfaceCreated==》"+Thread.currentThread().getName());
        //清楚颜色
        GLES20.glClearColor(1.0f,0,0,0);
    }

    //只有在有新的界面修改才会调用
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG,"==onSurfaceChanged==》"+Thread.currentThread().getName());
        GLES20.glViewport(0,0,width,height);

    }

    //每次刷新都会调用，requestRender就会调用
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG,"==onDrawFrame==》"+Thread.currentThread().getName());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    }
}
