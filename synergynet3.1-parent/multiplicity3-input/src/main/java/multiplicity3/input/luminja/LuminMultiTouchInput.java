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

package multiplicity3.input.luminja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.exceptions.MultiTouchInputException;
import multiplicity3.input.luminja.exceptions.LuminSystemException;
import multiplicity3.input.utils.ClickDetector;

import com.jme3.math.Vector2f;

import de.evoluce.multitouch.adapter.java.BlobJ;
import de.evoluce.multitouch.adapter.java.JavaAdapter;

/**
 * Support for the Lumin multi-touch table. This implementation currently only
 * supports cursor information - it does not support objects/fiducials.
 *
 * @author dcs0ah1
 */
public class LuminMultiTouchInput implements IMultiTouchInputSource
{

	/** The Constant BLOB_CHANGED_THRESHOLD. */
	private final static float BLOB_CHANGED_THRESHOLD = 0.003f;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(LuminMultiTouchInput.class.getName());

	/** The click detector. */
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);

	/** The current blob registry. */
	protected Map<Integer, BlobJ> currentBlobRegistry = new HashMap<Integer, BlobJ>();

	/** The current blobs. */
	protected BlobJ[] currentBlobs = new BlobJ[0];

	/** The ja. */
	protected JavaAdapter ja;

	/** The last known position. */
	protected Map<Integer, Vector2f> lastKnownPosition = new HashMap<Integer, Vector2f>();

	/** The listeners. */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();

	/** The same position tolerance. */
	protected float samePositionTolerance = 0.002f;

	/**
	 * Instantiates a new lumin multi touch input.
	 */
	public LuminMultiTouchInput()
	{
		ja = new JavaAdapter("localhost");
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#endListening()
	 */
	@Override
	public void endListening()
	{
	}

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
	 * @see multiplicity3.input.IMultiTouchInputSource#requiresMouseDisplay()
	 */
	@Override
	public boolean requiresMouseDisplay()
	{
		return false;
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

	/**
	 * Sets the same position tolerance.
	 *
	 * @param samePositionTolerance
	 *            the new same position tolerance
	 */
	public void setSamePositionTolerance(float samePositionTolerance)
	{
		this.samePositionTolerance = samePositionTolerance;
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
	 * @see multiplicity3.input.IMultiTouchInputSource#update(float)
	 */
	@Override
	public void update(float tpf) throws MultiTouchInputException
	{
		process();
	}

	/**
	 * Process.
	 *
	 * @throws MultiTouchInputException
	 *             the multi touch input exception
	 */
	private void process() throws MultiTouchInputException
	{
		try
		{
			currentBlobs = ja.getBlobsOfNextFrame().mBlobs;
		}
		catch (Exception e)
		{
			throw new LuminSystemException(e);
		}

		Vector2f pos = new Vector2f();
		Vector2f vel = new Vector2f();

		Map<Integer, BlobJ> newRegistry = new HashMap<Integer, BlobJ>();
		// notify for new blobs or changes to existing blobs
		for (BlobJ blob : currentBlobs)
		{
			newRegistry.put(blob.mID, blob);
			pos.x = blob.mX;
			pos.y = 1.0f - blob.mY;

			MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, pos, vel, blob.mWidth, 0f);

			if (currentBlobRegistry.containsKey(blob.mID))
			{
				if (new Vector2f(blob.mX, blob.mY).distance(new Vector2f(currentBlobRegistry.get(blob.mID).mX, currentBlobRegistry.get(blob.mID).mY)) > BLOB_CHANGED_THRESHOLD)
				{
					for (IMultiTouchEventListener listener : listeners)
					{
						try
						{
							listener.cursorChanged(event);
						}
						catch (Exception ex)
						{
							log.warning("Problem in dispatching cursorChanged event " + event);
							log.log(Level.WARNING, "  Error as follows:", ex);
						}
					}
					lastKnownPosition.put(blob.mID, new Vector2f(blob.mX, blob.mY));
				}
				else
				{
					newRegistry.put(blob.mID, currentBlobRegistry.get(blob.mID));
				}
			}
			else
			{
				clickDetector.newCursorPressed(blob.mID, pos);
				for (IMultiTouchEventListener listener : listeners)
				{
					try
					{
						listener.cursorPressed(event);
					}
					catch (Exception ex)
					{
						log.warning("Problem in dispatching cursorPressed event " + event);
						log.log(Level.WARNING, "  Error as follows:", ex);
					}
				}
			}
		}

		// notify if blobs have disappeared
		for (Integer i : currentBlobRegistry.keySet())
		{
			if (!newRegistry.keySet().contains(i))
			{
				BlobJ blob = currentBlobRegistry.get(i);
				pos.x = blob.mX;
				pos.y = 1.0f - blob.mY;
				MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, pos, vel, blob.mWidth, 0f);

				for (IMultiTouchEventListener l : listeners)
				{
					int clickCount = clickDetector.cursorReleasedGetClickCount(blob.mID, pos);

					if (clickCount > 0)
					{
						event.setClickCount(clickCount);
						try
						{
							l.cursorClicked(event);
						}
						catch (Exception ex)
						{
							log.warning("Problem in dispatching cursorClicked event " + event);
							log.log(Level.WARNING, "  Error as follows:", ex);
						}
					}

					try
					{
						l.cursorReleased(event);
					}
					catch (Exception ex)
					{
						log.warning("Problem in dispatching cursorReleased event " + event);
						log.log(Level.WARNING, "  Error as follows:", ex);
					}
				}
				lastKnownPosition.remove(blob.mID);
			}
		}

		currentBlobRegistry = newRegistry;
	}

}
