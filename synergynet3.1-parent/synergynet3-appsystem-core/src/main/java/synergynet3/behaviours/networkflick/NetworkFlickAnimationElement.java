package synergynet3.behaviours.networkflick;

import java.io.File;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.SynergyNetApp;
import synergynet3.additionalitems.jme.MediaPlayer;
import synergynet3.behaviours.BehaviourUtilities;
import synergynet3.behaviours.BehaviourUtilities.RelativePosition;
import synergynet3.behaviours.networkflick.NetworkFlickLogging.FLICKTYPE;
import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.feedbacksystem.FeedbackContainer;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.positioning.SynergyNetPositioning;
import synergynet3.web.core.AppSystemControlComms;

import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Repositions an item when flick using the JME animation system. Calculates
 * trajectories when an item bounces off a display border and detects when an
 * item should be transferred. Initiates network transfers when required.
 */
public class NetworkFlickAnimationElement extends AnimationElement {

	/**
	 * How far an item needs to travel off screen to be transferred.
	 */
	private static final float TRANSFER_DISTANCE_RATIO = 2.2f;

	/**
	 * The current direction and momentum of the managed item's flick motion.
	 */
	private Vector2f currentVelocity;

	/**
	 * The deceleration of the item in pixels squared per second.
	 */
	private float deceleration = 100f;

	/**
	 * Location of item when transfer initiated.
	 */
	private Vector2f exitLoc = null;

	/**
	 * Used to stop further calculations when an item when an item is to be
	 * transferred or has stopped moving.
	 */
	private boolean finished;

	/**
	 * Count of bounces due to inaccuracy
	 */
	private int inaccurateBounces = 0;

	/**
	 * The item which is being managed by this class.
	 */
	private IItem item;

	/**
	 * The largest out of the width or height of the managed item
	 */
	private float maxDim = 200;

	/**
	 * Structured message to be sent when the managed item is transferred.
	 */
	private FlickMessage message = null;

	/**
	 * Count of bounces due to lack of momentum
	 */
	private int momentumBounces = 0;

	/**
	 * Share of initial velocity to be returned on bounce.
	 */
	private final float reboundEnergyFactor = 0.6f;

	/**
	 * The environment the managed item belongs to.
	 */
	private IStage stage;

	/**
	 * Where the item is destined to arrive on a flick relative to the local
	 * table.
	 */
	private Vector2f targetLocation;

	/**
	 * Creates an animation element for the supplied item which can then create
	 * the flick motion by repositioning the managed item using JME's animation
	 * system.
	 *
	 * @param item The item to be influenced by this animation.
	 * @param stage The stage the item currently resides in.
	 */
	public NetworkFlickAnimationElement(IItem item, IStage stage) {
		this.item = item;
		this.stage = stage;
	}

