package com.osesm.randy.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.opengl.GLES20;
import android.util.Log;

import com.osesm.randy.framework.Simulation;
import com.osesm.randy.framework.math.Matrix4;

public class Mesh {

	private static final String TAG = "Randy";

	private boolean hasColor;
	private boolean hasTexCoords;
	private boolean hasNormals;
	private int vertexSize;
	private int[] tmpBuffer;
	private IntBuffer vertices;
	private ShortBuffer indices;
	private ShaderCompiler shaderCompiler;
	private int program;
	private Texture texture;

	Map<String, Integer> handles;

	private Matrix4 modelMatrix = new Matrix4();
	private Matrix4 mvpMatrix = new Matrix4();

	private Simulation simulation;

	private int drawStyle;

	protected static final int FLOAT_SIZE = 4;

	private static final int COLOR_SIZE = 4;
	private static final int TEXCOORDS_SIZE = 2;
	private static final int NORMAL_SIZE = 3;

	private static final int POSITION_SIZE = 3;

	public Mesh(Simulation simulation, int maxVertices, int maxIndices, boolean hasColor,
			boolean hasTexCoords, boolean hasNormals) {
		this.simulation = simulation;
		shaderCompiler = simulation.getShaderCompiler();

		handles = new HashMap<String, Integer>();

		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.hasNormals = hasNormals;
		vertexSize = (POSITION_SIZE + (hasColor ? COLOR_SIZE : 0)
				+ (hasTexCoords ? TEXCOORDS_SIZE : 0) + (hasNormals ? NORMAL_SIZE : 0))
				* FLOAT_SIZE;
		tmpBuffer = new int[maxVertices * vertexSize / FLOAT_SIZE];

		simulation.debug("vertex size = " + vertexSize);

		initializeVertices(maxVertices);
		initializeIndices(maxIndices);

		drawStyle = GLES20.GL_TRIANGLES;
	}

