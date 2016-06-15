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

package multiplicity3.input.simulator.jme;

import multiplicity3.input.simulator.AbstractMultiTouchSimulator;
import multiplicity3.input.simulator.AbstractSimCursor;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 * The Class JMEMouseKeyboardInputManager.
 */
public class JMEMouseKeyboardInputManager
{

	/** The input manager. */
	private InputManager inputManager;

	/** The mmb. */
	private boolean lmb, rmb, mmb; // left, middle, right mouse buttons down?

	/** The mouse move listener. */
	private AnalogListener mouseMoveListener = new AnalogListener()
	{

		@Override
		public void onAnalog(String name, float isPressed, float tpf)
		{
			if (lmb)
			{
				simulator.mouseDragged(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_LEFT);
			}
			else if (rmb)
			{
				simulator.mouseDragged(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_RIGHT);
			}
			else if (mmb)
			{
				simulator.mouseDragged(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_MIDDLE);
			}
			else
			{
				simulator.mouseMoved(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight);
			}
		}

	};

	/** The screen height. */
	private float screenHeight;

	/** The screen width. */
	private float screenWidth;

	/** The simulator. */
	protected AbstractMultiTouchSimulator simulator;

	/**
	 * Instantiates a new JME mouse keyboard input manager.
	 *
	 * @param simulator
	 *            the simulator
	 * @param inputManager
	 *            the input manager
	 * @param screenWidth
	 *            the screen width
	 * @param screenHeight
	 *            the screen height
	 */
	public JMEMouseKeyboardInputManager(final AbstractMultiTouchSimulator simulator, final InputManager inputManager, final float screenWidth, final float screenHeight)
	{
		this.simulator = simulator;
		this.inputManager = inputManager;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		inputManager.addMapping("MBL", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(new ActionListener()
		{
			@Override
			public void onAction(String name, boolean isPressed, float tpf)
			{
				if (isPressed)
				{
					lmb = true;
					simulator.mousePressed(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_LEFT);
				}
				else
				{
					lmb = false;
					simulator.mouseReleased(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_LEFT);
				}
			}
		}, "MBL");

		inputManager.addMapping("MBM", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
		inputManager.addListener(new ActionListener()
		{
			@Override
			public void onAction(String name, boolean isPressed, float tpf)
			{
				if (isPressed)
				{
					mmb = true;
					simulator.mousePressed(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_MIDDLE);
				}
				else
				{
					mmb = false;
					simulator.mouseReleased(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_MIDDLE);
				}
			}
		}, "MBM");

		inputManager.addMapping("MBR", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(new ActionListener()
		{
			@Override
			public void onAction(String name, boolean isPressed, float tpf)
			{
				if (isPressed)
				{
					rmb = true;
					simulator.mousePressed(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_RIGHT);
				}
				else
				{
					rmb = false;
					simulator.mouseReleased(inputManager.getCursorPosition().x / screenWidth, inputManager.getCursorPosition().y / screenHeight, AbstractSimCursor.MOUSE_BUTTON_RIGHT);
				}
			}
		}, "MBR");

		inputManager.addMapping("MY", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping("MYN", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping("MX", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping("MXN", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addListener(mouseMoveListener, "MX", "MXN", "MY", "MYN");

		inputManager.addMapping("SHIFT", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addListener(new ActionListener()
		{
			@Override
			public void onAction(String name, boolean isPressed, float tpf)
			{
				if (isPressed)
				{
					simulator.keyPressed(AbstractSimCursor.KEY_SHIFT);
				}
				else
				{
					simulator.keyReleased(AbstractSimCursor.KEY_SHIFT);
				}
			}
		}, "SHIFT");

		inputManager.addMapping("CONTROL", new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addListener(new ActionListener()
		{
			@Override
			public void onAction(String name, boolean isPressed, float tpf)
			{
				if (isPressed)
				{
					simulator.keyPressed(AbstractSimCursor.KEY_CONTROL);
				}
				else
				{
					simulator.keyReleased(AbstractSimCursor.KEY_CONTROL);
				}
			}
		}, "CONTROL");

		inputManager.addMapping("SPACE", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(new ActionListener()
		{
			@Override
			public void onAction(String name, boolean isPressed, float tpf)
			{
				if (isPressed)
				{
					simulator.keyPressed(AbstractSimCursor.KEY_SPACE);
				}
				else
				{
					simulator.keyReleased(AbstractSimCursor.KEY_SPACE);
				}
			}
		}, "SPACE");

	}

	/**
	 * On key.
	 *
	 * @param character
	 *            the character
	 * @param keyCode
	 *            the key code
	 * @param pressed
	 *            the pressed
	 */
	public void onKey(char character, int keyCode, boolean pressed)
	{
		if (pressed)
		{
			if (shiftKeysPressed(keyCode))
			{
				simulator.keyPressed(AbstractSimCursor.KEY_SHIFT);
			}
			else if (controlKeysPressed(keyCode))
			{
				simulator.keyPressed(AbstractSimCursor.KEY_CONTROL);
			}
			else if (spaceKeyPressed(keyCode))
			{
				simulator.keyPressed(AbstractSimCursor.KEY_SPACE);
			}
		}
		else
		{
			if (shiftKeysPressed(keyCode))
			{
				simulator.keyReleased(AbstractSimCursor.KEY_SHIFT);
			}
			else if (controlKeysPressed(keyCode))
			{
				simulator.keyReleased(AbstractSimCursor.KEY_CONTROL);
			}
			else if (spaceKeyPressed(keyCode))
			{
				simulator.keyReleased(AbstractSimCursor.KEY_SPACE);
			}
		}
	}

	/**
	 * Control keys pressed.
	 *
	 * @param keyCode
	 *            the key code
	 * @return true, if successful
	 */
	private boolean controlKeysPressed(int keyCode)
	{
		return (keyCode == KeyInput.KEY_LCONTROL) || (keyCode == KeyInput.KEY_RCONTROL);
	}

	/**
	 * Shift keys pressed.
	 *
	 * @param keyCode
	 *            the key code
	 * @return true, if successful
	 */
	private boolean shiftKeysPressed(int keyCode)
	{
		return (keyCode == KeyInput.KEY_LSHIFT) || (keyCode == KeyInput.KEY_RSHIFT);
	}

	/**
	 * Space key pressed.
	 *
	 * @param keyCode
	 *            the key code
	 * @return true, if successful
	 */
	private boolean spaceKeyPressed(int keyCode)
	{
		return keyCode == KeyInput.KEY_SPACE;
	}

}
