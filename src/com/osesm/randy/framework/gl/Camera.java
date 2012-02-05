package com.osesm.randy.framework.gl;

import android.opengl.Matrix;

import com.osesm.randy.framework.math.Matrix4;

public class Camera {

	private int height;
	private int width;
	private Matrix4 viewMatrix = new Matrix4();
	private Matrix4 projectionMatrix = new Matrix4();

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;

		Matrix.setLookAtM(viewMatrix.values(), 0, 0f, -2.5f, -4f, 0f, 0f, 0f, 0f, 1.0f,
				0.0f);

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

		// TODO This is where we want to be
		float h = 4.0f * height / width;
		// Matrix.frustumM(projectionMatrix.getValues(), 0, -2.0f, 2.0f,
		// -h/2.0f, h/2.0f, 5.0f, 10.0f);

		Matrix.frustumM(projectionMatrix.values(), 0, -h, h, -1, 1, 1, 10);
		// Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -2, 2, 3, 7);

		return projectionMatrix;
	}

	private Matrix4 getView() {

		return viewMatrix;

	}

}
