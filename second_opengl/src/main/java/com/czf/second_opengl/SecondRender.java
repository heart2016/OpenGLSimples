package com.czf.second_opengl;

import android.content.Context;
import android.graphics.Path;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class SecondRender implements GLSurfaceView.Renderer {
    private final String TAG = getClass().getSimpleName();
    //这个表示一个 float类型，有四个字节。
    private static final int BYTE_PER_FLOAT = 4;
    //这个表示一个顶点的向量是二维的
    private static final int POSITION_COMPONENT_COUNT = 2;

    //为了引入三维  我们把顶点的向量加为4  增加了 z 以及 w。 所以这就需要修改点的左边值了
//    private static final int POSITION_COMPONENT_COUNT = 4;

    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)*BYTE_PER_FLOAT;

    private int uColorLocation;

    private int aColorLocation;

    //注意下面的字符串常量，一定适合glsl中的变量名一致。

    //这两个值一定要和  glsl里面的变量面一直。
    private static final String U_COLOR = "u_Color";
    //添加这个是为了可以变换颜色，颜色使用数组颜色varying来渐变
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    //矩阵变换的
    private static final String U_MATRIX = "u_Matrix";

    //  4 x 4 的矩阵
    private final float[] matrixFloats = new float[16];
    private int uMatrixLocation;
    private int aPosition;
    private int linkedObjectId;
    //绘制矩形的四个顶点
    private float[] points = {
            //第一个点的位置
            0f,0f,
            //垂直于X轴的Y轴上面的点
            0,14f,
            9f,14f,
            9f,0f,
    };

    /*//两个三角形的坐标,  就是六个定点的
    private float[] trianglePoints = {
            //从原点开始绘制，逆时针的三角形
            0f,0f,
            9f,14f,
            0,14f,

            //从原点开始绘制，逆时针的三角形
            0f,0f,
            9f,0f,
            9f,14f,

            //边长 高方向上的两个中点
            0f,7f,
            9f,7f,

            //两个平台上面的
            4.5f,2f,
            4.5f,12f,

    };*/

    //在OpenGL中。都会把屏幕映射到[-1,1]  这个范围内，也就是说  在OpenGL里面
    //中点才会是 0，0   并且屏幕的最右侧是1，最左侧是-1.最上侧是1，最下侧是-1。

   /* private float[] trianglePoints = {

            //定点的选取，最好是同样的时针方向，并且是，两个三角形
           *//* -0.5f,-0.5f,
            0.5f,0.5f,
            -0.5f,0.5f,

            -0.5f,-0.5f,
            0.5f,-0.5f,
            0.5f,0.5f,
            这样引入点，只是粗略的分为两个三角形，但是我们想把矩形分为多个三角形，这样
            更加逼真写。所以接下来分成四个三角形，要对角线分割
*//*
              *//*
              0f,   0f,
            -0.5f, -0.5f,
             0.5f, -0.5f,
             0.5f,  0.5f,
            -0.5f, 0.5f,
            -0.5f, -0.5f,
            *//*

            //使用这种封闭的方式做四个矩形，必须使用
            // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);


            //后面接 三个数颜色rgb   透明度都是1，默认是1.   由于Y轴会高些 在竖屏的时候
            //  把y轴的左边改成0.8  这是由于手机的宽高比  也就是在竖屏的时候的差异决定的。
                0f,   0f,   0.5f, 0.5f, 0.5f,
             -0.5f,  -0.8f, 1f,0f,1f,
              0.5f,  -0.8f, 0f,1f,1f,
              0.5f,   0.8f, 0f,0f,0f,
             -0.5f,   0.8f, 1f,1f,0f,
             -0.5f,  -0.8f, 1f,0f,1f,

            //负数在前，起点刚开始第一点从哪个象限开始，后面也这样
            -0.5f,    0,  1f,  0f,  0f,
             0.5f,    0,  1f,  0f,  0f,


             0f,     -0.4f, 0f, 0f, 1f,
             0f,      0.4f, 1f, 0f, 0f,
    };
    */


    //为了增加三位效果。  那么就必须增加点的坐标向量。  由2维向量  变化为  3维向量
    //这个是使用uniform来生成z，w的值，但是这样的效果太差，我们优选方案是自动生成z和w的值。
   /* private float[] trianglePoints = {
            //通过这个设定z和w的值可以分析出：
            //  整个三位坐标是  接近底部是1， 也就是在三位里面最靠近用户的是1.0f
            //z  z州的值都为0，这个由于我们有了w  z轴为0  也能描绘之三为效果。

//            x      y      z     w       r    g     b
            0f,      0f,    0f,  1.5f,  0.5f, 0.5f, 0.5f,
            -0.5f,  -0.8f,  0f,  1f,  1f,   0f,   1f,
            0.5f,   -0.8f,  0f,  1f,  0f,   1f,   1f,

            0.5f,    0.8f,  0f,  2f,  0f,   0f,   0f,
            -0.5f,   0.8f,  0f,  2f,  1f,   1f,   0f,

            -0.5f,  -0.8f,  0f,  1f,  1f,   0f,   1f,

            -0.5f,   0,     0f,  1.5f,  1f,   0f,   0f,
            0.5f,    0,     0f,  1.5f,  1f,   0f,   0f,


            0f,     -0.4f,  0f,  1.25f,  0f,   0f,    1f,
            0f,      0.4f,  0f,  1.75f,  1f,   0f,    0f,
    };*/

    private float[] trianglePoints = {
            //通过这个设定z和w的值可以分析出：
            //  整个三位坐标是  接近底部是1， 也就是在三位里面最靠近用户的是1.0f
            //z  z州的值都为0，这个由于我们有了w  z轴为0  也能描绘之三为效果。

//            x      y      z     w       r    g     b
            0f,      0f,   /* 0f,  1.5f,*/  0.5f, 0.5f, 0.5f,
            -0.5f,  -0.8f,  /*0f,  1f,*/  1f,   0f,   1f,
            0.5f,   -0.8f,  /*0f,  1f,*/  0f,   1f,   1f,

            0.5f,    0.8f, /* 0f,  2f,*/  0f,   0f,   0f,
            -0.5f,   0.8f, /* 0f,  2f,*/  1f,   1f,   0f,

            -0.5f,  -0.8f,  /*0f,  1f,*/  1f,   0f,   1f,

            -0.5f,   0,     /*0f,  1.5f,*/  1f,   0f,   0f,
            0.5f,    0,    /* 0f,  1.5f,*/  1f,   0f,   0f,


            0f,     -0.4f, /* 0f,  1.25f, */ 0f,   0f,    1f,
            0f,      0.4f, /* 0f,  1.75f,*/  1f,   0f,    0f,
    };

    private FloatBuffer nativePoints;
    private Context context;

    //移动变换的矩阵  目的是为了定义z轴的坐标，也就是把平移出z轴的坐标
    private final float[] translateMatrix = new float[16];


    //下面要开始使用纹理：纹理texture。也就是多个着色器，目前只有一个点着色器和一个片段着色器
    //所以要学习纹理过滤，纹理组合，多个着色器。

    public SecondRender(Context context){
        //向本地存储（本地存储不是java虚拟机，是C底层）请求内存块
        nativePoints =  ByteBuffer.allocateDirect(trianglePoints.length * BYTE_PER_FLOAT)
               //一定要加这个，这个为了保证存储数据的顺序不会由于平台不同发生乱序
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
       //把上面的点放入本地内存中
        nativePoints.put(trianglePoints);
       this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        Log.e(TAG,"==onSurfaceCreated==》"+Thread.currentThread().getName());
        //清楚颜色，这个就是把颜色清楚为黑色。rgb都是0就是黑色
        GLES20.glClearColor(0f,0,0,0);

        //获得顶点着色器代码字符串，  或者片段着色器代码字符串
        String vertexSource = ReadOpenGLFileUtil.readStringFromRawResource(context,R.raw.simple_vertex_shader);
        String fragmentSource = ReadOpenGLFileUtil.readStringFromRawResource(context,R.raw.simple_fragment_shaper);

        int vertexObjectId = OpenGlUtil.compileVertexShader(vertexSource);

        int fragmentObjectId = OpenGlUtil.compileFragmentShader(fragmentSource);

        linkedObjectId = OpenGlUtil.linkShaderSource(vertexObjectId,fragmentObjectId);
        OpenGlUtil.validateLinked(linkedObjectId);

        //表示绘制任何OPGL，都必须使用这个对象，linkderObjectId指向的对象
        GLES20.glUseProgram(linkedObjectId);


        //获得OpenGL中的变化的指针地址;  方法名不能选错啊   变量是uniform就是选这个方法
        uMatrixLocation = GLES20.glGetUniformLocation(linkedObjectId,U_MATRIX);


//        uColorLocation = GLES20.glGetUniformLocation(linkedObjectId,U_COLOR);

        aColorLocation = GLES20.glGetAttribLocation(linkedObjectId,A_COLOR);
        //获取位置
        aPosition = GLES20.glGetAttribLocation(linkedObjectId,A_POSITION);

        //第一个点
        nativePoints.position(0);
        /**
         *第一个参数：index：上面获取的位置
         * 第二个参数：size：这里表示的就是每一个顶点会传递几个分量进入，不会超过四个分量，所谓分量就是是三维还是二维的
         * 第三个参数：type 数据类型，这里制定float类型
         * 第四个参数：boolean 只有使用了整数类型才有意义
         * 第五个参数：int stride ：
         * 第六个参数:Buffer :这个就是告诉OpenGL从那个缓冲区读取数据。
         */
       /* GLES20.glVertexAttribPointer(aPosition,POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT,false,0,nativePoints);*/

        GLES20.glVertexAttribPointer(aPosition,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,STRIDE,nativePoints);



        //通知OpenGL从哪里调用数据
        GLES20.glEnableVertexAttribArray(aPosition);

        //这个是
        nativePoints.position(POSITION_COMPONENT_COUNT);
        GLES20.glEnableVertexAttribArray(aColorLocation);
        //这第二个变量表示：颜色是三维向量的
        GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,STRIDE,nativePoints);
    }

    //只有在有新的界面修改才会调用  当宽高发生变化会调用。
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG,"==onSurfaceChanged==》"+Thread.currentThread().getName());
        //告诉OpenGL窗口是怎么映射的
        GLES20.glViewport(0,0,width,height);

       /*  这个只是加了二位的宽高比重新定义
       final float widthHeightRadio = width>height ? (width*1.0f)/height:(height*1.0f)/width;
        //这里是重新定义宽高比，也就是说这里避免发生拉升椭圆的情况
        if (width> height){
            Matrix.orthoM(matrixFloats,0,-widthHeightRadio,widthHeightRadio,-1f,1f,-1f,1f);
        }else {
            Matrix.orthoM(matrixFloats,0,-1f,1f,-widthHeightRadio,widthHeightRadio,-1f,1f);
        }*/

       //定义三位的宽高比
        OpenGlUtil.perspectiveMatrix(matrixFloats,45f,width*1.0f/height,1f,10f);
        //填满
        Matrix.setIdentityM(translateMatrix,0);
        //这个是沿 z  轴平移-2
