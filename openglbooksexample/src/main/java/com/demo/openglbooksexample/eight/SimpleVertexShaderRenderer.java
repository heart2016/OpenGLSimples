// The MIT License (MIT)
//
// Copyright (c) 2013 Dan Ginsburg, Budirijanto Purnomo
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

//
// Book:      OpenGL(R) ES 3.0 Programming Guide, 2nd Edition
// Authors:   Dan Ginsburg, Budirijanto Purnomo, Dave Shreiner, Aaftab Munshi
// ISBN-10:   0-321-93388-5
// ISBN-13:   978-0-321-93388-1
// Publisher: Addison-Wesley Professional
// URLs:      http://www.opengles-book.com
//            http://my.safaribooksonline.com/book/animation-and-3d/9780133440133
//

// Simple_VertexShader
//
//    This is a simple example that draws a rotating cube in perspective
//    using a vertex shader to transform the object
//

package com.demo.openglbooksexample.eight;


import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.demo.openglbooksexample.common.ESShader;
import com.demo.openglbooksexample.common.ESShapes;
import com.demo.openglbooksexample.common.ESTransform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleVertexShaderRenderer implements GLSurfaceView.Renderer {
    private final static String TAG = "Render";
    // Handle to a program object
    private int mProgramObject;

    // Uniform locations
    private int mMVPLoc;

    // Vertex data
    private ESShapes mCube = new ESShapes();

    // Rotation angle
    private float mAngle;

    // MVP matrix
    private ESTransform mMVPMatrix = new ESTransform();

    // Additional Member variables
    private int mWidth;
    private int mHeight;
    private long mLastTime = 0;

    private float[] resultFs = new float[16];

    public SimpleVertexShaderRenderer(Context context) { }

    // Initialize the shader and program object
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        String vShaderStr =
                "#version 300 es 							 \n" +
                        "uniform mat4 u_mvpMatrix;                   \n" +
                        "layout(location = 0) in vec4 a_position;    \n" +
                        "layout(location = 1) in vec4 a_color;       \n" +
                        "out vec4 v_color;                           \n" +
                        "void main()                                 \n" +
                        "{                                           \n" +
                        "   v_color = a_color;                       \n" +
                        "   gl_Position = u_mvpMatrix * a_position;  \n" +
                        "}                                           \n";

        String fShaderStr =
                "#version 300 es 							 \n" +
                        "precision mediump float;                    \n" +
                        "in vec4 v_color;                            \n" +
                        "layout(location = 0) out vec4 outColor;     \n" +
                        "void main()                                 \n" +
                        "{                                           \n" +
                        "  outColor = v_color;                       \n" +
                        "}                                           \n";

        // Load the shaders and get a linked program object
        mProgramObject = ESShader.loadProgram(vShaderStr, fShaderStr);

        // Get the uniform locations
        mMVPLoc = GLES30.glGetUniformLocation(mProgramObject, "u_mvpMatrix");
        Log.e(TAG,"======统一变量====>"+mMVPLoc);
        // Generate the vertex data
        mCube.genCube(1.0f);

        // Starting rotation angle for the cube
        mAngle = 45.0f;

        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    }

    // Draw a triangle using the shader pair created in onSurfaceCreated()
    public void onDrawFrame(GL10 glUnused) {
        update();

        // Set the viewport
        GLES30.glViewport(0, 0, mWidth, mHeight);

        // Clear the color buffer
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES30.glUseProgram(mProgramObject);

        // Load the vertex data
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false,
                0, mCube.getVertices());
        GLES30.glEnableVertexAttribArray(0);

        // Set the vertex color to red
        GLES30.glVertexAttrib4f(1, 1.0f, 0.0f, 0.0f, 1.0f);

        // Load the MVP matrix  使用变换的矩阵。 给统一变量赋值。
