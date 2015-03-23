// Utilises Andrew Davison's ArniesTracker (http://fivedots.coe.psu.ac.th/~ad/jg/nui1610/index.html)

package usertracking;

import java.util.*;

import org.OpenNI.*;

import synergynet3.tracking.network.shared.PointDirection;
import usertracking.networking.TrackerNetworking;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

enum PoseName {
	HORIZ_WAVE_RIGHT, VERT_WAVE_RIGHT,						// waving right hand
	HORIZ_WAVE_LEFT, VERT_WAVE_LEFT,						// waving left hand
	SWEEP_OUT,												// sweep both hands out
	PUSH_RIGHT, PULL_RIGHT,									// push and pull right hand
	PUSH_LEFT, PULL_LEFT,									// push and pull left hand
	HANDS_NEAR, HANDS_NEAR_TORSO, BOTH_HANDS_OUT,			// two hands
	LEAN_LEFT, LEAN_RIGHT, LEAN_FWD, LEAN_BACK,     		// leaning
	TURN_RIGHT, TURN_LEFT,                          		// turning
	POINT_RIGHT, POINT_LEFT,                          		// pointing
	RH_RHIP, LH_LHIP,	                              		// touching
	RH_UP, RH_OUT_H, RH_IN_H, RH_OUT_L, RH_IN_L, RH_DOWN,  	// right hand position
	LH_UP, LH_OUT_H, LH_IN_H, LH_OUT_L, LH_IN_L, LH_DOWN,	// left hand position
	RE_UP,													// right elbow position
	LE_UP													// left elbow position
}

/**
 * The Class Poses.
 */
public class Poses{

	/** The left hand point. */
	public static PointDirection leftHandPoint = null;

	// booleans set when gestures are being performed
	/** The Constant poseActivity. */
	public static final HashMap<PoseName, HashMap<Integer, Boolean>> poseActivity = new HashMap<PoseName, HashMap<Integer, Boolean>>(){
		private static final long serialVersionUID = -4827155036502072456L;
		{
			// two hands
			put(PoseName.HANDS_NEAR, new HashMap<Integer, Boolean>());
			put(PoseName.HANDS_NEAR_TORSO, new HashMap<Integer, Boolean>());
			put(PoseName.BOTH_HANDS_OUT, new HashMap<Integer, Boolean>());

			// leaning
			//	    	put(PoseName.LEAN_LEFT, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.LEAN_RIGHT, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.LEAN_FWD, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.LEAN_BACK, new HashMap<Integer, Boolean>());

			// turning
			//	    	put(PoseName.TURN_RIGHT, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.TURN_LEFT, new HashMap<Integer, Boolean>());

			// pointing
			put(PoseName.POINT_RIGHT, new HashMap<Integer, Boolean>());
			put(PoseName.POINT_LEFT, new HashMap<Integer, Boolean>());

			// touching
			//	    	put(PoseName.RH_RHIP, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.LH_LHIP, new HashMap<Integer, Boolean>());

			// right hand
			put(PoseName.RH_UP, new HashMap<Integer, Boolean>());
			put(PoseName.RH_OUT_H, new HashMap<Integer, Boolean>());
			put(PoseName.RH_IN_H, new HashMap<Integer, Boolean>());
			put(PoseName.RH_OUT_L, new HashMap<Integer, Boolean>());
			put(PoseName.RH_IN_L, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.RH_DOWN, new HashMap<Integer, Boolean>());

			// left hand
			put(PoseName.LH_UP, new HashMap<Integer, Boolean>());
			put(PoseName.LH_OUT_H, new HashMap<Integer, Boolean>());
			put(PoseName.LH_IN_H, new HashMap<Integer, Boolean>());
			put(PoseName.LH_OUT_L, new HashMap<Integer, Boolean>());
			put(PoseName.LH_IN_L, new HashMap<Integer, Boolean>());
			//	    	put(PoseName.LH_DOWN, new HashMap<Integer, Boolean>());

			// right elbow
			//	    	put(PoseName.RE_UP, new HashMap<Integer, Boolean>());

			// left elbow
			//	    	put(PoseName.LE_UP, new HashMap<Integer, Boolean>());
		}
	};

