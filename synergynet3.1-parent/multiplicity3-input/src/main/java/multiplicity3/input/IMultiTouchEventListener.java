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

package multiplicity3.input;

import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

/**
 * Defines possible input from a MultiTouch table. Eventually this interface
 * would be extended to support new aspects of MultiTouch tables such as RFID
 * proximity, hand area detection and bluetooth devices. For Cursors, the order
 * of events for a click is: cursorPressed, cursorClicked, cursorReleased.
 *
 * @author dcs0ah1
 */
public interface IMultiTouchEventListener {
	/**
	 * Cursor is on the table, and moves, or rotates, or changes in some way.
	 * 
	 * @param event
	 */
	public void cursorChanged(MultiTouchCursorEvent event);

	/**
	 * Cursor clicked. The sensitivity of the clicking detection is the
	 * responsibility of the appropriate IMultiTouchInputService. Event order is
	 * cursorPressed, cursorReleased, cursorClicked.
	 * 
	 * @param event
	 */
	public void cursorClicked(MultiTouchCursorEvent event);

	/**
	 * Cursor press arriving on the table.
	 * 
	 * @param event
	 */
	public void cursorPressed(MultiTouchCursorEvent event);

	/**
	 * Cursor left the table.
	 * 
	 * @param event
	 */
	public void cursorReleased(MultiTouchCursorEvent event);

	/**
	 * Object/fiducial is added to the table.
	 * 
	 * @param event
	 */
	public void objectAdded(MultiTouchObjectEvent event);

	/**
	 * Object/fiducial is changed - position or rotation.
	 * 
	 * @param event
	 */
	public void objectChanged(MultiTouchObjectEvent event);

	/**
	 * Object/fiducial is removed from the table.
	 * 
	 * @param event
	 */
	public void objectRemoved(MultiTouchObjectEvent event);
}