//        Matrix.translateM(translateMatrix,0,0,0,-2f);

        Matrix.translateM(translateMatrix,0,0,0,-2.5f);
        //这个才会旋转，把z轴旋转出来，做成视觉差。
        Matrix.rotateM(translateMatrix,0,-45f,1f,0,0);

        final  float[] temp = new float[16];
        //矩阵相乘
        Matrix.multiplyMM(temp,0,matrixFloats,0,translateMatrix,0);
        //复制temp数组到 matrixFloats
        System.arraycopy(temp,0,matrixFloats,0,temp.length);
    }

    //每次刷新都会调用，requestRender就会调用
    @Override
    public void onDrawFrame(GL10 gl) {
//        Log.e(TAG,"==onDrawFrame==》"+Thread.currentThread().getName());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //更新 片段着色器中的uniform vec4 的u_Color的值，  后面的四个参数死rgb颜色以及a透明度

        //为了修正宽高比
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrixFloats,0);


        //这个是给片段，定点设置颜色
//        GLES20.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);

        //这个就是连线绘制了，GL_TRIANGLES 表示的是三角形，表示绘制的是三角形
        // 后面两个参数表示从缓冲区的那个位置开始, 0表示从0开始，6表示几个长度，两个三角形是6个点
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);

//        GLES20.glUniform4f(uColorLocation,1.0f,0f,0f,1.0f);
        //绘制直线
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制两个顶点，就是防止木槌  后面的x，y，z，w  是颜色
//        GLES20.glUniform4f(uColorLocation,0f,0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

//        GLES20.glUniform4f(uColorLocation,1.0f,0f,0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
    }
}
