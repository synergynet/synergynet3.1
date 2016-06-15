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

package multiplicity3.input.simulator;

/**
 * The Interface IMultiTouchSimulator.
 */
public interface IMultiTouchSimulator
{

	/**
	 * Clear cursor.
	 */
	public void clearCursor();

	/**
	 * Delete cursor.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void deleteCursor(int id, float x, float y);

	/**
	 * Delete two cursors.
	 *
	 * @param id1
	 *            the id1
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param id2
	 *            the id2
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 */
	public void deleteTwoCursors(int id1, float x, float y, int id2, float x2, float y2);

	/**
	 * New cursor.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void newCursor(int id, float x, float y);

	/**
	 * Update cursor.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void updateCursor(int id, float x, float y);

	/**
	 * Update two cursors.
	 *
	 * @param id1
	 *            the id1
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param id2
	 *            the id2
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 */
	public void updateTwoCursors(int id1, float x, float y, int id2, float x2, float y2);
}
