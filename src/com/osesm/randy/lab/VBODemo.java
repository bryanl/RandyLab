package com.osesm.randy.lab;

import android.opengl.GLES20;

import com.osesm.randy.framework.FPSCounter;
import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.gl.Camera;
import com.osesm.randy.framework.gl.Mesh;

public class VBODemo extends Simulation {

	@Override
	public Scene getStartScene() {
		return new VBOScene(this);
	}

	class VBOScene extends Scene {

		private FPSCounter fpscounter;
		private Shape shape;
		private Camera camera;

		public VBOScene(Simulation simulation) {
			super(simulation);
			fpscounter = new FPSCounter(simulation);
		}

		@Override
		public void changed(int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			camera = new Camera(width, height);
		}

		@Override
		public void update(float deltaTime) {
			// TODO Auto-generated method stub

		}

		@Override
		public void present(float deltaTime) {
			shape.setProjectionMatrix(camera.getViewProjectionMatrix());
			shape.update();
			shape.draw();

			fpscounter.logFrame();
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			initTriangle();
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		private void initTriangle() {
			short triangleIndices[] = { 0, 1, 2 };
			float triangleCoords[] = { -0.7f, -0.25f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.3f, -0.25f, 0.25f, 0.0f, 0.0f, 1.0f, 1.0f, -0.2f, 0.559016994f, 0.2f, 1.0f, 0.0f, 0.0f, 1.0f };

			Mesh mesh = new Mesh(simulation, 21, 6, true, false, false);
			mesh.setVertices(triangleCoords, 0, triangleCoords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			shape = new Shape(mesh, "simple.vert", "simple.frag");

		}

	}

}
