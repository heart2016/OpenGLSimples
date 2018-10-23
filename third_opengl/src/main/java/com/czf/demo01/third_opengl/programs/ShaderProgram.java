package com.czf.demo01.third_opengl.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.czf.demo01.third_opengl.OpenGlUtil;
import com.czf.demo01.third_opengl.ReadOpenGLFileUtil;


/**
 * Created by Administrator on 2018/7/15.
 * 基础纹理类
 */

public class ShaderProgram {
    // 这里定义的都是  uniform的变量名
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    //这定义的都是  attribute的变量
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // 获得编译链接了得  可执行的对象的id  program：这个就是代码地址指令
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId) {

        String vShader = ReadOpenGLFileUtil.readStringFromRawResource(context,vertexShaderResourceId);

        String fShader = ReadOpenGLFileUtil.readStringFromRawResource(context,fragmentShaderResourceId);

        int idVertex = OpenGlUtil.compileVertexShader(vShader);
        int idFragment = OpenGlUtil.compileFragmentShader(fShader);
        program = OpenGlUtil.linkShaderSource(idVertex,idFragment);
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }

}
