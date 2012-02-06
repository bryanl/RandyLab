package com.osesm.randy.lab;

import java.util.Arrays;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.osesm.randy.framework.FPSCounter;
import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.WorldObject;
import com.osesm.randy.framework.gl.Camera;
import com.osesm.randy.framework.gl.Mesh;
import com.osesm.randy.framework.gl.Texture;
import com.osesm.randy.framework.math.Matrix4;

public class VBODemo extends Simulation {

	@Override
	public Scene getStartScene() {
		return new VBOScene(this);
	}

	class VBOScene extends Scene {

		private FPSCounter fpscounter;
		private WorldObject shape;
		private Camera camera;
		private Texture texture;
		private WorldObject shape2;

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

			// draw a billboard
			{

				shape2.setProjectionMatrix(camera.getViewProjectionMatrix());
				shape2.update();
				shape2.draw();

				// texture.bind();
				//
				// // set the up direction
				// float up_x = 0, up_y = 1, up_z = 0;
				//
				// // get the vector from the model view
				// float right_x = modelview[0];
				// float right_y = modelview[4];
				// float right_z = modelview[8];
				//
				// right_y = 0;
				// float right_len = FloatMath.sqrt(right_x * right_x + right_z
				// * right_z);
				// right_x /= right_len;
				// right_z /= right_len;
				//
				// // shade
				//
				// // show the texture
				// GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 5
				// * 4,
				// p_bboard_vertices_buff.position(2));
				// GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 5
				// * 4,
				// p_bboard_vertices_buff.position(0));
				// GLES20.glEnableVertexAttribArray(0);
				// GLES20.glEnableVertexAttribArray(1);
				// GLES20.glDrawElements(GLES20.GL_TRIANGLES, billboard_count *
				// 6,
				// GLES20.GL_UNSIGNED_INT, p_bboard_indices_buff);

			}

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

			float triangle1Coords[] = { -0.5f, -0.25f, 0.5f, 0f, 0f, 0.5f, -0.25f, 0.5f, 1f, 0f, 0.0f, 0.559016994f, 0.5f, 0.5f, 1f };

			Mesh mesh = new Mesh(simulation, 21, 6, true, false, false);
			mesh.setVertices(triangleCoords, 0, triangleCoords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			shape = new Shape(mesh, "simple.vert", "simple.frag");

			texture = new Texture(simulation, R.raw.basic_texture);
			mesh = new Mesh(simulation, 15, 6, false, true, false);
			mesh.setVertices(triangle1Coords, 0, triangle1Coords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			mesh.setTexture(texture);
	
			simulation.debug("translate: " + Arrays.toString(mesh.modelMatrix().values()));
			float[] values = mesh.modelMatrix().values();
			Matrix.translateM(values, 0, 0, 1, 0f);
			simulation.debug("translate: " + Arrays.toString(values));
			
			// shape2 = new Shape(mesh, "billboard.vert", "billboard.frag");
			shape2 = new Shape(mesh, "texture.vert", "texture.frag");

		}

	}

}
