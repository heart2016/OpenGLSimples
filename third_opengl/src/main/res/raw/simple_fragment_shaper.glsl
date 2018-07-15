/**
这个是片段着色器
*/
//定义所有小数的精确度，有值：lowp mediump highp  分别代表低精度，中等精度，高精度
precision mediump float;

//一个uniform 会让每个顶点都使用同一个值，除非我们再次改变它。
//uniform vec4 u_Color;

//varying vec4 v_Color;
uniform mat4 u_Color;

//这里就要知道varying和uniform 区别。  un
// uniform不会变，是一条直线的颜色定义
//varying 是会变得， 也就是可以颜色三角变化的。其实就是渐变颜色。
//所以我们可以使用使用线性插值器混合颜色。  书上有颜色插值器混合的计算方式。

//函数里面：gl_FragColor  这个就是把颜色复制给特殊变量gl_FragColor
//着色器一定要给gl_FragColor赋值
void main(){
//gl_FragColor
//    gl_FragColor = v_Color;
    gl_FragColor = u_Color;

}