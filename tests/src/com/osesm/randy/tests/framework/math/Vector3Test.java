package com.osesm.randy.tests.framework.math;

import junit.framework.TestCase;

import com.osesm.randy.framework.math.Vector3;

public class Vector3Test extends TestCase {

	public Vector3Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddVector1() {
		Vector3 p, q;

		p = new Vector3(2, 2, 2);
		q = new Vector3(3, 3, 3);

		String left = p.add(q).toString();

		p = new Vector3(2, 2, 2);
		q = new Vector3(3, 3, 3);

		String right = q.add(p).toString();

		assertEquals(left, right);
	}

	public void testAddVector2() {
		Vector3 p, q, r;

		p = new Vector3(2, 2, 2);
		q = new Vector3(3, 3, 3);
		r = new Vector3(4, 4, 4);

		String left = r.add(p.add(q)).toString(); 
			
		p = new Vector3(2, 2, 2);
		q = new Vector3(3, 3, 3);
		r = new Vector3(4, 4, 4);

		String right = p.add(q.add(r)).toString(); 
		
		assertEquals(left, right);
	}
	
	public void testMultiplyVector1() {
		float a, b;
		Vector3 p;
		
		a = 5;
		b = 6;		
		p = new Vector3(2,2,2);			
		
		Vector3 left = p.mul(a * b);
		
		a = 5;
		b = 6;		
		p = new Vector3(2,2,2);
		
		Vector3 right = p.mul(a).mul(b);

		assertEquals(left, right);			
	}

	public void testVectorString() {
		Vector3 p = new Vector3(2, 2, 2);
		assertEquals("[2.0, 2.0, 2.0]", p.toString());
	}
}
