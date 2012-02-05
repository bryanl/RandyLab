package com.osesm.randy.lab;

import com.osesm.randy.framework.Scene;
import com.osesm.randy.framework.Simulation;

public class VBODemo extends Simulation {

	@Override
	public Scene getStartScene() {
		return new VBOScene(this);
	}
	
	class VBOScene extends Scene {

		public VBOScene(Simulation simulation) {
			super(simulation);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void changed(int width, int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(float deltaTime) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void present(float deltaTime) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
