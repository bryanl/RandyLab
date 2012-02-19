package com.osesm.randy.lab.rts;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.math.Matrix4;
import com.osesm.randy.framework.math.Vector3;
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

	public class RTSCamera {

		private Matrix4 viewMatrix = new Matrix4();

		private Vector3 eye = new Vector3(0, 0.2f, -3f);
		private Vector3 look = new Vector3(0, 0, 0);
		private Vector3 up = new Vector3(0, 1, 0);

		public RTSCamera() {
			Matrix.setLookAtM(viewMatrix.asFloatArray(), 0, eye.x, eye.y, eye.z, look.x, look.y, look.z,
					up.x, up.y, up.z);
		}

		public Matrix4 getMatrix() {
			return viewMatrix;
		}

		public String dumpStats() {
			return "RTS Camera Stats: " + viewMatrix;
		}
	}

	public class RTSScene extends Scene {
		private Shape cube;

		private float projectionMatrix[] = new float[16];
		private float viewProjectionMatrix[] = new float[16];
		private RTSCamera camera;

		public RTSScene(Simulation simulation) {
			super(simulation);

			camera = new RTSCamera();
		}

		@Override
		public void changed(int width, int height) {
			GLES20.glViewport(0, 0, width, height);

			GLES20.glClearColor(0.75f, 0.75f, 0.75f, 1);

			final float ratio = (float) width / height;
			final float near = 1.0f;
			final float far = 100.0f;

			Matrix.perspectiveM(projectionMatrix, 0, 65, ratio, near, far);
		}

		@Override
		public void update(float deltaTime) {
			// TODO Auto-generated method stub
		}

		@Override
		public void present(float deltaTime) {
			Matrix.multiplyMM(viewProjectionMatrix, 0, camera.getMatrix().asFloatArray(), 0,
					projectionMatrix, 0);

			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

			simulation.debug(camera.dumpStats());

			cube.setViewProjectionMatrix(new Matrix4(viewProjectionMatrix));
			cube.update();

			cube.draw();
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
			Mesh mesh = createCubeMesh();
			cube = new Shape(mesh, "SimpleLighting.vert", "simple.frag");

			Matrix4 matrix = mesh.getModelMatrix();
			Matrix.scaleM(matrix.asFloatArray(), 0, .1f, .1f, 1);
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

			Mesh cube = new Mesh(simulation, vertices.length, indices.length, false,
					false, true);
			cube.setVertices(vertices, 0, vertices.length);
			cube.setIndices(indices, 0, indices.length);
			cube.setDrawStyle(GLES20.GL_TRIANGLES);

			return cube;
		}

	}

}
