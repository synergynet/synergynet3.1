package multiplicity3.csys.items;

import multiplicity3.csys.items.item.IItem;

/**
 * The Interface IPalet.
 */
public interface IPalet extends IItem {

	/**
	 * Lock palet.
	 *
	 * @param locked the locked
	 */
	public void lockPalet(boolean locked);

	/**
	 * Reset taps.
	 */
	public void resetTaps();

	/**
	 * Tap.
	 *
	 * @return the int
	 */
	public int tap();

}
