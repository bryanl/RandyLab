package com.osesm.randy.framework;

public abstract class Scene {
	protected Simulation simulation;

	public Scene(Simulation simulation) {
		this.simulation = simulation;
	}

	public abstract void changed(int width, int height);
	
	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();
}
