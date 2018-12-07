package com.czf.demo01.one_hello_triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

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
//    private final float[] mVerticesData =
//            {       0.0f, 0.5f, 0.0f,
//                    -0.5f, -0.5f, 0.0f,
//                    0.5f, -0.5f, 0.0f };

    private final float[] mVerticesData =
            {       0.0f, 0.0f, 0.0f,
                    0.1f, 0.5f, 0.0f,
                    0.1f, 0.1f, 0.0f ,
                    0.2f, 0.3f, 0.0f ,
                    -0.3f, 0.1f, 0.0f ,
                    -0.1f, 0.2f, 0.0f,
                    -0.3f, 0.4f, 0.0f
            };
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
        vertices = ByteBuffer.allocateDirect ( mVerticesData.length * 4 )
                .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
        vertices.put(mVerticesData).position(0);

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int pointShader = OpenGLUtil.createShader(GLES30.GL_VERTEX_SHADER,pointShaderSrc);

        if (pointShader == 0){
            Log.e(TAG,"=====pointShader========>顶点着色器失败");
            return;
        }
        int fragmentShader = OpenGLUtil.createShader(GLES30.GL_FRAGMENT_SHADER,fragmentShaderSrc);
        if (fragmentShader == 0){
            Log.e(TAG,"=====fragmentShader========>片段着色器失败");
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

        int[] compiled = new int[1];
        GLES30.glGetProgramiv(programObject,GLES30.GL_LINK_STATUS,compiled,0);
        if (compiled[0] ==0){
            Log.e(TAG,"=====链接失败========>"+GLES30.glGetProgramInfoLog(programObject));
            GLES30.glDeleteProgram(programObject);
            return;
        }
        Log.e(TAG,"=====链接成功=====111===>"+GLES30.glGetAttribLocation(programObject,"vPosition"));

//        GLES30.glGenBuffers(2, IntBuffer.allocate(100));

        mProgramObject = programObject;
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

        //first：在顶点数组中那个点开始绘制，count：绘制几个数组。
//        GLES30.glDrawArrays ( GLES30.GL_TRIANGLE_STRIP, 0, 7 );
        GLES30.glLineWidth(2);
        GLES30.glDrawArrays ( GLES30.GL_LINE_STRIP, 0, 7 );
        GLES30.glDrawElements();
        GLES30.glDrawArraysInstanced();
        GLES30.glDrawRangeElements();
        GLES30.glDrawElementsInstanced();
    }
}
