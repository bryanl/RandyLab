package com.osesm.randy.framework;

import com.osesm.randy.framework.gl.Mesh;

public abstract class WorldObject {

	private Mesh mesh;
	private float[] projectionMatrix = new float[16];

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setProjectionMatrix(float[] matrix) {
		projectionMatrix = matrix;
	}

	public float[] getProjectionMatrix() {
		return projectionMatrix;
	}

	public abstract void update();

	public abstract void draw();

}
