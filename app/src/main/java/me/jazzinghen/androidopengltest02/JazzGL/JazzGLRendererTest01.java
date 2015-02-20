package me.jazzinghen.androidopengltest02.JazzGL;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Test OpenGLESRenderer created to get back on track with OpenGL
 * Might not be useful, but... It is for me!
 */
public class JazzGLRendererTest01 implements GLSurfaceView.Renderer {

    /**
     * Store our model data in a float buffer.
     */
    private JazzGLTriangle Triangle1;
    //private final FloatBuffer mTriangle2Vertices;
    //private final FloatBuffer mTriangle3Vertices;

    private final Context renderingContext;
    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * Initialize the model data.
     */
    public JazzGLRendererTest01(Context context) {
        renderingContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // This triangle is red, green, and blue.
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f
        };

        Triangle1 = new JazzGLTriangle(triangle1VerticesData, "Shaders/lesson01.vtx.glsl", "Shaders/lesson01.fgx.glsl", renderingContext);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        Triangle1.setmViewMatrix(mViewMatrix);

        Triangle1.getTriangleShader().initialiseHandlers();

        // Tell OpenGL to use this program when rendering.
        Log.d("OpenGL", "Linking shader program to the scene.");
        GLES20.glUseProgram(Triangle1.getTriangleShader().getProgramHandler());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Redraw background color
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        //final float left = -ratio;
        //final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, bottom, top, near, far);
        Triangle1.setmProjectionMatrix(mProjectionMatrix);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        float[] mModelMatrix = new float[16];

        // Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);

        // Set model matrix after having computed it
        Triangle1.setmModelMatrix(mModelMatrix);

        // DRAW!
        //
        // [Haha, did you get it?]
        Triangle1.draw();

    }
}
