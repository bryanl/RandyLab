package com.osesm.randy.lab;

import com.osesm.randy.framework.WorldObject;

public class SimpleShape extends WorldObject {

	private short indices[];
	private float coords[];
		
	public SimpleShape(short[] indices, float[] coords) {
		super();
		this.indices = indices;
		this.coords = coords;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

}
