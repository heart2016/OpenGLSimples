package com.czf.demo01.third_opengl.models;

import android.opengl.GLES20;

import com.czf.demo01.third_opengl.Constants;
import com.czf.demo01.third_opengl.programs.ColorShaderProgram;
import com.czf.demo01.third_opengl.programs.ShaderProgram;

/**
 * Created by Administrator on 2018/7/15.
 * 这个就是棒槌的实体类
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)* Constants.BYTE_PER_FLOAT;

    //存放两个棒槌的初始坐标
    private static final float[] VERTEX_DATA = {
            0f,     -0.4f,      0f, 0f, 1f,
            0f,      0.4f,      1f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public Mallet(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }


    public void bindData(ColorShaderProgram shaderProgram) {
        vertexArray.setVertexAttributePoint(
                0,
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttributePoint(
                POSITION_COMPONENT_COUNT,
                shaderProgram.getColorLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2);
    }

}
