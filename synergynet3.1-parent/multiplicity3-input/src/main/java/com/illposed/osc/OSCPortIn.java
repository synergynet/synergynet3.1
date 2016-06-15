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
 * @author cramakrishnan OSCPortIn is the class that listens for OSC messages.
 *         To receive OSC, you need to construct the OSCPort with a An example
 *         based on com.illposed.osc.test.OSCPortTest::testReceiving() :
 *         receiver = new OSCPort(OSCPort.defaultSCOSCPort()); OSCListener
 *         listener = new OSCListener() { public void
 *         acceptMessage(java.util.Date time, OSCMessage message) {
 *         System.out.println("Message received!"); } };
 *         receiver.addListener("/message/receiving", listener);
 *         receiver.startListening(); Then, using a program such as
 *         SuperCollider or sendOSC, send a message to this computer, port 57110
 *         (defaultSCOSCPort), with the address /message/receiving
 */

package com.illposed.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.illposed.osc.utility.OSCByteArrayToJavaConverter;
import com.illposed.osc.utility.OSCPacketDispatcher;

/**
 * The Class OSCPortIn.
 */
public class OSCPortIn extends OSCPort implements Runnable
{

	/** The converter. */
	protected OSCByteArrayToJavaConverter converter = new OSCByteArrayToJavaConverter();

	/** The dispatcher. */
	protected OSCPacketDispatcher dispatcher = new OSCPacketDispatcher();

	// state for listening
	/** The is listening. */
	protected boolean isListening;

	/**
	 * Create an OSCPort that listens on port
	 *
	 * @param port
	 * @throws SocketException
	 */
	public OSCPortIn(int port) throws SocketException
	{
		socket = new DatagramSocket(port);
		this.port = port;
	}

	/**
	 * Register the listener for incoming OSCPackets addressed to an Address
	 *
	 * @param anAddress
	 *            the address to listen for
	 * @param listener
	 *            the object to invoke when a message comes in
	 */
	public void addListener(String anAddress, OSCListener listener)
	{
		dispatcher.addListener(anAddress, listener);
	}

	/**
	 * Close the socket and free-up resources. It's recommended that clients
	 * call this when they are done with the port.
	 */
	@Override
	public void close()
	{
		socket.close();
	}

	/**
	 * Am I listening for packets?
	 */
	public boolean isListening()
	{
		return isListening;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		byte[] buffer = new byte[1536];
		DatagramPacket packet = new DatagramPacket(buffer, 1536);
		while (isListening)
		{
			try
			{
				packet.setLength(1536);
				socket.receive(packet);
				OSCPacket oscPacket = converter.convert(buffer, packet.getLength());
				dispatcher.dispatchPacket(oscPacket);
			}
			catch (java.net.SocketException e)
			{
				if (isListening)
				{
					e.printStackTrace();
				}
			}
			catch (IOException e)
			{
				if (isListening)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Start listening for incoming OSCPackets
	 */
	public void startListening()
	{
		isListening = true;
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stop listening for incoming OSCPackets
	 */
	public void stopListening()
	{
		isListening = false;
	}

}
