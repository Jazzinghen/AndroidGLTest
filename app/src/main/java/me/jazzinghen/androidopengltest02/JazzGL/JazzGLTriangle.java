package me.jazzinghen.androidopengltest02.JazzGL;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class JazzGLTriangle {

    private FloatBuffer vertexBuffer;


    /**
     * This is the shader program instance
     */
    private JazzGLShader triangleShader;

    /**
     * How many bytes per float.
     * This is incredible. Since Java has beed designed to completely block the access to the size
     * of types you _have_ to declare it by hand.
     */
    private final int mBytesPerFloat = 4;

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program.
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

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
     * How many elements per vertex.
     */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /**
     * Offset of the position data.
     */
    private final int mPositionOffset = 0;

    /**
     * Size of the position data in elements.
     */
    private final int mPositionDataSize = 3;

    /**
     * Offset of the color data.
     */
    private final int mColorOffset = 3;

    /**
     * Size of the color data in elements.
     */
    private final int mColorDataSize = 4;

    public JazzGLTriangle(float[] triangleVerticesData, String vertexShader, String fragmentShader, Context context) {
        /**
         *  Initialize the buffers.
         * This call is needed to reserve a space big as we need, with a nativeOrder for the bytes.
         * We have to do this as OpenGLES requires data as you would pass to it in C++ in order to
         * load the vertexBuffer to the GPU memory, but Java otherwise would optimize the memory
         * in mysterious ways, fucking everything up.
         */

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleVerticesData.length * mBytesPerFloat);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleVerticesData);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        Log.d("GLSL Status", "Creation and Compilation of Vertex shader");
        triangleShader = new JazzGLShader(vertexShader, fragmentShader, context);
    }

    public void draw() {

        // Pass in the position information
        vertexBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(triangleShader.getmPositionHandle(), mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, vertexBuffer);

        GLES20.glEnableVertexAttribArray(triangleShader.getmPositionHandle());

        // Pass in the color information
        vertexBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(triangleShader.getmColorHandle(), mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, vertexBuffer);

        GLES20.glEnableVertexAttribArray(triangleShader.getmColorHandle());

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(triangleShader.getmMVPMatrixHandle(), 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    public JazzGLShader getTriangleShader() {
        return triangleShader;
    }

    public void setmModelMatrix(float[] mModelMatrix) {
        System.arraycopy(mModelMatrix, 0, this.mModelMatrix, 0, mModelMatrix.length);
    }

    public void setmViewMatrix(float[] mViewMatrix) {
        System.arraycopy(mViewMatrix, 0, this.mViewMatrix, 0, mViewMatrix.length);
    }

    public void setmProjectionMatrix(float[] mProjectionMatrix) {
        System.arraycopy(mProjectionMatrix, 0, this.mProjectionMatrix, 0, mProjectionMatrix.length);
    }

}
