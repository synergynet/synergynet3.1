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

package multiplicity3.input.simulator;

public abstract class AbstractSimCursor {
	
	public static final String KEY_SHIFT = "shift";
	public static final String KEY_CONTROL = "control";
	public static final String KEY_SPACE = "space";
	
	public static final int MOUSE_BUTTON_LEFT = 0;
	public static final int MOUSE_BUTTON_MIDDLE = 1;
	public static final int MOUSE_BUTTON_RIGHT = 2;
	
	
	public abstract void mousePressed(float x, float y, int buttonNumber);
	public abstract void mouseDragged(float x, float y, int buttonNumber);
	public abstract void mouseReleased(float x, float y, int buttonNumber);	
	public abstract void mouseMoved(float x, float y);
	public abstract void keyPressed(String key);
	public abstract void keyReleased(String key);

	
//	public static float getScaledX(int x, int width) {
//		return (float) x / (float) width;
//	}
//	
//	public static float getScaledY(int y, int height) {
//		return (float) y / (float) height;
//	}
	

}
