package me.jazzinghen.androidopengltest02.JazzGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class JazzGLTriangle {

    private FloatBuffer vertexBuffer;

    /**
     * How many bytes per float.
     * This is incredible. Since Java has beed designed to completely block the access to the size
     * of types you _have_ to declare it by hand.
     */
    private final int mBytesPerFloat = 4;

    public JazzGLTriangle(float[] triangle1VerticesData) {
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
                triangle1VerticesData.length * mBytesPerFloat);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangle1VerticesData);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }
}
