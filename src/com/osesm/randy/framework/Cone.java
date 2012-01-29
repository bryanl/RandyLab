package com.osesm.randy.framework;

import android.opengl.GLES20;
import android.util.FloatMath;

import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.math.Vector2;
import com.osesm.randy.framework.math.Vector3;
import com.osesm.randy.lab.*;

public class Cone extends ParametricSurface {

	private float radius;
	private float height;

	public Cone(float height, float radius) {
		super();
		
		this.height = height;
		this.radius = radius;

		ParametricInterval interval = new ParametricInterval();
		interval.divisions = new Vector2(20, 20);
		interval.upperBound = new Vector2((float) (2 * Math.PI), 1.0f);

		setInterval(interval);
	}

	@Override
	public Vector3 evaluate(Vector2 computeDomain) {
		float u = computeDomain.x, v = computeDomain.y;
		float x = radius * (1 - v) * FloatMath.cos(u);
		float y = height * (v - 0.5f);
		float z = radius * (1 - v) * -FloatMath.sin(u);

		return new Vector3(x, y, z);
	}

	@Override
	public boolean useDomainCoords() {
		return false;
	}

	@Override
	public Shape toShape(Simulation simulation) {
		float[] coneVertices = generateVertices(0);
		short[] coneIndices = generateTriangleIndices();

		Mesh mesh = new Mesh(simulation, coneVertices.length, coneIndices.length, true, false,
				false);
		mesh.setVertices(coneVertices, 0, coneVertices.length);
		mesh.setIndices(coneIndices, 0, coneIndices.length);
		mesh.setDrawStyle(GLES20.GL_TRIANGLES);
		
		Shape shape = new Shape(mesh, "simple.vert", "simple.frag");
		
		return shape;
	}
}
