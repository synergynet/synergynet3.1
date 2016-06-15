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

package multiplicity3.input.simulator.jmetuio;

import multiplicity3.input.simulator.IndividualCursor;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;

/**
 * The Class TableSimTUIOComms.
 */
public class TableSimTUIOComms
{

	/** The Constant DEFAULT_PORT. */
	public static final int DEFAULT_PORT = 3333;

	/** The instance. */
	private static TableSimTUIOComms instance;

	static
	{
		instance = new TableSimTUIOComms();
	}

	/** The current frame. */
	private int currentFrame;

	/** The osc port. */
	private OSCPortOut oscPort;

	/**
	 * Instantiates a new table sim tuio comms.
	 */
	private TableSimTUIOComms()
	{

	}

	/**
	 * Gets the single instance of TableSimTUIOComms.
	 *
	 * @return single instance of TableSimTUIOComms
	 */
	public static TableSimTUIOComms getInstance()
	{
		return instance;
	}

	/**
	 * Cursor delete.
	 */
	public void cursorDelete()
	{
		OSCBundle cursorBundle = new OSCBundle();

		OSCMessage remoteMessage = new OSCMessage("/tuio/2Dcur");
		remoteMessage.addArgument("source");
		remoteMessage.addArgument("vision");

		OSCMessage aliveMessage = new OSCMessage("/tuio/2Dcur");
		aliveMessage.addArgument("alive");

		cursorBundle.addPacket(remoteMessage);
		cursorBundle.addPacket(aliveMessage);

		sendOSC(cursorBundle);
	}

	/**
	 * Inits the.
	 *
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 */
	public void init(String host, int port)
	{
		try
		{
			oscPort = new OSCPortOut(java.net.InetAddress.getByName(host), port);
		}
		catch (Exception e)
		{
			oscPort = null;
		}
		reset();
		reset();
		reset();
	}

	/**
	 * Multi cursor message.
	 *
	 * @param cursors
	 *            the cursors
	 */
	public void multiCursorMessage(IndividualCursor[] cursors)
	{
		OSCBundle cursorBundle = new OSCBundle();

		OSCMessage remoteMessage1 = new OSCMessage("/tuio/2Dcur");
		remoteMessage1.addArgument("source");
		remoteMessage1.addArgument("vision");

		OSCMessage aliveMessage1 = new OSCMessage("/tuio/2Dcur");
		aliveMessage1.addArgument("alive");
		for (int i = 0; i < cursors.length; i++)
		{
			aliveMessage1.addArgument(new Integer(cursors[i].id));
		}

		cursorBundle.addPacket(remoteMessage1);
		cursorBundle.addPacket(aliveMessage1);

		for (int i = 0; i < cursors.length; i++)
		{
			addCursorInfoToBundle(cursors[i], cursorBundle);
		}

		sendOSC(cursorBundle);
	}

	/**
	 * Quit.
	 */
	public void quit()
	{
		reset();
		reset();
		reset();
		oscPort.close();
	}

	/**
	 * Reset.
	 */
	public void reset()
	{
		OSCBundle oscBundle = new OSCBundle();

		OSCMessage remoteMessage = new OSCMessage("/tuio/2Dobj");
		remoteMessage.addArgument("source");
		remoteMessage.addArgument("vision");

		OSCMessage aliveMessage = new OSCMessage("/tuio/2Dobj");
		aliveMessage.addArgument("alive");

		currentFrame++;
		OSCMessage frameMessage = new OSCMessage("/tuio/2Dobj");
		frameMessage.addArgument("fseq");
		frameMessage.addArgument(new Integer(currentFrame));

		oscBundle.addPacket(remoteMessage);
		oscBundle.addPacket(aliveMessage);
		oscBundle.addPacket(frameMessage);

		sendOSC(oscBundle);
	}

	/**
	 * Single cursor message.
	 *
	 * @param cursorInfo
	 *            the cursor info
	 */
	public void singleCursorMessage(IndividualCursor cursorInfo)
	{
		OSCBundle cursorBundle = new OSCBundle();

		OSCMessage remoteMessage = new OSCMessage("/tuio/2Dcur");
		remoteMessage.addArgument("source");
		remoteMessage.addArgument("vision");

		OSCMessage aliveMessage = new OSCMessage("/tuio/2Dcur");
		aliveMessage.addArgument("alive");
		aliveMessage.addArgument(new Integer(cursorInfo.id));

		cursorBundle.addPacket(remoteMessage);
		cursorBundle.addPacket(aliveMessage);

		addCursorInfoToBundle(cursorInfo, cursorBundle);
		sendOSC(cursorBundle);
	}

	/**
	 * Adds the cursor info to bundle.
	 *
	 * @param cursor
	 *            the cursor
	 * @param bundle
	 *            the bundle
	 */
	private void addCursorInfoToBundle(IndividualCursor cursor, OSCBundle bundle)
	{
		OSCMessage setMessage = new OSCMessage("/tuio/2Dcur");
		setMessage.addArgument("set");
		setMessage.addArgument(new Integer(cursor.id));
		setMessage.addArgument(new Float(cursor.x));
		setMessage.addArgument(new Float(cursor.y));
		setMessage.addArgument(new Float(0.0));
		setMessage.addArgument(new Float(0.0));
		setMessage.addArgument(new Float(0.0));
		bundle.addPacket(setMessage);
	}

	/**
	 * Send osc.
	 *
	 * @param packet
	 *            the packet
	 */
	private void sendOSC(OSCPacket packet)
	{
		try
		{
			oscPort.send(packet);
		}
		catch (java.io.IOException e)
		{
		}
	}
}
