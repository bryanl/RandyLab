package com.osesm.randy.framework.math;

import android.util.FloatMath;

public class Quaternion {

	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 1;
	}

	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion slerp(float mu, Quaternion v1) {
		float epsilon = 0.0005f;
		float dotProduct = dot(v1);

		if (dotProduct > 1 - epsilon) {
			Quaternion result = v1.add(this.sub(v1).scaled(mu));
			result.normalize();
			return result;
		}

		if (dotProduct < 0)
			dotProduct = 0;

		if (dotProduct > 1)
			dotProduct = 1;

		float theta0 = (float) Math.acos(dotProduct);
		float theta = theta0 * mu;

		Quaternion v2 = v1.sub(scaled(dotProduct));
		v2.normalize();

		Quaternion q = scaled(FloatMath.cos(theta)).add(v2.scaled(FloatMath.sin(theta)));
		q.normalize();

		return q;
	}

	public Quaternion rotatedBy(Quaternion q) {
		Quaternion newQuaternion = new Quaternion();
		newQuaternion.w = w * q.w - x * q.x - y * q.y - z * q.z;
		newQuaternion.x = w * q.x + x * q.w + y * q.z - z * q.y;
		newQuaternion.y = w * q.y + y * q.w + z * q.x - x * q.z;
		newQuaternion.z = w * q.z + z * q.w + x * q.y - y * q.x;

		return newQuaternion;
	}

	public Quaternion scaled(float scale) {
		return new Quaternion(x * scale, y * scale, z * scale, w * scale);
	}

	public float dot(Quaternion q) {
		return x * q.x + y * q.y + z * q.z + w * q.w;
	}

	public Matrix3 toMatrix3() {
		float s = 2;
		float xs, ys, zs;
		float wx, wy, wz;
		float xx, xy, xz;
		float yy, yz, zz;
		xs = x * s;
		ys = y * s;
		zs = z * s;
		wx = w * xs;
		wy = w * ys;
		wz = w * zs;
		xx = x * xs;
		xy = x * ys;
		xz = x * zs;
		yy = y * ys;
		yz = y * zs;
		zz = z * zs;
		Matrix3 m = new Matrix3();

		m.vals[Matrix3.M00] = 1 - (yy + zz);
		m.vals[Matrix3.M10] = xy - wz;
		m.vals[Matrix3.M20] = xz + wy;
		m.vals[Matrix3.M01] = xy + wz;
		m.vals[Matrix3.M11] = 1 - (xx + zz);
		m.vals[Matrix3.M21] = yz - wx;
		m.vals[Matrix3.M02] = xz - wy;
		m.vals[Matrix3.M12] = yz + wx;
		m.vals[Matrix3.M22] = 1 - (xx + yy);

		return m;
	}

	// public Vector4 toVector() {
	//
	// }

	public Quaternion sub(Quaternion quaternion) {
		return new Quaternion(x - quaternion.x, y - quaternion.y, z - quaternion.z, w
				- quaternion.w);
	}

	public Quaternion add(Quaternion quaternion) {
		return new Quaternion(x + quaternion.x, y + quaternion.y, z + quaternion.z, w
				+ quaternion.w);
	}

	public Quaternion normalize() {
		return scaled(1 / FloatMath.sqrt(dot(this)));
	}
}
