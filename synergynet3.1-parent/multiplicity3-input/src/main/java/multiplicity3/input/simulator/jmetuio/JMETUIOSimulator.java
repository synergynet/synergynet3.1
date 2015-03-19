/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
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
 */

package multiplicity3.input.simulator.jmetuio;

import multiplicity3.input.simulator.AbstractMultiTouchSimulator;
import multiplicity3.input.simulator.IndividualCursor;



public class JMETUIOSimulator extends AbstractMultiTouchSimulator {

//	public JMETUIOSimulator(int tableWidth, int tableHeight) {
		//super(tableWidth, tableHeight);
//	}

	public void start() {
		TableSimTUIOComms.getInstance().init("localhost", TableSimTUIOComms.DEFAULT_PORT);
	}
	
	public void stop() {
		TableSimTUIOComms.getInstance().quit();
	}

	@Override
	public void updateCursor(int id, float x, float y) {
		TableSimTUIOComms.getInstance().singleCursorMessage(new IndividualCursor(id, x, y));
	}

	@Override
	public void updateTwoCursors(int id1, float x, float y, int id2, float x2, float y2) {
		IndividualCursor[] cursorInfo = new IndividualCursor[2];
		cursorInfo[0] = new IndividualCursor(id1, x, y);
		cursorInfo[1] = new IndividualCursor(id2, x2, y2);
		TableSimTUIOComms.getInstance().multiCursorMessage(cursorInfo);
	}

	@Override
	public void deleteCursor(int id, float x, float y) {
		TableSimTUIOComms.getInstance().cursorDelete();		
	}

	@Override
	public void deleteTwoCursors(int id1, float x1, float y1, int id2, float x2, float y2) {
		TableSimTUIOComms.getInstance().cursorDelete();		
	}

	@Override
	public void newCursor(int id, float x, float y) {
		updateCursor(id, x, y);	
	}

	public void update(float tpf) {}

	@Override
	public boolean requiresMouseDisplay() {
		return true;
	}
	
	@Override
	public void endListening() {
		stop();
	}

}
