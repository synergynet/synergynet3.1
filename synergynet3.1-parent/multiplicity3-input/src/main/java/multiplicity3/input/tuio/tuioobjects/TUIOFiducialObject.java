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

/**
 * The Class TUIOFiducialObject.
 */
public class TUIOFiducialObject extends TUIOObjectRepresentation {

	/** The angle. */
	private float angle;

	/** The angle acceleration. */
	private float angleAcceleration;

	/** The angle velocity. */
	private float angleVelocity;

	/** The fiducial id. */
	private int fiducialId;

	/**
	 * Instantiates a new TUIO fiducial object.
	 *
	 * @param id the id
	 * @param fiducial_id the fiducial_id
	 */
	public TUIOFiducialObject(long id, int fiducial_id) {
		super();
		setId(id);
		this.fiducialId = fiducial_id;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Gets the angle acceleration.
	 *
	 * @return the angle acceleration
	 */
	public float getAngleAcceleration() {
		return angleAcceleration;
	}

	/**
	 * Gets the angle velocity.
	 *
	 * @return the angle velocity
	 */
	public float getAngleVelocity() {
		return angleVelocity;
	}

	/**
	 * Gets the fiducial id.
	 *
	 * @return the fiducial id
	 */
	public int getFiducialId() {
		return fiducialId;
	}

	/**
	 * Sets the angle.
	 *
	 * @param angle the new angle
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * Sets the angle acceleration.
	 *
	 * @param r_accel the new angle acceleration
	 */
	public void setAngleAcceleration(float r_accel) {
		this.angleAcceleration = r_accel;
	}

	/**
	 * Sets the angle velocity.
	 *
	 * @param r_speed the new angle velocity
	 */
	public void setAngleVelocity(float r_speed) {
		this.angleVelocity = r_speed;
	}

	/**
	 * Sets the fiducial id.
	 *
	 * @param fiducialId the new fiducial id
	 */
	public void setFiducialId(int fiducialId) {
		this.fiducialId = fiducialId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "TUIOFiducialObject(" + fiducialId + ") @" + this.getPosition()
				+ " rot:" + angle + " rotVel:" + angleVelocity;
	}

}
