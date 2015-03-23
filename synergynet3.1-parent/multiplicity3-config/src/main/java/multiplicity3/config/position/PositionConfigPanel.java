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

package multiplicity3.config.position;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The Class PositionConfigPanel.
 */
public class PositionConfigPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1959347964564852506L;

	/** The cb enable developer mode. */
	private JCheckBox cbEnableDeveloperMode = new JCheckBox();

	/** The cb enable normal position mode. */
	private JCheckBox cbEnableNormalPositionMode = new JCheckBox();

	/** The horizontal. */
	private JCheckBox horizontal = new JCheckBox();

	/** The j label placement. */
	private JLabel jLabelPlacement = new JLabel();

	/** The j label table distances. */
	private JLabel jLabelTableDistances = new JLabel();

	/** The j label table height. */
	private JLabel jLabelTableHeight = new JLabel();

	/** The j label table limits. */
	private JLabel jLabelTableLimits = new JLabel();

	/** The j label table orientation. */
	private JLabel jLabelTableOrientation = new JLabel();

	/** The j label table position. */
	private JLabel jLabelTablePosition = new JLabel();

	/** The j label x. */
	private JLabel jLabelX = new JLabel();

	/** The j label x distance. */
	private JLabel jLabelXDistance = new JLabel();

	/** The j label x limit. */
	private JLabel jLabelXLimit = new JLabel();

	/** The j label y. */
	private JLabel jLabelY = new JLabel();

	/** The j label y distance. */
	private JLabel jLabelYDistance = new JLabel();

	/** The j label y limit. */
	private JLabel jLabelYLimit = new JLabel();

	/** The j text field distance x. */
	private JTextField jTextFieldDistanceX = new JTextField();

	/** The j text field distance y. */
	private JTextField jTextFieldDistanceY = new JTextField();

	/** The j text field limit x. */
	private JTextField jTextFieldLimitX = new JTextField();

	/** The j text field limit y. */
	private JTextField jTextFieldLimitY = new JTextField();

	/** The j text field position x. */
	private JTextField jTextFieldPositionX = new JTextField();

	/** The j text field position y. */
	private JTextField jTextFieldPositionY = new JTextField();

	/** The j text field table angle. */
	private JTextField jTextFieldTableAngle = new JTextField();

	/** The j text field table height. */
	private JTextField jTextFieldTableHeight = new JTextField();

	/** The prefs item. */
	private PositionConfigPrefsItem prefsItem;

	/** The vertical. */
	private JCheckBox vertical = new JCheckBox();

	/**
	 * Instantiates a new position config panel.
	 *
	 * @param positionConfigPrefsItem the position config prefs item
	 */
	public PositionConfigPanel(PositionConfigPrefsItem positionConfigPrefsItem) {
		this.prefsItem = positionConfigPrefsItem;
		initComponents();
	}

	/**
	 * Enable developer mode.
	 *
	 * @param evt the evt
	 */
	private void enableDeveloperMode(ActionEvent evt) {
		prefsItem.setDeveloperMode(cbEnableDeveloperMode.isSelected());
	}

	/**
	 * Enable horizontal placement.
	 *
	 * @param evt the evt
	 */
	private void enableHorizontalPlacement(ActionEvent evt) {
		prefsItem.setHorizontalPlacement(horizontal.isSelected());
	}

	/**
	 * Gets the float from text field.
	 *
	 * @param tf the tf
	 * @param previousValue the previous value
	 * @return the float from text field
	 */
	private Float getFloatFromTextField(JTextField tf, float previousValue) {
		if (tf.getText().length() > 0) {
			try {
				float num = Float.parseFloat(tf.getText());
				tf.setForeground(Color.black);
				return num;
			} catch (NumberFormatException ex) {
				tf.setForeground(Color.red);
			}
		}
		return previousValue;
	}

	/**
	 * Gets the integer from text field.
	 *
	 * @param tf the tf
	 * @param previousValue the previous value
	 * @return the integer from text field
	 */
	private int getIntegerFromTextField(JTextField tf, int previousValue) {
		if (tf.getText().length() > 0) {
			try {
				int num = Integer.parseInt(tf.getText());
				tf.setForeground(Color.black);
				return num;
			} catch (NumberFormatException ex) {
				tf.setForeground(Color.red);
			}
		}
		return previousValue;
	}

	/**
	 * Inits the components.
	 */
	private void initComponents() {

		setLayout(null);

		jLabelTableOrientation.setText("Orientation (degrees) = ");

		jTextFieldTableAngle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setAngle(getFloatFromTextField(jTextFieldTableAngle,
						prefsItem.getAngle()));
			}
		});

		jLabelTableHeight.setText("Table Height (m) =");

		jTextFieldTableHeight.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setTableHeight(getFloatFromTextField(
						jTextFieldTableHeight, prefsItem.getTableHeight()));
			}
		});

		cbEnableNormalPositionMode
				.setText("Enable User Defined Table Positioning");
		cbEnableNormalPositionMode.setSelected(!prefsItem.getDeveloperMode());
		cbEnableNormalPositionMode
				.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		cbEnableNormalPositionMode
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						if (cbEnableNormalPositionMode.isSelected()) {
							cbEnableDeveloperMode
									.setSelected(!cbEnableNormalPositionMode
											.isSelected());
							enableDeveloperMode(evt);
						} else {
							cbEnableNormalPositionMode.setSelected(true);
						}
					}
				});

		jLabelTablePosition.setText("Position (m):");

		jLabelX.setText(" X = ");

		jTextFieldPositionX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setXPos(getFloatFromTextField(jTextFieldPositionX,
						prefsItem.getXPos()));
			}
		});

		jLabelY.setText(" Y = ");

		jTextFieldPositionY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setYPos(getFloatFromTextField(jTextFieldPositionY,
						prefsItem.getYPos()));
			}
		});

		cbEnableDeveloperMode.setText("Enable Developer Mode");
		cbEnableDeveloperMode.setSelected(prefsItem.getDeveloperMode());
		cbEnableDeveloperMode
				.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		cbEnableDeveloperMode
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						if (cbEnableDeveloperMode.isSelected()) {
							cbEnableNormalPositionMode
									.setSelected(!cbEnableNormalPositionMode
											.isSelected());
							enableDeveloperMode(evt);
						} else {
							cbEnableDeveloperMode.setSelected(true);
						}
					}
				});

		jLabelTableDistances.setText("Distances between displays (m):");

		jLabelXDistance.setText(" X = ");

		jTextFieldDistanceX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setXDistance(getFloatFromTextField(
						jTextFieldDistanceX, prefsItem.getGridDistanceX()));
			}
		});

		jLabelYDistance.setText(" Y = ");

		jTextFieldDistanceY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setYDistance(getFloatFromTextField(
						jTextFieldDistanceY, prefsItem.getGridDistanceY()));
			}
		});

		jLabelTableLimits.setText("Grid Limit (0 = no limit):");

		jLabelXLimit.setText(" X = ");

		jTextFieldLimitX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setXLimit(getIntegerFromTextField(jTextFieldLimitX,
						prefsItem.getGridLimitX()));
			}
		});

		jLabelYLimit.setText(" Y = ");

		jTextFieldLimitY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefsItem.setYLimit(getIntegerFromTextField(jTextFieldLimitY,
						prefsItem.getGridLimitY()));
			}
		});

		jLabelPlacement.setText("Placement:");

		horizontal.setText("Horizontal");
		horizontal.setSelected(prefsItem.getHorizontalPlacement());
		horizontal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (horizontal.isSelected()) {
					vertical.setSelected(!horizontal.isSelected());
					enableHorizontalPlacement(evt);
				} else {
					horizontal.setSelected(true);
				}
			}
		});

		vertical.setText("Vertical");
		vertical.setSelected(!prefsItem.getHorizontalPlacement());

		vertical.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (horizontal.isSelected()) {
					horizontal.setSelected(!vertical.isSelected());
					enableHorizontalPlacement(evt);
				} else {
					vertical.setSelected(false);
				}
			}
		});

		jLabelTableOrientation.setBounds(new Rectangle(30, 30, 175, 24));
		jTextFieldTableAngle.setBounds(new Rectangle(205, 30, 50, 24));
		jLabelTableHeight.setBounds(new Rectangle(290, 30, 150, 24));
		jTextFieldTableHeight.setBounds(new Rectangle(440, 30, 50, 24));

		cbEnableNormalPositionMode.setBounds(new Rectangle(30, 90, 300, 24));
		jLabelTablePosition.setBounds(new Rectangle(70, 120, 133, 24));
		jLabelX.setBounds(new Rectangle(290, 120, 390, 24));
		jTextFieldPositionX.setBounds(new Rectangle(325, 120, 57, 24));
		jLabelY.setBounds(new Rectangle(440, 120, 39, 24));
		jTextFieldPositionY.setBounds(new Rectangle(475, 120, 57, 24));

		cbEnableDeveloperMode.setBounds(new Rectangle(30, 170, 300, 24));
		jLabelTableDistances.setBounds(new Rectangle(70, 200, 210, 24));
		jLabelXDistance.setBounds(new Rectangle(290, 200, 39, 24));
		jTextFieldDistanceX.setBounds(new Rectangle(325, 200, 57, 24));
		jLabelYDistance.setBounds(new Rectangle(440, 200, 39, 24));
		jTextFieldDistanceY.setBounds(new Rectangle(475, 200, 57, 24));
		jLabelTableLimits.setBounds(new Rectangle(70, 230, 200, 24));
		jLabelXLimit.setBounds(new Rectangle(290, 230, 39, 24));
		jTextFieldLimitX.setBounds(new Rectangle(325, 230, 57, 24));
		jLabelYLimit.setBounds(new Rectangle(440, 230, 39, 24));
		jTextFieldLimitY.setBounds(new Rectangle(475, 230, 57, 24));
		jLabelPlacement.setBounds(new Rectangle(70, 260, 210, 24));
		horizontal.setBounds(new Rectangle(290, 260, 150, 24));
		vertical.setBounds(new Rectangle(440, 260, 155, 24));

		add(jLabelTableOrientation, null);
		add(jTextFieldTableAngle, null);
		add(jLabelTableHeight, null);
		add(jTextFieldTableHeight, null);
		add(cbEnableNormalPositionMode, null);
		add(jLabelTablePosition, null);
		add(jLabelX, null);
		add(jTextFieldPositionX, null);
		add(jLabelY, null);
		add(jTextFieldPositionY, null);
		add(cbEnableDeveloperMode, null);
		add(jLabelTableDistances, null);
		add(jLabelXDistance, null);
		add(jTextFieldDistanceX, null);
		add(jLabelYDistance, null);
		add(jTextFieldDistanceY, null);
		add(jLabelTableLimits, null);
		add(jLabelXLimit, null);
		add(jTextFieldLimitX, null);
		add(jLabelYLimit, null);
		add(jTextFieldLimitY, null);
		add(jLabelPlacement, null);
		add(horizontal, null);
		add(vertical, null);
		loadPreferences();
	}

	/**
	 * Load preferences.
	 */
	private void loadPreferences() {
		jTextFieldTableAngle.setText("" + prefsItem.getAngle());
		jTextFieldTableHeight.setText("" + prefsItem.getTableHeight());
		jTextFieldPositionX.setText("" + prefsItem.getXPos());
		jTextFieldPositionY.setText("" + prefsItem.getYPos());
		jTextFieldDistanceX.setText("" + prefsItem.getGridDistanceX());
		jTextFieldDistanceY.setText("" + prefsItem.getGridDistanceY());
		jTextFieldLimitX.setText("" + prefsItem.getGridLimitX());
		jTextFieldLimitY.setText("" + prefsItem.getGridLimitY());
	}
}
