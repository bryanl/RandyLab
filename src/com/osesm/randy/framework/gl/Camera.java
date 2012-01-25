package com.osesm.randy.framework.gl;

import android.opengl.Matrix;

public class Camera {

	private int height;
	private int width;
	private float[] viewMatrix = new float[16];
	float[] projectionMatrix = new float[16];

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;

		Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 2.0f, 0.0f);

	}

	public float[] getViewProjectionMatrix() {
		float[] viewProjectionMatrix = new float[16];

		Matrix.multiplyMM(viewProjectionMatrix, 0, getProjection(), 0, getView(), 0);
		return viewProjectionMatrix;
	}

	public void rotate(float angle) {
		Matrix.rotateM(viewMatrix, 0, angle, 0.2f, 0f, 0f);
	}

	private float[] getProjection() {
		float ratio = (float) width / height;
		Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 7);
		// Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

		return projectionMatrix;
	}

	private float[] getView() {

		return viewMatrix;

	}

}
