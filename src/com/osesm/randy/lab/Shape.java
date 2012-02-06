package com.osesm.randy.lab;

import com.osesm.randy.framework.WorldObject;
import com.osesm.randy.framework.gl.Mesh;

public class Shape extends WorldObject {

	private float angle;

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
		getMesh().modelMatrix().rotate(angle, 0, 0, 1.0f);
		getMesh().prepare(getProjectionMatrix());
	}

	@Override
	public void draw() {
		getMesh().draw();
	}
}