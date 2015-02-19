package me.jazzinghen.androidopengltest02.util;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.jazzinghen.androidopengltest02.JazzShader;

/**
 * Test OpenGLESRenderer created to get back on track with OpenGL
 * Might not be useful, but... It is for me!
 */
public class JazzGLRendererTest01 implements GLSurfaceView.Renderer {

    /**
     * Store our model data in a float buffer.
     */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;

    /**
     * How many bytes per float.
     * This is incredible. Since Java has beed designed to completely block the access to the size
     * of types you _have_ to declare it by hand.
     */
    private final int mBytesPerFloat = 4;

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /**
     * This is the shader program instance
     */
    private JazzShader triangleShader;

    /**
     * This will be used to pass in the transformation matrix.
     */
    private int mMVPMatrixHandle;

    /**
     * This will be used to pass in model position information.
     */
    private int mPositionHandle;

    /**
     * This will be used to pass in model color information.
     */
    private int mColorHandle;

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * Initialize the model data.
     */
    public JazzGLRendererTest01(Context context) {
        Log.d("GLSL Status", "Creation and Compilation of Vertex shader");
        triangleShader = new JazzShader("Shaders/lesson01.vtx.glsl", "Shaders/lesson01.fgx.glsl", context);

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

        /**
         *  Initialize the buffers.
         * This call is needed to reserve a space big as we need, with a nativeOrder for the bytes.
         * We have to do this as OpenGLES requires data as you would pass to it in C++ in order to
         * load the vertexBuffer to the GPU memory, but Java otherwise would optimize the memory
         * in mysterious ways, fucking everything up.
         */
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Copy the tirangle definition into our FloatBuffer
        mTriangle1Vertices.put(triangle1VerticesData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

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


        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(triangleShader.getProgramHandler(), "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(triangleShader.getProgramHandler(), "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(triangleShader.getProgramHandler(), "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(triangleShader.getProgramHandler());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Redraw background color
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // TODO: Actually Draw stuff! :D
    }
}