//        GLES30.glUniformMatrix4fv(mMVPLoc, 1, false,
//                mMVPMatrix.getAsFloatBuffer());

        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(resultFs.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(resultFs);
        floatBuffer.position(0);

        GLES30.glUniformMatrix4fv(mMVPLoc,1,false,
               floatBuffer);

        // Draw the cube
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, mCube.getNumIndices(),
                GLES30.GL_UNSIGNED_SHORT, mCube.getIndices());
    }

    private void update() {
        if (mLastTime == 0) {
            mLastTime = SystemClock.uptimeMillis();
        }

        long curTime = SystemClock.uptimeMillis();
        //获得刷新的时间差  单位秒
        float deltaTime = (curTime - mLastTime) / 1000.0f;
        mLastTime = curTime;
        //视图矩阵
        ESTransform perspective = new ESTransform();
        //模型矩阵
        ESTransform modelview = new ESTransform();
        float aspect;

        // Compute a rotation angle based on time to rotate the cube
        //根据时间计算旋转的角度，也就是每秒旋转40度
        mAngle += (deltaTime * 40f);

        if (mAngle >= 360.0f) {
            mAngle -= 360.0f;
        }

        // 计算绘制区域的宽高比
        aspect = (float) mWidth / (float) mHeight;

        // Generate a perspective matrix with a 60 degree FOV and near and far  clip planes at 1.0 and 2.0
        //  视图的距离是  far 是从  1.0f  -  20.0f
        //生成一个4 * 4  的矩阵，  起始这就是 vec4  表示4个顶点
        //视图矩阵
        perspective.matrixLoadIdentity();
        //最近是1.0f   最远才是20.0f
        perspective.perspective(60.0f, aspect, 1.0f, 20.0f);

        float[] perspectiveFs = new float[16];
        //初始化矩阵，这个就是把 对角线的元素置为1.0f ， 其他置为0.0f
        Matrix.setIdentityM(perspectiveFs,0);

        Matrix.perspectiveM(perspectiveFs,0,60.0f,aspect,1.0f,20.0f);

//        Matrix.frustumM(perspectiveFs,0,);

        float[] modelFs = new float[16];

        // Generate a model view matrix to rotate/translate the cube
        //生成一个模型视图矩阵来旋转/平移立方体
        modelview.matrixLoadIdentity();

        Matrix.setIdentityM(modelFs,0);
        // Translate away from the viewer
        //模型矩阵的移动
        modelview.translate(0.0f, 0.0f, -2.0f);

        Matrix.translateM(modelFs,0,0,0,-2.0f);
        // Rotate the cube
        //模型矩阵的旋转
        modelview.rotate(mAngle, 1.0f, 0.0f, 1.0f);
        Matrix.setRotateM(modelFs,0,mAngle,1.0f,0.0f,1.0f);
        // Compute the final MVP by multiplying the
        // modevleiw and perspective matrices together
        //矩阵相乘    也就是  模型矩阵  和  视图矩阵相乘  ===  投影矩阵
        // 模型矩阵： 这个就是建立模型的矩阵，世界坐标，起始就是建里模型的坐标
        // 视图矩阵：
        //  投影矩阵：起始就是在屏幕上显示的 矩阵
//        mMVPMatrix.matrixMultiply(modelview.get(), perspective.get());
//        mMVPMatrix.matrixMultiply(modelFs, perspectiveFs);

        Matrix.multiplyMV(resultFs,0,modelFs,0,perspectiveFs,0);

        /**
        1. Object or model coordinates  模型坐标或者物体坐标，坐标系物体某点建立的世界坐标系。

         2. World coordinates   世界坐标

         3. Eye (or Camera) coordinates  视图坐标，或者是相机坐标，以视点为原点，
         openGL会把世界坐标变换程视图坐标，这样才可以进行裁剪

         4. Clip coordinates   裁剪坐标

         5. Normalized device coordinates：标准化坐标

         6. Window (or screen) coordinates  窗口或是屏幕坐标，设备坐标


         一般变换是从：模型坐标 ====> 世界坐标 ====> 视图坐标

         常用四个坐标：世界坐标，物体坐标，设备坐标  视图坐标
         */



    }


    ///
    // Handle surface changes
    //
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        mWidth = width;
        mHeight = height;
    }


}
