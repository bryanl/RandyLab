package com.osesm.randy.framework;

public interface Surface {
	public int getVertexCount();

	public int getLineIndexCount();

	public int getTriangleIndexCount();

	public float[] generateVertices(int flags);

	public short[] generateLineIndices();

	public short[] generateTriangleIndices();
}
