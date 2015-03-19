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

import java.awt.geom.Point2D;


/**
 * Behaviour: middle-click + hold with a drag then release to define
 * the first finger and second finger.  Hold shift to rotate. Hold
 * CTRL to scale.  Left-click to finish. Space for third cursor.
 * 
 * @author dcs3ash
 *
 */

public class TripleFingerSimCursor extends AbstractSimCursor {
	
	private enum TripleFingerMode {
		Move,
		Rotate,
		Scale,
		InitialDistance,
		ThirdCursor
	}
	
	protected float mouseScreenX;
	protected float mouseScreenY;
	protected float radius;
	protected double angle = Math.toRadians(20.0);

	// technically don't need these anymore, just messy to get rid of
	protected Point2D.Float scaledCursor1 = new Point2D.Float();
	protected Point2D.Float scaledCursor2 = new Point2D.Float();
	protected Point2D.Float scaledCursor3 = new Point2D.Float();

	protected TripleFingerMode mode = TripleFingerMode.Move;
	private int id1;
	private int id2;
	private int id3;

	private float dy = 1;
	private float dx = 1;
	private Point2D.Float firstCursorPosition = new Point2D.Float();
	private Point2D.Float secondCursorPosition = new Point2D.Float();
	private Point2D.Float centralPoint = new Point2D.Float();
	private IMultiTouchSimulator simulator;
	

	public TripleFingerSimCursor(IMultiTouchSimulator simulator, int id1, int id2, int id3) {
		this.simulator = simulator;
		this.id1 = id1;
		this.id2 = id2;
		this.id3 = id3;
	}

	@Override
	public void mouseDragged(float x, float y, int button) {
		mouseScreenX = x;
		mouseScreenY = y;
		if(mode == TripleFingerMode.ThirdCursor) {
			scaledCursor3.setLocation(mouseScreenX, mouseScreenY);
			simulator.updateCursor(id3, scaledCursor3.x, scaledCursor3.y);
		}
	}

	@Override
	public void mouseMoved(float x, float y) {
		mouseScreenX = x;
		mouseScreenY = y;
		if(mode == TripleFingerMode.Move) {
			updatePositionInfo();
			simulator.updateTwoCursors(id1, scaledCursor1.x, scaledCursor1.y, id2, scaledCursor2.x, scaledCursor2.y);
		}else if(mode == TripleFingerMode.Rotate) {
			updateRotation(true);
			simulator.updateTwoCursors(id1, scaledCursor1.x, scaledCursor1.y, id2, scaledCursor2.x, scaledCursor2.y);
		}else if(mode == TripleFingerMode.Scale) {
			updateScaling();
			simulator.updateTwoCursors(id1, scaledCursor1.x, scaledCursor1.y, id2, scaledCursor2.x, scaledCursor2.y);
		}
		
	}

	@Override
	public void mousePressed(float x, float y, int button) {
		mouseScreenX = x;
		mouseScreenY = y;

		switch(button) {
		case AbstractSimCursor.MOUSE_BUTTON_LEFT: {
			if(mode == TripleFingerMode.ThirdCursor) {
				scaledCursor3.setLocation(mouseScreenX, mouseScreenY);				
				simulator.newCursor(id3, scaledCursor3.x, scaledCursor3.y);
				simulator.updateCursor(id3, scaledCursor3.x, scaledCursor3.y);
			}
			break;
		}
		case AbstractSimCursor.MOUSE_BUTTON_MIDDLE: {
			if(mode != TripleFingerMode.InitialDistance) {
				this.firstCursorPosition = new Point2D.Float(x, y);
				switchMode(TripleFingerMode.InitialDistance);
			}
			break;
		}
		}
	}

	@Override
	public void mouseReleased(float x, float y, int buttonNumber) {
		mouseScreenX = x;
		mouseScreenY = y;
		if(buttonNumber == AbstractSimCursor.MOUSE_BUTTON_MIDDLE && mode == TripleFingerMode.InitialDistance) {
			this.secondCursorPosition = new Point2D.Float(x, y);
			radius = (float) (firstCursorPosition.distance(secondCursorPosition)/2f);			
			updatePositionInfo();
			angle = -Math.atan(dy/dx) + Math.PI/2;
			switchMode(TripleFingerMode.Move);
			simulator.newCursor(id1, scaledCursor1.x, scaledCursor1.y);
			simulator.newCursor(id2, scaledCursor2.x, scaledCursor2.y);
			simulator.updateTwoCursors(id1, scaledCursor1.x, scaledCursor1.y, id2, scaledCursor2.x, scaledCursor2.y);
		}else if(buttonNumber == AbstractSimCursor.MOUSE_BUTTON_LEFT && mode == TripleFingerMode.Move) {
			simulator.deleteTwoCursors(id1, scaledCursor1.x, scaledCursor1.y, id2, scaledCursor2.x, scaledCursor2.y);
			simulator.clearCursor();
		}else if(buttonNumber == AbstractSimCursor.MOUSE_BUTTON_LEFT && mode == TripleFingerMode.ThirdCursor) {
			simulator.deleteCursor(id3, scaledCursor3.x, scaledCursor3.y);
		}
	}



