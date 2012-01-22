package com.osesm.randy.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.osesm.randy.framework.Simulation;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

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

	private int maPositionHandle;
	private int maTextureHandle;
	private int maColorHandle;
	private int msTextureHandle;

	private int muMVPMatrixHandle;
	private float[] modelMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	private static final int VERTICES_OFFSET = 0;
	private static final int UV_OFFSET = 3;
	private static final int COLOR_OFFSET = 3;
	protected static final int FLOAT_SIZE = 4;

	public Mesh(Simulation simulation, int maxVertices, int maxIndices, boolean hasColor,
			boolean hasTexCoords, boolean hasNormals) {

		setShaderCompiler(simulation.getShaderCompiler());

		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.hasNormals = hasNormals;
		vertexSize = (3 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0) + (hasNormals ? 3
				: 0)) * FLOAT_SIZE;
		tmpBuffer = new int[maxVertices * vertexSize / FLOAT_SIZE];

		Log.d("Randy", "vertex size = " + vertexSize);

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

	public void setShaderCompiler(ShaderCompiler shaderCompiler) {
		this.shaderCompiler = shaderCompiler;
	}

	public void compile(String vertextShaderFileName, String fragmentShaderFileName) {
		program = shaderCompiler.compile(vertextShaderFileName, fragmentShaderFileName);
	}

	public void setProgram(int program) {
		this.program = program;
	}

	public int getProgram() {
		return program;
	}

	public int getVertexSize() {
		return vertexSize;
	}

	protected IntBuffer getVertices() {
		return vertices;
	}

	protected ShortBuffer getIndices() {
		return indices;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public boolean hasTexture() {
		return hasTexCoords;
	}

	public boolean hasColor() {
		return hasColor;
	}

	public boolean hasNormals() {
		return hasNormals;
	}

	public void prepare(float[] viewProjectionMatrix) {
		maPositionHandle = GLES20.glGetAttribLocation(getProgram(), "aPosition");

		if (hasTexture()) {
			maTextureHandle = GLES20.glGetAttribLocation(getProgram(), "aTextureCoord");
			msTextureHandle = GLES20.glGetAttribLocation(getProgram(), "sTexture");
		} else {
			maColorHandle = GLES20.glGetAttribLocation(getProgram(), "SourceColor");
		}

		muMVPMatrixHandle = GLES20.glGetUniformLocation(getProgram(), "uMVPMatrix");
		Matrix.multiplyMM(mMVPMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
	}
	
	public void setModelMatrix(float[] matrix) {
		modelMatrix = matrix;
	}
	
	public float[] getModelMatrix() {
		return modelMatrix;
	}

	public void draw() {
		GLES20.glUseProgram(getProgram());

		if (hasTexture()) {
			getTexture().bind();
		}

		getVertices().position(VERTICES_OFFSET);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
				getVertexSize(), getVertices());
		checkGlError("position vertex attrib pointer");
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		checkGlError("enable position vertex array");

		if (hasTexture()) {
			getVertices().position(UV_OFFSET);
			GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,
					getVertexSize(), getVertices());
			checkGlError("texture vertex attrib pointer");
			GLES20.glEnableVertexAttribArray(maTextureHandle);
			checkGlError("enable texture vertex array");
		} else {
			getVertices().position(COLOR_OFFSET);
			GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,
					getVertexSize(), getVertices());
			checkGlError("color vertex attrib pointer");
			GLES20.glEnableVertexAttribArray(maColorHandle);
			checkGlError("enable color vertex array");
		}

		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
				getIndices());
	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
