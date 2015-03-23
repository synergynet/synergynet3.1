// Utilises Andrew Davison's ArniesTracker
// (http://fivedots.coe.psu.ac.th/~ad/jg/nui1610/index.html)

package usertracking;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class PoseSequences.
 */
public class PoseSequences {

	/** The Constant poseSequencesEnabled. */
	public static final HashMap<PoseName, PoseName[]> poseSequencesEnabled = new HashMap<PoseName, PoseName[]>() {
		private static final long serialVersionUID = -4827155036502072456L;

		{
			// complex gesture sub-sequences that are looked for in the user's
			// full gesture sequence.
			// put(PoseName.HORIZ_WAVE_RIGHT, new PoseName[]{PoseName.RH_OUT,
			// PoseName.RH_IN, PoseName.RH_OUT, PoseName.RH_IN});
			// put(GestureName.VERT_WAVE_RIGHT, new
			// GestureName[]{GestureName.RH_UP, GestureName.RH_DOWN,
			// GestureName.RH_UP, GestureName.RH_DOWN });
			// put(PoseName.HORIZ_WAVE_LEFT, new PoseName[] {PoseName.LH_OUT,
			// PoseName.LH_IN, PoseName.LH_OUT, PoseName.LH_IN });
			// put(GestureName.VERT_WAVE_LEFT, new
			// GestureName[]{GestureName.LH_UP, GestureName.LH_DOWN,
			// GestureName.LH_UP, GestureName.LH_DOWN });
			put(PoseName.PUSH_RIGHT, new PoseName[] { PoseName.RH_IN_L,
					PoseName.RH_OUT_L });
			put(PoseName.PUSH_LEFT, new PoseName[] { PoseName.LH_IN_L,
					PoseName.LH_OUT_L });
			put(PoseName.PULL_RIGHT, new PoseName[] { PoseName.RH_OUT_H,
					PoseName.RH_IN_H });
			put(PoseName.PULL_LEFT, new PoseName[] { PoseName.LH_OUT_H,
					PoseName.LH_IN_H });
			put(PoseName.SWEEP_OUT, new PoseName[] { PoseName.HANDS_NEAR_TORSO,
					PoseName.BOTH_HANDS_OUT });
		}
	};

	// object that is notified of a complex gesture by calling its pose() method
	/** The tracker panel. */
	private TrackerPanel trackerPanel;

	/** The user gest seqs. */
	private HashMap<Integer, ArrayList<PoseName>> userGestSeqs; // gesture
																// sequence for
																// each user

	/**
	 * Instantiates a new pose sequences.
	 *
	 * @param trackerPanel the tracker panel
	 */
	public PoseSequences(TrackerPanel trackerPanel) {
		this.trackerPanel = trackerPanel;
		userGestSeqs = new HashMap<Integer, ArrayList<PoseName>>();
	}

	/**
	 * Adds the user.
	 *
	 * @param kinectID the kinect id
	 */
	public void addUser(int kinectID) { // create a new empty gestures sequence
										// for a user
		userGestSeqs.put(new Integer(kinectID), new ArrayList<PoseName>());
	}

	/**
	 * Adds the user gest.
	 *
	 * @param kinectID the kinect id
	 * @param gest the gest
	 */
	public void addUserGest(int kinectID, PoseName gest) { // called from
															// SkeletonsGestures:
															// add an gesture to
															// the end of the
															// user's sequence
		ArrayList<PoseName> gestsSeq = userGestSeqs.get(kinectID);
		if (gestsSeq != null) {
			gestsSeq.add(gest);
		}
	}

	/**
	 * Check sequences.
	 *
	 * @param userID the user id
	 */
	public void checkSequences(int userID) { // look for gesture sub-sequences
												// in the user's full gesture
												// sequence and notify the
												// watcher
		ArrayList<PoseName> gestsSeq = userGestSeqs.get(userID);
		if (gestsSeq != null) {
			checkIndividualSequence(userID, gestsSeq);
		}
	}

	/**
	 * Clear sequences.
	 */
	public void clearSequences() {
		for (int userID : userGestSeqs.keySet()) {
			userGestSeqs.get(userID).clear();
		}
	}

	/**
	 * Removes the user.
	 *
	 * @param kinectID the kinect id
	 */
	public void removeUser(int kinectID) { // remove the gesture sequence for
											// this user
		userGestSeqs.remove(kinectID);
	}

	/**
	 * Check generic sequence.
	 *
	 * @param userID the user id
	 * @param gestsSeq the gests seq
	 * @param gestureName the gesture name
	 */
	private void checkGenericSequence(int userID, ArrayList<PoseName> gestsSeq,
			PoseName gestureName) {
		if (poseSequencesEnabled.containsKey(gestureName)) {
			int endPos = findSubSeq(gestsSeq,
					poseSequencesEnabled.get(gestureName));
			if (endPos != -1) {
				trackerPanel.pose(userID, gestureName, true);
				purgeSeq(gestsSeq, endPos);
			}
		}
	}

	/**
	 * Check individual sequence.
	 *
	 * @param userID the user id
	 * @param gestsSeq the gests seq
	 */
	private void checkIndividualSequence(int userID,
			ArrayList<PoseName> gestsSeq) {
		// look for gesture sub-sequences. If one is found, then the part of the
		// user's gesture sequence containing the sub-sequence is deleted.
		checkGenericSequence(userID, gestsSeq, PoseName.HORIZ_WAVE_RIGHT);
		checkGenericSequence(userID, gestsSeq, PoseName.HORIZ_WAVE_LEFT);
		checkGenericSequence(userID, gestsSeq, PoseName.VERT_WAVE_RIGHT);
		checkGenericSequence(userID, gestsSeq, PoseName.VERT_WAVE_LEFT);
		checkGenericSequence(userID, gestsSeq, PoseName.PUSH_RIGHT);
		checkGenericSequence(userID, gestsSeq, PoseName.PUSH_LEFT);
		checkGenericSequence(userID, gestsSeq, PoseName.PULL_RIGHT);
		checkGenericSequence(userID, gestsSeq, PoseName.PULL_LEFT);
		checkGenericSequence(userID, gestsSeq, PoseName.SWEEP_OUT);
	}

	/**
	 * Find sub seq.
	 *
	 * @param gestsSeq the gests seq
	 * @param gests the gests
	 * @return the int
	 */
	private int findSubSeq(ArrayList<PoseName> gestsSeq, PoseName[] gests) {
		/*
		 * Try to find all the gests[] array GestureName objects inside the
		 * list, and return the position *after* the last object, or -1. The
		 * array elements do not have to be stored contigiously in the list.
		 */
		int pos = 0;
		for (PoseName gest : gests) { // iterate through the array
			while (pos < gestsSeq.size()) { // find the gesture in the list
				if (gest == gestsSeq.get(pos)) {
					break;
				}
				pos++;
			}
			if (pos == gestsSeq.size()) {
				return -1;
			} else {
				pos++; // carry on, starting with next gesture in list
			}
		}
		return pos;
	}

	/**
	 * Purge seq.
	 *
	 * @param gestsSeq the gests seq
	 * @param pos the pos
	 */
	private void purgeSeq(ArrayList<PoseName> gestsSeq, int pos) { // remove all
																	// the
																	// elements
																	// in the
																	// seq
																	// between
																	// the
																	// positions
																	// 0 and
																	// pos-1
		for (int i = 0; i < pos; i++) {
			if (gestsSeq.isEmpty()) {
				return;
			}
			try {
				gestsSeq.remove(0);
			} catch (IndexOutOfBoundsException e) {
			}
		}
	}

}