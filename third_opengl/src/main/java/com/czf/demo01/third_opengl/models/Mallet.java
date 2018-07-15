package com.czf.demo01.third_opengl.models;

import android.opengl.GLES20;

import com.czf.demo01.third_opengl.Constants;
import com.czf.demo01.third_opengl.geometrics.ObjectBuilder;
import com.czf.demo01.third_opengl.geometrics.Point;
import com.czf.demo01.third_opengl.programs.ColorShaderProgram;
import com.czf.demo01.third_opengl.programs.ShaderProgram;

import java.util.List;

/**
 * Created by Administrator on 2018/7/15.
 * 这个就是棒槌的实体类
 */

public class Mallet {
//    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)* Constants.BYTE_PER_FLOAT;

    //存放两个棒槌的初始坐标
    private static final float[] VERTEX_DATA = {
            0f,     -0.4f,      0f, 0f, 1f,
            0f,      0.4f,      1f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public final float radius;
    public final float height;
    private final List<ObjectBuilder.DrawCommand> drawList;

//    public Mallet(){
//        vertexArray = new VertexArray(VERTEX_DATA);
//    }

    public Mallet(float radius,float height,int numPointAroundMallet){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Point(0f,0f,0f),
                radius,
                height,
                numPointAroundMallet
        );

        this.radius = radius;
        this.height = height;
        this.drawList = generatedData.drawList;
        this.vertexArray = new VertexArray(generatedData.vertexData);
    }


    public void bindData(ColorShaderProgram shaderProgram) {
        vertexArray.setVertexAttributePoint(
                0,
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
//                STRIDE
                0
        );

       /* vertexArray.setVertexAttributePoint(
                POSITION_COMPONENT_COUNT,
                shaderProgram.getColorLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );*/
    }

    public void draw() {
//        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2);

        for (ObjectBuilder.DrawCommand drawCommand: drawList){
            drawCommand.draw();
        }
    }

}
