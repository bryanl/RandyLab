package com.osesm.randy.lab;

import android.opengl.Matrix;

import com.osesm.randy.framework.WorldObject;
import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.math.Matrix4;

public class Shape extends WorldObject {

	private float angle = 0;
	int counter = 0;

	public Shape(Mesh mesh, String vertexShader, String fragmentShader) {
		super();

		setFragmentShader(fragmentShader);
		setVertexShader(vertexShader);

		setMesh(mesh);
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void setMesh(Mesh mesh) {
		super.setMesh(mesh);

		if ((getFragmentShader() == null) || (getVertexShader() == null)) {
			throw new MissingShaderException();
		}

		mesh.compile(getVertexShader(), getFragmentShader());
	}

	@Override
	public void update() {
		getMesh().setupVBO(getViewProjectionMatrix());
	}

	@Override
	public void draw() {
		getMesh().draw();
	}
	
	public void setScale(float scale) {
		Matrix4 modelMatrix = getMesh().getModelMatrix();
		Matrix.scaleM(modelMatrix.asFloatArray(), 0, scale, 0, 0);
	}
}