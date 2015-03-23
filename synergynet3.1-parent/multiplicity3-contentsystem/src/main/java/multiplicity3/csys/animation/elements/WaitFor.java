package multiplicity3.csys.animation.elements;

/**
 * The Class WaitFor.
 */
public class WaitFor extends AnimationElement {

	/** The elapsed. */
	private float elapsed;

	/** The finished. */
	private boolean finished;

	/** The seconds. */
	private float seconds;

	/**
	 * Instantiates a new wait for.
	 *
	 * @param seconds the seconds
	 */
	public WaitFor(float seconds) {
		this.seconds = seconds;
		this.elapsed = 0f;
		finished = false;
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

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return finished;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset() {
		elapsed = 0f;
		finished = false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#updateAnimationState
	 * (float)
	 */
	@Override
	public void updateAnimationState(float tpf) {
		elapsed += tpf;
		if (elapsed > seconds) {
			finished = true;
		}
	}

}
