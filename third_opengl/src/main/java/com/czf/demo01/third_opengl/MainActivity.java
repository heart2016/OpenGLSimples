package com.czf.demo01.third_opengl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.MoreAsserts;
import android.test.mock.MockContext;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private GLSurfaceView.Renderer renderer;
    private boolean renderSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (OpenGlUtil.isSupportOpenGL2(this)){
            glSurfaceView = new GLSurfaceView(this);
            glSurfaceView.setEGLContextClientVersion(2);
            renderer = new ThirdRender(this);
            glSurfaceView.setRenderer(renderer);
            setContentView(glSurfaceView);
            renderSet = true;
        }else {
            setContentView(R.layout.activity_main);
        }

//        MockContext
//        MoreAsserts
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
