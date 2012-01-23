package com.osesm.randy.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.osesm.randy.framework.Simulation;

public class Mesh {

	private static final String TAG = "Randy";

	private boolean hasColor;
	private boolean hasTexCoords;
	private boolean hasNormals;
	private int vertexSize;
	private int[] tmpBuffer;
	private IntBuffer vertices;
	private ShortBuffer indices;
	private ShaderCompiler shaderCompiler;
	private int program;
	private Texture texture;

	Map<String, Integer> handles;

	private float[] modelMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	private static final int VERTICES_OFFSET = 0;
	private static final int UV_OFFSET = 3;
	private static final int COLOR_OFFSET = 3;
	protected static final int FLOAT_SIZE = 4;

	public Mesh(Simulation simulation, int maxVertices, int maxIndices, boolean hasColor,
			boolean hasTexCoords, boolean hasNormals) {

		shaderCompiler = simulation.getShaderCompiler();
		
		handles = new HashMap<String, Integer>();

		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.hasNormals = hasNormals;
		vertexSize = (3 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0) + (hasNormals ? 3
				: 0)) * FLOAT_SIZE;
		tmpBuffer = new int[maxVertices * vertexSize / FLOAT_SIZE];

		simulation.debug("vertex size = " + vertexSize);

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}
	}

	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++)
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		this.vertices.put(tmpBuffer, 0, length);
		this.vertices.flip();
	}

	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}

	public void compile(String vertextShaderFileName, String fragmentShaderFileName) {
		program = shaderCompiler.compile(vertextShaderFileName, fragmentShaderFileName);
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public boolean hasTexture() {
		return texture != null;
	}

	public boolean hasColor() {
		return hasColor;
	}

	public boolean hasNormals() {
		return hasNormals;
	}

	public void prepare(float[] viewProjectionMatrix) {
		handles.put("aPosition", GLES20.glGetAttribLocation(program, "aPosition"));

		if (hasTexture()) {
			handles.put("aTextureCoord", GLES20.glGetAttribLocation(program, "aTextureCoord"));
			handles.put("sTexture", GLES20.glGetAttribLocation(program, "sTexture"));
		} else {
			handles.put("aColor", GLES20.glGetAttribLocation(program, "SourceColor"));
		}

		handles.put("uMVPMatrix", GLES20.glGetUniformLocation(program, "uMVPMatrix"));
		Matrix.multiplyMM(mMVPMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
	}

	public void setModelMatrix(float[] matrix) {
		modelMatrix = matrix;
	}

	public float[] getModelMatrix() {
		return modelMatrix;
	}

	public void draw() {
		GLES20.glUseProgram(program);

		if (hasTexture()) {
			getTexture().bind();
		}

		vertices.position(VERTICES_OFFSET);
		GLES20.glVertexAttribPointer(handles.get("aPosition"), 3, GLES20.GL_FLOAT, false,
				vertexSize, vertices);
		checkGlError("position vertex attrib pointer");
		GLES20.glEnableVertexAttribArray(handles.get("aPosition"));
		checkGlError("enable position vertex array");

		if (hasColor) {
			vertices.position(COLOR_OFFSET);
			GLES20.glVertexAttribPointer(handles.get("aColor"), 4, GLES20.GL_FLOAT, false,
					vertexSize, vertices);
			checkGlError("color vertex attrib pointer");
			GLES20.glEnableVertexAttribArray(handles.get("aColor"));
			checkGlError("enable color vertex array");
		} else if (hasTexCoords) {
			vertices.position(UV_OFFSET);
			GLES20.glVertexAttribPointer(handles.get("aTextureCoord"), 2, GLES20.GL_FLOAT, false,
					vertexSize, vertices);
			checkGlError("texture vertex attrib pointer");
			GLES20.glEnableVertexAttribArray(handles.get("aTextureCoord"));
			checkGlError("enable texture vertex array");
		}

		GLES20.glUniformMatrix4fv(handles.get("uMVPMatrix"), 1, false, mMVPMatrix, 0);

		if (indices == null) {
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.limit());
			checkGlError("drawArrays");
		} else {
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
					indices);
			checkGlError("drawElements");
		}
	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
