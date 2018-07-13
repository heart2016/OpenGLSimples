package com.czf.second_opengl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 这个例子学习openGL着色器的作用。
 */
public class SecondActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private GLSurfaceView.Renderer renderer;
    private boolean renderSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (OpenGlUtil.isSupportOpenGL2(this)){
            glSurfaceView = new GLSurfaceView(this);
            renderer = new SecondRender();
            glSurfaceView.setRenderer(renderer);
            setContentView(glSurfaceView);
            renderSet = true;
        }else {
            setContentView(R.layout.activity_second);
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
