package com.czf.demo01.third_opengl.models;

import android.opengl.GLES20;

import com.czf.demo01.third_opengl.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Administrator on 2018/7/15.
 */
//数据点的封装类
public class VertexArray {
    private final FloatBuffer floatBuffer ;

    public VertexArray(float[] data){
        floatBuffer =  ByteBuffer.allocateDirect(data.length * Constants.BYTE_PER_FLOAT)
                //一定要加这个，这个为了保证存储数据的顺序不会由于平台不同发生乱序
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(data);
    }

    public void setVertexAttributePoint(int dataOffset,int attributeLocation,int component,int stride){
        floatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(attributeLocation,component,GLES20.GL_FLOAT,false,stride,floatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);

        //回到0位置
        floatBuffer.position(0);
    }
}
