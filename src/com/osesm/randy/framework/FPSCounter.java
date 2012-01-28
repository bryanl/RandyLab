package com.osesm.randy.framework;

public class FPSCounter {
	long startTime = System.nanoTime();
	int frames = 0;
	private Simulation simulation;

	public FPSCounter(Simulation simulation) {
		this.simulation = simulation;
	}
	
	public void logFrame() {
		frames++;
		if (System.nanoTime() - startTime >= 1000000000) {
			simulation.setStatus("frames per second: " + frames);
			frames = 0;
			startTime = System.nanoTime();
		}
	}
}