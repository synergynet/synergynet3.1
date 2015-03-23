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

package multiplicity3.input.simulator.jme;

import multiplicity3.input.simulator.AbstractDirectMultiTouchSimulator;

import com.jme3.input.InputManager;

/**
 * The Class JMEDirectSimulator.
 */
public class JMEDirectSimulator extends AbstractDirectMultiTouchSimulator {

	/** The input listener. */
	protected JMEMouseKeyboardInputManager inputListener;

	/**
	 * Instantiates a new JME direct simulator.
	 *
	 * @param inputManager the input manager
	 * @param displayWidth the display width
	 * @param displayHeight the display height
	 */
	public JMEDirectSimulator(InputManager inputManager, int displayWidth,
			int displayHeight) {
		super();
		inputListener = new JMEMouseKeyboardInputManager(this, inputManager,
				displayWidth, displayHeight);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#endListening()
	 */
	@Override
	public void endListening() {
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#requiresMouseDisplay()
	 */
	@Override
	public boolean requiresMouseDisplay() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#update(float)
	 */
	@Override
	public void update(float tpf) {
	}
}
