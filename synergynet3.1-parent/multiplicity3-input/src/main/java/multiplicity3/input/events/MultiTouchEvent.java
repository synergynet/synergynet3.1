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

package multiplicity3.input.events;

import java.io.Serializable;
import java.util.List;

import multiplicity3.input.data.CursorPositionRecord;

import com.jme3.math.Vector2f;

/**
 * Base class for  multi-touch cursor events whose coordinate system is
 * in the range 0..1.
 * 
 * Position (0,0) is the notional BOTTOM-LEFT side of the surface.
 * Position (1,1) is the notional TOP-RIGHT side of the surface.
 * 
 * Supports  position, rotation, velocity and pressure, although these values
 * will only be present for tables that provide that information.
 * 
 * @author dcs0ah1
 *
 */
public abstract class MultiTouchEvent implements Serializable {
	private static long lastEvent = System.nanoTime();
	private static final long serialVersionUID = 7048581503978460895L;
	
	protected long cursorID;
	protected List<CursorPositionRecord> positionHistory;
	protected Vector2f currentPosition;
	protected Vector2f velocity;
	protected long timeOfCreationNanos;
	protected float pressure;
	protected double angle;

	/**
	 * Constructs a new MultiTouchEvent with cursor id and 2D position
	 * where x and y components should be in the range 0..1. System
	 * behaviour undefined for values outside this range.
	 * @param id
	 * @param position
	 */
	public MultiTouchEvent(long id, Vector2f position) {
		this(id, position, new Vector2f());
	}
	
	public MultiTouchEvent(long id, Vector2f position, Vector2f velocity) {
		this(id, position, velocity, 1f, 0d);
	}
	
	public MultiTouchEvent(long id, Vector2f position, Vector2f velocity, float pressure, double angle) {
		this.cursorID = id;
		this.currentPosition = position;
		this.velocity = velocity;
		this.pressure = pressure;
		this.angle = angle;
		this.timeOfCreationNanos = System.nanoTime();
		updateLastEvent();
	}

	public long getCursorID() {
		return cursorID;
	}
	
	public Vector2f getPosition() {
		return currentPosition;
	}
		
	public void setPositionHistory(List<CursorPositionRecord> history) {
		this.positionHistory = history;
	}
	
	public List<CursorPositionRecord> getPositionHistory() {
		return this.positionHistory;
	}

	public Vector2f getVelocity() {
		return velocity;
	}
	
	public float getPressure() {
		return pressure;
	}

	public double getAngle() {
		return angle;
	}
	
	public long getTimeOfCreationNanos() {
		return timeOfCreationNanos;
	}
	
	public String toString() {
		return getClass().getName() + " " + cursorID + " @" + currentPosition + " vel: " + velocity;
	}
	
	private void updateLastEvent(){
		lastEvent = System.nanoTime();
	}

	public static long getLastEvent() {
		return lastEvent;
	}
}
