package synergynet3.museum.table.settingsapp.appearance;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import synergynet3.museum.table.settingsapp.SettingsUtil;

public class AppearanceConfigPanel extends JPanel {
	
	private static final long serialVersionUID = -2406804492643797288L;
	
	private static final int X_PADDING = 10;
	private static final int Y_PADDING = 10;
    
    private static final int BIGGER_SPACING_SIZE_X = 3;
    private static final int BIGGER_SPACING_SIZE_Y = 3;
    
    private static final int HEIGHT = 24;
    private static final int LABEL_WIDTH = 175;
    private static final int COMBOBOX_WIDTH = 110;    
    private static final int TEXTBOX_WIDTH = COMBOBOX_WIDTH + X_PADDING + HEIGHT + (X_PADDING * BIGGER_SPACING_SIZE_X) + LABEL_WIDTH + X_PADDING + COMBOBOX_WIDTH;
    
    private int x = X_PADDING;
    private int y = Y_PADDING;

    public AppearanceConfigPanel(AppearanceConfigPrefsItem appearanceConfigPrefsItem){   		
        setLayout(null);      
    }
    
    private void initContent(){
        
    	JPanel scrollPanel = new JPanel();    
		scrollPanel.setLayout(null);     	
		
        //background        
        generateTitle("Background: ", scrollPanel);   
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.BG_COLOUR, scrollPanel);  
        generateSeparator(scrollPanel);  
        
