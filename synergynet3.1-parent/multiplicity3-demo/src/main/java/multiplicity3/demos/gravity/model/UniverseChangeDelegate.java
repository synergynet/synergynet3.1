package multiplicity3.demos.gravity.model;

/**
 * The Interface UniverseChangeDelegate.
 */
public interface UniverseChangeDelegate
{

	/**
	 * Body added.
	 *
	 * @param b
	 *            the b
	 */
	public void bodyAdded(Body b);

	/**
	 * Body position changed.
	 *
	 * @param body
	 *            the body
	 */
	public void bodyPositionChanged(Body body);

	/**
	 * Body removed.
	 *
	 * @param b
	 *            the b
	 */
	public void bodyRemoved(Body b);
}
