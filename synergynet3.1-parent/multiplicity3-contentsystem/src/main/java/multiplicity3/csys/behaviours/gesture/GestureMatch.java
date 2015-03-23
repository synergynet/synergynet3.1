package multiplicity3.csys.behaviours.gesture;

/**
 * The Class GestureMatch.
 */
public class GestureMatch {

	/** The gesture. */
	public Gesture gesture;

	/** The library gesture. */
	public Gesture libraryGesture;

	/** The match score. */
	public float matchScore;

	/**
	 * Instantiates a new gesture match.
	 *
	 * @param gesture the gesture
	 * @param libraryGesture the library gesture
	 * @param score the score
	 */
	public GestureMatch(Gesture gesture, Gesture libraryGesture, float score) {
		this.gesture = gesture;
		this.libraryGesture = libraryGesture;
		this.matchScore = score;
	}
}
