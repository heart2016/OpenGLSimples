package com.czf.demo01.third_opengl.models;

import android.opengl.GLES20;

import com.czf.demo01.third_opengl.geometrics.Cylinder;
import com.czf.demo01.third_opengl.geometrics.ObjectBuilder;
import com.czf.demo01.third_opengl.geometrics.Point;
import com.czf.demo01.third_opengl.models.VertexArray;
import com.czf.demo01.third_opengl.programs.ColorShaderProgram;

import java.util.List;

/**
 * Created by Administrator on 2018/7/15.
 */
//冰球
public class Puck {
    private final static int POSITION_COMPONENT_COUNT = 3;
    public final float radius,height;

    private final VertexArray vertexArray;

    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius,float height,int numPointAroundPuch){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(
                new Cylinder(new Point(0f,0f,0f),radius,height),
                numPointAroundPuch
        );
        this.radius = radius;
        this.height = height;
        this.vertexArray = new VertexArray(generatedData.vertexData);
        this.drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram shaderProgram){
        vertexArray.setVertexAttributePoint(
                0,
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                0
        );

    }

    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand: drawList){
            drawCommand.draw();
        }
    }
}
