/*
 * Copyright (c) 2003, C. Ramakrishnan / Auracle All rights reserved.
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
 * $Id: OSCByteArrayToJavaConverter.java,v 1.1 2010/05/06 14:59:53 dcs0ah1 Exp $
 * Created on 28.10.2003
 */
package com.illposed.osc.utility;

import java.math.BigInteger;
import java.util.Date;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

/**
 * @author cramakrishnan
 */
public class OSCByteArrayToJavaConverter
{

	/** The float bytes. */
	private byte[] floatBytes = new byte[4];

	/** The int bytes. */
	private byte[] intBytes = new byte[4];

	/** The picosec bytes. */
	private byte[] picosecBytes = new byte[8];

	/** The second bytes. */
	private byte[] secondBytes = new byte[8];

	/** The bytes. */
	byte[] bytes;

	/** The bytes length. */
	int bytesLength;

	/** The stream position. */
	int streamPosition;

	/**
	 * Helper object for converting from a byte array to Java objects
	 */
	/*
	 * public OSCByteArrayToJavaConverter() { super(); }
	 */

	public OSCPacket convert(byte[] byteArray, int bytesLength)
	{
		bytes = byteArray;
		this.bytesLength = bytesLength;
		streamPosition = 0;
		if (isBundle())
		{
			return convertBundle();
		}
		else
		{
			return convertMessage();
		}
	}

	/**
	 * Convert bundle.
	 *
	 * @return the OSC bundle
	 */
	private OSCBundle convertBundle()
	{
		// skip the "#bundle " stuff
		streamPosition = 8;
		Date timestamp = readTimeTag();
		OSCBundle bundle = new OSCBundle(timestamp);
		OSCByteArrayToJavaConverter myConverter = new OSCByteArrayToJavaConverter();
		while (streamPosition < bytesLength)
		{
			// recursively read through the stream and convert packets you find
			int packetLength = ((Integer) readInteger()).intValue();
			byte[] packetBytes = new byte[packetLength];
			// streamPosition++;
			System.arraycopy(bytes, streamPosition, packetBytes, 0, packetLength);
			streamPosition += packetLength;
			// for (int i = 0; i < packetLength; i++)
			// packetBytes[i] = bytes[streamPosition++];
			OSCPacket packet = myConverter.convert(packetBytes, packetLength);
			bundle.addPacket(packet);
		}
		return bundle;
	}

	/**
	 * Convert message.
	 *
	 * @return the OSC message
	 */
	private OSCMessage convertMessage()
	{
		OSCMessage message = new OSCMessage();
		message.setAddress(readString());
		char[] types = readTypes();
		if (null == types)
		{
			// we are done
			return message;
		}
		moveToFourByteBoundry();
		for (int i = 0; i < types.length; i++)
		{
			if ('[' == types[i])
			{
				// we're looking at an array -- read it in
				message.addArgument(readArray(types, i));
				// then increment i to the end of the array
				while (']' != types[i])
				{
					i++;
				}
			}
			else
			{
				message.addArgument(readArgument(types[i]));
			}
		}
		return message;
	}

	/**
	 * Checks if is bundle.
	 *
	 * @return true, if is bundle
	 */
	private boolean isBundle()
	{
		// only need the first 7 to check if it is a bundle
		String bytesAsString = new String(bytes, 0, 7);
		return bytesAsString.startsWith("#bundle");
	}

	/**
	 * Length of current string.
	 *
	 * @return the int
	 */
	private int lengthOfCurrentString()
	{
		int i = 0;
		while (bytes[streamPosition + i] != 0)
		{
			i++;
		}
		return i;
	}

	/**
	 * Move to four byte boundry.
	 */
	private void moveToFourByteBoundry()
	{
		// If i'm already at a 4 byte boundry, I need to move to the next one
		int mod = streamPosition % 4;
		streamPosition += (4 - mod);
	}

	/**
	 * @param c
	 *            type of argument
	 * @return a Java representation of the argument
	 */
	private Object readArgument(char c)
	{
		switch (c)
		{
			case 'i':
				return readInteger();
			case 'h':
				return readBigInteger();
			case 'f':
				return readFloat();
			case 'd':
				return readDouble();
			case 's':
				return readString();
			case 'c':
				return readChar();
			case 'T':
				return Boolean.TRUE;
			case 'F':
				return Boolean.FALSE;
		}

		return null;
	}

