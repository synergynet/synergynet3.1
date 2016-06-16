// Utilises Andrew Davison's ArniesTracker
// (http://fivedots.coe.psu.ac.th/~ad/jg/nui15/)

package usertracking;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import org.OpenNI.CalibrationProgressEventArgs;
import org.OpenNI.CalibrationProgressStatus;
import org.OpenNI.DepthGenerator;
import org.OpenNI.IObservable;
import org.OpenNI.IObserver;
import org.OpenNI.Point3D;
import org.OpenNI.PoseDetectionCapability;
import org.OpenNI.PoseDetectionEventArgs;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.SkeletonProfile;
import org.OpenNI.StatusException;
import org.OpenNI.UserEventArgs;
import org.OpenNI.UserGenerator;

import synergynet3.tracking.network.shared.UserColourUtils;
import usertracking.networking.TeacherControlPanel;
import usertracking.networking.TrackerNetworking;

/**
 * The Class Skeletons.
 */
public class Skeletons
{

	/**
	 * An asynchronous update interface for receiving notifications about
	 * CalibrationComplete information as the CalibrationComplete is
	 * constructed.
	 */
	class CalibrationCompleteObserver implements IObserver<CalibrationProgressEventArgs>
	{

		/*
		 * (non-Javadoc)
		 * @see org.OpenNI.IObserver#update(org.OpenNI.IObservable,
		 * java.lang.Object)
		 */
		@Override
		public void update(IObservable<CalibrationProgressEventArgs> observable, CalibrationProgressEventArgs args)
		{
			int userID = args.getUser();
			if (trackerPanel.getTeacherControlPanel().isTeacher(userID))
			{
				UserTracker.writeToAnnouncementBox("Calibration status: " + args.getStatus() + " for person " + userID);
				try
				{
					if (args.getStatus() == CalibrationProgressStatus.OK)
					{
						// calibration succeeded; move to skeleton tracking
						UserTracker.writeToAnnouncementBox("Starting tracking person " + userID);
						getSkelCap().startTracking(userID);
						userSkels.put(new Integer(userID), new HashMap<SkeletonJoint, SkeletonJointPosition>());

						getGestSeqs().addUser(userID); // add user to the
						// gesture detectors

						// create new skeleton map for the user in userSkels
					}
					else
					{
						getSkelCap().startTracking(userID);
					}
				}
				catch (StatusException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * An asynchronous update interface for receiving notifications about
	 * LostUser information as the LostUser is constructed.
	 */
	class LostUserObserver implements IObserver<UserEventArgs>
	{

		/*
		 * (non-Javadoc)
		 * @see org.OpenNI.IObserver#update(org.OpenNI.IObservable,
		 * java.lang.Object)
		 */
		@Override
		public void update(IObservable<UserEventArgs> observable, UserEventArgs args)
		{
			int userID = args.getId();

			TrackerNetworking.uniqueIDs[userID] = 0;
			TrackerNetworking.teacherStatuses[userID] = false;
			UserTracker.writeToAnnouncementBox("Lost track of person " + userID);
			userSkels.remove(userID); // remove user from userSkels
			getGestSeqs().removeUser(userID); // remove user from the gesture
			// detectors

			TrackerNetworking.removeUserLocation(userID);
			TeacherControlPanel.getInstance().fillTable();
		}
	}

	/**
	 * An asynchronous update interface for receiving notifications about
	 * NewUser information as the NewUser is constructed.
	 */
	class NewUserObserver implements IObserver<UserEventArgs>
	{

		/*
		 * (non-Javadoc)
		 * @see org.OpenNI.IObserver#update(org.OpenNI.IObservable,
		 * java.lang.Object)
		 */
		@Override
		public void update(IObservable<UserEventArgs> observable, UserEventArgs args)
		{
			int userID = args.getId();
			trackerPanel.getTeacherControlPanel().fillTable();

			UserTracker.writeToAnnouncementBox("Detected person " + userID);
		}
	}

	/**
	 * An asynchronous update interface for receiving notifications about
	 * PoseDetected information as the PoseDetected is constructed.
	 */
	class PoseDetectedObserver implements IObserver<PoseDetectionEventArgs>
	{

		/*
		 * (non-Javadoc)
		 * @see org.OpenNI.IObserver#update(org.OpenNI.IObservable,
		 * java.lang.Object)
		 */
		@Override
		public void update(IObservable<PoseDetectionEventArgs> observable, PoseDetectionEventArgs args)
		{
			int userID = args.getUser();
			UserTracker.writeToAnnouncementBox(args.getPose() + " pose detected for person " + userID);
			try
			{
				// finished pose detection; switch to skeleton calibration
				getPoseDetectionCap().stopPoseDetection(userID);
				getSkelCap().requestSkeletonCalibration(userID, true);
			}
			catch (StatusException e)
			{
				e.printStackTrace();
			}
		}
	}

	/** The Constant UPDATE_TIME. */
	protected final static int UPDATE_TIME = 500;

	/** The calib pose name. */
	private String calibPoseName = null;

	/** The depth gen. */
	private DepthGenerator depthGen;

	// gesture detectors
	/** The gest seqs. */
	private PoseSequences gestSeqs;

	/** The last frame time. */
	private long lastFrameTime = 0;

	/** The pose detection cap. */
	private PoseDetectionCapability poseDetectionCap; // to recognise when the
	// user is in a specific
	// position

	// OpenNI capabilities used by UserGenerator
	/** The skel cap. */
	private SkeletonCapability skelCap; // to output skeletal data, including
	// the location of the joints

	/** The skels gests. */
	private Poses skelsGests;

	/** The tracker panel. */
	private TrackerPanel trackerPanel;

	// OpenNI
	/** The user gen. */
	private UserGenerator userGen;

	/** The user skels. */
	private HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> userSkels; // was
	// SkeletonJointTransformation

	/*
	 * userSkels maps kinect IDs --> a joints map (i.e. a skeleton) skeleton
	 * maps joints --> positions (was positions + orientations)
	 */

	/**
	 * Instantiates a new skeletons.
	 *
	 * @param userGen
	 *            the user gen
	 * @param depthGen
	 *            the depth gen
	 * @param trackerPanel
	 *            the tracker panel
	 */
	public Skeletons(UserGenerator userGen, DepthGenerator depthGen, TrackerPanel trackerPanel)
	{
		this.userGen = userGen;
		this.depthGen = depthGen;
		this.trackerPanel = trackerPanel;

		configure();
		userSkels = new HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>>();

		// create the two gesture detectors, and tell them who to notify
		gestSeqs = new PoseSequences(trackerPanel);
		skelsGests = new Poses(trackerPanel, userSkels, gestSeqs);
	}

	/**
	 * Clear sequences.
	 */
	public void clearSequences()
	{
		gestSeqs.clearSequences();
	}

	/**
	 * Draw.
	 *
	 * @param g2d
	 *            the g2d
	 */
	public void draw(Graphics2D g2d)
	{ // draw skeleton of each user
		g2d.setStroke(new BasicStroke(8));

		try
		{
			int[] userIDs = userGen.getUsers();

			for (int i = 0; i < userIDs.length; ++i)
			{
				int userID = userIDs[i];
				setLimbColor(g2d, userID);

				updateUserPos(userID);
				drawUserStatus(g2d, userID);

				if (skelCap.isSkeletonCalibrating(userIDs[i]))
				{
				} // test to avoid occasional crashes with isSkeletonTracking()
				else if (skelCap.isSkeletonTracking(userIDs[i]))
				{
					HashMap<SkeletonJoint, SkeletonJointPosition> skel = userSkels.get(userIDs[i]);
					updateSkeletonPos(userID, skel);
					drawSkeleton(g2d, skel);
				}
			}

			long currentTime = System.currentTimeMillis();
			long dt = currentTime - lastFrameTime;
			if (dt > UPDATE_TIME)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						TrackerNetworking.broadcastUserLocations();
					}
				}).start();

				lastFrameTime = currentTime;
			}

		}
		catch (StatusException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Gets the calib pose name.
	 *
	 * @return the calib pose name
	 */
	public String getCalibPoseName()
	{
		return calibPoseName;
	}

	/**
	 * Gets the gest seqs.
	 *
	 * @return the gest seqs
	 */
	public PoseSequences getGestSeqs()
	{
		return gestSeqs;
	}

	/**
	 * Gets the pose detection cap.
	 *
	 * @return the pose detection cap
	 */
	public PoseDetectionCapability getPoseDetectionCap()
	{
		return poseDetectionCap;
	}

	/**
	 * Gets the skel cap.
	 *
	 * @return the skel cap
	 */
	public SkeletonCapability getSkelCap()
	{
		return skelCap;
	}

	/**
	 * Gets the skels gests.
	 *
	 * @return the skels gests
	 */
	public Poses getSkelsGests()
	{
		return skelsGests;
	}

	/**
	 * Sets the calib pose name.
	 *
	 * @param calibPoseName
	 *            the new calib pose name
	 */
	public void setCalibPoseName(String calibPoseName)
	{
		this.calibPoseName = calibPoseName;
	}

	/**
	 * Sets the gest seqs.
	 *
	 * @param gestSeqs
	 *            the new gest seqs
	 */
	public void setGestSeqs(PoseSequences gestSeqs)
	{
		this.gestSeqs = gestSeqs;
	}

	/**
	 * Sets the pose detection cap.
	 *
	 * @param poseDetectionCap
	 *            the new pose detection cap
	 */
	public void setPoseDetectionCap(PoseDetectionCapability poseDetectionCap)
	{
		this.poseDetectionCap = poseDetectionCap;
	}

	/**
	 * Sets the skel cap.
	 *
	 * @param skelCap
	 *            the new skel cap
	 */
	public void setSkelCap(SkeletonCapability skelCap)
	{
		this.skelCap = skelCap;
	}

	/**
	 * Sets the skels gests.
	 *
	 * @param skelsGests
	 *            the new skels gests
	 */
	public void setSkelsGests(Poses skelsGests)
	{
		this.skelsGests = skelsGests;
	}

	/**
	 * Update.
	 */
	public void update()
	{ // update skeleton of each user
		try
		{
			int[] userIDs = userGen.getUsers(); // there may be many users in
			// the scene
			for (int i = 0; i < userIDs.length; ++i)
			{
				int userID = userIDs[i];
				if (skelCap.isSkeletonCalibrating(userID))
				{
					continue; // test to avoid occasional crashes with
					// isSkeletonTracking()
				}
				if (skelCap.isSkeletonTracking(userID))
				{
					updateJoints(userID);

					// when a skeleton changes, have the detectors look for
					// gesture start/finish
					gestSeqs.checkSequences(userID);
					skelsGests.checkGests(userID, depthGen);
				}
			}
		}
		catch (StatusException e)
		{
			System.out.println(e);
		}

	}

	/**
	 * Configure.
	 */
	private void configure()
	{// create pose and skeleton detection capabilities
		// for the user generator, and set up observers
		// (listeners)
		try
		{
			// setup UserGenerator pose and skeleton detection capabilities;
			// should really check these using
			// ProductionNode.isCapabilitySupported()
			poseDetectionCap = userGen.getPoseDetectionCapability();

			skelCap = userGen.getSkeletonCapability();
			calibPoseName = skelCap.getSkeletonCalibrationPose(); // the 'psi'
			// pose
			skelCap.setSkeletonProfile(SkeletonProfile.UPPER_BODY);
			// other possible values: UPPER_BODY, LOWER_BODY, HEAD_HANDS

			trackerPanel.getTeacherControlPanel().setPoseDetection(this);

			// set up four observers
			userGen.getNewUserEvent().addObserver(new NewUserObserver()); // new
			// user
			// found
			userGen.getLostUserEvent().addObserver(new LostUserObserver()); // lost
			// a
			// user

			poseDetectionCap.getPoseDetectedEvent().addObserver(new PoseDetectedObserver()); // for
																								// when
																								// a
																								// pose
																								// is
																								// detected

			skelCap.getCalibrationCompleteEvent().addObserver(new CalibrationCompleteObserver()); // for
																									// when
																									// skeleton
																									// calibration
																									// is
																									// completed,
																									// and
																									// tracking
																									// starts
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.exit(1);
		}
	}

	/**
	 * Draw line.
	 *
	 * @param g2d
	 *            the g2d
	 * @param skel
	 *            the skel
	 * @param j1
	 *            the j1
	 * @param j2
	 *            the j2
	 */
	private void drawLine(Graphics2D g2d, HashMap<SkeletonJoint, SkeletonJointPosition> skel, SkeletonJoint j1, SkeletonJoint j2)
	{
		// draw a line (limb) between the two joints (if they have positions)
		Point3D p1 = getJointPos(skel, j1);
		Point3D p2 = getJointPos(skel, j2);
		if ((p1 != null) && (p2 != null))
		{
			g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
		}
	}

	/**
	 * Draw skeleton.
	 *
	 * @param g2d
	 *            the g2d
	 * @param skel
	 *            the skel
	 */
	private void drawSkeleton(Graphics2D g2d, HashMap<SkeletonJoint, SkeletonJointPosition> skel)
	{
		// draw skeleton as lines (limbs) between its joints; hard-wired to
		// avoid non-implemented joints
		drawLine(g2d, skel, SkeletonJoint.HEAD, SkeletonJoint.NECK);

		drawLine(g2d, skel, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.TORSO);
		drawLine(g2d, skel, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.TORSO);

		drawLine(g2d, skel, SkeletonJoint.NECK, SkeletonJoint.LEFT_SHOULDER);
		drawLine(g2d, skel, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.LEFT_ELBOW);
		drawLine(g2d, skel, SkeletonJoint.LEFT_ELBOW, SkeletonJoint.LEFT_HAND);

		drawLine(g2d, skel, SkeletonJoint.NECK, SkeletonJoint.RIGHT_SHOULDER);
		drawLine(g2d, skel, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.RIGHT_ELBOW);
		drawLine(g2d, skel, SkeletonJoint.RIGHT_ELBOW, SkeletonJoint.RIGHT_HAND);

		// drawLine(g2d, skel, SkeletonJoint.LEFT_HIP, SkeletonJoint.TORSO);
		// drawLine(g2d, skel, SkeletonJoint.RIGHT_HIP, SkeletonJoint.TORSO);
		// drawLine(g2d, skel, SkeletonJoint.LEFT_HIP, SkeletonJoint.RIGHT_HIP);

		// drawLine(g2d, skel, SkeletonJoint.LEFT_HIP, SkeletonJoint.LEFT_KNEE);
		// drawLine(g2d, skel, SkeletonJoint.LEFT_KNEE,
		// SkeletonJoint.LEFT_FOOT);

		// drawLine(g2d, skel, SkeletonJoint.RIGHT_HIP,
		// SkeletonJoint.RIGHT_KNEE);
		// drawLine(g2d, skel, SkeletonJoint.RIGHT_KNEE,
		// SkeletonJoint.RIGHT_FOOT);
	}

	/**
	 * Draw user status.
	 *
	 * @param g2d
	 *            the g2d
	 * @param userID
	 *            the user id
	 * @throws StatusException
	 *             the status exception
	 */
	private void drawUserStatus(Graphics2D g2d, int userID) throws StatusException
	{ // draw user ID and status on the skeleton
		// at its centre of mass (CoM)

		Point3D massCenter = depthGen.convertRealWorldToProjective(userGen.getUserCoM(userID));
		String label = null;
		if (skelCap.isSkeletonCalibrating(userID))
		{
			label = new String("Calibrating person " + userID);
		}
		else
		{
			label = new String("Person " + userID);
		}

		g2d.drawString(label, (int) massCenter.getX(), (int) massCenter.getY());
	}

	/**
	 * Gets the joint pos.
	 *
	 * @param skel
	 *            the skel
	 * @param j
	 *            the j
	 * @return the joint pos
	 */
	private Point3D getJointPos(HashMap<SkeletonJoint, SkeletonJointPosition> skel, SkeletonJoint j)
	{
		// get the (x, y, z) coordinate for the joint (or return null)
		SkeletonJointPosition pos = skel.get(j);
		if (pos == null)
		{
			return null;
		}
		if (pos.getConfidence() == 0)
		{
			return null; // don't draw a line to a joint with a zero-confidence
			// pos
		}
		return pos.getPosition();
	}

	/**
	 * Sets the limb color.
	 *
	 * @param g2d
	 *            the g2d
	 * @param userID
	 *            the user id
	 */
	private void setLimbColor(Graphics2D g2d, int userID)
	{ // use the
		// 'opposite' of the
		// user ID colour
		// for the limbs, so
		// they stand out
		// against the
		// colored body

		Color c = UserColourUtils.getColour(TrackerNetworking.uniqueIDs[userID]);
		Color oppColor = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
		g2d.setColor(oppColor);
	}

	/**
	 * Update joint.
	 *
	 * @param skel
	 *            the skel
	 * @param userID
	 *            the user id
	 * @param joint
	 *            the joint
	 */
	private void updateJoint(HashMap<SkeletonJoint, SkeletonJointPosition> skel, int userID, SkeletonJoint joint)
	{
		// update the position of the specified user's joint by looking at the
		// skeleton capability
		try
		{
			// report unavailable joints (should not happen)
			if (!skelCap.isJointAvailable(joint) || !skelCap.isJointActive(joint))
			{
				System.out.println(joint + " not available for updates");
				return;
			}

			SkeletonJointPosition pos = skelCap.getSkeletonJointPosition(userID, joint);
			if (pos == null)
			{
				System.out.println("No update for " + joint);
				return;
			}

			SkeletonJointPosition jPos = null;
			if (pos.getPosition().getZ() != 0)
			{ // has a depth position
				jPos = new SkeletonJointPosition(depthGen.convertRealWorldToProjective(pos.getPosition()), pos.getConfidence());
			}
			else
			{ // no info found for that user's joint
				jPos = new SkeletonJointPosition(new Point3D(), 0);
			}
			skel.put(joint, jPos);
		}
		catch (StatusException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Update joints.
	 *
	 * @param userID
	 *            the user id
	 */
	private void updateJoints(int userID)
	{ // update all the joints for this
		// userID in userSkels
		HashMap<SkeletonJoint, SkeletonJointPosition> skel = userSkels.get(userID);

		updateJoint(skel, userID, SkeletonJoint.HEAD);
		updateJoint(skel, userID, SkeletonJoint.NECK);

		updateJoint(skel, userID, SkeletonJoint.LEFT_SHOULDER);
		updateJoint(skel, userID, SkeletonJoint.LEFT_ELBOW);
		updateJoint(skel, userID, SkeletonJoint.LEFT_HAND);

		updateJoint(skel, userID, SkeletonJoint.RIGHT_SHOULDER);
		updateJoint(skel, userID, SkeletonJoint.RIGHT_ELBOW);
		updateJoint(skel, userID, SkeletonJoint.RIGHT_HAND);

		updateJoint(skel, userID, SkeletonJoint.TORSO);
		//
		// updateJoint(skel, userID, SkeletonJoint.LEFT_HIP);
		// updateJoint(skel, userID, SkeletonJoint.LEFT_KNEE);
		// updateJoint(skel, userID, SkeletonJoint.LEFT_FOOT);
		//
		// updateJoint(skel, userID, SkeletonJoint.RIGHT_HIP);
		// updateJoint(skel, userID, SkeletonJoint.RIGHT_KNEE);
		// updateJoint(skel, userID, SkeletonJoint.RIGHT_FOOT);
	}

	/**
	 * Update skeleton pos.
	 *
	 * @param userID
	 *            the user id
	 * @param skel
	 *            the skel
	 */
	private void updateSkeletonPos(int userID, HashMap<SkeletonJoint, SkeletonJointPosition> skel)
	{

		Point3D p1 = getJointPos(skel, SkeletonJoint.LEFT_HAND);
		Point3D p2 = getJointPos(skel, SkeletonJoint.RIGHT_HAND);

		try
		{
			if ((p1 != null) && (p2 != null))
			{
				p1 = depthGen.convertProjectiveToRealWorld(p1);
				p2 = depthGen.convertProjectiveToRealWorld(p2);
				TrackerNetworking.setBothUserHandLocations(userID, p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
			}
			else if (p1 != null)
			{
				p1 = depthGen.convertProjectiveToRealWorld(p1);
				TrackerNetworking.setSingleUserHandLocation(userID, p1.getX(), p1.getY(), p1.getZ());
			}
			else if (p2 != null)
			{
				p2 = depthGen.convertProjectiveToRealWorld(p2);
				TrackerNetworking.setSingleUserHandLocation(userID, p2.getX(), p2.getY(), p2.getZ());
			}
		}
		catch (StatusException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Update user pos.
	 *
	 * @param userID
	 *            the user id
	 * @throws StatusException
	 *             the status exception
	 */
	private void updateUserPos(int userID) throws StatusException
	{ // draw user
		// ID and
		// status on
		// the
		// skeleton
		// at its
		// centre of
		// mass
		// (CoM)
		Point3D massCenter = userGen.getUserCoM(userID);
		TrackerNetworking.setUserBodyLocation(userID, massCenter.getX(), massCenter.getZ());
	}

}
