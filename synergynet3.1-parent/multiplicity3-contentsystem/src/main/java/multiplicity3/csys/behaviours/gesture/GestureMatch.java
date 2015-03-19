package multiplicity3.csys.behaviours.gesture;

public class GestureMatch {
	public Gesture gesture;
	public float matchScore;
	public Gesture libraryGesture;
	
	public GestureMatch(Gesture gesture, Gesture libraryGesture, float score) {
		this.gesture = gesture;
		this.libraryGesture = libraryGesture;
		this.matchScore = score;
	}
}
