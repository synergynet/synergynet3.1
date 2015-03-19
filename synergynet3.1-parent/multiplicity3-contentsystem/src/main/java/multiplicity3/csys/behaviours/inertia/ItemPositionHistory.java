package multiplicity3.csys.behaviours.inertia;

import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.Vector2f;

public class ItemPositionHistory {
	
	private static final int SAMPLE_SIZE = 4;
	private static final int SAMPLE_OFFSET = 3;
	private static final float TIME_THRESHOLD = 500;
	
	public IItem item;
	public List<PositionTime> positions;
	public Vector2f velocity = new Vector2f(0f, 0f);
	
	public ItemPositionHistory(IItem item) {
		this.item = item;
		this.positions = new ArrayList<PositionTime>();
	}
	
	Vector2f posTemp = new Vector2f();
	private IStage stage;
	public void add(Vector2f position, long timeStampMillis) {
		stage.tableToScreen(position, posTemp);		
		positions.add(new PositionTime(posTemp.clone(), timeStampMillis));
	}
	
	/**
	 * screen units per second
	 * @return
	 */
	public Vector2f getVelocity() {
		updateVelocity();
		return velocity;
	}
	
	private void updateVelocity() {
		if(positions.size() < SAMPLE_SIZE + SAMPLE_OFFSET) return;
		
		PositionTime earlier = null, later = null;

		earlier = positions.get(positions.size() - SAMPLE_SIZE - SAMPLE_OFFSET);
		later = positions.get(positions.size() - SAMPLE_OFFSET);
		
		later.pos.subtract(earlier.pos, velocity);
		float timeSeconds =  (later.timeStampMillis - earlier.timeStampMillis);
		
		if (timeSeconds > TIME_THRESHOLD){
			clear();
		}else{
			velocity.multLocal(SAMPLE_SIZE + SAMPLE_OFFSET);
		}

			
	}
	
	public void clear() {
		positions.clear();		
		velocity = new Vector2f(0f, 0f);
	}

	public static class PositionTime {
		public Vector2f pos;
		public long timeStampMillis;
		
		public PositionTime(Vector2f pos, long timeMillis) {
			this.pos = pos;
			this.timeStampMillis = timeMillis;
		}
	}

	public void setStage(IStage stage) {
		this.stage = stage;
		
	}

	
}