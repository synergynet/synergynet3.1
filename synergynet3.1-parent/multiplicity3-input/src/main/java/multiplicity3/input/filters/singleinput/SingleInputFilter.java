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

package multiplicity3.input.filters.singleinput;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import multiplicity3.input.filters.IMultiTouchInputFilter;

/**
 * Ensures only one cursor is active at any one time.
 * 
 * @author dcs0ah1
 *
 */
public class SingleInputFilter implements IMultiTouchInputFilter {
	
	private static final int INVALID_CURSOR = Integer.MIN_VALUE;
	
	private IMultiTouchEventListener next;
	private long activeCursor = INVALID_CURSOR;

	public void setNext(IMultiTouchEventListener el) {
		this.next = el;
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
		if(event.getCursorID() == activeCursor) {
			next.cursorChanged(event);
		}
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
		if(event.getCursorID() == activeCursor) {
			next.cursorClicked(event);
		}
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		if(activeCursor == INVALID_CURSOR) {
			activeCursor = event.getCursorID();			
			next.cursorPressed(event);
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		if(event.getCursorID() == activeCursor) {
			activeCursor = INVALID_CURSOR;
			next.cursorReleased(event);
		}
	}

	public void objectAdded(MultiTouchObjectEvent event) {
		next.objectAdded(event);
	}

	public void objectChanged(MultiTouchObjectEvent event) {
		next.objectChanged(event);
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
		next.objectRemoved(event);
	}

}
