#version 300 es
// vec4 表示一个四分量的向量。in 表示输入参数
//layout(location = 0) 限定符，表示这个变量的位置是顶点属性0，
in vec4 vPosition;
//opengl中着色器的执行开始，主题代码。
void main(){
//gl_Position   这个是一个特殊的变量，记者就行，每个顶点着色器必须在gl_Position变量中
// 输出一个位置。gl_Position:这个变量会传递到下一个流程项。
    gl_Position = vPosition;
}