package com.czf.demo01.third_opengl.models;

import android.opengl.GLES20;

import com.czf.demo01.third_opengl.Constants;
import com.czf.demo01.third_opengl.programs.TextureShaderProgram;

/**
 * Created by Administrator on 2018/7/15.
 */

//桌子实体类
public class Table {
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    public static final int STRIDE =
            (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTE_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
//            X         Y           S           T   记住S T  是纹理坐标  纹理绘制 0.1 -0.9  表示只会绘制中间部分
             0f,        0f,        0.5f,       0.5f,
            -0.5f,     -0.8f,      0f,         0.9f,
             0.5f,     -0.8f,      1f,         0.9f,
             0.5f,      0.8f,      1f,         0.1f,
            -0.5f,      0.8f,      0f,         0.1f,
            -0.5f,     -0.8f,      0f,         0.9f,
    };

    private final VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram shaderProgram) {
        //从着色器获取每个属性的位置
        vertexArray.setVertexAttributePoint(
                0,
                //把位置数据绑定到引用的着色器
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttributePoint(
                POSITION_COMPONENT_COUNT,
                //把纹理坐标数据绑定到被引用的着色器上
                shaderProgram.getTextureCoordinatesLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    //表示绘制当前这张桌子
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
