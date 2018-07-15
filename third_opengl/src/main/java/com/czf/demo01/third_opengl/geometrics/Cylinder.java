package com.czf.demo01.third_opengl.geometrics;

/**
 * Created by Administrator on 2018/7/15.
 */

//几何图形圆柱
public class Cylinder {
    public final Point center;
    public final float radius;
    public final float height;

    public Cylinder(Point center,float radius,float height){
        this.center = center;
        this.radius = radius;
        this.height = height;
    }
}
