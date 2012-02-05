package com.osesm.randy.framework.math;

import android.util.FloatMath;

public class Vector3 {

	public float z;
	public float y;
	public float x;
	
	public static int size() {
		return 3 * 4;
	}

	public Vector3() {
	}

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Vector3 other) {
		this(other.x, other.y, other.z);
	}

	public Vector3 copy() {
		return new Vector3(x, y, z);
	}

	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3 add(Vector3 other) {
		return add(other.x, other.y, other.z);
	}

	public Vector3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vector3 sub(Vector3 other) {
		return sub(other.x, other.y, other.z);
	}

	public Vector3 mul(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	public float length() {
		return FloatMath.sqrt(x * x + y * y + z * z);
	}

	public Vector3 normalize() {
		float length = length();
		if (length != 0) {
			this.x /= length;
			this.y /= length;
			this.z /= length;
		}

		return this;
	}
	
	public float[] toFloat() {
		float tmp[] = {x, y, z};
		return tmp;
	}

	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x
				* other.y - y * other.x);
	}

	public float distance(Vector3 other) {
		return FloatMath.sqrt(distanceSquared(other));
	}

	private float distanceSquared(Vector3 other) {
		return distanceSquared(other.x, other.y, other.z);
	}

	private float distanceSquared(float x2, float y2, float z2) {
		float distanceX = this.x - x;
		float distanceY = this.y - y;
		float distanceZ = this.z - z;
		return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
	}
}
