package com.osesm.randy.framework.gl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import com.osesm.randy.framework.FileIO;

public class ShaderCompiler {
	private String logTag = "RandyLab";
	private AssetManager assetManager;
	private final String shaderBaseDirectory = "shaders";

	public ShaderCompiler(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public int compile(String vertexShaderFileName, String fragmentShaderFileName) {
		FileIO fileIO = new FileIO(assetManager);

		try {
			String vertexShaderSource = convertStreamToString(fileIO
					.readAsset(shaderBaseDirectory + "/" + vertexShaderFileName));
			String fragmentShaderSource = convertStreamToString(fileIO
					.readAsset(shaderBaseDirectory + "/" + fragmentShaderFileName));

			Log.d(logTag, vertexShaderSource);
			Log.d(logTag, fragmentShaderSource);
			
			int vertexShader = getShader(vertexShaderSource, GLES20.GL_VERTEX_SHADER);
			int fragmentShader = getShader(fragmentShaderSource,
					GLES20.GL_FRAGMENT_SHADER);

			return createProgram(vertexShader, fragmentShader);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(logTag, "Error: " + e.getMessage());
			return 0;
		}
	}

	private int createProgram(int vertexShader, int fragmentShader) {
		int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		GLES20.glLinkProgram(program);

		int[] linked = { 0 };
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
		if (linked[0] == 0) {
			Log.e(logTag, GLES20.glGetProgramInfoLog(program));
			return 0;
		} else {
			Log.d(logTag, "Sucessfully linked program");
		}
		return program;
	}

	private int getShader(String source, int type) {
		int shader = GLES20.glCreateShader(type);
		if (shader == 0)
			return 0;

		GLES20.glShaderSource(shader, source);
		GLES20.glCompileShader(shader);
		int[] compiled = { 0 };
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			Log.e(logTag, GLES20.glGetShaderInfoLog(shader));
		}
		return shader;
	}

	private String convertStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(inputStream,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
}
