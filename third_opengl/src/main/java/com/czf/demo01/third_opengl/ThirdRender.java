package com.czf.demo01.third_opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.czf.demo01.third_opengl.models.Mallet;
import com.czf.demo01.third_opengl.models.Table;
import com.czf.demo01.third_opengl.programs.ColorShaderProgram;
import com.czf.demo01.third_opengl.programs.TextureShaderProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ThirdRender implements GLSurfaceView.Renderer {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private Table table;
    private Mallet mallet;
    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private final float[] projectMatrix = new float[16];
    private final  float[] modeMatrix = new float[16];

    //纹理id
    private int textureID;

    public ThirdRender(Context context){
        this.context = context;

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        Log.e(TAG,"==onSurfaceCreated==》"+Thread.currentThread().getName());
        //清楚颜色，这个就是把颜色清楚为黑色。rgb都是0就是黑色
        GLES20.glClearColor(0f,0,0,0);

        table = new Table();
        mallet = new Mallet();

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);

        textureID = OpenGlUtil.readTexture(context,R.drawable.air_hockey_surface);



    }

    //只有在有新的界面修改才会调用  当宽高发生变化会调用。
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "==onSurfaceChanged==》" + Thread.currentThread().getName());
        //告诉OpenGL窗口是怎么映射的
        GLES20.glViewport(0, 0, width, height);

        //定义三位的宽高比
        OpenGlUtil.perspectiveMatrix(projectMatrix,45f,width*1.0f/height,1f,10f);
        //填满
        Matrix.setIdentityM(modeMatrix,0);
        //这个是沿 z  轴平移-2
//        Matrix.translateM(translateMatrix,0,0,0,-2f);

        Matrix.translateM(projectMatrix,0,0,0,-2.5f);
        //这个才会旋转，把z轴旋转出来，做成视觉差。
        Matrix.rotateM(modeMatrix,0,-45f,1f,0,0);

        final  float[] temp = new float[16];
        //矩阵相乘
        Matrix.multiplyMM(temp,0,projectMatrix,0,modeMatrix,0);
        //复制temp数组到 matrixFloats
        System.arraycopy(temp,0,projectMatrix,0,temp.length);


    }
    //每次刷新都会调用，requestRender就会调用
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectMatrix,textureID);
        table.bindData(textureShaderProgram);
        table.draw();

        colorShaderProgram.useProgram();
        colorShaderProgram.setUniform(projectMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

    }
}
