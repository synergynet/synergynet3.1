/*
 * Copyright (c) 2009 University of Durham, England
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

package multiplicity3.csys.animation.elements;

public abstract class AnimationElement {
	protected boolean enabled;
	protected boolean firstCall = true;

	public void enable(boolean b) {
		this.enabled = b;
	}
	
	/**
	 * Called by the AnimationSystem on each frame. Will call
	 * <code>elementStart</code> and 
	 * <code>updateAnimationState</code> as necessary.
	 * @param tpf
	 */
	public void updateState(float tpf) {
		if(firstCall) {
			elementStart(tpf);
			firstCall = false;
		}
		updateAnimationState(tpf);
	}
	
	/**
	 * Called when the animation element is finished.
	 * See <code>isFinished()</code>
	 */
	public abstract void reset();
	
	/**
	 * Called on the first 
	 * @param tpf
	 */
	public abstract void elementStart(float tpf);
	
	/**
	 * Called each frame in the game loop.
	 * @param tpf
	 */
	public abstract void updateAnimationState(float tpf);
	
	/**
	 * When this method returns true, the animation element
	 * will be reset.
	 * @return
	 */
	public abstract boolean isFinished();
	
	public String toString() {
		return this.getClass().getName();
	}
}
