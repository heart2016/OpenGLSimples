#version 300 es
//这个是片段着色器
//precision medium float:设定浮点变量的默认精度。
precision mediump float;
//定义片段着色器的输出变量。
out vec4 fragColor;
void main(){
            //颜色值 R, G,  B, A
  fragColor = vec4(1.0,0.0,0.0,1.0);
}