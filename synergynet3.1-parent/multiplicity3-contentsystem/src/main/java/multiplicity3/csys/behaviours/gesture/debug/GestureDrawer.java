package multiplicity3.csys.behaviours.gesture.debug;


import java.nio.FloatBuffer;

import multiplicity3.csys.behaviours.gesture.Gesture;


import com.jme3.math.Vector2f;
import com.jme3.scene.shape.Line;
import com.jme3.scene.Node;
//TODO: is this needed in jme3?
//import com.jme3.scene.shape.Line.Mode;
import com.jme3.util.BufferUtils;

public class GestureDrawer extends Node {

	private Line a;
	private Line b;

	public GestureDrawer() {
		super("gesture drawer");
		
		a = new Line();
		//TODO: work out where translation happens for lines
		//a.setLocalTranslation(100, 100, 0);		
		//a.setLocalTranslation(200, 100, 0);
		b = new Line();
		
		initLine(a);
		initLine(b);

	}
	
	private void initLine(Line l) {
		l.setLineWidth(3f);
		
		// TODO: where do we set color, scale, translation, etc.
		//l.setSolidColor(ColorRGBA.White);
		//l.setLocalScale(200f);
		//l.setMode(Mode.Connected);
		
		//TODO: work out why Line is no longer a spatial
		//attachChild(l);		
	}

	public void updateGesturePair(Gesture g1, Gesture g2) {
		blat(g1, a);
		blat(g2, b);
	}
	
	public void blat(Gesture g, Line l) {
		FloatBuffer v = BufferUtils.createFloatBuffer(g.numPoints() * 3);
		for(int i = 0; i < g.numPoints(); i++) {
			Vector2f x = g.getPoint(i);
			v.put(x.x).put(x.y).put(0);
		}
		//TODO: Line
		//l.reconstruct(v, null, null, null);
	}
	
}
