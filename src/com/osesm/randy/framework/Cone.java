package com.osesm.randy.framework;

import android.util.FloatMath;

import com.osesm.randy.framework.math.Vector2;
import com.osesm.randy.framework.math.Vector3;

public class Cone extends ParametricSurface {

	private float radius;
	private float height;

	public Cone(float height, float radius) {
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
}
