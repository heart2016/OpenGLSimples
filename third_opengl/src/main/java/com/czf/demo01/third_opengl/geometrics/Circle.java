package com.czf.demo01.third_opengl.geometrics;

/**
 * Created by Administrator on 2018/7/15.
 */
//圆形
public class Circle {
    public final Point center;
    public final float radius;

    public Circle(Point center,float radius){
        this.center = center;
        this.radius = radius;
    }

    //缩放圆。
    public Circle scale(float scale){
        return new Circle(center,radius*scale);
    }
}
