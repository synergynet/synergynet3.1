package multiplicity3.input.data;

import java.io.Serializable;

import com.jme3.math.Vector2f;

/**
 * Stores history instance of a cursor position in screen coordinates with time in millis
 * @author dcs0ah1
 *
 */
public class CursorPositionRecord implements Serializable {
	private static final long serialVersionUID = -4462161324401346938L;
	private long timeMillis;
	private Vector2f position;

	public CursorPositionRecord(Vector2f position, long timeMillis) {
		this.setPosition(position);
		this.setTimeMillis(timeMillis);
	}

	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}

	public long getTimeMillis() {
		return timeMillis;
	}

	public void setPosition(Vector2f position) {
		this.position = position.clone();
	}

	public Vector2f getPosition() {
		return position;
	}
}
