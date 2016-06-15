package synergynet3.apps.numbernet.ui.animation;

import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

/**
 * The Class MoveItem.
 */
public class MoveItem extends AnimationElement
{

	/** The current pos. */
	private Vector2f currentPos;

	/** The direction. */
	private Vector2f direction;

	/** The elapsed time. */
	private float elapsedTime = 0f;

	/** The finished. */
	private boolean finished;

	/** The from. */
	private Vector2f from;

	/** The in seconds. */
	private float inSeconds;

	/** The item. */
	private IItem item;

	/** The relative. */
	private boolean relative = true;

	/** The to. */
	private Vector2f to;

	/** The units per second. */
	private float unitsPerSecond;

	/**
	 * Instantiates a new move item.
	 *
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param inSeconds
	 *            the in seconds
	 * @param item
	 *            the item
	 * @param inWorldCoords
	 *            the in world coords
	 */
	public MoveItem(Vector2f from, Vector2f to, float inSeconds, IItem item, boolean inWorldCoords)
	{
		this.inSeconds = inSeconds;
		this.from = from;
		this.to = to;
		this.item = item;
		relative = !inWorldCoords;
		reset();
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
		currentPos = from.clone();
		direction = to.subtract(from).normalize();
		unitsPerSecond = to.subtract(from).length() / inSeconds;
		elapsedTime = 0f;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished()
	{
		return finished;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset()
	{
		finished = false;
		currentPos = from.clone();
		direction = to.subtract(from).normalize();
		elapsedTime = 0f;
		unitsPerSecond = to.subtract(from).length() / inSeconds;
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
		elapsedTime += tpf;
		currentPos.addLocal(direction.mult(unitsPerSecond * tpf));
		// if(currentPos.distance(to) < 0.2f) {
		if (elapsedTime > inSeconds)
		{
			currentPos.set(to);
			finished = true;
		}
		updatePosition();
	}

	/**
	 * Update position.
	 */
	private void updatePosition()
	{
		if (relative)
		{
			item.setRelativeLocation(currentPos);
		}
		else
		{
			item.setWorldLocation(currentPos);
		}
	}

}
