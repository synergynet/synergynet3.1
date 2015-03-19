/*
 * Copyright (c) 2002-2006, C. Ramakrishnan / Illposed Software
 * All rights reserved. 
 *   
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *  
 *  *  Redistributions of source code must retain the above copyright 
 *     notice, this list of conditions and the following disclaimer.
 *     
 *  *  Redistributions in binary form must reproduce the above copyright 
 *     notice, this list of conditions and the following disclaimer in the 
 *     documentation and/or other materials provided with the distribution.
 *     
 *  *  Neither the name of the Illposed Software nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
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
 * 
 */

package com.illposed.osc;
import java.math.BigInteger;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.illposed.osc.utility.*;

public class OSCBundle extends OSCPacket {

	protected Date timestamp;
	//	protected OSCPacket[] packets;
	protected Vector<OSCPacket> packets;
	public static final BigInteger SECONDS_FROM_1900_to_1970 =
		new BigInteger("2208988800");
	// 17 leap years

	/**
	 * Create a new OSCBundle, with a timestamp of now.
	 * You can add packets to the bundle with addPacket()
	 */
	public OSCBundle() {
		this(null, GregorianCalendar.getInstance().getTime());
	}
	
	/**
	 * Create an OSCBundle with the specified timestamp
	 * @param timestamp
	 */
	public OSCBundle(Date timestamp) {
		this(null, timestamp);
	}

	/**
	 * @param newPackets Array of OSCPackets to initialize this object with
	 */
	public OSCBundle(OSCPacket[] newPackets) {
		this(newPackets, GregorianCalendar.getInstance().getTime());
	}

	/**
	 * @param newPackets OscPacket[]
	 * @param time java.lang.Time
	 */
	public OSCBundle(OSCPacket[] newPackets, Date newTimestamp) {
		super();
		if (null != newPackets) {
			packets = new Vector<OSCPacket>(newPackets.length);
			for (int i = 0; i < newPackets.length; i++) {
				packets.add(newPackets[i]);
			}
		} else
			packets = new Vector<OSCPacket>();
		timestamp = newTimestamp;
		init();
	}
	
	/**
	 * Return the timestamp for this bundle
	 * @return a Date
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Set the timestamp for this bundle
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Add a packet to the list of packets in this bundle
	 * @param packet
	 */
	public void addPacket(OSCPacket packet) {
		packets.add(packet);
	}
	
	/**
	 * Get the packets contained in this bundle
	 * @return an array of packets
	 */
	public OSCPacket[] getPackets() {
		OSCPacket[] packetArray = new OSCPacket[packets.size()];
		packets.toArray(packetArray);
		return packetArray;
	}

	protected void computeTimeTagByteArray(OSCJavaToByteArrayConverter stream) {
		long millisecs = timestamp.getTime();
		long secsSince1970 = (long) (millisecs / 1000);
		long secs = secsSince1970 + SECONDS_FROM_1900_to_1970.longValue();
		long picosecs = (long) (millisecs - (secsSince1970 * 1000)) * 1000;
		
		stream.write((int) secs);
		stream.write((int) picosecs);

	}

	/**
	 * @param stream OscPacketByteArrayConverter
	 */
	protected void computeByteArray(OSCJavaToByteArrayConverter stream) {
		stream.write("#bundle");
		computeTimeTagByteArray(stream);
		Enumeration<OSCPacket> enm = packets.elements();
		OSCPacket nextElement;
		byte[] packetBytes;
		while (enm.hasMoreElements()) {
			nextElement = enm.nextElement();
			packetBytes = nextElement.getByteArray();
			stream.write(packetBytes.length);
			stream.write(packetBytes);
		}
		byteArray = stream.toByteArray();
	}

}