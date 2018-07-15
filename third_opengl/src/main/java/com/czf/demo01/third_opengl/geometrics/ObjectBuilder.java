package com.czf.demo01.third_opengl.geometrics;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/15.
 */

public class ObjectBuilder {
    public static class GeneratedData {
        //点的数据
        public final float[] vertexData;
        //绘制的命令
        public final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    //绘制命令接口
    public static interface DrawCommand {
        void draw();
    }


    //表示一个点  有三个float数据组成
    private static final int FLOATS_PER_VERTEX = 3;
    //存放图形点坐标的数组。
    private final float[] vertexData;
    //绘制命令的集合
    private final List<DrawCommand> drawList = new ArrayList<>();
    private int offsets = 0;

    //这个图形需要的点数。
    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    //计算圆的顶点数  参数是：有多少个点
    private static int sizeofCircleInVertices(int numPoint) {
        //  圆心  + 圆边线上的点（这个会有一个点分成两个，这是因为封闭起来的时候是两个合一个，但是计算必须分开）
        return 1 + numPoint + 1;
    }

    //计算圆桶的点数。  不需要计算圆心，因为圆柱展开式矩形啦   每条上面的边就是numPoint+1。上下就是乘以2
    private static int sizeofCylinderInVertices(int numPoint) {
        return 2 * (numPoint + 1);
    }

    //创建冰球。使用圆柱体创建冰球。
    public static GeneratedData createPuck(Cylinder cylinder, int numPoint) {
        //计算圆柱的点数，因为底部的圆面不可见，所以可以不算
        int size = sizeofCircleInVertices(numPoint) + sizeofCylinderInVertices(numPoint);

        ObjectBuilder builder = new ObjectBuilder(size);

        Circle puckTop = new Circle(
                //圆心会在Y的方向上  移动到高度的一半，这是为了圆面实在圆柱的高度中点上
                cylinder.center.translateY(cylinder.height / 2f),
                cylinder.radius
        );

        builder.appendCircle(puckTop, numPoint);

        builder.appendOpenCylinder(cylinder, numPoint);

        return builder.build();
    }

    public static GeneratedData createMallet(Point center, float radius, float height, int numPoint) {
        int size = sizeofCircleInVertices(numPoint) * 2 +
                sizeofCylinderInVertices(numPoint) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        //计算底盘的高度
        float baseHeight = height * 0.25f;

        //地盘的圆
        Circle baseCircle = new Circle(
                center.translateY(-baseHeight),
                radius
        );
        //地盘的圆筒
        Cylinder baseCylinder = new Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f),
                radius,
                baseHeight
        );

        builder.appendCircle(baseCircle, numPoint);
        builder.appendOpenCylinder(baseCylinder, numPoint);

        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(
                center.translateY(height * 0.5f),
                handleRadius
        );
        Cylinder handleCylinder = new Cylinder(
                handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius,
                handleHeight
        );

        builder.appendCircle(handleCircle, numPoint);
        builder.appendOpenCylinder(handleCylinder, numPoint);

        return builder.build();
    }


    public ObjectBuilder appendCircle(Circle circle, int numPoint) {
        //圆形绘制的其实位置点
        final int startVertex = offsets / FLOATS_PER_VERTEX;
        //计算出圆的点数
        final int numVertices = sizeofCircleInVertices(numPoint);

        //把圆心的点坐标加入  vertexData中
        vertexData[offsets++] = circle.center.x;
        vertexData[offsets++] = circle.center.y;
        vertexData[offsets++] = circle.center.z;

        //注意这里是<=  说明就是  numPoint + 1个啦
        for (int i = 0; i <= numPoint; i++) {
            float angle = (float) (Math.PI * 2.0f * i / numPoint);
            vertexData[offsets++] = (float) (circle.center.x + circle.radius * Math.cos(angle));
            vertexData[offsets++] = circle.center.y;
            vertexData[offsets++] = (float) (circle.center.z + circle.radius * Math.sin(angle));
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
        return this;
    }

    public ObjectBuilder appendOpenCylinder(Cylinder cylinder, int numPoint) {
        //圆柱的起始位置
        final int start = offsets / FLOATS_PER_VERTEX;
        //绘制的点数
        final int num = sizeofCylinderInVertices(numPoint);

        //下面点的Y值
        final float yStart = cylinder.center.y - cylinder.height / 2;
        //上面点的Y值
        final float yEnd = cylinder.center.y + cylinder.height / 2;
        for (int i = 0; i <= numPoint; i++) {
            float angle = (float) (Math.PI * 2.0f * i / numPoint);
            float xPosition = (float) (cylinder.center.x + cylinder.radius * Math.cos(angle));

            float zPosition = (float) (cylinder.center.z + cylinder.radius * Math.sin(angle));

            vertexData[offsets++] = xPosition;
            vertexData[offsets++] = yStart;
            vertexData[offsets++] = zPosition;

            vertexData[offsets++] = xPosition;
            vertexData[offsets++] = yEnd;
            vertexData[offsets++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, start, num);
            }
        });
        return this;
    }

    public GeneratedData build() {
        return new GeneratedData(this.vertexData, this.drawList);
    }

}
