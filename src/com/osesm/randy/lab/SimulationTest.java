package com.osesm.randy.lab;

import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.gl.ObjLoader;
import com.osesm.randy.framework.gl.Texture;

public class SimulationTest extends Simulation {

	@Override
	public Scene getStartScene() {
		return new SimulationScene(this);
	}

	public class SimulationScene extends Scene {

		private float[] viewMatrix = new float[16];
		private float[] mProjMatrix = new float[16];
		private float[] viewProjectionMatrix = new float[16];
		
		List<Shape> shapes;

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
			Matrix.multiplyMM(viewProjectionMatrix, 0, mProjMatrix, 0, viewMatrix, 0);

			long time = SystemClock.uptimeMillis() % 4000L;
			float angle = 0.090f * ((int) time);

			for (Shape shape : shapes) {
				shape.setAngle(shape.getId() % 2 == 0 ? angle : -angle);
				shape.setProjectionMatrix(viewProjectionMatrix);
				shape.update();
				shape.draw();
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

			Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 2.0f, 0.0f);
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

			shapes = new ArrayList<Shape>();
			Mesh mesh;
			
			mesh = ObjLoader.load(simulation, "sphere.obj");
			shapes.add(new Shape(mesh));
 				
			mesh = new Mesh(simulation, 15, 6, false, true, false);
			mesh.setVertices(triangle1Coords, 0, triangle1Coords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			mesh.setTexture(texture);
			shapes.add(new Shape(mesh));
		
			mesh = new Mesh(simulation, 21, 6, true, false, false);
			mesh.setVertices(triangle2Coords, 0, triangle2Coords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			shapes.add(new Shape(mesh));
			
			mesh = ObjLoader.load(simulation, "sphere.obj");
			shapes.add(new Shape(mesh));
		}		
	}

}
