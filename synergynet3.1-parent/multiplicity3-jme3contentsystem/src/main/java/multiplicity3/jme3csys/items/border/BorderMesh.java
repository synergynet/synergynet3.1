package multiplicity3.jme3csys.items.border;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

public class BorderMesh extends Mesh {
	
	public BorderMesh(float iw, float ih, float ow, float oh, float radius, int cornerDivisions) {
		
		Vector3f[] topLeft = (Vector3f[]) getTopLeft(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		Vector3f[] topRight = (Vector3f[]) getTopRight(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		Vector3f[] bottomLeft = (Vector3f[]) getBottomLeft(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		Vector3f[] bottomRight = (Vector3f[]) getBottomRight(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		
		int vertexCount = topLeft.length + topRight.length + bottomLeft.length + bottomRight.length + 4;  // 4 inner vertices
		
		int triangleCount = vertexCount + 4;
		
		setBuffer(Type.Position, 3, new float[3 * vertexCount]);
		setBuffer(Type.Normal, 3, new float[3 * vertexCount]);
		setBuffer(Type.Index, 3, new short[3 * triangleCount]);
		
		FloatBuffer normalBuffer = (FloatBuffer) getBuffer(Type.Normal).getData();
		FloatBuffer vertexBuffer = (FloatBuffer) getBuffer(Type.Position).getData();
		ShortBuffer indexBuffer = (ShortBuffer) getBuffer(Type.Index).getData();
		
		vertexBuffer.rewind();
		normalBuffer.rewind();
		indexBuffer.rewind();
		
		for (int x = 0; x < getVertexCount(); x++) {			
			normalBuffer.put(0).put(0).put(1);
		}

		short pos = 0;
		short positionA = pos;
		
		vertexBuffer.put(iw/2).put(-ih/2).put(0); // inner bottom right
		for(int i = 0; i < bottomRight.length; i++) { // others
			vertexBuffer.put(bottomRight[i].x).put(bottomRight[i].y).put(bottomRight[i].z);
		}

		for(short i = 0; i < bottomRight.length - 1; i++) {
			short first = pos; short second = (short)(pos + i + 1); short third = (short)(pos + i + 2);
			indexBuffer.put(first).put(second).put(third);			
		}

		pos = (short)(vertexBuffer.position() / 3);
		short positionD = pos;
		vertexBuffer.put(iw/2).put(ih/2).put(0); // inner top right
		for(short i = 0; i < topRight.length; i++) { // others
			vertexBuffer.put(topRight[i].x).put(topRight[i].y).put(topRight[i].z);
		}

		for(short i = 0; i < topRight.length - 1; i++) {
			short first = pos; short second = (short)(pos + i + 1); short third = (short)(pos + i + 2);
			indexBuffer.put(first).put(second).put(third);			
		}


		pos = (short)(vertexBuffer.position() / 3);
		short positionG = pos;
		vertexBuffer.put(-iw/2).put(ih/2).put(0); // inner top left
		for(short i = 0; i < topLeft.length; i++) { // others
			vertexBuffer.put(topLeft[i].x).put(topLeft[i].y).put(topLeft[i].z);
		}

		for(short i = 0; i < topLeft.length - 1; i++) {
			short first = pos; short second = (short)(pos + i + 1); short third = (short)(pos + i + 2);
			indexBuffer.put(first).put(second).put(third);			
		}

		pos = (short)(vertexBuffer.position() / 3);
		short positionJ = pos;
		vertexBuffer.put(-iw/2).put(-ih/2).put(0); // inner bottom left
		for(short i = 0; i < bottomLeft.length; i++) { // all other vertices on the bottom right
			vertexBuffer.put(bottomLeft[i].x).put(bottomLeft[i].y).put(bottomLeft[i].z);
		}

		for(short i = 0; i < bottomLeft.length - 1; i++) {
			short first = pos; short second = (short)(pos + i + 1); short third = (short)(pos + i + 2);
			indexBuffer.put(first).put(second).put(third);			
		}
		
		short positionB = (short)(positionA + bottomRight.length);
		short positionC = (short)(positionD + 1);
		short positionE = (short)(positionD + topRight.length);
		short positionF = (short)(positionE + 2);
		short positionH = (short)(positionG + topLeft.length);
		short positionI = (short)(positionH + 2);
		short positionK = (short)(positionJ + bottomLeft.length);
		short positionL = (short)(positionA + 1);
		
		// join the corners
		indexBuffer.put(positionA).put(positionB).put(positionC);
		indexBuffer.put(positionA).put(positionC).put(positionD);
		indexBuffer.put(positionD).put(positionE).put(positionF);
		indexBuffer.put(positionD).put(positionF).put(positionG);
		indexBuffer.put(positionG).put(positionH).put(positionI);
		indexBuffer.put(positionG).put(positionI).put(positionJ);
		indexBuffer.put(positionJ).put(positionK).put(positionL);
		indexBuffer.put(positionJ).put(positionL).put(positionA);
		
		
		updateBound();
		
		
	}


	public static List<Vector3f> getBottomRight(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x + w-radius, y, 0));
		float t = FastMath.PI * 1.5f;
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x + w - radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0));
		}
		float sx = x + w - radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0));
		return list;
	}

	public static List<Vector3f> getTopRight(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x + w, y + h - radius, 0)); 
		float t = 0;
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x + w - radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + h -radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0f));
		}
		float sx = x + w - radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + h -radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0f));		
		return list;
	}

	public static List<Vector3f> getTopLeft(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x + radius, y + h, 0));
		float t = FastMath.PI * 0.5f;
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x  + radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + h - radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0));			
		}
		float sx = x  + radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + h - radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0));	
		return list;
	}

	public static List<Vector3f> getBottomLeft(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x, y + radius, 0)); 
		float t = FastMath.PI;				
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x + radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0));
		}
		float sx = x + radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0));		
		return list;
	}
}
