package com.czf.second_opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;

public class OpenGlUtil {
    private final static String TAG = "OpenGlUtil";

    //判断手机是不是支持OpenGL2.0。
    public static boolean isSupportOpenGL2(Context context){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        int supportOpenGlL = configurationInfo.reqGlEsVersion;
        return supportOpenGlL >= 0x20000
                || ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH  && isEmulator());

    }

    //执行顶点着色器代码。
    public static int compileVertexShader(String source){
        return compileShader(GLES20.GL_VERTEX_SHADER,source);
    }

    //执行片段着色器代码
    public static int compileFragmentShader(String source){
        return compileShader(GLES20.GL_FRAGMENT_SHADER,source);
    }

    //编译 glsl 里面的源代码  编译的代码会生成一个内存地址。
    // 返回的地址就会只是一个地址ID
    private static int compileShader(int type,String source){
        //创建一个着色器对象，并且返回对象的id。相当于地址，
        //后面想用这个对象，只需要把id传回OpenGL就可以啦，返回0表示创建失败
        final int shaderObjectId = GLES20.glCreateShader(type);
        //表示没有创建，也就是创建失败
        if (shaderObjectId == 0){
            Log.e(TAG,"error 创建着色器对象失败");
            return 0;
        }
        //一旦创建了成功着色器对象，就可以把对象和源代码关联起来
        GLES20.glShaderSource(shaderObjectId,source);

        //编译关联起来的源代码，其实就是会执行里面的main函数
        GLES20.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        //获得gl执行源代码是否成功的状态吗，这个状态码会写入整形数组compileStatus中
        GLES20.glGetShaderiv(shaderObjectId,GLES20.GL_COMPILE_STATUS,compileStatus,0);

        Log.d(TAG,"执行代码："+ source +" ,状态码====》"+compileStatus[0]);
        //如果是0   就表示编译失败，编译失败就把着色器对象删除了
        if (compileStatus[0] == 0){
            GLES20.glDeleteShader(shaderObjectId);
            return 0;
        }
        //如果编译成功就返回着色对象的id。
        return shaderObjectId;
    }

    //编译好了就是链接了，这个方法就是链接源代码，把定点着色器和片段着色器链接起来
    public static int linkShaderSource(int vertexObjectId,int fragmentObjectId){
        //创建链接源代码的对象
        final int linkedObjectId = GLES20.glCreateProgram();
        if (linkedObjectId == 0){
            Log.e(TAG,"创建链接对象失败===》"+linkedObjectId);
            return 0;
        }
        //把顶点着色器  放入预备链接中
        GLES20.glAttachShader(linkedObjectId,vertexObjectId);
        //把片段着色器 放入预备链接中
        GLES20.glAttachShader(linkedObjectId,fragmentObjectId);
        //执行链接命令
        GLES20.glLinkProgram(linkedObjectId);

        final int[] linkedStatus = new int[1];
        GLES20.glGetProgramiv(linkedObjectId,GLES20.GL_LINK_STATUS,linkedStatus,0);
        if (linkedStatus[0] == 0){
            Log.e(TAG,"链接失败");
            GLES20.glDeleteProgram(linkedObjectId);
            return 0;
        }
        return linkedObjectId;
    }

    public static boolean validateLinked(int linkedObjectId){
        GLES20.glValidateProgram(linkedObjectId);

        final int[] validateStatus = new int[1];

        GLES20.glGetProgramiv(linkedObjectId,GLES20.GL_VALIDATE_STATUS,validateStatus,0);
        Log.e(TAG,"验证下执行=====validateLinked   "+validateStatus[0]);
        return validateStatus[0] != 0;
    }


    //下面是三位计算工具类：

    /**
     * 定义一个矩阵的计算。并且对矩阵赋值
     *这个矩阵是：
     * {
     *     a/radio  0       0               0
     *     0        a       0               0
     *     0        0       -(f+n)/(f-n);   -((2*f*f*n)/(f-n));
     *     0        0       -1              0
     *
     * }
     * @param matrixFloat  要进行的矩阵  4 x 4 的矩阵数据
     * @param y  y轴方向上的  角度
     * @param radio  宽高比
     * @param n 近距离点  距离视野的距离
     * @param f  远距离点  距离视野的距离
     */
    public static void perspectiveMatrix(float[] matrixFloat, float y,float radio, float n, float f){
        //计算焦距  基于y轴的焦距
        //求取弧度值
        final float angleRadio = (float) (y * Math.PI /180.0f);
        //计算角度一般的正切值
        final  float a = (float) (1.0 /Math.tan(angleRadio/2.0));

        // 矩阵的   第-列
        matrixFloat[0] = a/radio;
        matrixFloat[1] = 0f;
        matrixFloat[2] = 0f;
        matrixFloat[3] = 0f;

        //矩阵的  第二列
        matrixFloat[4] = 0f;
        matrixFloat[5] = a;
        matrixFloat[6] = 0f;
        matrixFloat[7] = 0f;

        //矩阵的  第三列
        matrixFloat[8] = 0f;
        matrixFloat[9] = 0f;
        matrixFloat[10] = -((f+n)/(f-n));
        matrixFloat[11] = -1f;

        //矩阵的  第四列
        matrixFloat[12] = 0f;
        matrixFloat[13] = 0f;
        matrixFloat[14] =  -((2f*f*n)/(f-n));
        matrixFloat[15] = 0f;

    }


    //把一个资源文件，图片载入到OpenGL中做成纹理，并且返回纹理的ID
    public static int readTexture(Context context, int resource){
        final int[] textureIDs = new int[1];
        //生成一个纹理对象的ID
        GLES20.glGenTextures(1,textureIDs,0);
        if (textureIDs[0] == 0){
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        //表明想要图像的原始大小而不是缩放，也就是图像的真实尺寸
        options.inScaled = false;

        final Bitmap bitmap =
                BitmapFactory.decodeResource(context.getResources(),resource,options);
        if (bitmap == null){
            Log.e(TAG,"图片解码失败=====》");
            GLES20.glDeleteTextures(1,textureIDs,0);
            return 0;
        }

        //绑定 Bitmap和纹理id。 做成一个2D 对待纹理。
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureIDs[0]);

        /**纹理会发生的情况：缩小，放大
         * 当我们尽力把几个纹理元素挤进一个片段时，缩小就发生了，当我们把一个纹理元素扩展到许多片段时
         * 放大就发生了
         *
         *  纹理过滤有很多方式：
         *  第一种方式：最近邻过滤 为每一个片段选择最近的纹理元素，当我们放大纹理时，他的锯齿就会看起来效果很明显
         *  但是缩小是，因为没有足够的片段来绘制所有纹理，细节将会丢失
         *
         *  第二种方式：双线性过滤  使用双线性插值平滑像素之间的过渡，而不是为每个片段使用最近的纹理元素
         *  OpenGL会使用四个相邻的纹理元素，在这四个直接使用线性插值算法做插值，这样放大锯齿会减小很多
         *  平滑很多。当时当这个放大缩小倍率过大  也是会出不是那么平滑的
         *
         *  MIP贴图：可以提高质量，比双线性过滤好些，这个还是双线性插值，只是加入了MIP优化
         *
         *  三线性过滤：就是在两个MIP之间插值，这样就是8个纹理元素插值
         *
         */

        //指定做小的情况   缩小选用GL_LINEAR_MIPMAP_LINEAR 三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //放大的时候的情况   放大选用 双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //所以最后一个参数是选用的过滤方式：

      /*  GLES20.GL_NEAREST;   //最近邻过滤
        GLES20.GL_NEAREST_MIPMAP_NEAREST;  //使用MIP贴图的最近邻过滤
        GLES20.GL_NEAREST_MIPMAP_LINEAR;    //使用MIP贴图级别之间插值的最近邻过滤
        GLES20.GL_LINEAR;   //双性过滤
        GLES20.GL_LINEAR_MIPMAP_NEAREST; //使用MIP贴图的双线性过滤
        GLES20.GL_LINEAR_MIPMAP_LINEAR; // 三线性过滤（使用MIP贴图级别之间插值的双线性过滤）
*/
        //加载位图到OpenGL中，这个时候就会把bitmap复制到OpenGL中原来的bitmap就不要了，可以回收了
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        //生成MIP贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        //解除与纹理的绑定,这样就可以避免目前生成的纹理被意外的改变
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return textureIDs[0];
    }


    public static  boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
