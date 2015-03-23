package multiplicity3.csys.behaviours.inertia;

import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.Vector2f;

/**
 * The Class ItemPositionHistory.
 */
public class ItemPositionHistory {

	/**
	 * The Class PositionTime.
	 */
	public static class PositionTime {

		/** The pos. */
		public Vector2f pos;

		/** The time stamp millis. */
		public long timeStampMillis;

		/**
		 * Instantiates a new position time.
		 *
		 * @param pos the pos
		 * @param timeMillis the time millis
		 */
		public PositionTime(Vector2f pos, long timeMillis) {
			this.pos = pos;
			this.timeStampMillis = timeMillis;
		}
	}

	/** The Constant SAMPLE_OFFSET. */
	private static final int SAMPLE_OFFSET = 3;

	/** The Constant SAMPLE_SIZE. */
	private static final int SAMPLE_SIZE = 4;

	/** The Constant TIME_THRESHOLD. */
	private static final float TIME_THRESHOLD = 500;

	/** The item. */
	public IItem item;

	/** The positions. */
	public List<PositionTime> positions;

	/** The velocity. */
	public Vector2f velocity = new Vector2f(0f, 0f);

	/** The stage. */
	private IStage stage;

	/** The pos temp. */
	Vector2f posTemp = new Vector2f();

	/**
	 * Instantiates a new item position history.
	 *
	 * @param item the item
	 */
	public ItemPositionHistory(IItem item) {
		this.item = item;
		this.positions = new ArrayList<PositionTime>();
	}

	/**
	 * Adds the.
	 *
	 * @param position the position
	 * @param timeStampMillis the time stamp millis
	 */
	public void add(Vector2f position, long timeStampMillis) {
		stage.tableToScreen(position, posTemp);
		positions.add(new PositionTime(posTemp.clone(), timeStampMillis));
	}

	/**
	 * Clear.
	 */
	public void clear() {
		positions.clear();
		velocity = new Vector2f(0f, 0f);
	}

	/**
	 * screen units per second
	 * 
	 * @return
	 */
	public Vector2f getVelocity() {
		updateVelocity();
		return velocity;
	}

	/**
	 * Sets the stage.
	 *
	 * @param stage the new stage
	 */
	public void setStage(IStage stage) {
		this.stage = stage;

	}

	/**
	 * Update velocity.
	 */
	private void updateVelocity() {
		if (positions.size() < (SAMPLE_SIZE + SAMPLE_OFFSET)) {
			return;
		}

		PositionTime earlier = null, later = null;

		earlier = positions.get(positions.size() - SAMPLE_SIZE - SAMPLE_OFFSET);
		later = positions.get(positions.size() - SAMPLE_OFFSET);

		later.pos.subtract(earlier.pos, velocity);
		float timeSeconds = (later.timeStampMillis - earlier.timeStampMillis);

		if (timeSeconds > TIME_THRESHOLD) {
			clear();
		} else {
			velocity.multLocal(SAMPLE_SIZE + SAMPLE_OFFSET);
		}

	}

}