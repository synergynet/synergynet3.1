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

package multiplicity3.input;

import multiplicity3.input.exceptions.MultiTouchInputException;

/**
 * Generic type for multi-touch input services.  Allows for the registration
 * and de-registration of listeners to multi-touch events, as defined in
 * IMultiTouchInputListener.
 * 
 * @author dcs0ah1
 *
 */

public interface IMultiTouchInputSource {
	
	/**
	 * Some sources run in a separate thread.
	 * This ensures that, say OpenGL based threads
	 * can use this input source without any
	 * tedious mucking about.
	 * @param tpf
	 * @throws MultiTouchInputException 
	 */
	public void update(float tpf) throws MultiTouchInputException;
	
	/**
	 * Register the listener to receive multi-touch events, as defined in
	 * IMultiTouchInputListener.
	 * @param listener
	 */
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener);
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index);
	
	/**
	 * Stops the listener from receiving multi-touch events.
	 * @param listener
	 */
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener);
		
	/**
	 * Determines whether two successive cursor add/remove events constitute a 'click'.  Determines
	 * how close together in both time (in milliseconds) and space (in coordinate mode distance).
	 * @param time in milliseconds
	 * @param distance in coordinate space
	 */
	public void setClickSensitivity(long time, float distance);
	
	/**
	 * Should the mouse be shown or hidden?
	 * @return
	 */
	public boolean requiresMouseDisplay();
	
	
	/**
	 * Stop the listener from running
	 */
	public void endListening();

}
