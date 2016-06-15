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

/**
 * The Class AbstractSimCursor.
 */
public abstract class AbstractSimCursor
{

	/** The Constant KEY_CONTROL. */
	public static final String KEY_CONTROL = "control";

	/** The Constant KEY_SHIFT. */
	public static final String KEY_SHIFT = "shift";

	/** The Constant KEY_SPACE. */
	public static final String KEY_SPACE = "space";

	/** The Constant MOUSE_BUTTON_LEFT. */
	public static final int MOUSE_BUTTON_LEFT = 0;

	/** The Constant MOUSE_BUTTON_MIDDLE. */
	public static final int MOUSE_BUTTON_MIDDLE = 1;

	/** The Constant MOUSE_BUTTON_RIGHT. */
	public static final int MOUSE_BUTTON_RIGHT = 2;

	/**
	 * Key pressed.
	 *
	 * @param key
	 *            the key
	 */
	public abstract void keyPressed(String key);

	/**
	 * Key released.
	 *
	 * @param key
	 *            the key
	 */
	public abstract void keyReleased(String key);

	/**
	 * Mouse dragged.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param buttonNumber
	 *            the button number
	 */
	public abstract void mouseDragged(float x, float y, int buttonNumber);

	/**
	 * Mouse moved.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public abstract void mouseMoved(float x, float y);

	/**
	 * Mouse pressed.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param buttonNumber
	 *            the button number
	 */
	public abstract void mousePressed(float x, float y, int buttonNumber);

	/**
	 * Mouse released.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param buttonNumber
	 *            the button number
	 */
	public abstract void mouseReleased(float x, float y, int buttonNumber);

	// public static float getScaledX(int x, int width) {
	// return (float) x / (float) width;
	// }
	//
	// public static float getScaledY(int y, int height) {
	// return (float) y / (float) height;
	// }

}
