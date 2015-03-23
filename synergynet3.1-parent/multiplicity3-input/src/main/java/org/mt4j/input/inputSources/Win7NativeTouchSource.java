/*
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All
 * rights reserved. This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.mt4j.input.inputSources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.utils.ClickDetector;
import multiplicity3.input.win7.Win7TouchInput;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Input source for native Windows 7 WM_TOUCH messages for single/multi-touch.
 */
public class Win7NativeTouchSource {

	/** The Constant dllName32. */
	private static final String dllName32 = "Win7Touch";

	/** The Constant dllName64. */
	private static final String dllName64 = "Win7Touch64";

	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(Win7NativeTouchSource.class.getName());

	/** The loaded. */
	static boolean loaded = false;

	/** The app handle. */
	private int appHandle;

	/** The initialized. */
	private boolean initialized;

	/** The success. */
	private boolean success;

	/** The win7 touch input. */
	private Win7TouchInput win7TouchInput;

	/** The wm touch event. */
	private Windows7TouchEvent wmTouchEvent;

	/** The calling list. */
	protected List<Callable<Object>> callingList = new ArrayList<Callable<Object>>();

	/** The click detector. */
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);

	/** The listeners. */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();

	/**
	 * Instantiates a new win7 native touch source.
	 */
	public Win7NativeTouchSource(Win7TouchInput win7TouchInputIn) {
		this.win7TouchInput = win7TouchInputIn;

		this.success = false;

		String platform = System.getProperty("os.name").toLowerCase();
		logger.info("Platform: \"" + platform + "\"");

		if (!platform.contains("windows")) {
			logger.severe("Win7NativeTouchSource input source can only be used on windows.");
		}

		if (!loaded) {
			loaded = true;

			// Copy dll to temp directory to avoid loading issues from jar
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {

					String dllName = dllName32;
					if (win7TouchInput.isIs64bitJava()) {
						dllName = dllName64;
					}

					File tmpDir = new File(System.getProperty("java.io.tmpdir"));
					File tmpFile = new File(tmpDir, dllName + ".dll");

					try {
						InputStream in = Win7NativeTouchSource.class
								.getResourceAsStream(dllName + ".dll");
						OutputStream out = new FileOutputStream(tmpFile);

						byte[] buf = new byte[8192];
						int len;
						while ((len = in.read(buf)) != -1) {
							out.write(buf, 0, len);
						}

						in.close();
						out.close();

						System.load(tmpFile.getAbsolutePath());

					} catch (FileNotFoundException fnfe) {
						logger.warning("Could not create temporary dll.");
					} catch (IOException ioe) {
						logger.warning("Could not copy dll.");
					}

					return null;
				}
			});

		} else {
			logger.warning("Win7NativeTouchSource may only be instantiated once.");
			return;
		}

		boolean touchAvailable = this.getSystemMetrics();
		if (!touchAvailable) {
			logger.severe("Windows 7 Touch Input currently not available!");
			System.exit(0);
			return;
		} else {
			logger.info("Windows 7 Touch Input available.");
		}

		wmTouchEvent = new Windows7TouchEvent();
		wmTouchEvent.id = -1;
		wmTouchEvent.type = -1;
		wmTouchEvent.x = -1;
		wmTouchEvent.y = -1;

		initialized = false;

		this.getNativeWindowHandles();
		success = true;

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (isSuccessfullySetup()) {
					logger.info("Cleaning up Win7 touch source..");
					quit();
				}
			}
		}));

	}

	/**
	 * Checks if is successfully setup.
	 *
	 * @return true, if is successfully setup
	 */
	public boolean isSuccessfullySetup() {
		return success;
	}

	/**
	 * Poll events.
	 */
	public void pollEvents() {
		if (initialized) { // Only poll events if native c++ core was
							// initialized successfully
			while (pollEvent(wmTouchEvent)) {
				switch (wmTouchEvent.type) {
					case Windows7TouchEvent.TOUCH_DOWN: {
						// logger.info("TOUCH_DOWN ==> ID:" + wmTouchEvent.id +
						// " x:" + wmTouchEvent.x + " y:" + wmTouchEvent.y);

						win7TouchInput.addTouchCursor(wmTouchEvent);

						break;
					}
					case Windows7TouchEvent.TOUCH_MOVE: {
						// logger.info("TOUCH_MOVE ==> ID:" + wmTouchEvent.id +
						// " x:" + wmTouchEvent.x + " y:" + wmTouchEvent.y);
						win7TouchInput.updateTouchCursor(wmTouchEvent);
						break;
					}
					case Windows7TouchEvent.TOUCH_UP: {
						// logger.info("TOUCH_UP ==> ID:" + wmTouchEvent.id +
						// " x:" + wmTouchEvent.x + " y:" + wmTouchEvent.y);

						win7TouchInput.removeTouchCursor(wmTouchEvent);
						break;
					}
					default:
						break;
				}
			}
		}
	}

	// NATIVE METHODS //
	/**
	 * Find window.
	 *
	 * @param tmpTitle the tmp title
	 * @param subWindowTitle the sub window title
	 * @return the int
	 */
	private native int findWindow(String tmpTitle, String subWindowTitle);

	/**
	 * Gets the native window handles.
	 *
	 * @return the native window handles
	 */
	private void getNativeWindowHandles() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HWND fgWindow = User32.INSTANCE.GetForegroundWindow();
				int titleLength = User32.INSTANCE.GetWindowTextLength(fgWindow) + 1;
				char[] title = new char[titleLength];
				User32.INSTANCE.GetWindowText(fgWindow, title, titleLength);
				int awtCanvasHandle = (int) Pointer.nativeValue(fgWindow
						.getPointer());
				setApplicationHandle(awtCanvasHandle);
			}
		});
	}

	/**
	 * Gets the system metrics.
	 *
	 * @return the system metrics
	 */
	private native boolean getSystemMetrics();

	/**
	 * Inits the.
	 *
	 * @param HWND the hwnd
	 * @return true, if successful
	 */
	private native boolean init(long HWND);

	/**
	 * Poll event.
	 *
	 * @param myEvent the my event
	 * @return true, if successful
	 */
	private native boolean pollEvent(Windows7TouchEvent myEvent);

	// NATIVE METHODS //

	/**
	 * Quit.
	 *
	 * @return true, if successful
	 */
	private native boolean quit();

	/**
	 * Sets the application handle.
	 *
	 * @param HWND the new application handle
	 */
	private void setApplicationHandle(int HWND) {
		if (HWND > 0) {
			appHandle = HWND;
			logger.info("-> Found application HWND: " + appHandle);
			init(appHandle); // Initialises the c++ core in the native dll
			initialized = true;
		} else {
			logger.severe("-> Couldnt retrieve the application handle!");
		}
	}

}
