package me.jazzinghen.androidopengltest02;

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
 * Class created to convert shaders into strings from files.
 * I read a couple of tutorials and they all integrated constant strings into the code instead
 * of using external files. HerpDerp!
 */
public class JazzShader {

    // Using AssetManager to access the assets
    private final AssetManager fileManager;

    private int ShaderHandler;

    public JazzShader(String shaderName, int shaderType, Context cont) {
        fileManager = cont.getAssets();

        ShaderHandler = GLES20.glCreateShader(shaderType);

        if (ShaderHandler != 0) {
            // Get the goddamn file and give it to the shader handler.
            GLES20.glShaderSource(ShaderHandler, readFromFile(shaderName));

            // Compile the shader
            GLES20.glCompileShader(ShaderHandler);

            // Get informations for compilation of shader
            final int[] compilationStatus = new int[1];
            GLES20.glGetShaderiv(ShaderHandler, GLES20.GL_COMPILE_STATUS, compilationStatus, 0);

            if (compilationStatus[0] == 0) {
                Log.w("GLSL Status:", "Vertex Shader copilation failed: " + GLES20.glGetShaderInfoLog(ShaderHandler));
                GLES20.glDeleteShader(ShaderHandler);
                ShaderHandler = 0;
            }

            if (ShaderHandler == 0) {
                throw new RuntimeException("Error creating vertex shader.");
            }
        }
    }

    public int getShaderHandler() {
        return ShaderHandler;
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
