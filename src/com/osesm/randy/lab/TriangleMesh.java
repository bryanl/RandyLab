package com.osesm.randy.lab;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.gl.Mesh;

class TriangleMesh extends Mesh {
	private static final int VERTICES_OFFSET = 0;
	private static final int UV_OFFSET = 3;
	private static final int COLOR_OFFSET = 3;

	private static final String TAG = "Randy";

	private int maPositionHandle;
	private int maTextureHandle;

	private int muMVPMatrixHandle;
	private float[] mMMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	private float mAngle;

	private int maColorHandle;
	private int msTextureHandle;

	public TriangleMesh(Simulation simulation, int maxVertices, int maxIndices,
			boolean hasColor, boolean hasTexCoords, boolean hasNormals) {
		super(maxVertices, maxIndices, hasColor, hasTexCoords, hasNormals);

		setShaderCompiler(simulation.getShaderCompiler());

		if (hasTexture()) {
			int program = compile("hellogl_with_mesh_texture_vertex_shader.glsl",
					"hellogl_with_mesh_texture_fragment_shader.glsl");
			setProgram(program);
		} else {
			int program = compile("hellogl_with_mesh_vertex_shader.glsl",
					"hellogl_with_mesh_fragment_shader.glsl");
			setProgram(program);
		}
	}

	@Override
	public void prepare(float[] viewProjectionMatrix) {
		maPositionHandle = GLES20.glGetAttribLocation(getProgram(), "aPosition");

		if (hasTexture()) {
			maTextureHandle = GLES20.glGetAttribLocation(getProgram(),
					"aTextureCoord");
			msTextureHandle = GLES20.glGetAttribLocation(getProgram(), "sTexture");
		} else {
			maColorHandle = GLES20.glGetAttribLocation(getProgram(), "SourceColor");
		}

		muMVPMatrixHandle = GLES20.glGetUniformLocation(getProgram(), "uMVPMatrix");

		Matrix.setRotateM(mMMatrix, 0, mAngle, 0, 0, 1.0f);
		Matrix.multiplyMM(mMVPMatrix, 0, viewProjectionMatrix, 0, mMMatrix, 0);
	}

	@Override
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

	public void setAngle(float angle) {
		mAngle = angle;
	}



	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}