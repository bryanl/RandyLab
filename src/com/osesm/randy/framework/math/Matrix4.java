package com.osesm.randy.framework.math;

import java.nio.FloatBuffer;
import java.util.Arrays;

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

	private final float _floatArray[] = new float[16];
	private float tmp[] = new float[16];
	
	public Matrix4() {
		identity();
	}
	
	public Matrix4(float[] values) {
		this.set(values);
	}
	
	public Matrix4 identity() {
		Matrix.setIdentityM(_floatArray, 0);		
		return this;
	}

	public void set(float[] floatArray) {
		this._floatArray[M00] = floatArray[M00];
		this._floatArray[M10] = floatArray[M10];
		this._floatArray[M20] = floatArray[M20];
		this._floatArray[M30] = floatArray[M30];
		this._floatArray[M01] = floatArray[M01];
		this._floatArray[M11] = floatArray[M11];
		this._floatArray[M21] = floatArray[M21];
		this._floatArray[M31] = floatArray[M31];
		this._floatArray[M02] = floatArray[M02];
		this._floatArray[M12] = floatArray[M12];
		this._floatArray[M22] = floatArray[M22];
		this._floatArray[M32] = floatArray[M32];
		this._floatArray[M03] = floatArray[M03];
		this._floatArray[M13] = floatArray[M13];
		this._floatArray[M23] = floatArray[M23];
		this._floatArray[M33] = floatArray[M33];
	}

	public Matrix4 rotate(float angle, float scaleX, float scaleY, float scaleZ) {
		Matrix.setRotateM(_floatArray, 0, angle, scaleX, scaleY, scaleZ);
		return this;
	}

	public Matrix4 multiplyByMatrix(Matrix4 otherMatrix) {
		Matrix.multiplyMM(tmp, 0, _floatArray, 0, otherMatrix.asFloatArray(), 0);
		return new Matrix4(tmp);
	}

	public float[] asFloatArray() {
		return _floatArray;
	}

	public FloatBuffer toFloatBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return Arrays.toString(asFloatArray());
	}
	
}
