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

package synergynet3.museum.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import synergynet3.museum.table.settingsapp.appearance.AppearanceConfigPrefsItem;

import multiplicity3.config.PreferencesItem;
import multiplicity3.ioutils.FileUtils;

public class MuseumSettingsApp {
    
	public static JFrame jf;	
    protected static JTabbedPane jtp;
    
	private static final String CORE_PREFS_LIST_FILE = "museumpreferences.list";

	public static Preferences getPreferences(Class<? extends PreferencesItem> c) {
		return Preferences.userNodeForPackage(c);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		startConfigGUI();		
	}

	
	protected static void startConfigGUI() throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		jf = new JFrame("Museum App Settings");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(new BorderLayout());
		
		jtp = new JTabbedPane();
		jtp.addKeyListener(new KeyAdapter() {
			@Override public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 27 || e.getKeyCode() == 157) exit();
			}
		});

		loadCorePreferencesItems(jtp);
		
		int w = 800;
		int h = 400;

		jf.getContentPane().add(jtp, BorderLayout.CENTER);
		jf.setSize(w, h);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width-w)/2;
		int screenY = (dim.height-h)/2;
		jf.setLocation(screenX, screenY);	
		
		jf.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	AppearanceConfigPrefsItem.savetoXML();
		    }
		});
		
		jf.setResizable(false);
		jf.setVisible(true);
	}
        
	public static JTabbedPane getTabbedPane() {
		return jtp;
	}
	
	protected static void exit() {
		System.exit(0);		
	}

	private static void loadCorePreferencesItems(JTabbedPane jtp) throws IOException {
		final List<PreferencesItem> prefsItems = getCorePreferencesItems();
		for(PreferencesItem item : prefsItems) {
			jtp.add(item.getConfigurationPanelName(), item.getConfigurationPanel());
		}		
	}

	private static List<PreferencesItem> getCorePreferencesItems() throws IOException {
		List<PreferencesItem> items = new ArrayList<PreferencesItem>();
		List<String> classes = FileUtils.readAsStringList(MuseumSettingsApp.class.getResourceAsStream(CORE_PREFS_LIST_FILE), true);
		for(String c : classes) {
			try {
				PreferencesItem item = getPreferencesItemForClass(c);
				items.add(item);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	private static PreferencesItem getPreferencesItemForClass(String classname) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<? extends PreferencesItem> prefsClass = (Class<? extends PreferencesItem>) Class.forName(classname);
		PreferencesItem item = prefsClass.newInstance();
		return item;
	}
	
}
