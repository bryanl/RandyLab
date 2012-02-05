package com.osesm.randy.framework.math;

import java.nio.FloatBuffer;

public class Vector4 {
	public float x;
	public float y;
	public float z;
	public float w;

	public Vector4() {
	}
	
	public Vector4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0f;
	}

	public Vector4(Vector4 other) {
		this(other.x, other.y, other.z, other.w);
	}

	public Vector4 copy() {
		return new Vector4(x, y, z, w);
	}
	
	public Vector4 add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}

	public Vector4 add(Vector4 other) {
		return add(other.x, other.y, other.z, other.w);
	}

	public Vector4 sub(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
		return this;
	}

	public Vector4 sub(Vector4 other) {
		return sub(other.x, other.y, other.z, other.w);
	}

	public Vector4 mul(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	public FloatBuffer toFloatBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
