package multiplicity3.csys.behaviours.gesture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * Adaptation of Recognition of Handwritten Gestures 
 * by Oleg Dopertchouk
 * http://www.gamedev.net/reference/articles/article2039.asp
 * 
 * @author dcs0ah1
 *
 */

public class Gesture implements Serializable {
	private static final long serialVersionUID = -6367313496201753269L;
	
	List<Vector2f> points = new ArrayList<Vector2f>();
	Vector2f minExtents = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
	Vector2f maxExtents = new Vector2f(Float.MIN_VALUE, Float.MIN_VALUE);
	private float width;
	private float height;

	private String name;
	
	public Gesture(String name) {
		this.name = name;
	}
	
	public void addPoint(Vector2f p) {
		points.add(p.clone());
		if(p.x < minExtents.x) minExtents.x = p.x;
		if(p.x > maxExtents.x) maxExtents.x = p.x;
		if(p.y < minExtents.y) minExtents.y = p.y;
		if(p.y > maxExtents.y) maxExtents.y = p.y;	
	}
	
	public int numPoints() {
		return points.size();
	}
	
	public void setPoint(int index, Vector2f v) {
		this.points.get(index).set(v);
	}
	
	public Vector2f getPoint(int index) {
		return this.points.get(index);
	}
	
	public void normalizeSize() {
		if(points.size() < 2) return;
		width = maxExtents.x - minExtents.x;
		height = maxExtents.y - minExtents.y;
		float scale = (width > height) ? width : height;
		scale = 1.0f/scale;
		for(Vector2f p : points) {
			p.x *= scale;
			p.y *= scale;
		}
	}
	
	private float getStrokeLength() {
		if(points.size() < 2) return 0f;
		float len = 0f;
		
		for(int i = 0; i < points.size() - 1; i++) {
			len += points.get(i + 1).distance(points.get(i));
		}
		
		return len;
	}
	
	public void normalizeCenter() {
		float cx = 0f;
		float cy = 0f;
		
		for(Vector2f v : points) {
			cx += v.x;
			cy += v.y;
		}
		
		cx /= points.size();
		cy /= points.size();
		
		for(Vector2f v : points) {
			v.x -= cx;
			v.y -= cy;
		}
	}
	
	public Gesture normalizeResolution(int numPoints) {
		if(points.size() < 2) return null;
		
		Gesture s = new Gesture(name);
		
		float lengthBetweenPoints = getStrokeLength() / numPoints;
		float distanceToNextPoint = lengthBetweenPoints;
		float lengthConsumedThisSegment = 0f;
		
		int i = 0;
		while(i < points.size() - 1) {
			Vector2f a = points.get(i);
			Vector2f b = points.get(i + 1);
			float lengthAB = b.subtract(a).length();
			if(lengthAB - (lengthConsumedThisSegment) > distanceToNextPoint) {
				Vector2f dirAB = b.subtract(a).normalizeLocal(); 
				Vector2f newPoint = a.add(dirAB.mult(lengthConsumedThisSegment + distanceToNextPoint));
				s.addPoint(newPoint);
				lengthConsumedThisSegment += distanceToNextPoint;
				distanceToNextPoint = lengthBetweenPoints; 
			}else{
				distanceToNextPoint = distanceToNextPoint - lengthAB;
				i++;
			}
		}
		if(s.numPoints() < numPoints) s.addPoint(points.get(points.size() -1));
		
		return s;
	}
	
	public String toString() {
		return Gesture.class.getName() + " [" + name + "]";
	}
	
	public static float dotProduct(Gesture a, Gesture b) {
		float dotProduct = 0f;
		
		List<Vector2f> as = a.getPoints();
		List<Vector2f> bs = b.getPoints();
		
		if(as.size() != bs.size()) return -1f;
		
		for(int i = 0; i < as.size(); i++) {
			dotProduct += as.get(i).dot(bs.get(i));
		}
		
		return dotProduct;
	}
	
	public float compareTo(Gesture g) {
		return match(this, g);
	}
		
	public static float match(Gesture a, Gesture b) {
		float dotProductAB = dotProduct(a,b);
		float dotProductAA = dotProduct(a,a);
		float dotProductBB = dotProduct(b,b);		
		if(dotProductAB <= 0) return 0f;
		float score = dotProductAB;
		score /= FastMath.sqrt(dotProductAA * dotProductBB);
		return score;
	}
	
	private List<Vector2f> getPoints() {
		return points;
	}

	public static void main(String[] args) {
		writeCircleLine();
//		testMatching();
	}
	
	public static void testMatching() {
		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");
		
		Gesture in = new Gesture("circle");
		
		float i = FastMath.PI / 8;
		float r = 1f;
		for(float theta = 0f; theta < 2*FastMath.PI; theta += i) {
			float x = r * FastMath.cos(theta);
			float y = r * FastMath.sin(theta);
			in.addPoint(new Vector2f(x, y));
		}
		
		in = in.normalizeResolution(32);
		
		GestureMatch match = GestureLibrary.getInstance().findGestureMatch(in, 0.9f);
		if(match != null) {
			System.out.println("matched with " + match.gesture + " with score " + match.matchScore);
		}
	}
	
	public static void writeCircleLine() {
		Gesture circle = new Gesture("circle");
		
		float i = FastMath.PI / 32;
		float r = 1f;
		for(float theta = 0f; theta < 2*FastMath.PI; theta += i) {
			float x = r * FastMath.cos(theta);
			float y = r * FastMath.sin(theta);
			circle.addPoint(new Vector2f(x, y));
		}
		
		circle.normalizeSize();
		circle = circle.normalizeResolution(32);
		circle.normalizeCenter();

		Gesture line = new Gesture("line");
		for(float x = 0; x < 32; x+= 1f) {
			line.addPoint(new Vector2f(x, 0));
		}
		line.normalizeSize();
		line = line.normalizeResolution(32);
		line.normalizeCenter();
		
		try {
			GestureLibrary.getInstance().writeGesture("circle", circle, new File("."));
			GestureLibrary.getInstance().writeGesture("line", line, new File("."));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(match(circle, line));
	}

	public String getName() {
		return name;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
	
}
