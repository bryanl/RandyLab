package com.osesm.randy.lab;

import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.gl.Texture;
import com.osesm.randy.lab.R;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

public class SimulationTest extends Simulation {

	@Override
	public Scene getStartScene() {
		return new SimulationScene(this);
	}

	public class SimulationScene extends Scene {

		private float[] mVMatrix = new float[16];
		private float[] mProjMatrix = new float[16];
		private float[] viewProjectionMantrix = new float[16];
		private TriangleMesh[] meshes;

		public SimulationScene(Simulation simulation) {
			super(simulation);
		}

		@Override
		public void update(float deltaTime) {
			// TODO Auto-generated method stub
		}

		@Override
		public void present(float deltaTime) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

			Matrix.multiplyMM(viewProjectionMantrix, 0, mProjMatrix, 0, mVMatrix, 0);

			long time = SystemClock.uptimeMillis() % 4000L;
			float angle = 0.090f * ((int) time);

			for (int i = 0; i < meshes.length; i++) {
				((TriangleMesh) meshes[i]).setAngle(i == 0 ? angle : -angle);
				meshes[i].prepare(viewProjectionMantrix);
				meshes[i].draw();
			}
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			initShapes();

			Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 2.0f, 0.0f);
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void changed(int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			float ratio = (float) width / height;
			Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		}

		private void initShapes() {
			short triangleIndices[] = { 0, 1, 2 };

			float triangle1Coords[] = { -0.5f, -0.25f, 0.5f, 0f, 0f, 0.5f, -0.25f, 0.5f, 1f, 0f, 0.0f, 0.559016994f, 0.5f, 0.5f, 1f };

			float triangle2Coords[] = { -0.7f, -0.25f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.3f, -0.25f, 0.25f, 0.0f, 0.0f, 1.0f, 1.0f, -0.2f, 0.559016994f, 0.2f, 1.0f, 0.0f, 0.0f, 1.0f };

			Texture texture = new Texture(simulation, R.raw.basic_texture);

			meshes = new TriangleMesh[2];

			meshes[1] = new TriangleMesh(simulation, 15, 6, false, true, false);
			meshes[1].setVertices(triangle1Coords, 0, triangle1Coords.length);
			meshes[1].setIndices(triangleIndices, 0, triangleIndices.length);
			meshes[1].setTexture(texture);

			meshes[0] = new TriangleMesh(simulation, 21, 6, true, false, false);
			meshes[0].setVertices(triangle2Coords, 0, triangle2Coords.length);
			meshes[0].setIndices(triangleIndices, 0, triangleIndices.length);
		}

	}

}
