//指定openGL 的版本号。
#version 300 es
//统一变量，一个矩阵数组。
uniform mat4 u_mvpMatrix;

//输入顶点属性
in vec4 a_position;
//输入顶点颜色属性
in vec4 a_color;

//输出变量颜色。
out vec4 v_color;

void main(){
    //把输入的参数赋值给输出的参数
    v_color = a_color;
    //
    gl_Position = u_mvpMatrix * a_position;
}
