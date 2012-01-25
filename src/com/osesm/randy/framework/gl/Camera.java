package com.osesm.randy.framework.gl;

import com.osesm.randy.framework.math.Matrix4;

import android.opengl.Matrix;

public class Camera {

	private int height;
	private int width;
	private Matrix4 viewMatrix = new Matrix4();
	private Matrix4 projectionMatrix = new Matrix4();

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;

		Matrix.setLookAtM(viewMatrix.getValues(), 0, 0, 0, -3, 0f, 0f, 0f, 0f, 2.0f, 0.0f);

	}

	public Matrix4 getViewProjectionMatrix() {
		Matrix4 viewProjectionMatrix = getProjection().multiplyByMatrix(getView());
		return viewProjectionMatrix;
	}

	public void rotate(float angle) {
		viewMatrix.rotate(angle, 0.2f, 0, 0);
	}

	private Matrix4 getProjection() {
		float ratio = (float) width / height;
		Matrix.frustumM(projectionMatrix.getValues(), 0, -ratio, ratio, -1, 1, 1, 7);
		// Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

		return projectionMatrix;
	}

	private Matrix4 getView() {

		return viewMatrix;

	}

}
