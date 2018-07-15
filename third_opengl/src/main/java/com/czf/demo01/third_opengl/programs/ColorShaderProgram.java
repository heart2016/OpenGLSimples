package com.czf.demo01.third_opengl.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.czf.demo01.third_opengl.R;

/**
 * Created by Administrator on 2018/7/15.
 * 颜色着色器的类
 *
 * 着色器的类要注意的就是  顶点的变量  特别是  需要赋值的变量就是  uniform以及attribute变量。varying是不需要赋值的
 * 因为varying是演变的
 */

public class ColorShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;

    private final int aPositionLocation;
    private final int aColorLocation;
    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shaper);

        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);

        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);

        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
    }

    public void setUniform(float[] matrix){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getColorLocation() {
        return aColorLocation;
    }
}
