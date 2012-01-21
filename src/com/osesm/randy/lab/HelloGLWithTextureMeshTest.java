package com.osesm.randy.lab;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.gl.ShaderCompiler;
import com.osesm.randy.framework.gl.Texture;

public class HelloGLWithTextureMeshTest extends Activity {

	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLView = new HelloGLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	class HelloGLSurfaceView extends GLSurfaceView {
		private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
		private HelloGLRenderer mRenderer;
		private float mPreviousX;
		private float mPreviousY;

		public HelloGLSurfaceView(Context context) {
			super(context);

			setEGLContextClientVersion(2);

			mRenderer = new HelloGLRenderer(context);
			setRenderer(mRenderer);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;

				if (y > getHeight() / 2) {
					dx = dx * -1;
				}

				if (x < getWidth() / 2) {
					dy = dy * -1;
				}

				mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
			}

			mPreviousX = x;
			mPreviousY = y;
			return true;
		}

	}

	public class HelloGLRenderer implements Renderer {
		private float[] mVMatrix = new float[16];
		private float[] mProjMatrix = new float[16];
		private float[] viewProjectionMantrix = new float[16];

		public float mAngle;
		ShaderCompiler shaderCompiler;
		private Mesh meshes[];
		private Context context;

		public HelloGLRenderer(Context context) {
			this.context = context;
			shaderCompiler = new ShaderCompiler(getAssets());
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

			Matrix.multiplyMM(viewProjectionMantrix, 0, mProjMatrix, 0, mVMatrix, 0);

			long time = SystemClock.uptimeMillis() % 4000L;
			mAngle = 0.090f * ((int) time);

			for (int i = 0; i < meshes.length; i++) {
				((TriangleMesh) meshes[i]).setAngle(i == 0 ? mAngle : -mAngle);
				meshes[i].prepare(viewProjectionMantrix);
				meshes[i].draw();
			}
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			float ratio = (float) width / height;
			Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			initShapes();

			Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 2.0f, 0.0f);
		}

		public ShaderCompiler getShaderCompiler() {
			return shaderCompiler;
		}

		private void initShapes() {
			short triangleIndices[] = { 0, 1, 2 };

			float triangle1Coords[] = { -0.5f, -0.25f, 0.5f, 0f, 0f, 0.5f, -0.25f, 0.5f, 1f, 0f, 0.0f, 0.559016994f, 0.5f, 0.5f, 1f };

			float triangle2Coords[] = { -0.7f, -0.25f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.3f, -0.25f, 0.25f, 0.0f, 0.0f, 1.0f, 1.0f, -0.2f, 0.559016994f, 0.2f, 1.0f, 0.0f, 0.0f, 1.0f };

			Texture texture = new Texture(context, R.raw.basic_texture);

			meshes = new TriangleMesh[2];

			meshes[1] = new TriangleMesh(this, 15, 6, false, true, false);
			meshes[1].setVertices(triangle1Coords, 0, triangle1Coords.length);
			meshes[1].setIndices(triangleIndices, 0, triangleIndices.length);
			meshes[1].setTexture(texture);

			meshes[0] = new TriangleMesh(this, 21, 6, true, false, false);
			meshes[0].setVertices(triangle2Coords, 0, triangle2Coords.length);
			meshes[0].setIndices(triangleIndices, 0, triangleIndices.length);
		}

	}

	class TriangleMesh extends Mesh {
		private static final int VERTICES_OFFSET = 0;
		private static final int UV_OFFSET = 3;
		private static final int COLOR_OFFSET = 3;

		private static final String TAG = "Randy";

		private int maPositionHandle;
		private int maTextureHandle;

		private int muMVPMatrixHandle;
		private float[] mMMatrix = new float[16];
		private float[] mMVPMatrix = new float[16];

		private float mAngle;
		private HelloGLRenderer renderer;
		private int maColorHandle;
		private int msTextureHandle;

		public TriangleMesh(HelloGLRenderer renderer, int maxVertices, int maxIndices,
				boolean hasColor, boolean hasTexCoords, boolean hasNormals) {
			super(maxVertices, maxIndices, hasColor, hasTexCoords, hasNormals);

			this.renderer = renderer;
			setShaderCompiler(renderer.getShaderCompiler());

			if (hasTexture()) {
				int program = compile("hellogl_with_mesh_texture_vertex_shader.glsl",
						"hellogl_with_mesh_texture_fragment_shader.glsl");
				setProgram(program);
			} else {
				int program = compile("hellogl_with_mesh_vertex_shader.glsl",
						"hellogl_with_mesh_fragment_shader.glsl");
				setProgram(program);
			}
		}

		@Override
		public void prepare(float[] viewProjectionMatrix) {
			maPositionHandle = GLES20.glGetAttribLocation(getProgram(), "aPosition");

			if (hasTexture()) {
				maTextureHandle = GLES20.glGetAttribLocation(getProgram(),
						"aTextureCoord");
				msTextureHandle = GLES20.glGetAttribLocation(getProgram(), "sTexture");
			} else {
				maColorHandle = GLES20.glGetAttribLocation(getProgram(), "SourceColor");
			}

			muMVPMatrixHandle = GLES20.glGetUniformLocation(getProgram(), "uMVPMatrix");

			Matrix.setRotateM(mMMatrix, 0, mAngle, 0, 0, 1.0f);
			Matrix.multiplyMM(mMVPMatrix, 0, viewProjectionMatrix, 0, mMMatrix, 0);
		}

		@Override
		public void draw() {
			GLES20.glUseProgram(getProgram());

			if (hasTexture()) {
				getTexture().bind();
			}

			getVertices().position(VERTICES_OFFSET);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
					getVertexSize(), getVertices());
			checkGlError("position vertex attrib pointer");
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			checkGlError("enable position vertex array");

			if (hasTexture()) {
				getVertices().position(UV_OFFSET);
				GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,
						getVertexSize(), getVertices());
				checkGlError("texture vertex attrib pointer");
				GLES20.glEnableVertexAttribArray(maTextureHandle);
				checkGlError("enable texture vertex array");
			} else {
				getVertices().position(COLOR_OFFSET);
				GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,
						getVertexSize(), getVertices());
				checkGlError("color vertex attrib pointer");
				GLES20.glEnableVertexAttribArray(maColorHandle);
				checkGlError("enable color vertex array");
			}

			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
					getIndices());
		}

		public void setAngle(float angle) {
			mAngle = angle;
		}

		public Context getContext() {
			return renderer.context;
		}

		private void checkGlError(String op) {
			int error;
			while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
				Log.e(TAG, op + ": glError " + error);
				throw new RuntimeException(op + ": glError " + error);
			}
		}
	}
}