	/** The right hand point. */
	public static PointDirection rightHandPoint = null;

	/** The Constant ELBOW_ANGLE_THRESHOLD. */
	private static final float ELBOW_ANGLE_THRESHOLD = 25;

	/** The ref dis. */
	private static float REF_DIS = 100f;

	/** The Constant SHOULDER_ANGLE_MAX_THRESHOLD. */
	private static final float SHOULDER_ANGLE_MAX_THRESHOLD = 160;

	/** The Constant SHOULDER_ANGLE_MIN_THRESHOLD. */
	private static final float SHOULDER_ANGLE_MIN_THRESHOLD = 90;

	/** The gest seqs. */
	private PoseSequences gestSeqs;	// stores gesture sequences for each user, and looks for more complex gestures (actually only right hand waving at present)

	/** The tracker panel. */
	private TrackerPanel trackerPanel; // object that is notified of an gesture start/stop by calling its pose() method

	/* skeleton joints for each user; uses screen coordinate system
	 * i.e. positive z-axis is into the scene; positive x-axis is to the right; positive y-axis is down*/
	/** The user skels. */
	private HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> userSkels;

	/**
	 * Instantiates a new poses.
	 *
	 * @param trackerPanel the tracker panel
	 * @param uSkels the u skels
	 * @param gSeqs the g seqs
	 */
	public Poses(TrackerPanel trackerPanel, HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> uSkels, PoseSequences gSeqs){
		this.trackerPanel = trackerPanel;
		userSkels = uSkels;
		gestSeqs = gSeqs;
	}

	/**
	 * Check gests.
	 *
	 * @param kinectID the kinect id
	 * @param depthGen the depth gen
	 */
	public void checkGests(int kinectID, DepthGenerator depthGen){ // decide which gestures have just started or just finished, and notify the watcher.
		HashMap<SkeletonJoint, SkeletonJointPosition> skel = userSkels.get(kinectID);
		if (skel == null) {
			return;
		}

		/* repeatedly calculate lengths since the size of a skeleton *on-screen* will change if the user moves closer or further away.
		 * This overhead would disappear if skeletons were stored using real-world coordinates instead of screen-based values. */
		calcSkelLengths(skel);

		//Methods for checking for specific poses
		if (UserTracker.GESTURING_USER == TrackerNetworking.uniqueIDs[kinectID]){

			twoHandsNear(kinectID, skel, PoseName.HANDS_NEAR);
			twoHandsNearTorso(kinectID, skel, PoseName.HANDS_NEAR_TORSO);
			twoHandsApart(kinectID, skel, PoseName.BOTH_HANDS_OUT);

			leanLeft(kinectID, skel, PoseName.LEAN_LEFT);
			leanRight(kinectID, skel, PoseName.LEAN_RIGHT);
			leanFwd(kinectID, skel, PoseName.LEAN_FWD);
			leanBack(kinectID, skel, PoseName.LEAN_BACK);

			turnRight(kinectID, skel, PoseName.TURN_RIGHT);
			turnLeft(kinectID, skel, PoseName.TURN_LEFT);

			rightHandPoint(kinectID, skel, PoseName.POINT_RIGHT, depthGen);
			leftHandPoint(kinectID, skel, PoseName.POINT_LEFT, depthGen);

			rightHandTouchHip(kinectID, skel, PoseName.RH_RHIP);
			leftHandTouchHip(kinectID, skel, PoseName.LH_LHIP);

			rightHandUp(kinectID, skel, PoseName.RH_UP);
			rightHandOutHigh(kinectID, skel, PoseName.RH_OUT_H);
			rightHandInHigh(kinectID, skel, PoseName.RH_IN_H);
			rightHandOutLow(kinectID, skel, PoseName.RH_OUT_L);
			rightHandInLow(kinectID, skel, PoseName.RH_IN_L);
			rightHandDown(kinectID, skel, PoseName.RH_DOWN);

			leftHandUp(kinectID, skel, PoseName.LH_UP);
			leftHandOutHigh(kinectID, skel, PoseName.LH_OUT_H);
			leftHandInHigh(kinectID, skel, PoseName.LH_IN_H);
			leftHandOutLow(kinectID, skel, PoseName.LH_OUT_L);
			leftHandInLow(kinectID, skel, PoseName.LH_IN_L);
			leftHandDown(kinectID, skel, PoseName.LH_DOWN);

			rightElbowUp(kinectID, skel, PoseName.RE_UP);

			leftElbowUp(kinectID, skel, PoseName.LE_UP);

		}else if (UserTracker.GESTURING_USER == -1){

			//Only check gesture involved in waving
			rightHandUp(kinectID, skel, PoseName.RH_UP);
			rightHandDown(kinectID, skel, PoseName.RH_DOWN);

			leftHandUp(kinectID, skel, PoseName.LH_UP);
			leftHandDown(kinectID, skel, PoseName.LH_DOWN);
		}
	}

