package com.osesm.randy.lab;

import com.osesm.randy.framework.WorldObject;
import com.osesm.randy.framework.gl.Mesh;

class Shape extends WorldObject {

	private static long nextId = 0;
	private float angle;
	private long id;

	public static long nextId() {
		return nextId++;
	}

	public Shape(Mesh mesh) {		
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

		if (mesh.hasTexture()) {
			mesh.compile("hellogl_with_mesh_texture_vertex_shader.glsl",
					"hellogl_with_mesh_texture_fragment_shader.glsl");
		} else {
			mesh.compile("hellogl_with_mesh_vertex_shader.glsl",
					"hellogl_with_mesh_fragment_shader.glsl");
		}
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