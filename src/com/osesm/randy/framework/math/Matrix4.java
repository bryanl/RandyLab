package com.osesm.randy.framework.math;

import java.nio.FloatBuffer;

import android.opengl.Matrix;

public class Matrix4 {

	public static final int M00 = 0;
	public static final int M01 = 4;
	public static final int M02 = 8;
	public static final int M03 = 12;
	public static final int M10 = 1;
	public static final int M11 = 5;
	public static final int M12 = 9;
	public static final int M13 = 13;
	public static final int M20 = 2;
	public static final int M21 = 6;
	public static final int M22 = 10;
	public static final int M23 = 14;
	public static final int M30 = 3;
	public static final int M31 = 7;
	public static final int M32 = 11;
	public static final int M33 = 15;

	private final float values[] = new float[16];
	private final float tmp[] = new float[16];

	public Matrix4() {
		identity();
	}
	
	public Matrix4(float[] values) {
		this.set(values);
	}
	
	public Matrix4 identity() {
		this.values[0] = 1;
		this.values[1] = 0;
		this.values[2] = 0;
		this.values[3] = 0;

		this.values[4] = 0;
		this.values[5] = 1;
		this.values[6] = 0;
		this.values[7] = 0;

		this.values[8] = 0;
		this.values[9] = 0;
		this.values[10] = 1;
		this.values[11] = 0;

		this.values[12] = 0;
		this.values[13] = 0;
		this.values[14] = 0;
		this.values[15] = 1;
		
		return this;
	}


	public void set(float[] values) {
		this.values[M00] = values[M00];
		this.values[M10] = values[M10];
		this.values[M20] = values[M20];
		this.values[M30] = values[M30];
		this.values[M01] = values[M01];
		this.values[M11] = values[M11];
		this.values[M21] = values[M21];
		this.values[M31] = values[M31];
		this.values[M02] = values[M02];
		this.values[M12] = values[M12];
		this.values[M22] = values[M22];
		this.values[M32] = values[M32];
		this.values[M03] = values[M03];
		this.values[M13] = values[M13];
		this.values[M23] = values[M23];
		this.values[M33] = values[M33];
	}

	public Matrix4 rotate(float angle, float scaleX, float scaleY, float scaleZ) {
		Matrix.setRotateM(values, 0, angle, scaleX, scaleY, scaleZ);
		return this;
	}

	public Matrix4 multiplyByMatrix(Matrix4 otherMatrix) {
		Matrix.multiplyMM(tmp, 0, values, 0, otherMatrix.values(), 0);
		return new Matrix4(tmp);
	}

	public float[] values() {
		return values;
	}

	public FloatBuffer toFloatBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

}
