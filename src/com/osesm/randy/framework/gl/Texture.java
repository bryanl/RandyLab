package com.osesm.randy.framework.gl;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture {
	private static final String TAG = "Texture";
	private int rawResource;
	private Context context;
	private int textureID;

	public Texture(Context context, int rawResource) {
		this.context = context;
		this.rawResource = rawResource;
		load();
	}

	private void load() {
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		textureID = textures[0];
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
		checkGlError("bind texture");

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
				GLES20.GL_NEAREST);
		checkGlError("set min filter");
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR);
		checkGlError("set mag filter");

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_REPEAT);
		checkGlError("set wrap s");
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_REPEAT);
		checkGlError("set wrap t");
		
		InputStream is = context.getResources().openRawResource(rawResource);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Ignore.
			}
		}

		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();		
	}
	
	public void bind() {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		checkGlError("activate texture");
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
		checkGlError("bind texture");
	}
	
	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
