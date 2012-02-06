package com.osesm.randy.framework;

import java.util.ArrayList;
import java.util.List;

import com.osesm.randy.framework.math.Vector2;
import com.osesm.randy.framework.math.Vector3;

public abstract class ParametricSurface implements Surface {

	private Vector2 divisions;
	private Vector2 upperBound;
	private Vector2 textureCount;
	private Vector2 slices;

	public final static int VERTEX_NORMALS = 1;
	public final static int VERTEX_TEX_COORDS = 2;

	private List<Float> colorList;
	
	public ParametricSurface() {
		colorList = new ArrayList<Float>();
		colorList.add(1.0f);
		colorList.add(0.0f);
		colorList.add(0.0f);
		colorList.add(1.0f);		
	}
	
	@Override
	public int getVertexCount() {
		return (int) (divisions.x * divisions.y);
	}

	@Override
	public int getLineIndexCount() {
		return (int) (4 * slices.x * slices.x);
	}

	@Override
	public int getTriangleIndexCount() {
		return (int) (6 * slices.x * slices.y);
	}
	
	@Override
	public float[] generateVertices(int flags) {
		List<Float> list = new ArrayList<Float>();
		float[] tmp;
		
		for (int j = 0; j < divisions.y; j++) {
			for (int i = 0; i < divisions.x; i++) {

				// Compute position
				Vector2 domain = computeDomain(i, j);
				Vector3 range = evaluate(domain);

				tmp = range.toFloat();
				for (int x = 0; x < tmp.length; x++) {
					list.add(tmp[x]);
				}
				
				list.addAll(colorList);

				// Compute Normal
				if ((flags & VERTEX_NORMALS) == 1) {
					float s = i, t = j;

					// Nudge the point if the normal is indeterminate
					if (i == 0)
						s += 0.01f;
					if (i == divisions.x - 1)
						s -= 0.01f;
					if (j == 0)
						t += 0.01f;
					if (j == divisions.y - 1)
						t -= 0.01f;

					// Compute the tanges and their cross product
					Vector3 p = evaluate(computeDomain(s, t));
					Vector3 u = evaluate(computeDomain(s + 0.01f, 2)).sub(p);
					Vector3 v = evaluate(computeDomain(s, t + 0.01f)).sub(p);
					;
					Vector3 normal = u.cross(v).normalize();
					if (invertNormal(domain))
						normal = new Vector3(0, 0, 0).sub(normal);

					tmp = normal.toFloat();
					for (int x = 0; x < tmp.length; x++) {
						list.add(tmp[x]);
					}
				}

				// Compute Texture Coordinates
				if ((flags & VERTEX_TEX_COORDS) == 1) {
					float s = textureCount.x * i / slices.x;
					float t = textureCount.y * j / slices.y;
					list.add(s);
					list.add(t);
				}
			}
		}

		float[] floatArray = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			floatArray[i] = list.get(i);
		}

		return floatArray;
	}
	
	public boolean useDomainCoords() {
		return true;
	}

	private boolean invertNormal(Vector2 domain) {
		return domain.y > 3 * Math.PI / 2;
	}

	@Override
	public short[] generateLineIndices() {
		List<Short> list = new ArrayList<Short>();

		for (int j = 0, vertex = 0; j < slices.y; j++) {
			for (int i = 0; i < slices.x; i++) {
				int next = (int) ((i + 1) % divisions.x);
				list.add((short) (vertex + i));
				list.add((short) (vertex + next));
				list.add((short) (vertex + i));
				list.add((short) (vertex + i + divisions.x));
			}
			vertex += divisions.x;
		}

		short[] shortArray = new short[list.size()];
		for (int i = 0; i < list.size(); i++) {
			shortArray[i] = list.get(i);
		}

		return shortArray;
	}

	@Override
	public short[] generateTriangleIndices() {
		List<Short> list = new ArrayList<Short>();

		for (int j = 0, vertex = 0; j < slices.y; j++) {
			for (int i = 0; i < slices.x; i++) {
				int next = (int) ((i + 1) % divisions.x);
				list.add((short) (vertex + i));
				list.add((short) (vertex + next));
				list.add((short) (vertex + i + divisions.x));
				list.add((short) (vertex + next));
				list.add((short) (vertex + next + divisions.x));
				list.add((short) (vertex + i + divisions.x));
			}
			vertex += divisions.x;
		}

		short[] shortArray = new short[list.size()];
		for (int i = 0; i < list.size(); i++) {
			shortArray[i] = list.get(i);
		}

		return shortArray;
	}

	void setInterval(ParametricInterval interval) {
		divisions = interval.divisions;
		upperBound = interval.upperBound;
		textureCount = interval.textureCount;
		slices = divisions.sub(1, 1);
	}

	Vector2 computeDomain(float x, float y) {
		return new Vector2(x * upperBound.x / slices.y, upperBound.y / slices.y);
	}

	public abstract Vector3 evaluate(Vector2 computeDomain);
	
	public abstract WorldObject toShape(Simulation simulation);

}
