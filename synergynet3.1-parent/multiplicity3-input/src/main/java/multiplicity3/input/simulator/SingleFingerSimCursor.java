/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
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

package multiplicity3.input.simulator;

import java.util.logging.Logger;

/**
 * The Class SingleFingerSimCursor.
 */
public class SingleFingerSimCursor extends AbstractSimCursor {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(SingleFingerSimCursor.class.getName());

	/** The id. */
	private int id;

	/** The screen x. */
	private int screenX;

	/** The screen y. */
	private int screenY;

	/** The simulator. */
	private IMultiTouchSimulator simulator;

	/** The x. */
	protected float x;

	/** The y. */
	protected float y;

	/**
	 * Instantiates a new single finger sim cursor.
	 *
	 * @param simulator the simulator
	 * @param id the id
	 */
	public SingleFingerSimCursor(IMultiTouchSimulator simulator, int id) {
		this.simulator = simulator;
		this.id = id;
	}

	/**
	 * Gets the mouse x.
	 *
	 * @return the mouse x
	 */
	public int getMouseX() {
		return screenX;
	}

	/**
	 * Gets the mouse y.
	 *
	 * @return the mouse y
	 */
	public int getMouseY() {
		return screenY;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractSimCursor#keyPressed(java.lang.
	 * String)
	 */
	@Override
	public void keyPressed(String key) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractSimCursor#keyReleased(java.lang
	 * .String)
	 */
	@Override
	public void keyReleased(String key) {
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.AbstractSimCursor#mouseDragged(float,
	 * float, int)
	 */
	@Override
	public void mouseDragged(float x, float y, int buttonNumber) {
		this.x = x;
		this.y = y;
		log.fine("mouseDragged");
		simulator.updateCursor(id, this.x, this.y);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.AbstractSimCursor#mouseMoved(float,
	 * float)
	 */
	@Override
	public void mouseMoved(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.AbstractSimCursor#mousePressed(float,
	 * float, int)
	 */
	@Override
	public void mousePressed(float x, float y, int button) {
		this.x = x;
		this.y = y;
		simulator.newCursor(id, this.x, this.y);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.AbstractSimCursor#mouseReleased(float,
	 * float, int)
	 */
	@Override
	public void mouseReleased(float x, float y, int button) {
		this.x = x;
		this.y = y;
		simulator.deleteCursor(id, this.x, this.y);
		simulator.clearCursor();
	}
}
