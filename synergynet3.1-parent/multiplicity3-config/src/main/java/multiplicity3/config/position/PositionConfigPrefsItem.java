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

package multiplicity3.config.position;

import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

public class PositionConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationApplication.getPreferences(PositionConfigPrefsItem.class);

	public static final String PREFS_LOCATION_X = "DISPLAY_LOCATION_X";
	public static final String PREFS_LOCATION_Y = "DISPLAY_LOCATION_Y";
	public static final String PREFS_ANGLE = "DISPLAY_ANGLE";
	public static final String PREFS_POS_DEVELOPER_MODE = "POS_DEVELOPER_MODE";
	public static final String HORIZONTAL_PLACEMENT = "HORIZONTAL_PLACEMENT";
	public static final String GRID_LIMIT_X = "GRID_LIMIT_X";
	public static final String GRID_LIMIT_Y = "GRID_LIMIT_Y";
	public static final String GRID_DISTANCE_X = "GRID_DISTANCE_X";
	public static final String GRID_DISTANCE_Y = "GRID_DISTANCE_Y";
	public static final String REFERENCE_DISTANCE = "REFERENCE_DISTANCE";
	public static final String TABLE_HEIGHT = "TABLE_HEIGHT";

	@Override
	public JPanel getConfigurationPanel() {
		JPanel panel = new JPanel();
		panel.add(new JButton("Position panel"));
		return new PositionConfigPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Position";
	}

	public float getAngle() {
		return prefs.getFloat(PREFS_ANGLE, 0);
	}
	
	public float getTableHeight() {
		return prefs.getFloat(TABLE_HEIGHT, 1f);
	}
	
	public boolean getDeveloperMode() {
		return prefs.get(PREFS_POS_DEVELOPER_MODE, "false").equals("true");
	}

	public boolean getHorizontalPlacement() {
		return prefs.get(HORIZONTAL_PLACEMENT, "false").equals("true");
	}

	public float getXPos() {
		return prefs.getFloat(PREFS_LOCATION_X, 0);
	}

	public float getYPos() {
		return prefs.getFloat(PREFS_LOCATION_Y, 0);
	}

	public float getGridDistanceX() {
		return prefs.getFloat(GRID_DISTANCE_X, 1.5f);
	}

	public float getGridDistanceY() {
		return prefs.getFloat(GRID_DISTANCE_Y, 0);
	}

	public int getGridLimitX() {
		return prefs.getInt(GRID_LIMIT_X, 0);
	}

	public int getGridLimitY() {
		return prefs.getInt(GRID_LIMIT_Y, 0);
	}

	public void setDeveloperMode(boolean selected) {
		prefs.put(PREFS_POS_DEVELOPER_MODE, "" + selected);

	}

	public void setHorizontalPlacement(boolean selected) {
		prefs.put(HORIZONTAL_PLACEMENT, "" + selected);

	}
	
	public void setAngle(float angle) {
		prefs.putFloat(PREFS_ANGLE, angle);
	}
	
	public void setTableHeight(float height) {
		prefs.putFloat(TABLE_HEIGHT, height);
	}

	public void setXPos(float xPos) {
		prefs.putFloat(PREFS_LOCATION_X, xPos);
	}

	public void setYPos(float yPos) {
		prefs.putFloat(PREFS_LOCATION_Y, yPos);
	}

	public void setXDistance(float xDis) {
		prefs.putFloat(GRID_DISTANCE_X, xDis);
	}

	public void setYDistance(float yDis) {
		prefs.putFloat(GRID_DISTANCE_Y, yDis);
	}

	public void setXLimit(int xLimit) {
		prefs.putInt(GRID_LIMIT_X, xLimit);
	}

	public void setYLimit(int yLimit) {
		prefs.putInt(GRID_LIMIT_Y, yLimit);
	}
	
}
