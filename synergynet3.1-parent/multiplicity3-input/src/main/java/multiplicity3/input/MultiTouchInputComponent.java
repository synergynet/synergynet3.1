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

package multiplicity3.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import multiplicity3.input.data.CursorPositionRecord;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import multiplicity3.input.filters.IMultiTouchInputFilter;

/**
 * Manages a multi-touch input source and passes it through a set of filters.
 * Register a listener with this class to get multi-touch events that have been
 * through the filtering process.
 *
 * @author dcs0ah1
 */
public class MultiTouchInputComponent implements IMultiTouchEventListener,
		IMultiTouchEventProducer {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(MultiTouchInputComponent.class.getName());

	/** The cursor trails. */
	private Map<Long, List<CursorPositionRecord>> cursorTrails = new HashMap<Long, List<CursorPositionRecord>>();

	/** The exception listeners. */
	private List<IDispatchedMultiTouchEventExceptionListener> exceptionListeners = new ArrayList<IDispatchedMultiTouchEventExceptionListener>();

	/** The filters. */
	private List<IMultiTouchInputFilter> filters = new ArrayList<IMultiTouchInputFilter>();

	/** The is multi touch input enabled. */
	private boolean isMultiTouchInputEnabled = true;

	/** The listeners. */
	private List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();

	/** The source. */
	private IMultiTouchInputSource source;

	/**
	 * Instantiates a new multi touch input component.
	 *
	 * @param source the source
	 */
	public MultiTouchInputComponent(IMultiTouchInputSource source) {
		this.source = source;
		source.registerMultiTouchEventListener(this);
	}

	/**
	 * Add a multi-touch input filter. The filter will be added at the end of
	 * the current (possibly empty) filter queue. The filter will be told to
	 * pass events back to the MultiTouchInputComponent so that it can then pass
	 * it on to any listeners.
	 * 
	 * @param filter
	 */
	public void addMultiTouchInputFilter(IMultiTouchInputFilter filter) {
		if (filters.size() > 0) {
			// if we already have filters
			// get the last one
			IMultiTouchInputFilter t = getLastFilter();
			// last one now daisy chains onto this new filter
			t.setNext(filter);
			// add to collection of filters
			filters.add(filter);
			// get the filter to pass the results back to us
			// so that we can pass it on to our listeners
			filter.setNext(this);
		} else {
			// if we don't currently have any filters
			// unregister this from the source
			source.unregisterMultiTouchEventListener(this);
			// get the source to pass to the filter
			source.registerMultiTouchEventListener(filter);
			// get the filter to pass the results back
			// so that we can pass it on to our listeners
			filter.setNext(this);
			// add to collection of filters
			filters.add(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) {
			return;
		}

		List<CursorPositionRecord> trail = cursorTrails
				.get(event.getCursorID());
		trail.add(new CursorPositionRecord(event.getPosition(), System
				.currentTimeMillis()));
		event.setPositionHistory(trail);

		for (IMultiTouchEventListener l : listeners) {
			try {
				l.cursorChanged(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorClicked(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) {
			return;
		}
		for (IMultiTouchEventListener l : listeners) {
			try {
				l.cursorClicked(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorPressed(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) {
			return;
		}
		List<CursorPositionRecord> trail = new ArrayList<CursorPositionRecord>();
		trail.add(new CursorPositionRecord(event.getPosition(), System
				.currentTimeMillis()));
		cursorTrails.put(event.getCursorID(), trail);
		event.setPositionHistory(trail);

		for (IMultiTouchEventListener l : listeners) {
			try {
				l.cursorPressed(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorReleased(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		// if (!this.isMultiTouchInputEnabled) return; // if this line was not
		// commented out, release events would not complete, causing problems

		List<CursorPositionRecord> trail = cursorTrails.remove(event
				.getCursorID());
		event.setPositionHistory(trail);
		cursorTrails.remove(event.getCursorID());

		for (IMultiTouchEventListener l : listeners) {
			try {
				l.cursorReleased(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	/**
	 * Gets the active filter classes.
	 *
	 * @return the active filter classes
	 */
	public List<Class<? extends IMultiTouchInputFilter>> getActiveFilterClasses() {
		List<Class<? extends IMultiTouchInputFilter>> classes = new ArrayList<Class<? extends IMultiTouchInputFilter>>();
		for (IMultiTouchInputFilter f : filters) {
			classes.add(f.getClass());
		}
		return classes;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchEventProducer#getLastFilter()
	 */
	public IMultiTouchInputFilter getLastFilter() {
		return filters.get(filters.size() - 1);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventProducer#isFilterActive(java.lang
	 * .Class)
	 */
	public boolean isFilterActive(Class<? extends IMultiTouchInputFilter> filter) {
		for (IMultiTouchInputFilter f : filters) {
			if (f.getClass().equals(filter)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is multi touch input enabled.
	 *
	 * @return true, if is multi touch input enabled
	 */
	public boolean isMultiTouchInputEnabled() {
		return isMultiTouchInputEnabled;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectAdded(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
		if (!this.isMultiTouchInputEnabled) {
			return;
		}
		for (IMultiTouchEventListener l : listeners) {
			try {
				l.objectAdded(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	// **********************************

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectChanged(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
		if (!this.isMultiTouchInputEnabled) {
			return;
		}
		for (IMultiTouchEventListener l : listeners) {
			try {
				l.objectChanged(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectRemoved(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
		// if (!this.isMultiTouchInputEnabled) return;
		for (IMultiTouchEventListener l : listeners) {
			try {
				l.objectRemoved(event);
			} catch (Throwable t) {
				notifyListenersOfException(t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventProducer#registerMultiTouchEventListener
	 * (multiplicity3.input.IMultiTouchEventListener)
	 */
	public void registerMultiTouchEventListener(
			IMultiTouchEventListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventProducer#registerMultiTouchEventListener
	 * (multiplicity3.input.IMultiTouchEventListener, int)
	 */
	public void registerMultiTouchEventListener(
			IMultiTouchEventListener listener, int index) {
		if (!listeners.contains(listener)) {
			listeners.add(index, listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchEventProducer#
	 * registerMultiTouchExceptionListener
	 * (multiplicity3.input.IDispatchedMultiTouchEventExceptionListener)
	 */
	@Override
	public void registerMultiTouchExceptionListener(
			IDispatchedMultiTouchEventExceptionListener listener) {
		if (!exceptionListeners.contains(listener)) {
			this.exceptionListeners.add(listener);
		}
	}

	/**
	 * Sets the multi touch input enabled.
	 *
	 * @param isMultiTouchInputEnabled the new multi touch input enabled
	 */
	public void setMultiTouchInputEnabled(boolean isMultiTouchInputEnabled) {
		this.isMultiTouchInputEnabled = isMultiTouchInputEnabled;
	}

	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	public void setSource(IMultiTouchInputSource source) {
		this.source = source;
		source.registerMultiTouchEventListener(filters.get(0));
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchEventProducer#
	 * unregisterMultiTouchEventListener
	 * (multiplicity3.input.IMultiTouchEventListener)
	 */
	public void unregisterMultiTouchEventListener(
			IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify listeners of exception.
	 *
	 * @param t the t
	 */
	private void notifyListenersOfException(Throwable t) {
		if (exceptionListeners.size() < 1) {
			// we dont' have anybody interested in exceptions, so
			// should log
			log.severe("Exception occurred: " + t.toString());
			StringBuffer sb = new StringBuffer();
			for (StackTraceElement elem : t.getStackTrace()) {
				sb.append("   " + elem.toString() + "\n");
			}
			log.severe("  Stack tace follows:\n" + sb);
		} else {
			for (IDispatchedMultiTouchEventExceptionListener l : exceptionListeners) {
				l.dispatchedMultiTouchEventException(t);
			}
		}
	}
}
