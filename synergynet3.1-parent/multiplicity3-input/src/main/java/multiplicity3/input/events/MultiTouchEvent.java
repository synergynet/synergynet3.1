/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergySpace' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package multiplicity3.input.events;

import java.io.Serializable;
import java.util.List;

import multiplicity3.input.data.CursorPositionRecord;

import com.jme3.math.Vector2f;

/**
 * Base class for multi-touch cursor events whose coordinate system is in the
 * range 0..1. Position (0,0) is the notional BOTTOM-LEFT side of the surface.
 * Position (1,1) is the notional TOP-RIGHT side of the surface. Supports
 * position, rotation, velocity and pressure, although these values will only be
 * present for tables that provide that information.
 *
 * @author dcs0ah1
 */
public abstract class MultiTouchEvent implements Serializable {

	/** The last event. */
	private static long lastEvent = System.nanoTime();

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7048581503978460895L;

	/** The angle. */
	protected double angle;

	/** The current position. */
	protected Vector2f currentPosition;

	/** The cursor id. */
	protected long cursorID;

	/** The position history. */
	protected List<CursorPositionRecord> positionHistory;

	/** The pressure. */
	protected float pressure;

	/** The time of creation nanos. */
	protected long timeOfCreationNanos;

	/** The velocity. */
	protected Vector2f velocity;

	/**
	 * Constructs a new MultiTouchEvent with cursor id and 2D position where x
	 * and y components should be in the range 0..1. System behaviour undefined
	 * for values outside this range.
	 * 
	 * @param id
	 * @param position
	 */
	public MultiTouchEvent(long id, Vector2f position) {
		this(id, position, new Vector2f());
	}

	/**
	 * Instantiates a new multi touch event.
	 *
	 * @param id the id
	 * @param position the position
	 * @param velocity the velocity
	 */
	public MultiTouchEvent(long id, Vector2f position, Vector2f velocity) {
		this(id, position, velocity, 1f, 0d);
	}

	/**
	 * Instantiates a new multi touch event.
	 *
	 * @param id the id
	 * @param position the position
	 * @param velocity the velocity
	 * @param pressure the pressure
	 * @param angle the angle
	 */
	public MultiTouchEvent(long id, Vector2f position, Vector2f velocity,
			float pressure, double angle) {
		this.cursorID = id;
		this.currentPosition = position;
		this.velocity = velocity;
		this.pressure = pressure;
		this.angle = angle;
		this.timeOfCreationNanos = System.nanoTime();
		updateLastEvent();
	}

	/**
	 * Gets the last event.
	 *
	 * @return the last event
	 */
	public static long getLastEvent() {
		return lastEvent;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Gets the cursor id.
	 *
	 * @return the cursor id
	 */
	public long getCursorID() {
		return cursorID;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2f getPosition() {
		return currentPosition;
	}

	/**
	 * Gets the position history.
	 *
	 * @return the position history
	 */
	public List<CursorPositionRecord> getPositionHistory() {
		return this.positionHistory;
	}

	/**
	 * Gets the pressure.
	 *
	 * @return the pressure
	 */
	public float getPressure() {
		return pressure;
	}

	/**
	 * Gets the time of creation nanos.
	 *
	 * @return the time of creation nanos
	 */
	public long getTimeOfCreationNanos() {
		return timeOfCreationNanos;
	}

	/**
	 * Gets the velocity.
	 *
	 * @return the velocity
	 */
	public Vector2f getVelocity() {
		return velocity;
	}

	/**
	 * Sets the position history.
	 *
	 * @param history the new position history
	 */
	public void setPositionHistory(List<CursorPositionRecord> history) {
		this.positionHistory = history;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + " " + cursorID + " @" + currentPosition
				+ " vel: " + velocity;
	}

	/**
	 * Update last event.
	 */
	private void updateLastEvent() {
		lastEvent = System.nanoTime();
	}
}
