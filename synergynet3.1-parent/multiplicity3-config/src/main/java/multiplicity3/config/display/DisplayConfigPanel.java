/*
 * DisplayPreferences2.java
 *
 * Created on March 31, 2010, 9:54 PM
 */

package multiplicity3.config.display;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;

import multiplicity3.config.display.DisplayPrefsItem;
import multiplicity3.config.PreferencesItem;

/**
 *
 * @author  dcs0ah1
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DisplayConfigPanel extends JPanel implements PreferencesItem {
	
	private static final long serialVersionUID = 8757133417077939163L;
	private DisplayPrefsItem prefs;
	
    private JTextField alphaBitsField = new JTextField();
    private JTextField antiAliasField = new JTextField();
    private JTextField depthBitsField = new JTextField();
    private JTextField displayWidthField = new JTextField();
    private JTextField stencilBitsField = new JTextField();
    private JTextField tuioTextbox = new JTextField();
    private JComboBox displaySelector = new JComboBox();
    private JCheckBox fullScreen = new JCheckBox();
    private JComboBox jcb = new JComboBox();
    private JLabel displaySizeLabel = new JLabel();
    private JLabel antiAliasLabel = new JLabel();
    private JLabel StencilBitsLabel = new JLabel();
    private JLabel alphaBitsLabel = new JLabel();
    private JLabel depthBitsLabel = new JLabel();
    private JLabel displayWidthLabel = new JLabel();
	private JLabel inputType = new JLabel();
	private JLabel tuioLabel = new JLabel();

	public DisplayConfigPanel(DisplayPrefsItem prefs) {
		this.prefs = prefs;
		initComponents();
		loadCurrentSettings();
	}

	private void initComponents() {

        displaySizeLabel.setText("Display Size:");
        displaySizeLabel.setName("jLabel1"); 

        displaySelector.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        displaySelector.setName("displaySelector"); 
        displaySelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                displaySelectorItemStateChanged(evt);
            }
        });

        fullScreen.setText("Full Screen");
        fullScreen.setHorizontalTextPosition(SwingConstants.LEADING);
        fullScreen.setName("fullScreen"); 
        fullScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	prefs.setFullScreen(fullScreen.isSelected());
            }
        });

        antiAliasLabel.setText("Anti-alias min samples:");
        antiAliasLabel.setName("jLabel2"); 

        antiAliasField.setText("jTextField1");
        antiAliasField.setName("antiAlias"); 
        antiAliasField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	prefs.setMinimumAntiAliasSamples(getIntegerFromTextField(antiAliasField, prefs.getMinimumAntiAliasSamples()));
            }
        });

        StencilBitsLabel.setText("Stencil bits:");
        StencilBitsLabel.setName("jLabel3"); 

        stencilBitsField.setText("jTextField1");
        stencilBitsField.setName("stencilBits"); 
        stencilBitsField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	prefs.setStencilBits(getIntegerFromTextField(stencilBitsField, prefs.getStencilBits()));
            }
        });

        alphaBitsLabel.setText("Alpha bits:");
        alphaBitsLabel.setName("jLabel4"); 

        alphaBitsField.setText("jTextField1");
        alphaBitsField.setName("alphaBits"); 
        alphaBitsField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	prefs.setAlphaBits(getIntegerFromTextField(alphaBitsField, prefs.getAlphaBits()));
            }
        });

        depthBitsLabel.setText("Depth bits:");
        depthBitsLabel.setName("jLabel5"); 

        depthBitsField.setText("jTextField1");
        depthBitsField.setName("depthBits"); 
        depthBitsField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	prefs.setDepthBits(getIntegerFromTextField(depthBitsField, prefs.getDepthBits()));
            }
        });
        
        displayWidthLabel.setText("Display Width (m):");
        displayWidthLabel.setName("jLabel6"); 

        displayWidthField.setText("jTextField1");
        displayWidthField.setName("displayWidth"); 
        displayWidthField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	prefs.setRealWidth(getFloatFromTextField(displayWidthField, prefs.getRealWidth()));
            }
        });
        
        
            
        inputType.setText("Input Type:");

        initInputSelector();
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.setInputType((String)jcb.getSelectedItem());
				updateTuioOptionsVisibility();
			}			
		});
		
		tuioLabel.setText("TUIO Port: ");
		
		tuioTextbox.setText(""+prefs.getTuioPort());
		tuioTextbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	prefs.setTuioPort(getIntegerFromTextField(tuioTextbox, prefs.getTuioPort()));
            }
        });

		
	    setLayout(null);
	    
		inputType.setBounds(new Rectangle(30, 30, 100, 24));
		jcb.setBounds(new Rectangle(130, 30, 200, 24));
		
		tuioLabel.setBounds(new Rectangle(30, 60, 250, 24));
		tuioTextbox.setBounds(new Rectangle(130, 60, 150, 24));
	    
	    displaySizeLabel.setBounds(new Rectangle(30, 90, 100, 24));
	    displaySelector.setBounds(new Rectangle(130, 90, 200, 24));	    
	    fullScreen.setBounds(new Rectangle(350, 90, 125, 24));
	    
	    displayWidthLabel.setBounds(new Rectangle(30, 165, 175, 24));
	    displayWidthField.setBounds(new Rectangle(200, 165, 60, 24));
	    
	    antiAliasLabel.setBounds(new Rectangle(30, 195, 175, 24));
	    antiAliasField.setBounds(new Rectangle(200, 195, 35, 24));
	    
	    StencilBitsLabel.setBounds(new Rectangle(275, 195, 150, 24));
	    stencilBitsField.setBounds(new Rectangle(360, 195, 35, 24));
	    
	    alphaBitsLabel.setBounds(new Rectangle(30, 225, 150, 24));
	    alphaBitsField.setBounds(new Rectangle(200, 225, 35, 24));
	    
	    depthBitsLabel.setBounds(new Rectangle(275, 225, 150, 24));
	    depthBitsField.setBounds(new Rectangle(360, 225, 35, 24));
	    
	    updateTuioOptionsVisibility();
	    
	    add(inputType);
	    add(jcb);
		add(tuioLabel);
		add(tuioTextbox);
	    add(displaySizeLabel);
	    add(displaySelector);	
	    add(fullScreen);
	    add(displayWidthLabel);
	    add(displayWidthField);
	    add(antiAliasLabel);
		add(antiAliasField);
	    add(StencilBitsLabel);
		add(stencilBitsField);
	    add(alphaBitsLabel);
		add(alphaBitsField);
	    add(depthBitsLabel);
	    add(depthBitsField);

    }    
    
    private void updateTuioOptionsVisibility(){
		tuioLabel.setVisible(prefs.getInputType().equals(DisplayPrefsItem.INPUT_TYPES[1]));
		tuioTextbox.setVisible(prefs.getInputType().equals(DisplayPrefsItem.INPUT_TYPES[1]));
    }

	private void displaySelectorItemStateChanged(java.awt.event.ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED && displaySelector != null) {
			DisplayMode m = (DisplayMode) displaySelector.getSelectedItem();
			setSelectedDisplayMode(m);
		}
	}
	
	private int getIntegerFromTextField(JTextField tf, int previousValue) {
		if(tf.getText().length() > 0) {
			try {
				int num = Integer.parseInt(tf.getText());
				tf.setForeground(Color.black);
				return num;
			}catch(NumberFormatException ex) {
				tf.setForeground(Color.red);
			}    
		}
		return previousValue;
	}
	
	private Float getFloatFromTextField(JTextField tf, float previousValue) {
		if(tf.getText().length() > 0) {
			try {
				float num = Float.parseFloat(tf.getText());
				tf.setForeground(Color.black);
				return num;
			}catch(NumberFormatException ex) {
				tf.setForeground(Color.red);
			}    
		}
		return previousValue;
	}


	private DisplayMode[] getDisplayModes() {
		DisplayMode[] modes = null;
		try {
			AppSettings settings = new AppSettings(true);
			JmeSystem.initialize(settings);
			modes = Display.getAvailableDisplayModes();
			Arrays.sort(modes, new DisplayModeComparator());
			Display.destroy();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return modes;
	}

	private int getCurrentDisplayModeIndex(DisplayMode[] modes) {
		for(int i = 0; i < modes.length; i++) {
			DisplayMode m = modes[i];

			if(m.getHeight() == prefs.getHeight() &&
					m.getWidth() == prefs.getWidth() &&
					m.getBitsPerPixel() == prefs.getBitsPerPixel() &&
					m.getFrequency() == prefs.getFrequency())
			{
				return i;
			}
		}

		return -1;
	}

	public DisplayMode getCurrentDisplayMode(DisplayMode[] modes) {
		for(DisplayMode m : modes) {
			if(m.getHeight() == prefs.getHeight() &&
					m.getWidth() == prefs.getWidth() &&
					m.getBitsPerPixel() == prefs.getBitsPerPixel() &&
					m.getFrequency() == prefs.getFrequency())
			{
				return m;
			}
		}

		return null;
	}

	private void setSelectedDisplayMode(DisplayMode m) {
		prefs.setWidth(m.getWidth());
		prefs.setHeight(m.getHeight());
		prefs.setBitsPerPixel(m.getBitsPerPixel());
		prefs.setFrequency(m.getFrequency());
	}

	private void loadCurrentSettings() {		
		initDisplaySelector();
		initFullScreen();
		antiAliasField.setText("" + prefs.getMinimumAntiAliasSamples());
		alphaBitsField.setText("" + prefs.getAlphaBits());
		stencilBitsField.setText("" + prefs.getStencilBits());
		depthBitsField.setText("" + prefs.getDepthBits());
		displayWidthField.setText("" + prefs.getRealWidth());
	}

	private void initFullScreen() {
		fullScreen.setSelected(prefs.getFullScreen());
	}

	private void initDisplaySelector() {
		// get the saved display mode as loading the list causes a change
		DisplayMode currentMode = getCurrentDisplayMode(getDisplayModes());

		displaySelector.removeAllItems();
		for (DisplayMode dm : getDisplayModes()) {
			displaySelector.addItem(dm);
		}

		if(currentMode == null) {
			// we didn't already have a display, pick the first
			currentMode = getDisplayModes()[0];
		}

		// restore saved display mode
		setSelectedDisplayMode(currentMode);
		displaySelector.setSelectedIndex(getCurrentDisplayModeIndex(getDisplayModes()));		
	}
	
	private void initInputSelector() {
		jcb.removeAllItems();
		for (String input : DisplayPrefsItem.INPUT_TYPES) {
			jcb.addItem(input);
		}
		jcb.setSelectedItem(prefs.getInputType());
	}
	
	@Override
	public JPanel getConfigurationPanel() {
		return this;
	}

	@Override
	public String getConfigurationPanelName() {
		return "Display";
	}

}
