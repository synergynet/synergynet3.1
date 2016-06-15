package synergynet3.activitypack1.table.gravitysim.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import multiplicity3.csys.animation.elements.AnimationElement;
import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.jme3.math.Vector2f;

/**
 * The Class Universe.
 */
public class Universe extends AnimationElement
{

	/** The Constant MASS_EARTH. */
	public static final double MASS_EARTH = 1;

	/** The Constant MASS_SUN. */
	public static final double MASS_SUN = 1e6;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(Universe.class.getName());

	/** The Constant MAX_DIST_FROM_UNIVERSE_CENTER. */
	private static final float MAX_DIST_FROM_UNIVERSE_CENTER = 1200;

	/** The minimum_distance_apart. */
	public float minimum_distance_apart = 1f;

	/** The bodies. */
	private List<Body> bodies;

	/** The default body mass. */
	private MassReference defaultBodyMass = new MassReference(1);

	/** The delegate. */
	private UniverseChangeDelegate delegate;

	/** The gravitational constant. */
	private double gravitationalConstant = 1e5;

	/** The max bodies. */
	private int maxBodies = 20;

	/** The time multiplier. */
	private double timeMultiplier = 0.001;;

	/**
	 * Instantiates a new universe.
	 *
	 * @param delegate
	 *            the delegate
	 */
	public Universe(UniverseChangeDelegate delegate)
	{
		bodies = new ArrayList<Body>();
		this.delegate = delegate;
	}

	/**
	 * Adds the body.
	 *
	 * @param b
	 *            the b
	 */
	public void addBody(Body b)
	{
		if (bodies.contains(b))
		{
			log.fine("body already there...");
			return;
		}

		this.bodies.add(b);
		log.fine("telling delegate " + delegate);
		delegate.bodyAdded(b);
	}

	/**
	 * Can add more.
	 *
	 * @return true, if successful
	 */
	public boolean canAddMore()
	{
		return bodies.size() < getMaxBodies();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#elementStart(float
	 * )
	 */
	@Override
	public void elementStart(float tpf)
	{
	}

	/**
	 * Gets the default body mass.
	 *
	 * @return the default body mass
	 */
	public MassReference getDefaultBodyMass()
	{
		return defaultBodyMass;
	}

	/**
	 * Gets the gravitational constant.
	 *
	 * @return the gravitational constant
	 */
	public double getGravitationalConstant()
	{
		return gravitationalConstant;
	}

	/**
	 * Gets the max bodies.
	 *
	 * @return the max bodies
	 */
	public int getMaxBodies()
	{
		return maxBodies;
	}

