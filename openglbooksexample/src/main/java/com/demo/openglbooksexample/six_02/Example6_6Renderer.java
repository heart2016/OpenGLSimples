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

// Example_6_6
//
//    This example demonstrates drawing a primitive with
//    a separate VBO per attribute
//
//

package com.demo.openglbooksexample.six_02;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.demo.openglbooksexample.common.ESShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Example6_6Renderer implements GLSurfaceView.Renderer {
   // Handle to a program object
   private int mProgramObject;

   // Additional member variables
   private int mWidth;
   private int mHeight;
   private FloatBuffer mVertices;
   private FloatBuffer mColors;
   private ShortBuffer mIndices;

   // VertexBufferObject Ids
   private int [] mVBOIds = new int[3];

   // 3 vertices, with (x,y,z) ,(r, g, b, a) per-vertex
   private final float[] mVerticesData =
           {
                   0.0f,  0.5f, 0.0f, // v0
                   -0.5f, -0.5f, 0.0f, // v1
                   0.5f, -0.5f, 0.0f  // v2
           };

   private final short[] mIndicesData =
           {
                   0, 1, 2
           };

   private final float [] mColorData =
           {
                   1.0f, 0.0f, 0.0f, 1.0f,   // c0
                   0.0f, 1.0f, 0.0f, 1.0f,   // c1
                   0.0f, 0.0f, 1.0f, 1.0f    // c2
           };

   final int VERTEX_POS_SIZE   = 3; // x, y and z
   final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a

   final int VERTEX_POS_INDX   = 0;
   final int VERTEX_COLOR_INDX = 1;

   private int[] vtxStrides =
           {
                   VERTEX_POS_SIZE * 4, //表示一个顶点的字节数

                   VERTEX_COLOR_SIZE * 4  //一个颜色的字节数
           };
   ///
   // Constructor
   //
   public Example6_6Renderer ( Context context ) {
      mVertices = ByteBuffer.allocateDirect ( mVerticesData.length * 4 )
                  .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mVertices.put ( mVerticesData ).position ( 0 );

      mColors = ByteBuffer.allocateDirect ( mColorData.length * 4 )
                .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mColors.put ( mColorData ).position ( 0 );

      mIndices = ByteBuffer.allocateDirect ( mIndicesData.length * 2 )
                 .order ( ByteOrder.nativeOrder() ).asShortBuffer();
      mIndices.put ( mIndicesData ).position ( 0 );
   }

   ///
   // Initialize the shader and program object
   //
   public void onSurfaceCreated ( GL10 glUnused, EGLConfig config ) {
      String vShaderStr =
         "#version 300 es                            \n" +
         "layout(location = 0) in vec4 a_position;   \n" +
         "layout(location = 1) in vec4 a_color;      \n" +
         "out vec4 v_color;                          \n" +
         "void main()                                \n" +
         "{                                          \n" +
         "    v_color = a_color;                     \n" +
         "    gl_Position = a_position;              \n" +
         "}";


      String fShaderStr =
         "#version 300 es            \n" +
         "precision mediump float;   \n" +
         "in vec4 v_color;           \n" +
         "out vec4 o_fragColor;      \n" +
         "void main()                \n" +
         "{                          \n" +
         "    o_fragColor = v_color; \n" +
         "}" ;

      // Load the shaders and get a linked program object
      mProgramObject = ESShader.loadProgram ( vShaderStr, fShaderStr );

      mVBOIds[0] = 0;
      mVBOIds[1] = 0;
      mVBOIds[2] = 0;

      GLES30.glClearColor ( 1.0f, 1.0f, 1.0f, 0.0f );
   }

   // /
   // Draw a triangle using the shader pair created in onSurfaceCreated()
   //
   public void onDrawFrame ( GL10 glUnused ) {
      // Set the viewport
      GLES30.glViewport ( 0, 0, mWidth, mHeight );

      // Clear the color buffer
      GLES30.glClear ( GLES30.GL_COLOR_BUFFER_BIT );

      // Use the program object
      GLES30.glUseProgram ( mProgramObject );

      drawPrimitiveWithVBOs();
   }

   private void drawPrimitiveWithVBOs() {
      int numVertices = 3;
      int numIndices = 3;

      // mVBOIds[0] - used to store vertex position  用于缓存顶点缓存
      // mVBOIds[1] - used to store vertex color  用于颜色缓存
      // mVBOIds[2] - used to store element indices  用于
      if ( mVBOIds[0] == 0 && mVBOIds[1] == 0 && mVBOIds[2] == 0 ) {
         // Only allocate on the first draw
         GLES30.glGenBuffers ( 3, mVBOIds, 0 );

         //缓存顶点
         mVertices.position ( 0 );
         GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[0] );
         GLES30.glBufferData ( GLES30.GL_ARRAY_BUFFER, vtxStrides[0] * numVertices,
                               mVertices, GLES30.GL_STATIC_DRAW );

         //缓存颜色
         mColors.position ( 0 );
         GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[1] );
         GLES30.glBufferData ( GLES30.GL_ARRAY_BUFFER, vtxStrides[1] * numVertices,
                               mColors, GLES30.GL_STATIC_DRAW );

         //缓存索引
         mIndices.position ( 0 );
         GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[2] );
         GLES30.glBufferData ( GLES30.GL_ELEMENT_ARRAY_BUFFER, 2 * numIndices,
                               mIndices, GLES30.GL_STATIC_DRAW );
      }

      //再启用缓存之前，还必须使用这个绑定，这个缓存表示的是顶点缓存
      GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[0] );
      //启用着色器中的索引功能
      GLES30.glEnableVertexAttribArray ( VERTEX_POS_INDX );

      //给指定的index赋值。这个就是把缓存赋值给index；
      // 使用了多少缓存：GLES30.glBindBuffer()  这个就是表示使用那个缓存
      GLES30.glVertexAttribPointer ( VERTEX_POS_INDX, VERTEX_POS_SIZE,
                                     GLES30.GL_FLOAT, false, vtxStrides[0], 0 );

      //表示启动的缓存是颜色缓存
      GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[1] );
      //启用着色器中的索引功能
      GLES30.glEnableVertexAttribArray ( VERTEX_COLOR_INDX );
      //给指定的index赋值。这个就是把缓存赋值给index
      GLES30.glVertexAttribPointer ( VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                                     GLES30.GL_FLOAT, false, vtxStrides[1], 0 );

      //表示的缓存是索引缓存
      GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[2] );

      GLES30.glDrawElements ( GLES30.GL_TRIANGLES, numIndices,
                              GLES30.GL_UNSIGNED_SHORT, 0 );

      GLES30.glDisableVertexAttribArray ( VERTEX_POS_INDX );
      GLES30.glDisableVertexAttribArray ( VERTEX_COLOR_INDX );

      GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, 0 );
      GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, 0 );
   }

   ///
   // Handle surface changes
   //
   public void onSurfaceChanged ( GL10 glUnused, int width, int height )
   {
      mWidth = width;
      mHeight = height;
   }


}
