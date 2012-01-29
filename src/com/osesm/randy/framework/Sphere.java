package com.osesm.randy.framework;

import android.util.FloatMath;

import com.osesm.randy.framework.math.Vector2;
import com.osesm.randy.framework.math.Vector3;

public class Sphere extends ParametricSurface {

	private float radius;

	public Sphere(float radius) {
		this.radius = radius;
		
		
		ParametricInterval interval = new ParametricInterval();
		interval.divisions = new Vector2(20,20);
		interval.upperBound = new Vector2((float)Math.PI, (float)(2*Math.PI));
		
		setInterval(interval);
	}
	
	@Override
	public Vector3 evaluate(Vector2 computeDomain) {
		float u = computeDomain.x, v = computeDomain.y;
		float x = radius * FloatMath.sin(u) * FloatMath.cos(v);
		float y = radius * FloatMath.cos(u);
		float z = radius * -FloatMath.sin(u) * FloatMath.sin(v);
		
		return new Vector3(x, y ,z);
		
	}

}
