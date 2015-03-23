package multiplicity3.csys.gfx;

import com.jme3.math.ColorRGBA;

/**
 * The Class Gradient.
 */
public class Gradient {

	/**
	 * The Enum GradientDirection.
	 */
	public static enum GradientDirection {

		/** The diagonal. */
		DIAGONAL,

		/** The horizontal. */
		HORIZONTAL,

		/** The vertical. */
		VERTICAL
	}

	/** The direction. */
	private GradientDirection direction;

	/** The from. */
	private ColorRGBA from;

	/** The to. */
	private ColorRGBA to;

	/**
	 * Instantiates a new gradient.
	 *
	 * @param from the from
	 * @param to the to
	 * @param direction the direction
	 */
	public Gradient(ColorRGBA from, ColorRGBA to, GradientDirection direction) {
		this.setFrom(from);
		this.setTo(to);
		this.setDirection(direction);
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public GradientDirection getDirection() {
		return direction;
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public ColorRGBA getFrom() {
		return from;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public ColorRGBA getTo() {
		return to;
	}

	/**
	 * Sets the direction.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(GradientDirection direction) {
		this.direction = direction;
	}

	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(ColorRGBA from) {
		this.from = from;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(ColorRGBA to) {
		this.to = to;
	}
}
