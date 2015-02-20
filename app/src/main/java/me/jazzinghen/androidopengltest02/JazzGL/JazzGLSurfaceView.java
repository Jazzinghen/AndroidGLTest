package me.jazzinghen.androidopengltest02.JazzGL;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Modified GLSurfaceView in order to check the enviornment before launching the GLRenderer
 */
public class JazzGLSurfaceView extends GLSurfaceView{

    public JazzGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);


        if(!isInEditMode()) {

            final JazzGLRendererTest01 mRenderer;

            // Check if the system supports OpenGL ES 2.0.
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
            final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
            Log.d("OpenGLStatus", "Version: " + supportsEs2);
            Log.d("SystemBuild", "Build: " + Build.FINGERPRINT);

            if (supportsEs2 || Build.FINGERPRINT.startsWith("generic"))
            {
                // Set the OpenGLES API version to 2.0, setting Buffer Config
                setEGLContextClientVersion(2);
                setEGLConfigChooser(8,8,8,8,16,0);
                setPreserveEGLContextOnPause(true);

                mRenderer = new JazzGLRendererTest01(context);

                setRenderer(mRenderer);

                // Set rendering only when new data to render is available
                // Needs a call to requestRender (), which is a public of SurfaceView, but I have
                // to call it from the Triangle drawer. How do I do it in a non-sucky way?
                // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            } else {
                Log.e("OpenGLES activity", "No OpenGLES 2.0 API support. Time to buy a new phone.");
            }
        }
    }



}
