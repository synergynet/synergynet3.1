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



/**
 * @author cramakrishnan
 *
 * An simple (non-bundle) OSC message. An OSC message is made up of 
 *     an address (who is this message sent to)
 *     and arguments (what is the contents of this message).
 * 
 * Internally, I use Vector to maintain jdk1.1 compatability
 * 
 */

package com.illposed.osc;

import java.util.Enumeration;
import java.util.Vector;

import com.illposed.osc.utility.*;

public class OSCMessage extends OSCPacket {

	protected String address;
	protected Vector<Object> arguments;

	/**
	 * Create an empty OSC Message
	 * In order to send this osc message, you need to set the address
	 * and, perhaps, some arguments.
	 */
	public OSCMessage() {
		super();
		arguments = new Vector<Object>();
	}

	/**
	 * Create an OSCMessage with an address already initialized
	 * @param newAddress The recepient of this OSC message
	 */
	public OSCMessage(String newAddress) {
		this(newAddress, null);
	}

	/**
	 * Create an OSCMessage with an address and arguments already initialized
	 * @param newAddress    The recepient of this OSC message
	 * @param newArguments  The data sent to the receiver
	 */
	public OSCMessage(String newAddress, Object[] newArguments) {
		super();
		address = newAddress;
		if (null != newArguments) {
			arguments = new Vector<Object>(newArguments.length);
			for (int i = 0; i < newArguments.length; i++) {
				arguments.add(newArguments[i]);
			}
		} else
			arguments = new Vector<Object>();
		init();
	}
	
	/**
	 * @return the address of this OSC Message
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Set the address of this messsage
	 * @param anAddress
	 */
	public void setAddress(String anAddress) {
		address = anAddress;
	}
	
	public void addArgument(Object argument) {
		arguments.add(argument);
	}
	
	public Object[] getArguments() {
		return arguments.toArray();
	}

	/**
	 * @param stream OscPacketByteArrayConverter
	 */
	protected void computeAddressByteArray(OSCJavaToByteArrayConverter stream) {
		stream.write(address);
	}

	/**
	 * @param stream OscPacketByteArrayConverter
	 */
	protected void computeArgumentsByteArray(OSCJavaToByteArrayConverter stream) {
		// SC starting at version 2.2.10 wants a comma at the beginning
		// of the arguments array.
		stream.write(',');
		if (null == arguments)
			return;
		stream.writeTypes(arguments);
		Enumeration<Object> enm = arguments.elements();
		while (enm.hasMoreElements()) {
			stream.write(enm.nextElement());
		}
	}

	/**
	 * @param stream OscPacketByteArrayConverter
	 */
	protected void computeByteArray(OSCJavaToByteArrayConverter stream) {
		computeAddressByteArray(stream);
		computeArgumentsByteArray(stream);
		byteArray = stream.toByteArray();
	}

}