	/**
	 * Bounce.
	 *
	 * @param vX the v x
	 * @param vY the v y
	 * @param tpf the tpf
	 * @param bouncePosition the bounce position
	 */
	public void bounce(float vX, float vY, float tpf,
			RelativePosition bouncePosition) {

		boolean boostNeeded = false;

		if ((currentVelocity == null) || finished) {
			moveWithVelocity(new Vector2f());
			boostNeeded = true;
		}

		float xVelocity = currentVelocity.x;
		float yVelocity = currentVelocity.y;

		switch (bouncePosition) {
			case TOPLEFT:
				if (xVelocity > 0) {
					xVelocity = -xVelocity * reboundEnergyFactor;
				}
				if (yVelocity < 0) {
					yVelocity = -yVelocity * reboundEnergyFactor;
				}
				break;
			case TOP:
				if (yVelocity < 0) {
					yVelocity = -yVelocity * reboundEnergyFactor;
				}
				break;
			case TOPRIGHT:
				if (xVelocity < 0) {
					xVelocity = -xVelocity * reboundEnergyFactor;
				}
				if (yVelocity < 0) {
					yVelocity = -yVelocity * reboundEnergyFactor;
				}
				break;
			case LEFT:
				if (xVelocity > 0) {
					xVelocity = -xVelocity * reboundEnergyFactor;
				}
				break;
			case RIGHT:
				if (xVelocity < 0) {
					xVelocity = -xVelocity * reboundEnergyFactor;
				}
				break;
			case BOTTOMLEFT:
				if (xVelocity > 0) {
					xVelocity = -xVelocity * reboundEnergyFactor;
				}
				if (yVelocity > 0) {
					yVelocity = -yVelocity * reboundEnergyFactor;
				}
				break;
			case BOTTOM:
				if (yVelocity > 0) {
					yVelocity = -yVelocity * reboundEnergyFactor;
				}
				break;
			case BOTTOMRIGHT:
				if (xVelocity < 0) {
					xVelocity = -xVelocity * reboundEnergyFactor;
				}
				if (yVelocity > 0) {
					yVelocity = -yVelocity * reboundEnergyFactor;
				}
				break;
		}

		Vector2f v = new Vector2f(vX, vY);
		v = v.mult(tpf / 10);
		xVelocity += v.x;
		yVelocity += v.y;
		currentVelocity.setX(xVelocity);
		currentVelocity.setY(yVelocity);

		if (boostNeeded) {
			AnimationSystem.getInstance().add(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#elementStart(float
	 * )
	 */
	@Override
	public void elementStart(float tpf) {
	}

	/**
	 * Gets the deceleration at which the managed item's momentum is reduced.
	 *
	 * @return A float representing the deceleration at which the managed item's
	 *         momentum is reduced.
	 */
	public float getDeceleration() {
		return this.deceleration;
	}

	/**
	 * Uses the managed item's bounding box to find the largest of its width or
	 * height. If the managed item does not have a bounding box then a
	 * predetermined value is used.
	 *
	 * @return A float representing the largest of the managed item's width or
	 *         height.
	 */
	public float getMaxDimension() {
		try {
			float x = ((BoundingBox) item.getManipulableSpatial()
					.getWorldBound()).getXExtent();
			float y = ((BoundingBox) item.getManipulableSpatial()
					.getWorldBound()).getYExtent();
			if (x > y) {
				return x;
			} else {
				return y;
			}
		} catch (Exception e) {
			return maxDim * item.getRelativeScale();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return finished;
	}

	/**
	 * This method effectively initiates the flick of the managed item.
	 *
	 * @param velocity The direction and momentum with which the managed item is
	 *            to be flicked.
	 */
	public void moveWithVelocity(Vector2f velocity) {
		this.currentVelocity = velocity.clone();
		this.finished = false;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset() {
		finished = true;
		currentVelocity = new Vector2f();
		message = null;
	}

	/**
	 * Sets the deceleration at which the managed item's momentum is reduced.
	 *
	 * @param drag The deceleration at which the managed item's momentum is
	 *            reduced.
	 */
	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}

	/**
	 * Sets the predetermined max dimension of the managed item. This is used
	 * for items which may not have a bounding box.
	 *
	 * @param maxDim The predetermined max dimension of the managed item
	 */
	public void setMaxDimension(float maxDim) {
		this.maxDim = maxDim;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#updateAnimationState
	 * (float)
	 */
	@Override
	public void updateAnimationState(float tpf) {
		if (message == null) {
			if (!finished) {
				item.setWorldLocation(item.getWorldLocation().add(
						currentVelocity.mult(tpf)));

				boolean outside = false;

				if (item.getWorldLocation().x > stage.getDisplayWidth()) {
					outside = true;
					if (currentVelocity.x > 0) {
						if (!initiateTransfer()) {
							currentVelocity.setX(-currentVelocity.getX());
						}
					}
				} else if (item.getWorldLocation().x < 0) {
					outside = true;
					if (currentVelocity.x < 0) {
						if (!initiateTransfer()) {
							currentVelocity.setX(-currentVelocity.getX());
						}
					}
				}

				if (!finished) {
					if (item.getWorldLocation().y > stage.getDisplayHeight()) {
						outside = true;
						if (currentVelocity.y > 0) {
							if (!initiateTransfer()) {
								currentVelocity.setY(-currentVelocity.getY());
							}
						}
					} else if (item.getWorldLocation().y < 0) {
						outside = true;
						if (currentVelocity.y < 0) {
							if (!initiateTransfer()) {
								currentVelocity.setY(-currentVelocity.getY());
							}
						}
					}

					if (!finished
							&& !(outside && (currentVelocity.length() < 1f))) {
						Vector2f reduceBy = currentVelocity.normalize().mult(
								getDeceleration() * tpf);
						currentVelocity.subtractLocal(reduceBy);

						if (currentVelocity.length() < 1f) {
							finished = true;
						}
					}
				}
			}
		} else {

			if (!finished && !(currentVelocity.length() < 1f)) {
				Vector2f reduceBy = currentVelocity.normalize().mult(
						getDeceleration() * tpf);
				currentVelocity.subtractLocal(reduceBy);
			}

			item.setWorldLocation(item.getWorldLocation().add(
					currentVelocity.mult(tpf)));
			if (exitLoc.distance(item.getWorldLocation()) > (getMaxDimension() * (TRANSFER_DISTANCE_RATIO / 2))) {
				transfer();
			}
		}
	}

	/**
	 * Log the reflecting of an item off the boundary if it is not being
	 * transferred.
	 */
	private boolean bounceBack() {
		Vector2f loc = item.getWorldLocation();
		if (NetworkFlickLogging.LOGGING_ENABLED) {
			NetworkFlickLogging.generateInaccurateBounceMessage(item.getName(),
					loc);
		}
		if (NetworkFlickLogging.BOUNCE_LIMIT > 0) {
			inaccurateBounces++;
			if (inaccurateBounces >= NetworkFlickLogging.BOUNCE_LIMIT) {
				finished = true;
				stage.removeItem(item);
				NetworkFlickLogging
						.generateRemovalDueToInaccurateBounceMessage(
								item.getName(), loc);
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the latency.
	 *
	 * @param remoteTable the remote table
	 * @return the latency
	 */
	private long getLatency(String remoteTable) {
		long latency = AppSystemControlComms.get().getLatency(remoteTable);
		if (latency < 0) {
			latency = 0;
		}
		return 0;
	}

	/**
	 * Calculates the speed at which the managed item is currently travelling in
	 * pixels per second.
	 *
	 * @return A float representing the speed at which the managed item is
	 *         currently travelling in pixels per second.
	 */
	private float getSpeed() {
		return FastMath.sqrt(FastMath.sqr(currentVelocity.x)
				+ FastMath.sqr(currentVelocity.y));
	}

	/**
	 * If the network flick feature is enabled by the current application this
	 * method creates a ray along the managed item's current trajectory to
	 * detect if another devices' interface lies in its path. If so the method
	 * then checks that the item has enough momentum to travel off screen. If
	 * this is so the method then constructs a structured method to be sent
	 * across the network to instigate a network flick.
	 *
	 * @return A boolean value representing whether the managed item is to be
	 *         transfered.
	 */
	private boolean initiateTransfer() {
		if (!SynergyNetApp.networkFlickEnabled) {
			return false;
		}
		if (((item instanceof IItemCachable) || FeedbackSystem
				.isItemFeedbackContainer(item))
				&& FeedbackSystem.isItemFeedbackEligible(item)) {

			float distance = -1;

			Ray ray = new Ray(new Vector3f(item.getRelativeLocation().x,
					item.getRelativeLocation().y, 0), new Vector3f(
					currentVelocity.x, currentVelocity.y, 0));
			Spatial targetTable = null;
			Vector2f locationFromTargetTableCentre = null;
			float targetAngle = 0;
			float targetScale = 1;
			float rot = 0;
			String targetTableID = "";

			for (Spatial table : VirtualTableUtilities.otherTables.values()) {

				CollisionResults results = new CollisionResults();
				ray.collideWith(table.getWorldBound(), results);
				if (results.size() > 0) {
					CollisionResult result = results.getClosestCollision();
					Vector3f interactionLocation = result.getContactPoint();
					Vector2f closestTargetLocation = new Vector2f(
							interactionLocation.x, interactionLocation.y);

					if ((distance == -1)
							|| (distance > targetLocation.distance(item
									.getRelativeLocation()))) {

						targetTable = table;
						targetLocation = closestTargetLocation;

						targetTableID = VirtualTableUtilities
								.getIDForTable(targetTable);
						rot = VirtualTableUtilities.otherTablesOrientation
								.get(targetTableID);

						if (BehaviourUtilities.FLICK_TYPE != FLICKTYPE.INSTANT) {
							interactionLocation = interactionLocation
									.add(ray.origin
											.subtract(interactionLocation)
											.normalize()
											.mult(getMaxDimension()
													* (TRANSFER_DISTANCE_RATIO / 2)));
						}

						locationFromTargetTableCentre = new Vector2f(
								interactionLocation.x
										- table.getWorldTranslation().x,
								interactionLocation.y
										- table.getWorldTranslation().y);

						locationFromTargetTableCentre.rotateAroundOrigin(
								SynergyNetApp.localDevicePosition
										.getOrientation(), true);
						locationFromTargetTableCentre.rotateAroundOrigin(rot,
								false);

						targetAngle = item.getRelativeRotation()
								+ (rot - SynergyNetApp.localDevicePosition
										.getOrientation());

						targetScale = item.getRelativeScale()
								* VirtualTableUtilities.otherTablesScaleChanges
										.get(targetTableID);
					}
				}
			}

			if (targetTable != null) {
				if (!withinStoppingDistance()) {
					targetTable = null;
				}
			} else {
				if (!bounceBack()) {
					return false;
				}
			}

			if (targetTable != null) {

				final String id = SynergyNetCluster.get().getIdentity();
				final String finalTargetTableID = targetTableID;
				final Vector2f finalLocationFromTargetTableCentre = locationFromTargetTableCentre;
				final float finalRot = rot;
				final float finalTargetAngle = targetAngle;
				final float finalTargetScale = targetScale;

				Thread cachingThread = new Thread(new Runnable() {
					public void run() {

						Vector2f newFlickDirection = currentVelocity.clone();
						newFlickDirection.rotateAroundOrigin(
								SynergyNetApp.localDevicePosition
										.getOrientation(), true);
						newFlickDirection.rotateAroundOrigin(finalRot, false);

						float width = FeedbackSystem
								.getFedbackEligbleItemDimensions(item).x;
						float height = FeedbackSystem
								.getFedbackEligbleItemDimensions(item).y;
						FeedbackContainer feedbackContained = null;
						if (FeedbackSystem.isItemFeedbackContainer(item)) {
							feedbackContained = FeedbackSystem
									.getFeedbackContainer(item);
						}
						Object[] info = { width, height, feedbackContained };

						GalleryItemDatabaseFormat galleryItem = ItemCaching
								.deconstructItem(
										item,
										info,
										CacheOrganisation.TRANSFER_DIR
												+ File.separator
												+ CacheOrganisation.NETWORK_FLICK_DIR);

						float locationFromTargetTableCentreXinMetres = SynergyNetPositioning
								.getMetreValue((int) finalLocationFromTargetTableCentre.x);
						float locationFromTargetTableCentreYinMetres = SynergyNetPositioning
								.getMetreValue((int) finalLocationFromTargetTableCentre.y);

						if (!id.equals(finalTargetTableID)) {

							exitLoc = item.getWorldLocation();

							message = new FlickMessage(finalTargetTableID, id,
									galleryItem,
									locationFromTargetTableCentreXinMetres,
									locationFromTargetTableCentreYinMetres,
									finalTargetAngle, finalTargetScale,
									newFlickDirection.x, newFlickDirection.y);

							if (BehaviourUtilities.FLICK_TYPE == FLICKTYPE.INSTANT) {
								transfer();
							}
						}

					}
				});
				cachingThread.start();

				if (!id.equals(targetTableID)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * If a network flick message has been created this method will instigate
	 * its transmission through the cluster to the appropriate device. The
	 * managed item is then removed from the application.
	 */
	private void transfer() {

		Vector2f worldLoc = item.getWorldLocation();
		Vector2f localLoc = item.getRelativeLocation();
		if (NetworkFlickLogging.LOGGING_ENABLED) {
			NetworkFlickLogging.generateFlickSentMessage(item.getName(),
					message.getTargetTableID(), worldLoc);
		}
		finished = true;
		IItem parent = item.getParentItem();
		try {
			if (item instanceof MediaPlayer) {
				((MediaPlayer) item).destroy();
			}
			parent.removeItem(item);
		} catch (Exception e) {
		}

		if (BehaviourUtilities.FLICK_TYPE == FLICKTYPE.PROPORTIONAL) {

			float distanceToTravel = localLoc.distance(targetLocation);
			float velocityOnArrival = FastMath.sqrt(FastMath.sqr(getSpeed())
					+ (2 * -getDeceleration() * distanceToTravel));
			final long timeToTravel = (long) (((FastMath.abs(getSpeed()) - FastMath
					.abs(velocityOnArrival)) / FastMath.abs(getDeceleration())) * 1000f);

			float speedChange = velocityOnArrival / getSpeed();
			if (speedChange <= 0) {
				message.setXDir(speedChange * message.getXDir());
				message.setYDir(speedChange * message.getYDir());
			}

			final long latency = getLatency(message.getTargetTableID());

			Runnable messageDispatch = new Runnable() {
				public void run() {
					try {
						Thread.sleep(timeToTravel - latency);

						AppSystemControlComms.get().networkFlick(message,
								message.getTargetTableID());
						message = null;

					} catch (InterruptedException ie) {
					}
				}
			};
			Thread sendThread = new Thread(messageDispatch);
			sendThread.start();
		} else {
			AppSystemControlComms.get().networkFlick(message,
					message.getTargetTableID());
			message = null;
		}

		currentVelocity = new Vector2f();

	}

	/**
	 * Checks to see if the managed item current has enough momentum to travel
	 * three times its max dimension. This is enough distance for the managed
	 * item to travel off screen after its collision with a boundary when
	 * transferred.
	 *
	 * @return A boolean value representing whether the managed item has enough
	 *         momentum to travel entirely off screen.
	 */
	private boolean withinStoppingDistance() {

		float stoppingDistance = -FastMath.sqr(getSpeed())
				/ (2 * -getDeceleration());
		float travelDistance = 0;
		if (BehaviourUtilities.FLICK_TYPE == FLICKTYPE.INSTANT) {
			return true;
		} else if (BehaviourUtilities.FLICK_TYPE == FLICKTYPE.PROPORTIONAL) {
			travelDistance = item.getRelativeLocation()
					.distance(targetLocation);
		} else {
			travelDistance = getMaxDimension() * TRANSFER_DISTANCE_RATIO;
		}

		if (stoppingDistance > travelDistance) {
			return true;
		}

		Vector2f loc = item.getWorldLocation();
		if (NetworkFlickLogging.LOGGING_ENABLED) {
			NetworkFlickLogging.generateLackOfMomentumBounceMessage(
					item.getName(), loc);
		}
		if (NetworkFlickLogging.BOUNCE_LIMIT > 0) {
			momentumBounces++;
			if (momentumBounces >= NetworkFlickLogging.BOUNCE_LIMIT) {
				finished = true;
				stage.removeItem(item);
				NetworkFlickLogging
						.generateRemovalDueToLackOfMomentumBounceMessage(
								item.getName(), loc);
				return false;
			}
		}
		return false;
	}

}
