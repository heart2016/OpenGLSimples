package com.czf.demo01.third_opengl.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.czf.demo01.third_opengl.Constants;
import com.czf.demo01.third_opengl.R;

/**
 * Created by Administrator on 2018/7/15.
 * 纹理类
 */

public class TextureShaderProgram extends ShaderProgram {
    //纹理定义的 uniform变量
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //纹理定义的Attribute变量
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader,
                R.raw.texture_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program,U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program,A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix,int textureId){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

        GLES20.glActiveTexture(textureId);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glUniform1i(uTextureUnitLocation,0);
    }

    public int getPositionLocation(){
        return aPositionLocation;
    }

    public int getTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
}
