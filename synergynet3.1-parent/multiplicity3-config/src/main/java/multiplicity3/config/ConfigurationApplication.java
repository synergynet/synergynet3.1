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

package multiplicity3.config;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import multiplicity3.ioutils.FileUtils;

/**
 * The Class ConfigurationApplication.
 */
public class ConfigurationApplication
{

	/** The Constant CORE_PREFS_LIST_FILE. */
	private static final String CORE_PREFS_LIST_FILE = "corepreferences.list";

	/** The jtp. */
	protected static JTabbedPane jtp;

	/**
	 * Gets the preferences.
	 *
	 * @param c
	 *            the c
	 * @return the preferences
	 */
	public static Preferences getPreferences(Class<? extends PreferencesItem> c)
	{
		return Preferences.userNodeForPackage(c);
	}

	/**
	 * Gets the tabbed pane.
	 *
	 * @return the tabbed pane
	 */
	public static JTabbedPane getTabbedPane()
	{
		return jtp;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		startConfigGUI(620, 360);
	}

	/**
	 * Gets the core preferences items.
	 *
	 * @return the core preferences items
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static List<PreferencesItem> getCorePreferencesItems() throws IOException
	{
		List<PreferencesItem> items = new ArrayList<PreferencesItem>();
		List<String> classes = FileUtils.readAsStringList(ConfigurationApplication.class.getResourceAsStream(CORE_PREFS_LIST_FILE), true);
		for (String c : classes)
		{
			try
			{
				PreferencesItem item = getPreferencesItemForClass(c);
				items.add(item);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return items;
	}

	/**
	 * Gets the preferences item for class.
	 *
	 * @param classname
	 *            the classname
	 * @return the preferences item for class
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	@SuppressWarnings("unchecked")
	private static PreferencesItem getPreferencesItemForClass(String classname) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class<? extends PreferencesItem> prefsClass = (Class<? extends PreferencesItem>) Class.forName(classname);
		PreferencesItem item = prefsClass.newInstance();
		return item;
	}

	/**
	 * Load core preferences items.
	 *
	 * @param jtp
	 *            the jtp
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void loadCorePreferencesItems(JTabbedPane jtp) throws IOException
	{
		List<PreferencesItem> prefsItems = getCorePreferencesItems();
		for (PreferencesItem item : prefsItems)
		{
			jtp.add(item.getConfigurationPanelName(), item.getConfigurationPanel());
		}
	}

	/**
	 * Exit.
	 */
	protected static void exit()
	{
		System.exit(0);

	}

	/**
	 * Start config GUI.
	 * 
	 * @param width
	 * 			Set the width of the GUI.
	 * 
	 * @param height
	 * 			Set the height of the GUI.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 */
	protected static void startConfigGUI(int width, int height) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		JFrame jf = new JFrame("Configuration Tool");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(new BorderLayout());
		jtp = new JTabbedPane();
		jtp.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				if ((e.getKeyCode() == 27) || (e.getKeyCode() == 157))
				{
					exit();
				}
			}
		});

		loadCorePreferencesItems(jtp);

		jf.getContentPane().add(jtp, BorderLayout.CENTER);
		jf.setSize(width, height);
		jf.setVisible(true);
	}

}
