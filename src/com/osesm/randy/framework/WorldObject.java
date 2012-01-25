package com.osesm.randy.framework;

import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.math.Matrix4;

public abstract class WorldObject {

	private Mesh mesh;
	private Matrix4 projectionMatrix = new Matrix4();

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setProjectionMatrix(Matrix4 matrix) {
		projectionMatrix = matrix;
	}

	public Matrix4 getProjectionMatrix() {
		return projectionMatrix;
	}

	public abstract void update();

	public abstract void draw();

}
