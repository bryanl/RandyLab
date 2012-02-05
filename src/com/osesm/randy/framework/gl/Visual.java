package com.osesm.randy.framework.gl;

import com.osesm.randy.framework.math.Quaternion;
import com.osesm.randy.framework.math.Vector2;
import com.osesm.randy.framework.math.Vector3;

public class Visual {
	Vector3 color;
	Vector2 lowerLeft;
	Vector2 viewportSize;
	Quaternion orientation;

	public Visual() {
		this.lowerLeft = new Vector2(0, 0);
	}

	public void changed(int rootWidth, int rootHeight) {
		viewportSize.set(rootWidth, rootHeight);
	}

	public Vector2 getLowerLeft() {
		return lowerLeft;
	}

	public void setLowerLeft(Vector2 lowerLeft) {
		this.lowerLeft = lowerLeft;
	}

	public Vector2 getViewportSize() {
		return viewportSize;
	}

	public void setViewportSize(Vector2 viewportSize) {
		this.viewportSize = viewportSize;
	}

	public Vector3 getColor() {
		return color;
	}

	public void setColor(Vector3 color) {
		this.color = color;
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}
	
	

}
