package multiplicity3.demos.gravity.model;

import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.animation.elements.AnimationElement;

import com.jme3.math.Vector2f;

/**
 * The Class Universe.
 */
public class Universe extends AnimationElement
{

	/** The Constant MAX_DIST_FROM_UNIVERSE_CENTER. */
	private static final float MAX_DIST_FROM_UNIVERSE_CENTER = 1200;

	/** The minimum_distance_apart. */
	public float minimum_distance_apart = 1f;

	/** The bodies. */
	private List<Body> bodies;

	/** The delegate. */
	private UniverseChangeDelegate delegate;

	/** The gravitational constant. */
	private double gravitationalConstant = 1e5;

	/** The max bodies. */
	private int maxBodies = 20;

	/** The time multiplier. */
	private double timeMultiplier = 0.001;

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
			return;
		}

		this.bodies.add(b);
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
	 * Removes the far away bodies.
	 */
	private void removeFarAwayBodies()
	{
		List<Body> toRemove = new ArrayList<Body>();
		Body a;
		for (int i = 0; i < bodies.size(); i++)
		{
			a = bodies.get(i);
			if (a.getPosition().length() > MAX_DIST_FROM_UNIVERSE_CENTER)
			{
				toRemove.add(a);
			}
		}

		bodies.removeAll(toRemove);

		for (Body b : toRemove)
		{
			delegate.bodyRemoved(b);
		}
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
					dist_x = b.getPosition().x - a.getPosition().x;
					dist_y = b.getPosition().y - a.getPosition().y;
					double r2 = b.getPosition().subtract(a.getPosition()).length();
					r2 = r2 * r2;
					double force = (getGravitationalConstant() * b.mass.getMass()) / r2;
					accel_x += dist_x * force;
					accel_y += dist_y * force;
				}
			}

			accel_x /= bodies.size() - 1;
			accel_y /= bodies.size() - 1;

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
