package me.jazzinghen.androidopengltest02.JazzGL;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by jazzinghen on 18/02/15.
 */
public class JazzGLRender implements GLSurfaceView.Renderer {

    // Function called the first time the surface is created or whenever the system has to recreate the context
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    // Function called for every frame
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    // Function called every time the size of the surface is changed. And the first time it is created [i.e. First
    // onSurfaceCreated is called and then on SurfaceChanged is called the first time the application is launched]
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
