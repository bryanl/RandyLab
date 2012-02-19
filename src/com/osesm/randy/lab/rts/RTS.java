package com.osesm.randy.lab.rts;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.gl.Texture;
import com.osesm.randy.framework.math.Matrix4;
import com.osesm.randy.lab.R;
import com.osesm.randy.lab.Shape;

public class RTS extends Simulation {

	@Override
	public Scene getStartScene() {
		return new RTSScene(this);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		boolean status = super.onTouch(view, event);

		debug("x: " + event.getX() + " y: " + event.getY());
		return status;
	}

	public class RTSScene extends Scene {

		private Shape cube;
		private int counter;

		private float viewMatrix[] = new float[16];
		private float projectionMatrix[] = new float[16];
		private float mvpMatrix[] = new float[16];
		private float viewProjectionMatrix[] = new float[16];

		public RTSScene(Simulation simulation) {
			super(simulation);
			counter = 0;

			final float eyeX = 0.0f;
			final float eyeY = 0.0f;
			final float eyeZ = 1.5f;

			final float lookX = 0.0f;
			final float lookY = 0.0f;
			final float lookZ = -5.0f;

			final float upX = 0.0f;
			final float upY = 1.0f;
			final float upZ = 0.0f;

			Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX,
					upY, upZ);
		}

		@Override
		public void changed(int width, int height) {
			GLES20.glViewport(0, 0, width, height);

			GLES20.glClearColor(0.75f, 0.75f, 0.75f, 1);

			final float ratio = (float) width / height;
			final float left = -ratio;
			final float right = ratio;
			final float bottom = -1.0f;
			final float top = 1.0f;
			final float near = 1.0f;
			final float far = 10.0f;

			Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
		}

		@Override
		public void update(float deltaTime) {
			// TODO Auto-generated method stub

		}

		@Override
		public void present(float deltaTime) {

			// camera stuff
			Matrix.setIdentityM(projectionMatrix, 0);
			Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, viewMatrix, 0);
			Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

			Matrix.multiplyMM(viewProjectionMatrix, 0, viewMatrix, 0, projectionMatrix, 0);

			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

			cube.setViewProjectionMatrix(new Matrix4(viewProjectionMatrix));
			cube.update();
			cube.draw();

			counter++;
			if (counter >= 0) {
				counter = 0;
			}
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			initCube();
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		private void initCube() {
			// Texture texture = new Texture(simulation, R.raw.basic_texture);
			//
			// float squareVertices[] = {
			// // FRONT
			// -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,
			// 0.5f, 0.5f,
			// // BACK
			// -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f,
			// 0.5f, 0.5f, -0.5f,
			// // LEFT
			// -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f,
			// -0.5f, 0.5f, -0.5f,
			// // RIGHT
			// 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
			// 0.5f, 0.5f,
			// // TOP
			// -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
			// 0.5f, -0.5f,
			// // BOTTOM
			// -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
			// -0.5f, -0.5f, };
			//
			// Mesh mesh = new Mesh(simulation, squareVertices.length, 0, false,
			// false,
			// false);
			// mesh.setVertices(squareVertices, 0, squareVertices.length);
			// mesh.setDrawStyle(GLES20.GL_TRIANGLE_STRIP);
			// cube = new Shape(mesh, "cube.vert", "cube.frag");

			Mesh mesh = createCubeMesh();
			cube = new Shape(mesh, "SimpleLighting.vert", "simple.frag");

			Matrix4 matrix = mesh.getModelMatrix();
			Matrix.scaleM(matrix.getFloatArray(), 0, .1f, .1f, 1);
		}

		private Mesh createCubeMesh() {
			float[] vertices = {
					// front
			-0.5f, -0.5f, 0.5f, 0, 0, 1, 0.5f, -0.5f, 0.5f, 0, 0, 1, 0.5f, 0.5f, 0.5f, 0, 0, 1, -0.5f, 0.5f, 0.5f, 0, 0, 1,

					// right
			0.5f, -0.5f, 0.5f, 1, 0, 0, 0.5f, -0.5f, -0.5f, 1, 0, 0, 0.5f, 0.5f, -0.5f, 1, 0, 0, 0.5f, 0.5f, 0.5f, 1, 0, 0,

					// back
			0.5f, -0.5f, -0.5f, 0, 0, -1, -0.5f, -0.5f, -0.5f, 0, 0, -1, -0.5f, 0.5f, -0.5f, 0, 0, -1, 0.5f, 0.5f, -0.5f, 0, 0, -1,

					// left
			-0.5f, -0.5f, -0.5f, -1, 0, 0, -0.5f, -0.5f, 0.5f, -1, 0, 0, -0.5f, 0.5f, 0.5f, -1, 0, 0, -0.5f, 0.5f, -0.5f, -1, 0, 0,

					// top
			-0.5f, 0.5f, 0.5f, 0, 1, 0, 0.5f, 0.5f, 0.5f, 0, 1, 0, 0.5f, 0.5f, -0.5f, 0, 1, 0, -0.5f, 0.5f, -0.5f, 0, 1, 0,

					// bottom
			-0.5f, -0.5f, -0.5f, 0, -1, 0, 0.5f, -0.5f, -0.5f, 0, -1, 0, 0.5f, -0.5f, 0.5f, 0, -1, 0, -0.5f, -0.5f, 0.5f, 0, -1, 0 };

			short[] indices = { 0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4, 8, 9, 10, 10, 11, 8, 12, 13, 14, 14, 15, 12, 16, 17, 18, 18, 19, 16, 20, 21, 22, 22, 23, 20, 24, 25, 26, 26, 27, 24 };

			Texture texture = new Texture(simulation, R.raw.basic_texture);

			Mesh cube = new Mesh(simulation, vertices.length, indices.length, false,
					false, true);
			cube.setVertices(vertices, 0, vertices.length);
			cube.setIndices(indices, 0, indices.length);
			cube.setDrawStyle(GLES20.GL_TRIANGLES);
			cube.setTexture(texture);

			return cube;
		}

	}

}