	@Override
	public void keyPressed(String key) {
		if(key.equals(AbstractSimCursor.KEY_SHIFT)) {
			switchMode(TripleFingerMode.Rotate);
		}else if(key.equals(AbstractSimCursor.KEY_CONTROL)) {
			switchMode(TripleFingerMode.Scale);
		}
	}

	@Override
	public void keyReleased(String key) {
		if(key.equals(AbstractSimCursor.KEY_SHIFT)) {
			switchMode(TripleFingerMode.Move);
		}else if(key.equals(AbstractSimCursor.KEY_CONTROL)) {
			switchMode(TripleFingerMode.Move);
		}else if(key.equals(AbstractSimCursor.KEY_SPACE)) {
			if(mode == TripleFingerMode.ThirdCursor) {
				switchMode(TripleFingerMode.Move);
			}else{
				switchMode(TripleFingerMode.ThirdCursor);
			}
		}
	}

	private void updateRotation(boolean updateAngle) {
		float deltaX = centralPoint.x - mouseScreenX;
		float deltaY = centralPoint.y - mouseScreenY;
		if(updateAngle) 
			angle = -Math.atan(deltaY/deltaX) + Math.PI/2;
		if(deltaX >= 0) {

			firstCursorPosition.y = (float)(centralPoint.y + radius * Math.cos(angle));
			firstCursorPosition.x = (float)(centralPoint.x + radius * Math.sin(angle));

			secondCursorPosition.y = (float)(centralPoint.y - radius * Math.cos(angle));
			secondCursorPosition.x = (float)(centralPoint.x - radius * Math.sin(angle));
		}else{
			secondCursorPosition.y = (float)(centralPoint.y + radius * Math.cos(angle));
			secondCursorPosition.x = (float)(centralPoint.x + radius * Math.sin(angle));

			firstCursorPosition.y = (float)(centralPoint.y - radius * Math.cos(angle));
			firstCursorPosition.x = (float)(centralPoint.x - radius * Math.sin(angle));			
		}
		scaledCursor1.x = firstCursorPosition.x;
		scaledCursor1.y = firstCursorPosition.y;

		scaledCursor2.x = secondCursorPosition.x;
		scaledCursor2.y = secondCursorPosition.y;
	}
	
	private void updatePositionInfo() {
		float deltaX = mouseScreenX - secondCursorPosition.x;
		float deltaY = mouseScreenY - secondCursorPosition.y;
		firstCursorPosition.x += deltaX;
		firstCursorPosition.y += deltaY;
		secondCursorPosition.x += deltaX;
		secondCursorPosition.y += deltaY;
		
		dx = secondCursorPosition.x - firstCursorPosition.x;
		dy = secondCursorPosition.y - firstCursorPosition.y;
		centralPoint = new Point2D.Float(
				(float)(firstCursorPosition.x + dx/2f),
				(float)(firstCursorPosition.y + dy/2f)
				);
		
		scaledCursor1.setLocation(firstCursorPosition.x, firstCursorPosition.y);
		scaledCursor2.setLocation(secondCursorPosition.x, secondCursorPosition.y);
	}
	
	private void updateScaling() {
		radius = (float) new Point2D.Float(mouseScreenX, mouseScreenY).distance(centralPoint);
		updateRotation(false);
	}

	private void switchMode(TripleFingerMode newMode) {
		this.mode = newMode;		
	}

	public TripleFingerMode getMode() {
		return mode;
	}

	public Point2D.Float getFirstCursorPosition() {
		return firstCursorPosition;
	}

	public float getMouseX() {
		return mouseScreenX;
	}

	public float getMouseY() {
		return mouseScreenY;
	}

	public Point2D.Float getCentralPoint() {
		return centralPoint;
	}

	public Point2D.Float getSecondCursorPosition() {
		return secondCursorPosition;
	}

	public int getIDForCursor1() {
		return id1;
	}

	public int getIDForCursor2() {
		return id2;
	}
	
}
