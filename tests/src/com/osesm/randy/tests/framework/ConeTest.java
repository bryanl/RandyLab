package com.osesm.randy.tests.framework;

import android.util.Log;

import com.osesm.randy.framework.Cone;

import junit.framework.TestCase;

public class ConeTest extends TestCase {

	public ConeTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testConeAssumptions() {
		Cone cone = new Cone(5, 5);
		float[] vertices = cone.generateVertices(0);
		float[] expected = {1.0f};
		
		int vertexCount = cone.getVertexCount();
	
		assertEquals(7*vertexCount, vertices.length);
	}

}
