package com.osesm.randy.framework;

import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.math.Matrix4;

public abstract class WorldObject {

	public class MissingShaderException extends RuntimeException {
		private static final long serialVersionUID = 4063952492692817338L;
	}

	private static long nextId = 0;

	public static long getNextObjectId() {
		return nextId++;
	}

	private Mesh mesh;
	private Matrix4 projectionMatrix = new Matrix4();

	private String vertexShader;
	private String fragmentShader;
	private long id;
	
	public WorldObject() {
		this.id = getNextObjectId();
	}

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

	public String getVertexShader() {
		return vertexShader;
	}

	public void setVertexShader(String vertexShader) {
		this.vertexShader = vertexShader;
	}

	/**
	 * 
	 * @return file name of
	 */
	public String getFragmentShader() {
		return fragmentShader;
	}

	public void setFragmentShader(String fragmentShader) {
		this.fragmentShader = fragmentShader;
	}

	public long getId() {
		return id;
	}
	
	public abstract void update();

	public abstract void draw();


}