	private void initializeIndices(int maxIndices) {
		ByteBuffer buffer;
		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}
	}

	private void initializeVertices(int maxVertices) {
		ByteBuffer buffer;
		buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();
	}

	public void setVertices(float[] vertices, int offset, int length) {
		simulation.debug("set vertices to: " + Arrays.toString(vertices));

		this.vertices.clear();
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++)
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		this.vertices.put(tmpBuffer, 0, length);
		this.vertices.flip();
	}

	public void setIndices(short[] indices, int offset, int length) {
		simulation.debug("set indices to: " + Arrays.toString(indices));

		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}

	public void compile(String vertextShaderFileName, String fragmentShaderFileName) {
		program = shaderCompiler.compile(vertextShaderFileName, fragmentShaderFileName);
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public boolean hasTexture() {
		return texture != null;
	}

	public boolean hasColor() {
		return hasColor;
	}

	public boolean hasNormals() {
		return hasNormals;
	}

	public void setupVBO(Matrix4 viewProjectionMatrix) {
		registerAttribute("aPosition");

		if (hasColor) {
			registerAttribute("aSourceColor");
		}
		
		if (hasTexCoords) {
			registerAttribute("aTextureCoord");
			registerAttribute("sTexture");
		}

		if (hasNormals) {
			registerAttribute("aNormal");
			registerAttribute("aDiffuseMaterial");
			registerUniform("uNormalMatrix");
			registerUniform("uLightPosition");
			registerUniform("uAmbientMaterial");
			registerUniform("uSpecularMaterial");
			registerUniform("uShininess");
		}

		registerUniform("uMVPMatrix");
		mvpMatrix = viewProjectionMatrix.multiplyByMatrix(modelMatrix);		
	}

	public void registerAttribute(String name) {
		handles.put(name, GLES20.glGetAttribLocation(program, name));
	}

	public void registerUniform(String name) {
		handles.put(name, GLES20.glGetUniformLocation(program, name));
	}

	public Matrix4 getModelMatrix() {
		return modelMatrix;
	}

	public void setDrawStyle(int drawStyle) {
		this.drawStyle = drawStyle;
	}

	public int getDrawStyle() {
		return drawStyle;
	}

	public void draw() {
		GLES20.glUseProgram(program);

		if (hasTexture())
			getTexture().bind();

		vertices.position(getPositionOffset());
		GLES20.glVertexAttribPointer(handles.get("aPosition"), POSITION_SIZE, GLES20.GL_FLOAT, false,
				vertexSize, vertices);
		checkGlError("position vertex attrib pointer");
		GLES20.glEnableVertexAttribArray(handles.get("aPosition"));
		checkGlError("enable position vertex array");
		
		if (hasColor)
			enableColor();

		if (hasTexCoords)
			enableTexture();

		if (hasNormals)
			enableNormals();

		GLES20.glUniformMatrix4fv(handles.get("uMVPMatrix"), 1, false,
				mvpMatrix.getFloatArray(), 0);

		if (indices == null) {
			GLES20.glDrawArrays(drawStyle, 0, vertices.limit());
			checkGlError("drawArrays");
		} else {
			GLES20.glDrawElements(drawStyle, indices.limit(), GLES20.GL_UNSIGNED_SHORT,
					indices);
			checkGlError("drawElements");
		}
	}

	private void enableNormals() {
		vertices.position(6);
		GLES20.glVertexAttribPointer(handles.get("aNormal"), NORMAL_SIZE, GLES20.GL_FLOAT, false,
				vertexSize, vertices);
		checkGlError("normal vertex attrib pointer");
		GLES20.glEnableVertexAttribArray(handles.get("aNormal"));
		checkGlError("enable normal vertex array");

		GLES20.glVertexAttrib4f(handles.get("aDiffuseMaterial"), 0.75f, 0.75f, 0.75f, 1);
		checkGlError("set diffuse material");

		float normalMatrix[] = { 0, -5, -10 };
		GLES20.glUniformMatrix3fv(handles.get("uNormalMatrix"), 1, false, normalMatrix, 0);
		checkGlError("set diffuse material");

		float lightPosition[] = { 2f, 0.25f, 1 };
		GLES20.glUniform3f(handles.get("uLightPosition"), lightPosition[0],
				lightPosition[1], lightPosition[2]);
		checkGlError("set diffuse material");

		float ambientMaterial[] = { 0.1f, 0.1f, 0.1f };
		GLES20.glUniform3f(handles.get("uAmbientMaterial"), ambientMaterial[0],
				ambientMaterial[1], ambientMaterial[2]);
		checkGlError("set diffuse material");
		
		GLES20.glUniform1f(handles.get("uShininess"), 19);
		checkGlError("set diffuse material");

		float specularMaterial[] = { 0, 0.2f, 0 };
		GLES20.glUniform3fv(handles.get("uSpecularMaterial"), 1, specularMaterial, 0);
		checkGlError("set diffuse material");
	}

	private void enableColor() {
		vertices.position(getColorOffset());
		GLES20.glVertexAttribPointer(handles.get("aSourceColor"), COLOR_SIZE, GLES20.GL_FLOAT,
				false, vertexSize, vertices);
		checkGlError("color vertex attrib pointer");
		GLES20.glEnableVertexAttribArray(handles.get("aSourceColor"));
		checkGlError("enable color vertex array");
	}

	private void enableTexture() {
		vertices.position(4);
		GLES20.glVertexAttribPointer(handles.get("aTextureCoord"), TEXCOORDS_SIZE, GLES20.GL_FLOAT,
				false, vertexSize, vertices);
		checkGlError("texture vertex attrib pointer");
		GLES20.glEnableVertexAttribArray(handles.get("aTextureCoord"));
		checkGlError("enable texture vertex array");
	}

	private int getPositionOffset() {
		return 0;
	}

	private int getColorOffset() {
		return hasColor() ? POSITION_SIZE : 0;
	}

	private int getTextureOffset() {
		if (hasColor)
			return getColorOffset() + COLOR_SIZE;
		else if (hasTexCoords)
			return getColorOffset();
		else
			return 0;
	}

	private int getNormalOffset() {
		if (hasTexCoords)
			return getTextureOffset() + TEXCOORDS_SIZE;
		else if (hasNormals)
			return getTextureOffset();
		else
			return 0;
	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
