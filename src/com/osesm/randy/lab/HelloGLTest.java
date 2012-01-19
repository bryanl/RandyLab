package com.osesm.randy.lab;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.MotionEvent;

public class HelloGLTest extends Activity {

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
			mRenderer = new HelloGLRenderer();
			setRenderer(mRenderer);
			
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();
			
			switch(event.getAction()) {
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
				requestRender();
			}
			
			mPreviousX = x;
			mPreviousY = y;
			return true;
		}

	}
	
	public class HelloGLRenderer implements Renderer {

		private FloatBuffer triangleVB;
		private final String vertexShaderCode = 
			"uniform mat4 uMVPMatrix;  \n" +
			"attribute vec4 vPosition; \n" +
			"void main(){              \n" +
			"  gl_Position = uMVPMatrix * vPosition; \n" +
			"}                         \n";
			
		private final String fragmentShaderCode = 
		    "precision mediump float;  \n" +
		    "void main(){              \n" +
		    "  gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" +
		    "}                         \n";
		
		private int mProgram;
		private int maPositionHandle;
		
		private int muMVPMatrixHandle;
		private float[] mMVPMatrix = new float[16];
		private float[] mMMatrix = new float[16];
		private float[] mVMatrix = new float[16];
		private float[] mProjMatrix = new float[16];
		public float mAngle;

		@Override
		public void onDrawFrame(GL10 gl) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);		
			
			GLES20.glUseProgram(mProgram);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 12, triangleVB);
			GLES20.glEnableVertexAttribArray(maPositionHandle);
		
//			long time = SystemClock.uptimeMillis() % 4000L;
//			float angle = 0.090f * ((int) time);
			Matrix.setRotateM(mMMatrix, 0, mAngle, 0, 0, 1.0f);
			Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
			Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		        
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);        
			
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
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
			
			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
			
			mProgram = GLES20.glCreateProgram();
			GLES20.glAttachShader(mProgram, vertexShader);		
			GLES20.glAttachShader(mProgram, fragmentShader);
			GLES20.glLinkProgram(mProgram);
			
			maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");		
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	        
	        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 2.0f, 0.0f);
		}

		private void initShapes() {
			float triangleCoords[] = { -0.5f, -0.25f, 0, 0.5f, -0.25f, 0, 0.0f, 0.559016994f, 0 };
			
			ByteBuffer vbb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			triangleVB = vbb.asFloatBuffer();
			triangleVB.put(triangleCoords);
			triangleVB.position(0);
		}
		
		private int loadShader(int type, String shaderCoder) {
			int shader = GLES20.glCreateShader(type);
			GLES20.glShaderSource(shader, shaderCoder);
			GLES20.glCompileShader(shader);
			
			return shader;
		}

	}

}
