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

package multiplicity3.input.tuio.tuioobjects;

import com.jme3.math.Vector2f;

/**
 * The Class TUIOObjectRepresentation.
 */
public abstract class TUIOObjectRepresentation {

	/** The creation time. */
	private long creationTime;

	/** The id. */
	private long id;

	/** The position. */
	private Vector2f position;

	/** The velocity. */
	private Vector2f velocity;

	/**
	 * Constructs the object with an id and also sets the creationTime to be
	 * now.
	 *
	 * @param id
	 */
	public TUIOObjectRepresentation() {
		setCreationTime(System.nanoTime());
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2f getPosition() {
		return position;
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
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * Sets the velocity.
	 *
	 * @param velocity the new velocity
	 */
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

}
