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

package multiplicity3.config.identity;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

/**
 * The Class IdentityConfigPrefsItem.
 */
public class IdentityConfigPrefsItem implements PreferencesItem {

	/** The Constant prefs. */
	private static final Preferences prefs = ConfigurationApplication
			.getPreferences(IdentityConfigPrefsItem.class);

	/** The Constant PREFS_TABLE_ID. */
	private static final String PREFS_TABLE_ID = "TABLE_ID";

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel() {
		return new IdentityConfigPanel();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanelName()
	 */
	@Override
	public String getConfigurationPanelName() {
		return "Identity";
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID() {
		return prefs.get(PREFS_TABLE_ID, "<no identity>");
	}

	/**
	 * Sets the id.
	 *
	 * @param uid the new id
	 */
	public void setID(String uid) {
		prefs.put(PREFS_TABLE_ID, uid);
	}

}
