package com.czf.first_opengl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;
    private GLSurfaceView.Renderer renderer;

    /**
     * 注意使用openGL 这个要非常注意渲染线程不是主线程。所以刷新OpenGL的
     * 线程
     * 默认情况下OpenGL的刷新频率是手机的刷新频率，但是如果我们想手动刷新，可以使用方法。
     * glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
     * 这样设置了之后就不会自动刷新，我们想刷新只需要调用 : glSurfaceView.requestRender();
     *
     * 这样可就是向后台渲染线程发送一个执行任务
   glSurfaceView.queueEvent(new Runnable() {
    @Override
    public void run() {

    }
    });
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (OpenGlUtil.isSupportOpenGL2(this)){
            glSurfaceView = new GLSurfaceView(this);
            //设置openGL的第二个版本
            glSurfaceView.setEGLContextClientVersion(2);
            renderer =  new FirstRender();
            //设置渲染的Render
            glSurfaceView.setRenderer(renderer);
            renderSet = true;
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//            glSurfaceView.requestRender();
            setContentView(glSurfaceView);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Log.e(getClass().getSimpleName(),"刷新页面");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            glSurfaceView.requestRender();
                        }
                    });
                }
            },1000,1000);


        }else {
            setContentView(R.layout.activity_first);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renderSet){
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet){
            glSurfaceView.onPause();
        }
    }
}
