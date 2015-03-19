package multiplicity3.csys.behaviours.gesture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class GestureLibrary {
	private static GestureLibrary instance;

	public static GestureLibrary getInstance() {
		synchronized(GestureLibrary.class) { 
			if(instance == null) instance = new GestureLibrary();
			return instance;
		}
	}
	
	private GestureLibrary() {}
	
	Map<String, Gesture> loadedGestures = new HashMap<String,Gesture>();
	
	public Gesture loadGesture(String name) {
		try {
			ObjectInputStream ois = new ObjectInputStream(GestureLibrary.class.getResourceAsStream(name + ".gesture"));
			Gesture g = (Gesture) ois.readObject();
			ois.close();
			loadedGestures.put(name, g);
			return g;
		} catch (IOException e) {
			// TODO propagate
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO propagate
			e.printStackTrace();
		}
		return null;
	}
	
	public GestureMatch findGestureMatch(Gesture g, float proximity) {
		g.normalizeSize();
		g = g.normalizeResolution(32);
		g.normalizeCenter();
		
		Gesture highestMatch = null;
		float highestScore = Float.MIN_VALUE;
		for(Gesture x : loadedGestures.values()) {
			float score = g.compareTo(x);
			if(score > proximity && score > highestScore) {
				highestMatch = x;
				highestScore = score;
			}
		}		
		if(highestMatch == null) return null;
		return new GestureMatch(g, highestMatch, highestScore);
	}
	
	public void writeGesture(String name, Gesture g, File dir) throws FileNotFoundException, IOException {
		if(g.numPoints() != 32) throw new IllegalArgumentException("Gesture must contain 32 points.");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, name+".gesture")));
		oos.writeObject(g);
		oos.close();
	}

}