	/**
	 * Gets the time multiplier.
	 *
	 * @return the time multiplier
	 */
	public double getTimeMultiplier()
	{
		return timeMultiplier;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished()
	{
		return false;
	}

	/**
	 * Removes the all bodies.
	 */
	public void removeAllBodies()
	{
		List<Body> removeList = new ArrayList<Body>();
		removeList.addAll(bodies);
		bodies.clear();
		for (Body b : removeList)
		{
			delegate.bodyRemoved(b);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset()
	{
	}

	/**
	 * Sets the gravitational constant.
	 *
	 * @param gravitationalConstant
	 *            the new gravitational constant
	 */
	public void setGravitationalConstant(double gravitationalConstant)
	{
		this.gravitationalConstant = gravitationalConstant;
	}

	/**
	 * Sets the max bodies.
	 *
	 * @param maxBodies
	 *            the new max bodies
	 */
	public void setMaxBodies(int maxBodies)
	{
		this.maxBodies = maxBodies;
		List<Body> toRemove = new ArrayList<Body>();
		if (bodies.size() > maxBodies)
		{
			for (int i = maxBodies; i < bodies.size(); i++)
			{
				toRemove.add(bodies.get(i));
			}
		}
		removeBodies(toRemove);
	}

	/**
	 * Sets the scenario.
	 *
	 * @param newValue
	 *            the new scenario
	 */
	public void setScenario(UniverseScenario newValue)
	{
		log.info("loading scenario " + newValue);
		switch (newValue)
		{
			case SUN_AND_MOONS:
			{
				log.info("Loading scenario for sun and moons");
				removeAllBodies();
				gravitationalConstant = 1e5;
				timeMultiplier = 0.001;
				Body sun = new Body("sun", new MassReference(MASS_SUN), new Vector2f(512, 768 / 2), new Vector2f(0, 0));
				log.fine("adding sun");
				addBody(sun);
				log.fine("updating position via delegate");
				delegate.bodyPositionChanged(sun);
				sun.setActive();
				defaultBodyMass = new MassReference(1);
				break;
			}
			case MOONS_ONLY:
			{
				log.info("Loading scenario for moons only");
				gravitationalConstant = 1e11;
				timeMultiplier = 0.0001;
				defaultBodyMass = new MassReference(1);
				removeAllBodies();
				break;
			}

			case BINARY_STAR_SYSTEM:
			{
				log.info("Loading binary star system.");
				Vector2f middle = new Vector2f(512, 768 / 2);
				removeAllBodies();
				gravitationalConstant = 1e5;
				timeMultiplier = 0.0002;
				// timeMultiplier = 0.0006;

				int distDiff = 100;
				int vel = 50;

				Vector2f vel1 = new Vector2f(vel, 0);
				vel1.multLocal(5e3f);
				Body sun = new Body("sun", new MassReference(MASS_SUN), middle.add(new Vector2f(0, distDiff)), vel1);
				log.fine("adding sun");
				addBody(sun);
				log.fine("updating position via delegate");
				delegate.bodyPositionChanged(sun);
				sun.setActive();

				Vector2f vel2 = new Vector2f(-vel, 0);
				vel2.multLocal(5e3f);
				Body sun2 = new Body("sun", new MassReference(MASS_SUN), middle.add(new Vector2f(0, -distDiff)), vel2);
				log.fine("adding sun");
				addBody(sun2);
				log.fine("updating position via delegate");
				delegate.bodyPositionChanged(sun2);
				sun2.setActive();

				defaultBodyMass = new MassReference(1);

				break;
			}

		}
	}

	/**
	 * Sets the time multiplier.
	 *
	 * @param timeMultiplier
	 *            the new time multiplier
	 */
	public void setTimeMultiplier(double timeMultiplier)
	{
		this.timeMultiplier = timeMultiplier;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#updateAnimationState
	 * (float)
	 */
	@Override
	public void updateAnimationState(float tpf)
	{
		if (bodies.size() < 2)
		{
			return;
		}

		updateUniverse(tpf);

	}

	/**
	 * Removes the bodies.
	 *
	 * @param toRemove
	 *            the to remove
	 */
	private void removeBodies(List<Body> toRemove)
	{
		bodies.removeAll(toRemove);

		for (Body b : toRemove)
		{
			delegate.bodyRemoved(b);
		}
	}

	/**
	 * Removes the far away bodies.
	 */
	private void removeFarAwayBodies()
	{
		List<Body> toRemove = new ArrayList<Body>();
		Body a;
		for (int i = 0; i < bodies.size(); i++)
		{
			a = bodies.get(i);
			if (a.getPosition().subtract(512, 768 / 2).length() > MAX_DIST_FROM_UNIVERSE_CENTER)
			{
				toRemove.add(a);
			}
		}

		removeBodies(toRemove);
	}

	/**
	 * Update all body positions.
	 *
	 * @param universeTime
	 *            the universe time
	 */
	private void updateAllBodyPositions(double universeTime)
	{
		Body body;
		for (int i = 0; i < bodies.size(); i++)
		{
			body = bodies.get(i);
			if (body.shouldIgnore())
			{
				continue;
			}
			body.setPosition(body.getPosition().x + (float) (body.velocity.x * universeTime), body.getPosition().y + (float) (body.velocity.y * universeTime));
			delegate.bodyPositionChanged(body);
		}
	}

	/**
	 * Update all body velocities.
	 *
	 * @param universeTime
	 *            the universe time
	 */
	private void updateAllBodyVelocities(double universeTime)
	{
		Body a, b;
		Vector2f oldVelocity = new Vector2f();
		for (int i = 0; i < bodies.size(); i++)
		{
			a = bodies.get(i);
			if (a.shouldIgnore())
			{
				continue;
			}
			oldVelocity.set(a.velocity.x, a.velocity.y);

			double accel_x = 0;
			double accel_y = 0;
			double dist_x = 0;
			double dist_y = 0;
			for (int j = 0; j < bodies.size(); j++)
			{
				if (i != j)
				{
					b = bodies.get(j);
					if (b.shouldIgnore())
					{
						continue;
					}
					dist_x = b.getPosition().x - a.getPosition().x;
					dist_y = b.getPosition().y - a.getPosition().y;
					double r2 = b.getPosition().subtract(a.getPosition()).length();
					r2 = r2 * r2;
					if (r2 < 0.00000001)
					{
						r2 = 0.00000001;
					}
					double force = (getGravitationalConstant() * b.mass.getMass()) / r2;
					accel_x += dist_x * force;
					accel_y += dist_y * force;
				}
			}

			// accel_x /= bodies.size() - 1;
			// accel_y /= bodies.size() - 1;

			a.velocity.addLocal((float) (universeTime * accel_x), (float) (universeTime * accel_y));

		}
	}

	/**
	 * Update universe.
	 *
	 * @param tpf
	 *            the tpf
	 */
	private void updateUniverse(float tpf)
	{
		double universeTime = tpf * timeMultiplier;
		updateAllBodyVelocities(universeTime);
		updateAllBodyPositions(universeTime);
		removeFarAwayBodies();
	}
}
