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
 * @author cramakrishnan OSCPortOut is the class that sends OSC messages. To
 *         send OSC, in your code you should instantiate and hold onto an
 *         OSCPort pointing at the address and port number for the receiver.
 *         When you want to send an OSC message, call send(). An example based
 *         on com.illposed.osc.test.OSCPortTest::testMessageWithArgs() : OSCPort
 *         sender = new OSCPort(); Object args[] = new Object[2]; args[0] = new
 *         Integer(3); args[1] = "hello"; OSCMessage msg = new
 *         OSCMessage("/sayhello", args); try { sender.send(msg); } catch
 *         (Exception e) { showError("Couldn't send"); }
 */

package com.illposed.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The Class OSCPortOut.
 */
public class OSCPortOut extends OSCPort
{

	/** The address. */
	protected InetAddress address;

	/**
	 * Create an OSCPort that sends to localhost, on the standard SuperCollider
	 * port Default the address to localhost Default the port to the standard
	 * one for SuperCollider
	 */
	public OSCPortOut() throws UnknownHostException, SocketException
	{
		this(InetAddress.getLocalHost(), defaultSCOSCPort);
	}

	/**
	 * Create an OSCPort that sends to newAddress, on the standard SuperCollider
	 * port
	 *
	 * @param newAddress
	 *            InetAddress Default the port to the standard one for
	 *            SuperCollider
	 */
	public OSCPortOut(InetAddress newAddress) throws SocketException
	{
		this(newAddress, defaultSCOSCPort);
	}

	/**
	 * Create an OSCPort that sends to newAddress, newPort
	 *
	 * @param newAddress
	 *            InetAddress
	 * @param newPort
	 *            int
	 */
	public OSCPortOut(InetAddress newAddress, int newPort) throws SocketException
	{
		socket = new DatagramSocket();
		address = newAddress;
		port = newPort;
	}

	/**
	 * @param aPacket
	 *            OSCPacket
	 */
	public void send(OSCPacket aPacket) throws IOException
	{
		byte[] byteArray = aPacket.getByteArray();
		DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, address, port);
		socket.send(packet);
	}
}
