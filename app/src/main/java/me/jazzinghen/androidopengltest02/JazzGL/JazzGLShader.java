package me.jazzinghen.androidopengltest02.JazzGL;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class created to get a vertex shader and a fragment shader and combine them into a shader Program.
 * I read a couple of tutorials and they all integrated constant strings into the code instead
 * of using external files. HerpDerp!
 */
public class JazzGLShader {

    // Using AssetManager to access the assets
    private final AssetManager fileManager;

    private int vertexShader;
    private int fragmentShader;

    private int programHandler;

    public JazzGLShader(String vertName, String fragName, Context cont) {
        fileManager = cont.getAssets();

        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        programHandler = GLES20.glCreateProgram();

        Log.d("GLSL Status", "Creation and Compilation of Vertex shader");

        if (vertexShader != 0) {
            // Get the goddamn file and give it to the shader handler.
            GLES20.glShaderSource(vertexShader, readFromFile(vertName));

            // Compile the shader
            GLES20.glCompileShader(vertexShader);

            // Get informations for compilation of shader
            final int[] compilationStatus = new int[1];
            GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compilationStatus, 0);

            if (compilationStatus[0] == 0) {
                Log.w("GLSL Status:", "Vertex Shader copilation failed: " + GLES20.glGetShaderInfoLog(vertexShader));
                GLES20.glDeleteShader(vertexShader);
                vertexShader = 0;
            }

            if (vertexShader == 0) {
                throw new RuntimeException("Error creating vertex shader.");
            }
        }

        Log.d("GLSL Status", "Creation and Compilation of Fragment shader");

        if (fragmentShader != 0) {
            // Get the goddamn file and give it to the shader handler.
            GLES20.glShaderSource(fragmentShader, readFromFile(fragName));

            // Compile the shader
            GLES20.glCompileShader(fragmentShader);

            // Get informations for compilation of shader
            final int[] compilationStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, compilationStatus, 0);

            if (compilationStatus[0] == 0) {
                Log.w("GLSL Status:", "Fragment Shader copilation failed: " + GLES20.glGetShaderInfoLog(fragmentShader));
                GLES20.glDeleteShader(fragmentShader);
                fragmentShader = 0;
            }

            if (fragmentShader == 0) {
                throw new RuntimeException("Error creating fragment shader.");
            }
        }

        Log.d("GLSL Status", "Creation and Linking of Shader Application");

        if (programHandler != 0) {
            // Attach Vertex Shader
            GLES20.glAttachShader(programHandler, vertexShader);

            // Attach Fragment Shader
            GLES20.glAttachShader(programHandler, fragmentShader);

            // Bind attributes of VBO
            GLES20.glBindAttribLocation(programHandler, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandler, 1, "a_Colour");

            // Link the two shaders into a single program
            GLES20.glLinkProgram(programHandler);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandler, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.w("GLSL Status:", "Shader Program link failed: " + GLES20.glGetShaderInfoLog(programHandler));
                GLES20.glDeleteProgram(programHandler);
                programHandler = 0;
            }
        }

        if (programHandler == 0) {
            throw new RuntimeException("Error creating program.");
        }

    }

    public int getVertexShader() {
        return vertexShader;
    }

    public int getFragmentShader() {
        return fragmentShader;
    }

    public int getProgramHandler() {
        return programHandler;
    }

    private String readFromFile(String fileToRead) {

        String ret = "";

        try {
            InputStream inputStream = fileManager.open(fileToRead);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append('\n');
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
