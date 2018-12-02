package com.czf.demo01.one_hello_triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/12/2.
 */

public class OneTriangleRender  implements GLSurfaceView.Renderer {
    private final String TAG = "OneTriangleRender";
    private final float[] mVerticesData =
            { 0.0f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f };

    private FloatBuffer vertices;

    private int mProgramObject;
    private String pointShaderSrc;
    private String fragmentShaderSrc;
    private Context context;
    private int mWidth;
    private int mHeight;

    public OneTriangleRender(Context context){
        this.context = context;
        pointShaderSrc = ResourceUtil.loadRawToString(context,R.raw.one_point_shader);
        fragmentShaderSrc = ResourceUtil.loadRawToString(context,R.raw.one_fragment_shader);
        Log.e(TAG,"==========>"+pointShaderSrc);
        Log.e(TAG,"==========>"+fragmentShaderSrc);
        vertices = ByteBuffer.allocateDirect ( mVerticesData.length * 4 )
                .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
        vertices.put(mVerticesData).position(0);

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int pointShader = OpenGLUtil.createShader(GLES30.GL_VERTEX_SHADER,pointShaderSrc);

        if (pointShader == 0){
            Log.e(TAG,"=====pointShader========>"+pointShader);
            return;
        }
        int fragmentShader = OpenGLUtil.createShader(GLES30.GL_FRAGMENT_SHADER,fragmentShaderSrc);
        if (fragmentShader == 0){
            Log.e(TAG,"=====fragmentShader========>"+fragmentShader);
            return;
        }
        int programObject = GLES30.glCreateProgram();

        if (programObject == 0){
            return;
        }
        // 连接到程序对象中
        GLES30.glAttachShader(programObject,pointShader);
        GLES30.glAttachShader(programObject,fragmentShader);

        //这个vPosition
        GLES30.glBindAttribLocation(programObject,0,"vPosition");

        GLES30.glLinkProgram(programObject);
        Log.e(TAG,"=====1111========>"+programObject);
        int[] compiled = new int[1];
        GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,compiled,0);
        if (compiled[0] ==0){
            GLES30.glDeleteProgram(programObject);
            return;
        }
        mProgramObject = programObject;
        Log.e(TAG,"=====222========>"+compiled);
        GLES30.glClearColor ( 1.0f, 1.0f, 1.0f, 0.0f );
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glViewport(0,0,mWidth,mHeight);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glUseProgram(mProgramObject);


        GLES30.glVertexAttribPointer ( 0, 3, GLES30.GL_FLOAT, false, 0, vertices );
        GLES30.glEnableVertexAttribArray ( 0 );

        GLES30.glDrawArrays ( GLES30.GL_TRIANGLES, 0, 3 );
    }
}