        //entities        
        generateTitle("Entities: ", scrollPanel);   
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.ENTITY_BG_COLOUR, scrollPanel);  
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.ENTITY_BORDER_COLOUR, scrollPanel);
        newLine();   
        generateColourCombo("User Generated Colour: ", AppearanceConfigPrefsItem.ENTITY_USER_GENERATED_COLOUR, scrollPanel);
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.ENTITY_FONT_COLOUR, scrollPanel); 
        newLine();         
        generateColourCombo("Close Button Colour: ", AppearanceConfigPrefsItem.ENTITY_CLOSE_BG_COLOUR, scrollPanel);  
        generateSingleLineTextBox("Spread: ", AppearanceConfigPrefsItem.ENTITY_SPREAD, COMBOBOX_WIDTH, scrollPanel);     
        generateSeparator(scrollPanel); 
        
        //link label
        generateTitle("Link Label: ", scrollPanel);   
        generateColourCombo("Button Colour: ", AppearanceConfigPrefsItem.ENTITY_LINK_BG_COLOUR, scrollPanel); 
        newLine();
        generateSingleLineTextBox("Text: ", AppearanceConfigPrefsItem.ENTITY_LINK_TEXT, TEXTBOX_WIDTH, scrollPanel); 
        generateSeparator(scrollPanel);  
        
        //recording prompt
        generateTitle("Recording Prompt: ", scrollPanel);   
        generateColourCombo("Button Colour: ", AppearanceConfigPrefsItem.ENTITY_RECORDER_BG_COLOUR, scrollPanel); 
        newLine();
        generateMultiLineStringBox("Prompt Texts: ", AppearanceConfigPrefsItem.RECORDING_PROMPTS, scrollPanel);
        newLine();
        generateCheckBox("Show one at random", AppearanceConfigPrefsItem.RECORDING_SINGLE_PROMPT, TEXTBOX_WIDTH, scrollPanel); 
        generateSeparator(scrollPanel);  
                
        //poi       
        generateTitle("Points of Interest: ", scrollPanel);  
        generateColourCombo("Locations Colour: ", AppearanceConfigPrefsItem.POI_COLOUR, scrollPanel);  
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.POI_BORDER_COLOUR, scrollPanel);  
        newLine();        
        generateSingleLineTextBox("Width: ", AppearanceConfigPrefsItem.POI_WIDTH, COMBOBOX_WIDTH, scrollPanel);
        generateSeparator(scrollPanel);  
        
        //recorder
        generateTitle("Recorder: ", scrollPanel);   
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.RECORDER_BG_COLOUR, scrollPanel);
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.RECORDER_FONT_COLOUR, scrollPanel); 
        newLine();
        generateMultiLineStringBox("Instructions Text: ",  AppearanceConfigPrefsItem.RECORDER_TEXT, scrollPanel);  
        newLine();
        generateTitle("Inactive Recorder Buttons: ", scrollPanel);  
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_BORDER_COLOUR, scrollPanel); 
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_FONT_COLOUR, scrollPanel); 
        newLine();
        generateTitle("Active Recorder Buttons: ", scrollPanel);  
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_BORDER_COLOUR, scrollPanel); 
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_FONT_COLOUR, scrollPanel); 
        generateSeparator(scrollPanel);  
        
        //text input
        generateTitle("Text Input: ", scrollPanel);   
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.TEXT_INPUT_BG_COLOUR, scrollPanel);
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.TEXT_INPUT_FONT_COLOUR, scrollPanel); 
        newLine();
        generateColourCombo("Key Background Colour: ", AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BG_COLOUR, scrollPanel); 
        generateColourCombo("Key Border Colour: ", AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BORDER_COLOUR, scrollPanel);         
        newLine();
        generateColourCombo("Input Text Font Colour: ", AppearanceConfigPrefsItem.TEXT_INPUT_SCROLLBOX_FONT_COLOUR, scrollPanel);   
        generateColourCombo("Key Font Colour: ", AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_FONT_COLOUR, scrollPanel);   
        newLine();
        generateMultiLineStringBox("Instructions Text: ",  AppearanceConfigPrefsItem.TEXT_INPUT_TEXT, scrollPanel);      
        generateSeparator(scrollPanel);         
        
        //metrics
        generateTitle("Metric Collection GUI: ", scrollPanel);   
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.METRIC_BG_COLOUR, scrollPanel);
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.METRIC_BORDER_COLOUR, scrollPanel); 
        newLine();
        generateColourCombo("Button Colour: ", AppearanceConfigPrefsItem.METRIC_BUTTON_COLOUR, scrollPanel); 
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.METRIC_FONT_COLOUR, scrollPanel);    
        newLine();
        generateMultiLineStringBox("Instructions Text: ",  AppearanceConfigPrefsItem.METRIC_TEXT, scrollPanel);      
        generateSeparator(scrollPanel);      
        
        //lens
        generateTitle("Lenses: ", scrollPanel);  
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.LENS_BG_COLOUR, scrollPanel); 
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.LENS_BORDER_COLOUR, scrollPanel);   
        newLine();  
        generateColourCombo("Arrow Colour: ", AppearanceConfigPrefsItem.LENS_ARROW_COLOUR, scrollPanel); 
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.LENS_FONT_COLOUR, scrollPanel);     
        newLine();  
        generateColourCombo("Close Colour: ", AppearanceConfigPrefsItem.LENS_CLOSE_COLOUR, scrollPanel); 
        generateSeparator(scrollPanel); 
        
        //slider button
        generateTitle("Create Sliders Button: ", scrollPanel);  
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.LENS_BUTTON_BG_COLOUR, scrollPanel); 
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.LENS_BUTTON_BORDER_COLOUR, scrollPanel);  
        newLine();   
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.LENS_BUTTON_FONT_COLOUR, scrollPanel); 
        newLine();  
        generateSingleLineTextBox("Button Text: ", AppearanceConfigPrefsItem.LENS_BUTTON_TEXT, TEXTBOX_WIDTH, scrollPanel);
        generateSeparator(scrollPanel);       
        
        //close button        
        generateTitle("Close Button: ", scrollPanel);   
        generateColourCombo("Colour: ", AppearanceConfigPrefsItem.CLOSE_BUTTON_COLOUR, scrollPanel);     
        generateSeparator(scrollPanel);  
        
        //shutdown screen
        generateTitle("Shutdown Screen: ", scrollPanel);  
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.SHUTDOWN_BG_COLOUR, scrollPanel);  
        generateColourCombo("Error Colour: ", AppearanceConfigPrefsItem.ERROR_MESSAGE_COLOUR, scrollPanel); 
        newLine();  
        generateSingleLineTextBox("Instruction Text: ", AppearanceConfigPrefsItem.SHUTDOWN_INSTRUCTIONS_TEXT, TEXTBOX_WIDTH, scrollPanel);    
        newLine();  

        //keypad
        generateTitle("Controls: ", scrollPanel);          
        generateColourCombo("Background Colour: ", AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BG_COLOUR, scrollPanel);  
        generateColourCombo("Border Colour: ", AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BORDER_COLOUR, scrollPanel);   
        newLine();  
        generateColourCombo("Font Colour: ", AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_FONT_COLOUR, scrollPanel);   
        newLine();          
                
		scrollPanel.setPreferredSize(new Dimension(800, y));
		JScrollPane scrollPane = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(800, 350));
		scrollPane.setBounds(new Rectangle(0, 0, 790, 350));  
        add(scrollPane);   
    }

	private void generateTitle(String label, JPanel scrollPanel){
		x = X_PADDING;
		
    	JLabel titleLabel = new JLabel(label);
    	titleLabel.setBounds(new Rectangle(x, y, LABEL_WIDTH * 2, HEIGHT));
    	scrollPanel.add(titleLabel);  
    	
        x = X_PADDING * BIGGER_SPACING_SIZE_X;
        y+= HEIGHT + Y_PADDING;  
    }
	
    private void generateColourCombo(String label, final String prefVar, JPanel scrollPanel){
		JLabel colourLabel = new JLabel(label);
		final JComboBox colourComboBox = new JComboBox(SettingsUtil.COLOUR_CHOICE);
		colourComboBox.setSelectedItem(AppearanceConfigPrefsItem.getValue(prefVar));
		colourComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppearanceConfigPrefsItem.setValue(prefVar, (String)colourComboBox.getSelectedItem());
			}
		}); 		
		JButton colourBorderReset = SettingsUtil.generateResetButton();
		colourBorderReset.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				colourComboBox.setSelectedItem(AppearanceConfigPrefsItem.getDefaultValue(prefVar));
				AppearanceConfigPrefsItem.setValue(prefVar, (String)colourComboBox.getSelectedItem());
			}
		});
		
        colourLabel.setBounds(new Rectangle(x, y, LABEL_WIDTH, HEIGHT));
        colourLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        x+= LABEL_WIDTH + X_PADDING;
        colourComboBox.setBounds(new Rectangle(x, y, COMBOBOX_WIDTH, HEIGHT));    
        x+= COMBOBOX_WIDTH + X_PADDING;
        colourBorderReset.setBounds(new Rectangle(x, y, HEIGHT, HEIGHT));        
        x += HEIGHT + (X_PADDING * BIGGER_SPACING_SIZE_X); 
        
        scrollPanel.add(colourLabel);
        scrollPanel.add(colourComboBox);
        scrollPanel.add(colourBorderReset);		
    }
        
    private void generateCheckBox(String label, final String prefVar, int width, JPanel scrollPanel){		
        final JCheckBox checkbox = new JCheckBox();
        checkbox.setText(label);
        if (AppearanceConfigPrefsItem.getValue(prefVar).equals(AppearanceConfigPrefsItem.YES)){
        	checkbox.setSelected(true);
        }else{
        	checkbox.setSelected(false);
        }
        checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if(checkbox.isSelected()){
            		AppearanceConfigPrefsItem.setValue(prefVar, AppearanceConfigPrefsItem.YES);
            	}else{
            		AppearanceConfigPrefsItem.setValue(prefVar, AppearanceConfigPrefsItem.NO);
            	}
            }
        });
 		
        x+= LABEL_WIDTH + X_PADDING;
        
        checkbox.setBounds(new Rectangle(x, y, LABEL_WIDTH, HEIGHT)); 		
        x += HEIGHT + (X_PADDING * BIGGER_SPACING_SIZE_X); 
 		
 		scrollPanel.add(checkbox);
    }
    
    private void generateSingleLineTextBox(String label, final String prefVar, int width, JPanel scrollPanel){
    	JLabel textLabel = new JLabel(label);
 		final JTextField textField = new JTextField();
		textField.setText(AppearanceConfigPrefsItem.getValue(prefVar)); 			

 		textField.addKeyListener(new KeyAdapter() {
 			@Override
 			public void keyReleased(KeyEvent e) {
 				store();
 			}			

 			@Override
 			public void keyTyped(KeyEvent e) {
 				store();
 			}			
 			
 			private void store() {
 	 			AppearanceConfigPrefsItem.setValue(prefVar, textField.getText());			
 			}
 		});   		
 		JButton textReset = SettingsUtil.generateResetButton();
 		textReset.addActionListener(new ActionListener() {			
 			@Override
 			public void actionPerformed(ActionEvent arg0) {
 				textField.setText(AppearanceConfigPrefsItem.getDefaultValue(prefVar));
 	 			AppearanceConfigPrefsItem.setValue(prefVar, textField.getText());	
 			}
 		});
 		
 		textLabel.setBounds(new Rectangle(x, y, LABEL_WIDTH, HEIGHT));
 		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
 		x+= LABEL_WIDTH + X_PADDING;
 		textField.setBounds(new Rectangle(x, y, width, HEIGHT)); 
 		x+= width + X_PADDING;
 		textReset.setBounds(new Rectangle(x, y, HEIGHT-2, HEIGHT-2));        
        x += HEIGHT + (X_PADDING * BIGGER_SPACING_SIZE_X); 
 		
 		scrollPanel.add(textLabel);
 		scrollPanel.add(textField);
 		scrollPanel.add(textReset);  
    }
    
    private void generateMultiLineStringBox(String label, final String prefVar, JPanel scrollPanel){
    	JLabel textLabel = new JLabel(label);
    	final JTextArea textField = new JTextArea();
 		textField.setText(AppearanceConfigPrefsItem.getValue(prefVar));
 		textField.setLineWrap(true);
 		textField.addKeyListener(new KeyAdapter() {
 			@Override
 			public void keyReleased(KeyEvent e) {
 				store();
 			}			

 			@Override
 			public void keyTyped(KeyEvent e) {
 				store();
 			}			
 			
 			private void store() {
 				AppearanceConfigPrefsItem.setValue(prefVar, textField.getText());
 			}
 		});   	
 		JScrollPane scrollMailBody = new JScrollPane(textField);
 		
 		JButton textReset = SettingsUtil.generateResetButton();
 		textReset.addActionListener(new ActionListener() {			
 			@Override
 			public void actionPerformed(ActionEvent arg0) {
 		 		textField.setText(AppearanceConfigPrefsItem.getDefaultValue(prefVar));
 				AppearanceConfigPrefsItem.setValue(prefVar, textField.getText());
 			}
 		});
 		
 		textLabel.setBounds(new Rectangle(x, y, LABEL_WIDTH, HEIGHT));
 		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
 		x+= LABEL_WIDTH + X_PADDING;
 		scrollMailBody.setBounds(new Rectangle(x, y, TEXTBOX_WIDTH, HEIGHT*2)); 
 		x+= TEXTBOX_WIDTH + X_PADDING;
 		textReset.setBounds(new Rectangle(x, y, HEIGHT-2, HEIGHT-2)); 
 		y+= HEIGHT;
 		
 		scrollPanel.add(textLabel);
 		scrollPanel.add(scrollMailBody);
 		scrollPanel.add(textReset);  
    }
	
    private void newLine() {
   	 	y+= HEIGHT + Y_PADDING;  
        x = X_PADDING * BIGGER_SPACING_SIZE_X;		
	}
    
	private void generateSeparator(JPanel scrollPanel){
        y+= HEIGHT + (Y_PADDING * BIGGER_SPACING_SIZE_Y/2);		
        JSeparator entitySeparator = new JSeparator();
        entitySeparator.setBounds(new Rectangle(0, y, 800, HEIGHT));        
        y+= Y_PADDING * BIGGER_SPACING_SIZE_Y/2;      
        scrollPanel.add(entitySeparator);
	}
	
	public void updateValues(){
		removeAll();
	    x = X_PADDING;
	    y = Y_PADDING;
		initContent();
	}

}