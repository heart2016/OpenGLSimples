package com.czf.demo01.third_opengl.geometrics;

/**
 * Created by Administrator on 2018/7/15.
 */
//自定义点坐标来定义三维的坐标。
public class Point {

    public final float x, y, z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //移动位置
    public Point translateY(float distance) {
        return new Point(x, y + distance, z);
    }
}
