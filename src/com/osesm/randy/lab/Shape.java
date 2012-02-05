package com.osesm.randy.lab;

import com.osesm.randy.framework.WorldObject;
import com.osesm.randy.framework.gl.Mesh;

public class Shape extends WorldObject {

	private static long nextId = 0;
	private float angle;
	private long id;

	public static long nextId() {
		return nextId++;
	}
	
	public Shape(Mesh mesh, String vertexShader, String fragmentShader) {
		setFragmentShader(fragmentShader);
		setVertexShader(vertexShader);
		
		setMesh(mesh);
		this.id = nextId();
	}

	public long getId() {
		return id;
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
		getMesh().getModelMatrix().rotate(angle, 0, 0, 1.0f);
		getMesh().prepare(getProjectionMatrix());
	}

	@Override
	public void draw() {
		getMesh().draw();
	}
}