	/**
	 * Calc skel lengths.
	 *
	 * @param skel the skel
	 */
	private void calcSkelLengths(HashMap<SkeletonJoint, SkeletonJointPosition> skel){
		// calculate lengths between certain joint pairs for this skeleton these values are used later to judge the distances between other joints
		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D leftElbowPt = getJointPos(skel, SkeletonJoint.LEFT_ELBOW);
		Point3D leftShoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);

		if ((leftHandPt != null) && (leftElbowPt != null) && (leftShoulderPt != null)) {
			REF_DIS = (distApart(leftHandPt, leftElbowPt) + distApart(leftElbowPt, leftShoulderPt))/2;
		}
	}

	/**
	 * Dist apart.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the float
	 */
	private float distApart(Point3D p1, Point3D p2){ // the distance between the two points
		Vector3f vec1 = new Vector3f(p1.getX(), p1.getY(), p1.getZ());
		Vector3f vec2 = new Vector3f(p2.getX(), p2.getY(), p2.getZ());
		return vec1.distance(vec2);
	}

	/**
	 * Dist apart xy.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the float
	 */
	private float distApartXY(Point3D p1, Point3D p2){ // the distance between the two points
		Vector2f vec1 = new Vector2f(p1.getX(), p1.getY());
		Vector2f vec2 = new Vector2f(p2.getX(), p2.getY());
		return vec1.distance(vec2);
	}

	/**
	 * Dist apart xz.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the float
	 */
	private float distApartXZ(Point3D p1, Point3D p2){ // the distance between the two points
		Vector2f vec1 = new Vector2f(p1.getX(), p1.getZ());
		Vector2f vec2 = new Vector2f(p2.getX(), p2.getZ());
		return vec1.distance(vec2);
	}

	/**
	 * Generator vector.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the vector3f
	 */
	private Vector3f generatorVector(Point3D from, Point3D to){
		return new Vector3f(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}

	/**
	 * Gesture activation.
	 *
	 * @param kinectID the kinect id
	 * @param active the active
	 * @param gesture the gesture
	 */
	private void gestureActivation(int kinectID, boolean active, PoseName gesture){
		if (!poseActivity.get(gesture).containsKey(kinectID)) {
			poseActivity.get(gesture).put(kinectID, false);
		}
		if (active){
			if (!poseActivity.get(gesture).get(kinectID)) {
				trackerPanel.pose(kinectID, gesture, true);
				gestSeqs.addUserGest(kinectID, gesture);
				poseActivity.get(gesture).put(kinectID, true);
			}
		}else{
			if (poseActivity.get(gesture).get(kinectID)) {
				trackerPanel.pose(kinectID, gesture, false);
				poseActivity.get(gesture).put(kinectID, false);
			}
		}
	}

	/**
	 * Gets the joint pos.
	 *
	 * @param skel the skel
	 * @param j the j
	 * @return the joint pos
	 */
	private Point3D getJointPos(HashMap<SkeletonJoint, SkeletonJointPosition> skel, SkeletonJoint j){  // get the (x, y, z) coordinate for the joint (or return null)
		SkeletonJointPosition pos = skel.get(j);
		if (pos == null) {
			return null;
		}
		if (pos.getConfidence() == 0) {
			return null;
		}

		return pos.getPosition();
	}


	// -------------------------- two hands ----------------------------------

	/**
	 * Lean back.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leanBack(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's head leaning back relative to their torso?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((torsoPt == null) || (headPt == null)) {
			return;
		}

		float zDiff = headPt.getZ() - torsoPt.getZ();
		boolean active = zDiff > REF_DIS;
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Lean fwd.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leanFwd(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's head forward of their torso?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((torsoPt == null) || (headPt == null)) {
			return;
		}

		float zDiff = headPt.getZ() - torsoPt.getZ();
		boolean active = zDiff < (-1*REF_DIS);
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Lean left.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leanLeft(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's head to the left of their left hip?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHipPt = getJointPos(skel, SkeletonJoint.LEFT_HIP);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((leftHipPt == null) || (headPt == null)) {
			return;
		}

		boolean active = headPt.getX() <= leftHipPt.getX();
		gestureActivation(kinectID, active, gesture);
	}

	// -------------------------- leaning ----------------------------------

	/**
	 * Lean right.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leanRight(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's head to the right of their right hip?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHipPt = getJointPos(skel, SkeletonJoint.RIGHT_HIP);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((rightHipPt == null) || (headPt == null)) {
			return;
		}

		boolean active = rightHipPt.getX() <= headPt.getX();
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Left elbow up.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftElbowUp(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand at head level or above?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftElbowPt = getJointPos(skel, SkeletonJoint.LEFT_ELBOW);
		Point3D leftShoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);
		if ((leftElbowPt == null) || (leftShoulderPt == null)) {
			return;
		}

		boolean active = leftElbowPt.getY() <= leftShoulderPt.getY();
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Left hand down.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandDown(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand at hip level or below?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D hipPt = getJointPos(skel, SkeletonJoint.LEFT_HIP);
		if ((leftHandPt == null) || (hipPt == null)) {
			return;
		}

		boolean active = leftHandPt.getY() >= hipPt.getY();
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Left hand in high.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandInHigh(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand inside (right) of their left elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D elbowPt = getJointPos(skel, SkeletonJoint.LEFT_ELBOW);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((leftHandPt == null) || (elbowPt == null) || (shoulderPt == null) || (headPt == null)) {
			return;
		}

		float diff = distApartXZ(leftHandPt, headPt);
		boolean active = (diff < (REF_DIS/3)) && (shoulderPt.getY() >= leftHandPt.getY()) && (elbowPt.getY() >= leftHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}


	// -------------------------- turning ----------------------------------

	/**
	 * Left hand in low.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandInLow(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand inside (right) of their left elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);
		Point3D neckPt = getJointPos(skel, SkeletonJoint.HEAD);
		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		if ((leftHandPt == null) || (shoulderPt == null) || (neckPt == null) || (torsoPt == null)) {
			return;
		}

		float diff = distApartXZ(leftHandPt, shoulderPt);
		boolean active = (diff < (REF_DIS/3)) && (neckPt.getY() <= leftHandPt.getY()) && (torsoPt.getY() >= leftHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Left hand out high.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandOutHigh(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand out to the left of the their left elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D elbowPt = getJointPos(skel, SkeletonJoint.LEFT_ELBOW);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((leftHandPt == null) || (elbowPt == null) || (shoulderPt == null) || (headPt == null)) {
			return;
		}

		float diff = distApartXZ(leftHandPt, headPt);
		boolean active = (diff > (REF_DIS - (REF_DIS/4))) && (shoulderPt.getY() >= leftHandPt.getY()) && (elbowPt.getY() >= leftHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}


	// -------------------------- pointing ----------------------------------

	/**
	 * Left hand out low.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandOutLow(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand out to the left of the their left elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);
		Point3D neckPt = getJointPos(skel, SkeletonJoint.HEAD);
		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		if ((leftHandPt == null) || (shoulderPt == null) || (neckPt == null) || (torsoPt == null)) {
			return;
		}

		float diff = distApartXZ(leftHandPt, shoulderPt);
		boolean active = (diff > (REF_DIS - (REF_DIS/4))) && (neckPt.getY() <= leftHandPt.getY()) && (torsoPt.getY() >= leftHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Left hand point.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 * @param depthGen the depth gen
	 */
	private void leftHandPoint(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture, DepthGenerator depthGen) {
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D handPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D elbowPt = getJointPos(skel, SkeletonJoint.LEFT_ELBOW);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.LEFT_SHOULDER);
		if ((handPt == null) || (elbowPt == null) || (shoulderPt == null)) {
			return;
		}

		boolean active = false;

		Vector3f upperArmUp = generatorVector(elbowPt, shoulderPt);
		Vector3f lowerArmDown = generatorVector(elbowPt, handPt);

		float elbowAngle = FastMath.abs(upperArmUp.normalize().angleBetween(lowerArmDown.normalize()));

		if (((FastMath.DEG_TO_RAD * (180 - ELBOW_ANGLE_THRESHOLD)) < elbowAngle) && (elbowAngle < (FastMath.DEG_TO_RAD * (180 + ELBOW_ANGLE_THRESHOLD)))){
			Vector3f up = new Vector3f(0, 1, 0).normalize();
			Vector3f upperArmDown = lowerArmDown.negate();

			float shoulderAngle = FastMath.abs(up.angleBetween(upperArmDown.normalize()));

			if (((FastMath.DEG_TO_RAD * SHOULDER_ANGLE_MIN_THRESHOLD) < shoulderAngle) && (shoulderAngle < (FastMath.DEG_TO_RAD * SHOULDER_ANGLE_MAX_THRESHOLD))){
				try{
					shoulderPt = depthGen.convertProjectiveToRealWorld(shoulderPt);
					handPt = depthGen.convertProjectiveToRealWorld(handPt);

					leftHandPoint = new PointDirection();
					leftHandPoint.setStartPoint(shoulderPt.getX(), shoulderPt.getY(), shoulderPt.getZ());
					leftHandPoint.setEndPoint(handPt.getX(), handPt.getY(), handPt.getZ());

					active = true;
				} catch (StatusException e) {
					e.printStackTrace();
				}
			}
		}
		gestureActivation(kinectID, active, gesture);
	}


	// -------------------------- touching ----------------------------------

	/**
	 * Left hand touch hip.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandTouchHip(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand touching their left hip?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D leftHipPt = getJointPos(skel, SkeletonJoint.LEFT_HIP);
		if ((leftHandPt == null) || (leftHipPt == null)) {
			return;
		}

		float dist = distApart(leftHipPt, leftHandPt);
		boolean active = dist < REF_DIS;
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Left hand up.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void leftHandUp(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's left hand at head level or above?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((leftHandPt == null) || (headPt == null)) {
			return;
		}

		boolean active = leftHandPt.getY() <= headPt.getY();
		gestureActivation(kinectID, active, gesture);
	}


	// -------------------------- right hand ----------------------------------

	/**
	 * Right elbow up.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightElbowUp(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand at head level or above?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightElbowPt = getJointPos(skel, SkeletonJoint.RIGHT_ELBOW);
		Point3D rightShoulderPt = getJointPos(skel, SkeletonJoint.RIGHT_SHOULDER);
		if ((rightElbowPt == null) || (rightShoulderPt == null)) {
			return;
		}

		boolean active = rightElbowPt.getY() <= rightShoulderPt.getY();
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand down.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandDown(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand at hip level or below?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D hipPt = getJointPos(skel, SkeletonJoint.RIGHT_HIP);
		if ((rightHandPt == null) || (hipPt == null)) {
			return;
		}

		boolean active = rightHandPt.getY() >= hipPt.getY();
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand in high.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandInHigh(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand inside (left) of their right elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D elbowPt = getJointPos(skel, SkeletonJoint.RIGHT_ELBOW);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.RIGHT_SHOULDER);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((rightHandPt == null) || (elbowPt == null) || (shoulderPt == null) || (headPt == null)) {
			return;
		}

		float diff = distApartXZ(rightHandPt, headPt);
		boolean active = (diff < (REF_DIS/3)) && (headPt.getY() >= rightHandPt.getY()) && (elbowPt.getY() >= rightHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand in low.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandInLow(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand inside (left) of their right elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.RIGHT_SHOULDER);
		Point3D neckPt = getJointPos(skel, SkeletonJoint.HEAD);
		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		if ((rightHandPt == null) || (shoulderPt == null) || (neckPt == null) || (torsoPt == null)) {
			return;
		}

		float diff = distApartXZ(rightHandPt, shoulderPt);
		boolean active = (diff < (REF_DIS/3)) && (neckPt.getY() <= rightHandPt.getY()) && (torsoPt.getY() >= rightHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand out high.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandOutHigh(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand out to the right of the their right elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D elbowPt = getJointPos(skel, SkeletonJoint.RIGHT_ELBOW);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.RIGHT_SHOULDER);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((rightHandPt == null) || (elbowPt == null) || (shoulderPt == null) || (headPt == null)) {
			return;
		}

		float diff = distApartXZ(rightHandPt, headPt);
		boolean active = (diff > (REF_DIS - (REF_DIS/4))) && (headPt.getY() >= rightHandPt.getY()) && (elbowPt.getY() >= rightHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand out low.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandOutLow(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand out to the right of the their right elbow?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.RIGHT_SHOULDER);
		Point3D neckPt = getJointPos(skel, SkeletonJoint.HEAD);
		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		if ((rightHandPt == null) || (shoulderPt == null) || (neckPt == null) || (torsoPt == null)) {
			return;
		}

		float diff = distApartXZ(rightHandPt, shoulderPt);
		boolean active = (diff > (REF_DIS - (REF_DIS/4))) && (neckPt.getY() <= rightHandPt.getY()) && (torsoPt.getY() >= rightHandPt.getY());
		gestureActivation(kinectID, active, gesture);
	}


	// -------------------------- left hand ----------------------------------

	/**
	 * Right hand point.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 * @param depthGen the depth gen
	 */
	private void rightHandPoint(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture, DepthGenerator depthGen) {
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D handPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D elbowPt = getJointPos(skel, SkeletonJoint.RIGHT_ELBOW);
		Point3D shoulderPt = getJointPos(skel, SkeletonJoint.RIGHT_SHOULDER);
		if ((handPt == null) || (elbowPt == null) || (shoulderPt == null)) {
			return;
		}

		boolean active = false;

		Vector3f upperArmUp = generatorVector(elbowPt, shoulderPt);
		Vector3f lowerArmDown = generatorVector(elbowPt, handPt);

		float elbowAngle = FastMath.abs(upperArmUp.normalize().angleBetween(lowerArmDown.normalize()));

		if (((FastMath.DEG_TO_RAD * (180 - ELBOW_ANGLE_THRESHOLD)) < elbowAngle) && (elbowAngle < (FastMath.DEG_TO_RAD * (180 + ELBOW_ANGLE_THRESHOLD)))){
			Vector3f up = new Vector3f(0, 1, 0).normalize();
			Vector3f upperArmDown = lowerArmDown.negate();

			float shoulderAngle = FastMath.abs(up.angleBetween(upperArmDown.normalize()));

			if (((FastMath.DEG_TO_RAD * SHOULDER_ANGLE_MIN_THRESHOLD) < shoulderAngle) && (shoulderAngle < (FastMath.DEG_TO_RAD * SHOULDER_ANGLE_MAX_THRESHOLD))){
				try{
					shoulderPt = depthGen.convertProjectiveToRealWorld(shoulderPt);
					handPt = depthGen.convertProjectiveToRealWorld(handPt);

					rightHandPoint = new PointDirection();
					rightHandPoint.setStartPoint(shoulderPt.getX(), shoulderPt.getY(), shoulderPt.getZ());
					rightHandPoint.setEndPoint(handPt.getX(), handPt.getY(), handPt.getZ());

					active = true;
				} catch (StatusException e) {
					e.printStackTrace();
				}
			}
		}
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand touch hip.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandTouchHip(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand touching their right hip?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}
		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D rightHipPt = getJointPos(skel, SkeletonJoint.RIGHT_HIP);
		if ((rightHandPt == null) || (rightHipPt == null)) {
			return;
		}

		float dist = distApart(rightHipPt, rightHandPt);
		boolean active = dist < REF_DIS;
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Right hand up.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void rightHandUp(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // is the user's right hand at head level or above?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D headPt = getJointPos(skel, SkeletonJoint.HEAD);
		if ((rightHandPt == null) || (headPt == null)) {
			return;
		}

		boolean active = rightHandPt.getY() <= headPt.getY();
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Turn left.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void turnLeft(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // has the user's right hip turned forward to be in front of their left hip?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHipPt = getJointPos(skel, SkeletonJoint.RIGHT_HIP);
		Point3D leftHipPt = getJointPos(skel, SkeletonJoint.LEFT_HIP);
		if ((rightHipPt == null) || (leftHipPt == null)) {
			return;
		}

		float zDiff = leftHipPt.getZ() - rightHipPt.getZ();
		boolean active = zDiff > REF_DIS;
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Turn right.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void turnRight(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){  // has the user's left hip turned forward to be in front of their right hip?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D rightHipPt = getJointPos(skel, SkeletonJoint.RIGHT_HIP);
		Point3D leftHipPt = getJointPos(skel, SkeletonJoint.LEFT_HIP);
		if ((rightHipPt == null) || (leftHipPt == null)) {
			return;
		}

		float zDiff = rightHipPt.getZ() - leftHipPt.getZ();
		boolean active = zDiff > REF_DIS;
		gestureActivation(kinectID, active, gesture);
	}

	/**
	 * Two hands apart.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void twoHandsApart(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){ // are the user's hand apart?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		if ((leftHandPt == null) || (rightHandPt == null) || (torsoPt == null)) {
			return;
		}

		float diff = distApartXY(leftHandPt, rightHandPt);
		boolean active = (diff > (REF_DIS*2)) && (torsoPt.getY() <= leftHandPt.getY()) && (torsoPt.getY() <= rightHandPt.getY());

		gestureActivation(kinectID, active, gesture);
	}


	// -------------------------- right elbow ----------------------------------

	/**
	 * Two hands near.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void twoHandsNear(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){ // are the user's hand close together?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		if ((leftHandPt == null) || (rightHandPt == null)) {
			return;
		}

		float diff = distApartXY(leftHandPt, rightHandPt);
		boolean active = diff < (REF_DIS/3);

		gestureActivation(kinectID, active, gesture);

	}

	// -------------------------- left elbow ----------------------------------

	/**
	 * Two hands near torso.
	 *
	 * @param kinectID the kinect id
	 * @param skel the skel
	 * @param gesture the gesture
	 */
	private void twoHandsNearTorso(int kinectID, HashMap<SkeletonJoint, SkeletonJointPosition> skel, PoseName gesture){ // are the user's hand close together?
		if (!poseActivity.containsKey(gesture)) {
			return;
		}

		Point3D leftHandPt = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D rightHandPt = getJointPos(skel, SkeletonJoint.RIGHT_HAND);
		Point3D torsoPt = getJointPos(skel, SkeletonJoint.TORSO);
		if ((leftHandPt == null) || (rightHandPt == null) || (torsoPt == null)) {
			return;
		}

		float diff = distApartXY(leftHandPt, rightHandPt);
		boolean active = (diff < ((REF_DIS/3)*2)) && (torsoPt.getY() <= leftHandPt.getY()) && (torsoPt.getY() <= rightHandPt.getY());

		gestureActivation(kinectID, active, gesture);

	}

}