/*
 * Copyright (c) 2002-2006, C. Ramakrishnan / Illposed Software All rights
 * reserved. Redistribution and use in source and binary forms, with or without
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

/**
 * @author cramakrishnan OscPacket is the abstract superclass for the various
 *         kinds of OSC Messages. Its direct subclasses are: OscMessage,
 *         OscBundle Subclasses need to know how to produce a byte array in the
 *         format specified by the OSC spec (or SuperCollider documentation, as
 *         the case may be). This implementation is based on Markus Gaelli and
 *         Iannis Zannos' OSC implementation in Squeak:
 *         http://www.emergent.de/Goodies/
 */

package com.illposed.osc;

import com.illposed.osc.utility.OSCJavaToByteArrayConverter;

/**
 * The Class OSCPacket.
 */
public abstract class OSCPacket {

	/** The byte array. */
	protected byte[] byteArray;

	/** The is byte array computed. */
	protected boolean isByteArrayComputed;

	/**
	 * Instantiates a new OSC packet.
	 */
	public OSCPacket() {
		super();
	}

	/**
	 * @return byte[]
	 */
	public byte[] getByteArray() {
		if (!isByteArrayComputed) {
			computeByteArray();
		}
		return byteArray;
	}

	/**
	 * Compute byte array.
	 */
	protected void computeByteArray() {
		OSCJavaToByteArrayConverter stream = new OSCJavaToByteArrayConverter();
		computeByteArray(stream);
	}

	/**
	 * @param stream OscPacketByteArrayConverter Subclasses should implement
	 *            this method to product a byte array formatted according to the
	 *            OSC/SuperCollider specification.
	 */
	protected abstract void computeByteArray(OSCJavaToByteArrayConverter stream);

	/**
	 * Inits the.
	 */
	protected void init() {

	}

}