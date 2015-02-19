package me.jazzinghen.androidopengltest02;

import me.jazzinghen.androidopengltest02.JazzGLRender;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by jazzinghen on 18/02/15.
 */
public class JazzGLSurfaceView extends GLSurfaceView{

    public JazzGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);


        if(!isInEditMode()) {

            final JazzGLRender mRenderer;

            // Check if the system supports OpenGL ES 2.0.
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
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

                mRenderer = new JazzGLRender();

                setRenderer(mRenderer);

                // Set rendering only when new data to render is available
                setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            }
            else
            {
                // This is where you could create an OpenGL ES 1.x compatible
                // renderer if you wanted to support both ES 1 and ES 2.
                return;
            }
        }
    }



}
