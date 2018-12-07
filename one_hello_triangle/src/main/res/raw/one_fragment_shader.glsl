#version 300 es
//这个是片段着色器
//precision medium float:设定浮点变量的默认精度。
precision mediump float;
//定义片段着色器的输出变量。
out vec4 fragColor;
struct flogStruct{
    vec2 mVec2;
    float start;
    float end;
};
void main(){
  flogStruct mFlogStruct = flogStruct(vec2(1.0,2.0),3.0,4.0);
            //颜色值 R, G,  B, A
  fragColor = vec4(1.0,0.0,0.0,1.0);
}