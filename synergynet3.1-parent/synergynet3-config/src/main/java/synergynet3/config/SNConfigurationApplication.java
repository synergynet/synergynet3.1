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

package synergynet3.config;

import java.io.FileNotFoundException;
import java.io.IOException;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

/**
 * The Class SNConfigurationApplication.
 */
public class SNConfigurationApplication extends ConfigurationApplication
{

	/** The Constant POSITION_PREF_NAME. */
	private final static String POSITION_PREF_NAME = "multiplicity3.config.position.PositionConfigPrefsItem";

	/** The Constant WEB_PREF_NAME. */
	private final static String WEB_PREF_NAME = "synergynet3.config.web.WebConfigPrefsItem";

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
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		startConfigGUI();

		Class<? extends PreferencesItem> webPrefsClass = (Class<? extends PreferencesItem>) Class.forName(WEB_PREF_NAME);
		PreferencesItem webPrefsItem = webPrefsClass.newInstance();
		jtp.add(webPrefsItem.getConfigurationPanelName(), webPrefsItem.getConfigurationPanel());

		Class<? extends PreferencesItem> positionPrefsClass = (Class<? extends PreferencesItem>) Class.forName(POSITION_PREF_NAME);
		PreferencesItem positionPrefsItem = positionPrefsClass.newInstance();
		jtp.add(positionPrefsItem.getConfigurationPanelName(), positionPrefsItem.getConfigurationPanel());
	}

}
