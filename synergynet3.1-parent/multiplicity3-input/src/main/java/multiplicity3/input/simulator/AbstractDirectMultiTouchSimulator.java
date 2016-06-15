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

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;

import com.jme3.math.Vector2f;

/**
 * The Class AbstractDirectMultiTouchSimulator.
 */
public abstract class AbstractDirectMultiTouchSimulator extends AbstractMultiTouchSimulator
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(AbstractDirectMultiTouchSimulator.class.getName());

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractMultiTouchSimulator#deleteCursor
	 * (int, float, float)
	 */
	@Override
	public void deleteCursor(int id, float x, float y)
	{
		Vector2f pos = new Vector2f(x, y);

		MultiTouchCursorEvent event = new MultiTouchCursorEvent(id, pos);
		int clickCount = clickDetector.cursorReleasedGetClickCount(id, pos);

		if (clickCount > 0)
		{
			log.finer("Dispatching cursorClicked " + event);
			for (IMultiTouchEventListener l : listeners)
			{
				l.cursorClicked(event);
			}
		}

		log.finer("Dispatching cursorReleased " + event);

		for (IMultiTouchEventListener l : listeners)
		{
			l.cursorReleased(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractMultiTouchSimulator#deleteTwoCursors
	 * (int, float, float, int, float, float)
	 */
	@Override
	public void deleteTwoCursors(int id1, float x1, float y1, int id2, float x2, float y2)
	{
		deleteCursor(id1, x1, y1);
		deleteCursor(id2, x2, y2);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractMultiTouchSimulator#newCursor(int,
	 * float, float)
	 */
	@Override
	public void newCursor(int id, float x, float y)
	{
		Vector2f pos = new Vector2f(x, y);
		MultiTouchCursorEvent event = new MultiTouchCursorEvent(id, pos);
		clickDetector.newCursorPressed(id, pos);

		log.finer("Dispatching cursorPressed: " + event);

		for (IMultiTouchEventListener l : listeners)
		{
			l.cursorPressed(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractMultiTouchSimulator#updateCursor
	 * (int, float, float)
	 */
	@Override
	public void updateCursor(int id, float x, float y)
	{
		Vector2f pos = new Vector2f(x, y);
		MultiTouchCursorEvent event = new MultiTouchCursorEvent(id, pos);
		log.finer("Dispatching cursorChanged " + event);
		for (IMultiTouchEventListener l : listeners)
		{
			l.cursorChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.AbstractMultiTouchSimulator#updateTwoCursors
	 * (int, float, float, int, float, float)
	 */
	@Override
	public void updateTwoCursors(int id1, float x, float y, int id2, float x2, float y2)
	{
		updateCursor(id1, x, y);
		updateCursor(id2, x2, y2);
	}

}
