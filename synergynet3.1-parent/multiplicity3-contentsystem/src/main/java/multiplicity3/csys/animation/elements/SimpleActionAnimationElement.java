package multiplicity3.csys.animation.elements;

/**
 * The Class SimpleActionAnimationElement.
 */
public abstract class SimpleActionAnimationElement extends AnimationElement
{

	/** The finished. */
	private boolean finished = false;

	/**
	 * Do simple action.
	 */
	public abstract void doSimpleAction();

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#elementStart(float
	 * )
	 */
	@Override
	public void elementStart(float tpf)
	{
		doSimpleAction();
		finished = true;
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
	}

}
