/*
 * Copyright (c) 2003, C. Ramakrishnan / Illposed Software All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Illposed Software nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission. THIS SOFTWARE
 * IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * $Id: OSCPacketDispatcher.java,v 1.1 2010/05/06 14:59:53 dcs0ah1 Exp $ Created
 * on 28.10.2003
 */
package com.illposed.osc.utility;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

/**
 * @author cramakrishnan Dispatches OSCMessages to registered listeners.
 */

public class OSCPacketDispatcher {
	// use Hashtable for JDK1.1 compatability
	/** The address to class table. */
	private Hashtable<String, OSCListener> addressToClassTable = new Hashtable<String, OSCListener>();

	/**
	 *
	 */
	public OSCPacketDispatcher() {
		super();
	}

	/**
	 * Adds the listener.
	 *
	 * @param address the address
	 * @param listener the listener
	 */
	public void addListener(String address, OSCListener listener) {
		addressToClassTable.put(address, listener);
	}

	/**
	 * Dispatch packet.
	 *
	 * @param packet the packet
	 */
	public void dispatchPacket(OSCPacket packet) {
		if (packet instanceof OSCBundle) {
			dispatchBundle((OSCBundle) packet);
		} else {
			dispatchMessage((OSCMessage) packet);
		}
	}

	/**
	 * Dispatch packet.
	 *
	 * @param packet the packet
	 * @param timestamp the timestamp
	 */
	public void dispatchPacket(OSCPacket packet, Date timestamp) {
		if (packet instanceof OSCBundle) {
			dispatchBundle((OSCBundle) packet);
		} else {
			dispatchMessage((OSCMessage) packet, timestamp);
		}
	}

	/**
	 * Dispatch bundle.
	 *
	 * @param bundle the bundle
	 */
	private void dispatchBundle(OSCBundle bundle) {
		Date timestamp = bundle.getTimestamp();
		OSCPacket[] packets = bundle.getPackets();
		for (int i = 0; i < packets.length; i++) {
			dispatchPacket(packets[i], timestamp);
		}
	}

	/**
	 * Dispatch message.
	 *
	 * @param message the message
	 */
	private void dispatchMessage(OSCMessage message) {
		dispatchMessage(message, null);
	}

	/**
	 * Dispatch message.
	 *
	 * @param message the message
	 * @param time the time
	 */
	private void dispatchMessage(OSCMessage message, Date time) {
		Enumeration<String> keys = addressToClassTable.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			// this supports the OSC regexp facility, but it
			// only works in JDK 1.4, so don't support it right now
			// if (key.matches(message.getAddress())) {
			if (key.equals(message.getAddress())) {
				OSCListener listener = addressToClassTable.get(key);
				listener.acceptMessage(time, message);
			}
		}
	}
}
