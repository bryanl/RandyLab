package com.osesm.randy.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Mesh {

	private boolean hasColor;
	private boolean hasTexCoords;
	private boolean hasNormals;
	private int vertexSize;
	private int[] tmpBuffer;
	private IntBuffer vertices;
	private ShortBuffer indices;

	public Mesh(int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords,
			boolean hasNormals) {
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.hasNormals = hasNormals;
		vertexSize = (3 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0) + (hasNormals ? 3
				: 0)) * 4;
		tmpBuffer = new int[maxVertices * vertexSize / 4];

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}
	}

	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++)
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		this.vertices.put(tmpBuffer, 0, length);
		this.vertices.flip();
	}
	
	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}
	
	public void setVertexShader() {
		
	}
	
	public void setFragmentShader() {
		
	}
	
	public void prepare() {
		
	}
	
	public void draw() {
		
	}
	

	
	
}
