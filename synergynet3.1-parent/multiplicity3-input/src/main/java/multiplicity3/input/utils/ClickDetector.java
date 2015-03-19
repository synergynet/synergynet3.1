/*
 * Copyright (c) 2008 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package multiplicity3.input.utils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.math.Vector2f;

/**
 * Determines whether cursor released events constitute a single click.  Needs
 * to know when new cursors are pressed, via newCursorPressed.  When a cursor
 * is released, use isCursorReleaseASingleClick to determine whether a single
 * click event occurred, based on the current sensitivity.
 * 
 * @author dcs0ah1
 */

public class ClickDetector {
	
	protected float distance;
	protected long time;
	
	protected Map<Long,CursorDownRecord> records = new HashMap<Long,CursorDownRecord>();
	protected long oldRecordTimeThreshold = 5 * 1000;
	protected long doubleClickTimeThreshold = 500;

	/**
	 * Click detector, based on supplied sensitivity values.
	 * @param time
	 * @param distance
	 */
	public ClickDetector(long time, float distance) {
		setSensitivity(time, distance);
	}
	
	/**
	 * Set new sensitivity values.
	 * @param time
	 * @param distance
	 */
	public void setSensitivity(long time, float distance) {
		this.time = time;
		this.distance = distance;
	}
	
	public float getDistanceSensitivity() {
		return this.distance;
	}
	
	public long getTimeSensitivity() {
		return this.time;
	}
	
	/**
	 * Register cursor - only registered cursors will ever pass the test
	 * of being a single click when isCursorReleaseASingleClick is called.
	 * @param id
	 * @param position
	 */
	public void newCursorPressed(long id, Vector2f position) {
		records.put(id, new CursorDownRecord(id, this, System.currentTimeMillis(), position));
	}
	
	/**
	 * Returns true if the released cursor is within the sensitivity values required
	 * to constitute a single click.
	 * @param id
	 * @param position
	 * @return
	 */
	public int cursorReleasedGetClickCount(long id, Vector2f position) {
		CursorDownRecord record = records.get(id);
		if(record == null) return -1;
		record.releaseTime = System.currentTimeMillis();
		boolean isDoubleClick = false;
		boolean isSingleClick = ((System.currentTimeMillis() - record.pressTime) < time) && isCloseEnough(position, record.position);
		if(isSingleClick) {
			record.wasClick = isSingleClick;
			isDoubleClick = isCursorReleaseADoubleClick(id, position, record);
			record.wasDoubleClick = isDoubleClick;
		}		
		
		cullOldRecords();
		if(isDoubleClick) {
			return 2;
		}else if(isSingleClick){
			return 1;
		}else{
			return 0;
		}
	}
	
	protected void printRecords() {
		for(CursorDownRecord r : records.values()) {
			System.out.println(r.id + ": " + r.wasClick);
		}
		
	}

	public boolean isCursorReleaseADoubleClick(long id, Vector2f position, CursorDownRecord causedBy) {
		CursorDownRecord record = records.get(id);
		if(record == null) return false;
		for(CursorDownRecord r : records.values()) {
			if(r == causedBy) continue;
			if(r.area.contains(new Point2D.Float(position.x, position.y)) && r.wasClick && !r.wasDoubleClick && ((System.currentTimeMillis() - r.releaseTime) < getDoubleClickTimeThreshold())) {
				return true;
			}
		}	
		return false;
	}
	
	private void cullOldRecords() {
		List<Long> toDelete = new ArrayList<Long>();
		for(CursorDownRecord r : records.values()) {
			if(System.currentTimeMillis() - r.releaseTime > oldRecordTimeThreshold ) {
				toDelete.add(r.id);
			}
		}
		for(Long idToDelete : toDelete) {
			records.remove(idToDelete);
		}		
	}
	
	private boolean isCloseEnough(Vector2f a, Vector2f b) {
		return Point2D.distance(a.x, a.y, b.x, b.y) < distance;
	}
	
	public void setDoubleClickTimeThreshold(long doubleClickTimeThreshold) {
		this.doubleClickTimeThreshold = doubleClickTimeThreshold;
	}

	public long getDoubleClickTimeThreshold() {
		return doubleClickTimeThreshold;
	}

	private class CursorDownRecord {
		public long id;
		public long pressTime;
		public long releaseTime;
		public Vector2f position;
		public Rectangle2D.Float area = new Rectangle2D.Float();
		public boolean wasClick = false;
		public boolean wasDoubleClick = false;
		
		public CursorDownRecord(long id, ClickDetector detector, long time, Vector2f position) {
			this.id = id;
			this.pressTime = time;
			this.position = position;
			area.x = position.x - detector.getDistanceSensitivity()/2;
			area.y = position.y - detector.getDistanceSensitivity()/2;
			area.width = detector.getDistanceSensitivity();
			area.height = detector.getDistanceSensitivity();
		}
	}
}
