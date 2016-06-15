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

import java.util.ArrayList;
import java.util.List;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.utils.ClickDetector;

/**
 * Default implementation of the jME simulator system. Left click to simulate a
 * single finger. Right click, drag and release to simulate two fingers that can
 * then be moved (mouse movement), rotated around their centre (using SHIFT) or
 * scaled in and out (using CTRL).
 *
 * @author ashatch
 */
public abstract class AbstractMultiTouchSimulator implements IMultiTouchSimulator, IMultiTouchInputSource
{

	/** The click detector. */
	protected ClickDetector clickDetector = new ClickDetector(500, 0.02f);

	/** The current cursor. */
	protected AbstractSimCursor currentCursor;

	/** The cursor id. */
	protected int cursorID;

	/** The listeners. */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.IMultiTouchSimulator#clearCursor()
	 */
	@Override
	public void clearCursor()
	{
		currentCursor = null;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.IMultiTouchSimulator#deleteCursor(int,
	 * float, float)
	 */
	@Override
	public abstract void deleteCursor(int id, float x, float y);

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.IMultiTouchSimulator#deleteTwoCursors(int,
	 * float, float, int, float, float)
	 */
	@Override
	public abstract void deleteTwoCursors(int id1, float x1, float y1, int id2, float x2, float y2);

	/**
	 * Gets the current cursor.
	 *
	 * @return the current cursor
	 */
	public AbstractSimCursor getCurrentCursor()
	{
		return currentCursor;
	}

	/**
	 * Gets the new cursor id.
	 *
	 * @return the new cursor id
	 */
	public int getNewCursorID()
	{
		return cursorID++;
	}

	/**
	 * Key pressed.
	 *
	 * @param key
	 *            the key
	 */
	public void keyPressed(String key)
	{
		if (currentCursor == null)
		{
			return;
		}
		currentCursor.keyPressed(key);
	}

	/**
	 * Key released.
	 *
	 * @param key
	 *            the key
	 */
	public void keyReleased(String key)
	{
		if (currentCursor == null)
		{
			return;
		}
		currentCursor.keyReleased(key);
	}

	/**
	 * Mouse dragged.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param button
	 *            the button
	 */
	public void mouseDragged(float x, float y, int button)
	{
		if (currentCursor == null)
		{
			return;
		}
		currentCursor.mouseDragged(x, y, button);
	}

	/**
	 * Mouse moved.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void mouseMoved(float x, float y)
	{
		if (currentCursor == null)
		{
			return;
		}
		currentCursor.mouseMoved(x, y);
	}

	/**
	 * Must supply x and y in range 0..1
	 *
	 * @param x
	 * @param y
	 * @param button
	 */
	public void mousePressed(float x, float y, int button)
	{
		if (currentCursor != null)
		{
			currentCursor.mousePressed(x, y, button);
			return;
		}

		switch (button)
		{
			case AbstractSimCursor.MOUSE_BUTTON_LEFT:
			{
				currentCursor = new SingleFingerSimCursor(this, getNewCursorID());
				currentCursor.mousePressed(x, y, button);
				break;
			}
			case AbstractSimCursor.MOUSE_BUTTON_MIDDLE:
			{
				currentCursor = new TripleFingerSimCursor(this, getNewCursorID(), getNewCursorID(), getNewCursorID());
				currentCursor.mousePressed(x, y, button);
				break;
			}

			case AbstractSimCursor.MOUSE_BUTTON_RIGHT:
			{
				currentCursor = new DoubleFingerSimCursor(this, getNewCursorID(), getNewCursorID());
				currentCursor.mousePressed(x, y, button);
				break;
			}

		}

	}

	/**
	 * Mouse released.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param button
	 *            the button
	 */
	public void mouseReleased(float x, float y, int button)
	{
		if (currentCursor == null)
		{
			return;
		}
		currentCursor.mouseReleased(x, y, button);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.IMultiTouchSimulator#newCursor(int,
	 * float, float)
	 */
	@Override
	public abstract void newCursor(int id, float x, float y);

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchInputSource#registerMultiTouchEventListener
	 * (multiplicity3.input.IMultiTouchEventListener)
	 */
	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchInputSource#registerMultiTouchEventListener
	 * (multiplicity3.input.IMultiTouchEventListener, int)
	 */
	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(index, listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#setClickSensitivity(long,
	 * float)
	 */
	@Override
	public void setClickSensitivity(long time, float distance)
	{
		clickDetector.setSensitivity(time, distance);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchInputSource#unregisterMultiTouchEventListener
	 * (multiplicity3.input.IMultiTouchEventListener)
	 */
	@Override
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener)
	{
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.simulator.IMultiTouchSimulator#updateCursor(int,
	 * float, float)
	 */
	@Override
	public abstract void updateCursor(int id, float x, float y);

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.simulator.IMultiTouchSimulator#updateTwoCursors(int,
	 * float, float, int, float, float)
	 */
	@Override
	public abstract void updateTwoCursors(int id1, float x, float y, int id2, float x2, float y2);
}
