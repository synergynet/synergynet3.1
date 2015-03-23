package multiplicity3.csys.items.keyboard.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class KeyboardDefinition.
 */
public class KeyboardDefinition {

	/** The keys collection. */
	private List<KeyboardKey> keysCollection = new ArrayList<KeyboardKey>();

	/** The string to key map. */
	private Map<String, KeyboardKey> stringToKeyMap = new HashMap<String, KeyboardKey>();

	/**
	 * Adds the key.
	 *
	 * @param k the k
	 */
	public void addKey(KeyboardKey k) {
		keysCollection.add(k);
		stringToKeyMap.put(k.getKeyStringRepresentation(), k);
	}

	/**
	 * Adds the keys.
	 *
	 * @param qwertyKeys the qwerty keys
	 */
	public void addKeys(Collection<KeyboardKey> qwertyKeys) {
		for (KeyboardKey k : qwertyKeys) {
			addKey(k);
		}
	}

	/**
	 * Gets the bounds.
	 *
	 * @return the bounds
	 */
	public Rectangle2D getBounds() {
		Rectangle2D rect = new Rectangle2D.Float();
		if (keysCollection.size() > 0) {
			rect.setRect(keysCollection.get(0).getKeyShape().getBounds());
		}
		for (KeyboardKey k : keysCollection) {
			rect = rect.createUnion(k.getKeyShape().getBounds());
		}
		rect.setRect(rect.getX(), rect.getY(),
				rect.getWidth() + rect.getMinX(),
				rect.getHeight() + rect.getMinY());
		return rect;
	}

	/**
	 * Gets the key.
	 *
	 * @param stringRep the string rep
	 * @return the key
	 */
	public KeyboardKey getKey(String stringRep) {
		return stringToKeyMap.get(stringRep);
	}

	/**
	 * Gets the key at.
	 *
	 * @param p the p
	 * @return the key at
	 */
	public KeyboardKey getKeyAt(Point2D p) {
		for (KeyboardKey k : keysCollection) {
			if (k.getKeyShape().contains(p)) {
				return k;
			}
		}
		return null;
	}

	/**
	 * Gets the keys iterator.
	 *
	 * @return the keys iterator
	 */
	public Iterable<KeyboardKey> getKeysIterator() {
		return keysCollection;
	}
}
