/**
这个是定点着色器
*/
//定义成vec4类型的着色器，这就是定点着色器
//一个 vec4分量会包含 4个分量，可以认识是：（x，y,z,w）  x,y,z 代表着三维坐标，w是一个特殊的坐标
//默认情况下：opengl  会把x，y ，z 设置为0， w为1
//关键字：attribute： 一个定点有属性：颜色，位置，这个关键字就是便是吧这些属性放入着色器中
attribute vec4 a_Position;
//attribute vec4 a_Color;

//增加工作顶点的颜色  在三角形中使用varying变量，可以有效的让三角形颜色变化
//varying vec4 v_Color;
//定义一个4 x 4 的矩阵
uniform mat4 u_Matrix;

void main(){

//    gl_Position  =   a_Position;

//点的位置变换  变化成矩阵变换
    gl_Position  = u_Matrix * a_Position;

  /*  //指定点的大小
    gl_PointSize = 10.0;

    //增加顶点的颜色向量
    v_Color = a_Color;*/


}