	/**
	 * @param types
	 * @param i
	 * @return an Array
	 */
	private Object[] readArray(char[] types, int i)
	{
		int arrayLen = 0;
		while (types[i + arrayLen] != ']')
		{
			arrayLen++;
		}
		Object[] array = new Object[arrayLen];
		for (int j = 0; i < arrayLen; j++)
		{
			array[j] = readArgument(types[i + j]);
		}
		return array;
	}

	/**
	 * @return a BigInteger
	 */
	private Object readBigInteger()
	{
		// byte[] intBytes = new byte[4];
		intBytes[0] = bytes[streamPosition++];
		intBytes[1] = bytes[streamPosition++];
		intBytes[2] = bytes[streamPosition++];
		intBytes[3] = bytes[streamPosition++];

		int intBits = ((intBytes[3] & 0xFF)) + ((intBytes[2] & 0xFF) << 8) + ((intBytes[1] & 0xFF) << 16) + ((intBytes[0] & 0xFF) << 24);

		return new Integer(intBits);
	}

	/**
	 * @return a Character
	 */
	private Object readChar()
	{
		return new Character((char) bytes[streamPosition++]);
	}

	/**
	 * @return a Double
	 */
	private Object readDouble()
	{
		return readFloat();
	}

	/**
	 * @return a Float
	 */
	private Object readFloat()
	{
		// byte[] floatBytes = new byte[4];
		floatBytes[0] = bytes[streamPosition++];
		floatBytes[1] = bytes[streamPosition++];
		floatBytes[2] = bytes[streamPosition++];
		floatBytes[3] = bytes[streamPosition++];

		int floatBits = ((floatBytes[3] & 0xFF)) + ((floatBytes[2] & 0xFF) << 8) + ((floatBytes[1] & 0xFF) << 16) + ((floatBytes[0] & 0xFF) << 24);

		return new Float(Float.intBitsToFloat(floatBits));
	}

	/**
	 * @return an Integer
	 */
	private Object readInteger()
	{
		// byte[] intBytes = new byte[4];
		intBytes[0] = bytes[streamPosition++];
		intBytes[1] = bytes[streamPosition++];
		intBytes[2] = bytes[streamPosition++];
		intBytes[3] = bytes[streamPosition++];

		int intBits = ((intBytes[3] & 0xFF)) + ((intBytes[2] & 0xFF) << 8) + ((intBytes[1] & 0xFF) << 16) + ((intBytes[0] & 0xFF) << 24);

		return new Integer(intBits);
	}

	/**
	 * Read string.
	 *
	 * @return the string
	 */
	private String readString()
	{
		int strLen = lengthOfCurrentString();
		char[] stringChars = new char[strLen];
		// System.arraycopy(bytes,streamPosition,stringChars,0,strLen);
		// streamPosition+=strLen;
		for (int i = 0; i < strLen; i++)
		{
			stringChars[i] = (char) bytes[streamPosition++];
		}
		moveToFourByteBoundry();
		return new String(stringChars);
	}

	/**
	 * @return a Date
	 */
	private Date readTimeTag()
	{
		// byte[] secondBytes = new byte[8];
		// byte[] picosecBytes = new byte[8];
		/*
		 * for (int i = 4; i < 8; i++) secondBytes[i] = bytes[streamPosition++];
		 * for (int i = 4; i < 8; i++) picosecBytes[i] =
		 * bytes[streamPosition++];
		 */
		System.arraycopy(bytes, streamPosition, secondBytes, 4, 4);
		streamPosition += 4;
		System.arraycopy(bytes, streamPosition, picosecBytes, 4, 4);
		streamPosition += 4;

		BigInteger secsSince1900 = new BigInteger(secondBytes);
		long secsSince1970 = secsSince1900.longValue() - OSCBundle.SECONDS_FROM_1900_to_1970.longValue();
		if (secsSince1970 < 0)
		{
			secsSince1970 = 0; // no point maintaining times in the distant past
		}
		BigInteger picosecs = new BigInteger(picosecBytes);
		long millisecs = (secsSince1970 * 1000) + (picosecs.longValue() / 1000);
		return new Date(millisecs);
	}

	/**
	 * @return a char array with the types of the arguments
	 */
	private char[] readTypes()
	{
		// the next byte should be a ","
		if (bytes[streamPosition] != 0x2C)
		{
			return null;
		}
		streamPosition++;
		// find out how long the list of types is
		int typesLen = lengthOfCurrentString();
		if (0 == typesLen)
		{
			return null;
		}
		// read in the types
		char[] typesChars = new char[typesLen];
		for (int i = 0; i < typesLen; i++)
		{
			typesChars[i] = (char) bytes[streamPosition++];
		}
		return typesChars;
	}

}
