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
		
		float eyeAngle = 0;

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
//			eyeAngle += 0.01f;
//			if (eyeAngle >= 360) 
//				eyeAngle = 0;
//			camera.rotate(eyeAngle);
		}

		@Override
		public void present(float deltaTime) {

			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
			
			//			shape.setProjectionMatrix(camera.getViewProjectionMatrix());
//			shape.update();
//			shape.draw();

			// draw a billboard
			{

				
				shape2.setViewProjectionMatrix(camera.getViewProjectionMatrix());
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
			
			
			short lCoordIndices[] = { 0, 1, 2,
					                  3, 4, 5,
					                  6, 7, 8,
					                  9, 10, 11,
					                  12, 13, 14,
					                  15, 16, 17,
					                  18, 19, 20,
					                  21, 22, 23,
					                  24, 25, 26,
					                  27, 28, 29,
					                  };
			float lCoords[] = {-0.8f, -0.6f, 0, 1f, 0, 0, 1f,  // 0
					           1.0f, -0.6f, 0, 0, 1f, 0, 1f,   // 1
					           -0.8f, 0, 0, 0, 0, 1f, 1f,      // 2
					           
					           1.0f, -0.6f, 0, 1f, 0, 0, 1f,   // 3
					           1.0f, 0, 0, 0, 1.0f, 0, 1f,     // 4
					           -0.8f, 0, 0, 0, 0, 1f, 1f,      // 5
					           
					           0.4f, 0.6f, 0, 1f, 0, 0, 1f,    // 6
					           0.4f, 0, 0, 0, 1f, 0, 1f,       // 7
					           1.0f, 0, 0, 0, 0, 1f, 1f,       // 8
					           
					           0.4f, 0.6f, 0, 1, 0, 0, 1,      // 9
					           1.0f, 0.6f, 0, 0, 1, 0, 1,      // 10
					           1.0f, 0, 0, 0, 0, 1, 1,         // 11
					           
					           -1, 0.2f, -0.3f, 1, 0, 0, 1,    // 12
					           0.4f, 0, 0, 0, 1, 0, 1,         // 13
					           -0.8f, 0, 0, 0 , 0, 1, 1,       // 14
					           
					           -1, 0.2f, -0.3f, 1, 0, 0, 1,    // 15
					           0.2f, 0.2f, -0.3f, 0, 1, 0, 1,  // 16
					           0.4f, 0, 0, 0, 0, 1, 1,         // 17
					           
					           0.2f, 0.2f, -0.3f, 1, 0, 0, 1,  // 18
					           0.2f, 0.8f, -0.3f, 0, 1, 0, 1,  // 19
					           0.4f, 0, 0, 0, 0, 1, 1,         // 20
					           
					           0.2f, 0.8f, -0.3f, 1, 0, 0, 1,  // 21
					           0.4f, 0.6f, 0, 0, 1, 0, 1,      // 22
					           0.4f, 0, 0, 0, 0, 0, 1, 1,      // 23
					           
					           0.2f, 0.8f, -0.3f, 1, 0, 0, 1,  // 24
					           0.4f, 0.6f, 0, 0, 1, 0, 1,      // 25
					           1.0f, 0.6f, 0, 0, 0, 1, 1,      // 26
					           
					           1.0f, 0.6f, 0, 1, 0, 0, 1,
					           0.8f, 0.8f, -0, 0, 1, 0, 1,
					           0.2f, 0.8f, -0.3f, 0, 0, 1, 1,
					           };   
			
			

			Mesh mesh = new Mesh(simulation, 21, 6, true, false, false);
			mesh.setVertices(triangleCoords, 0, triangleCoords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			shape = new Shape(mesh, "simple.vert", "simple.frag");

			texture = new Texture(simulation, R.raw.basic_texture);
			mesh = new Mesh(simulation, 15, 6, false, true, false);
			mesh.setVertices(triangle1Coords, 0, triangle1Coords.length);
			mesh.setIndices(triangleIndices, 0, triangleIndices.length);
			mesh.setTexture(texture);
	
			simulation.debug("translate: " + Arrays.toString(mesh.getModelMatrix().getFloatArray()));
			float[] values = mesh.getModelMatrix().getFloatArray();
			Matrix.translateM(values, 0, 0, 1, 0f);
			simulation.debug("translate: " + Arrays.toString(values));
			
			// shape2 = new Shape(mesh, "billboard.vert", "billboard.frag");
//			shape2 = new Shape(mesh, "texture.vert", "texture.frag");
			
			mesh = new Mesh(simulation, lCoords.length, lCoordIndices.length, true, false, false);
			mesh.setVertices(lCoords, 0, lCoords.length);
			mesh.setIndices(lCoordIndices, 0, lCoordIndices.length);
			shape2 = new Shape(mesh, "simple.vert", "simple.frag");
			

		}

	}

}
