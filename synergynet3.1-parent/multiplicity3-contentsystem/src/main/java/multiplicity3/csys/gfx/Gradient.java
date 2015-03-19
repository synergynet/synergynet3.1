package multiplicity3.csys.gfx;

import com.jme3.math.ColorRGBA;

public class Gradient {
	private ColorRGBA from;
	private ColorRGBA to;
	private GradientDirection direction;

	public Gradient(ColorRGBA from, ColorRGBA to, GradientDirection direction) {
		this.setFrom(from);
		this.setTo(to);
		this.setDirection(direction);
	}
	
	public void setFrom(ColorRGBA from) {
		this.from = from;
	}

	public ColorRGBA getFrom() {
		return from;
	}

	public void setDirection(GradientDirection direction) {
		this.direction = direction;
	}

	public GradientDirection getDirection() {
		return direction;
	}

	public void setTo(ColorRGBA to) {
		this.to = to;
	}

	public ColorRGBA getTo() {
		return to;
	}

	public static enum GradientDirection {
		VERTICAL,
		HORIZONTAL,
		DIAGONAL
	}
}
