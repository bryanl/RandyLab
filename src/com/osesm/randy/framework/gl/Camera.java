package com.osesm.randy.framework.gl;

import android.opengl.Matrix;

import com.osesm.randy.framework.math.Matrix4;
import com.osesm.randy.framework.math.Vector3;

public class Camera {

	private int height;
	private int width;
	private Matrix4 viewMatrix = new Matrix4();
	private Matrix4 projectionMatrix = new Matrix4();

	private Vector3 eye;
	private Vector3 up;
	private Vector3 center;

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;

		eye = new Vector3(0f, -2.5f, -4f);
		center = new Vector3(0, 0, 0);
		up = new Vector3(0, 1f, 0);

		Matrix.setLookAtM(viewMatrix.values(), 0, eye.x, eye.y, eye.z, center.x,
				center.y, center.z, up.x, up.y, up.z);
	}

	public Vector3 getEye() {
		return eye;
	}

	public void setEye(Vector3 eye) {
		this.eye = eye;
	}

	public Vector3 getUp() {
		return up;
	}

	public void setUp(Vector3 up) {
		this.up = up;
	}

	public Vector3 getCenter() {
		return center;
	}

	public void setCenter(Vector3 center) {
		this.center = center;
	}

	public Matrix4 getViewProjectionMatrix() {
		Matrix4 viewProjectionMatrix = getProjection().multiplyByMatrix(getView());
		return viewProjectionMatrix;
	}

	public void rotate(float angle) {
		viewMatrix.rotate(angle, 0.2f, 0, 0);
	}

	private Matrix4 getProjection() {
//		float ratio = (float) width / height;

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
