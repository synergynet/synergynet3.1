package synergynet3.museum.table.settingsapp.entitymanager.creationguis;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import multiplicity3.input.events.MultiTouchCursorEvent;

import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.museum.table.mainapp.EntityManager;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.settingsapp.entitymanager.EntityManagerGUI;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.attributecreationguis.EntityAttribute;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.attributecreationguis.FactCreationGUI;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.attributecreationguis.LinkCreationGUI;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.mappositiongui.LocationMapper;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.mappositiongui.ModifiableXandYFields;
import synergynet3.museum.table.utils.Entity;
import synergynet3.museum.table.utils.EntityType;

public class EntityCreatorGUI implements ModifiableXandYFields{
		
	protected JFrame jf;
	public Entity entity;	
	
	protected boolean newEntity = false;
	protected EntityManagerGUI mainGUI;
	private String name = "";
	private String location = "";
	
	private ArrayList<JComponent> poiControls = new ArrayList<JComponent>();        
	private ArrayList<JComponent> lensControls = new ArrayList<JComponent>();
	
    private JLabel textFieldX;
    private JLabel textFieldY;    
    private JCheckBox poiCheckbox;
    private JCheckBox lensCheckbox;
	
	private EntityCreatorGUI instance;
	
	public ArrayList<JFrame> relatedFrames = new ArrayList<JFrame>();
	
	private HashMap<EntityAttribute, JList> listItems = new HashMap<EntityAttribute, JList>();
	private JList lensList;
	
	public EntityCreatorGUI(Entity entity, EntityManagerGUI mainGUI){
		this.entity = entity;	
		this.mainGUI = mainGUI;
		instance = this;
		generateGenericContent();
	}
	
	protected void generateGenericContent(){		
		if (entity == null){
			generateNameRequestBox();
		}else{
			name = entity.getName();
			generateGenericMainScreenContent();
		}
	}
	
	private void generateNameRequestBox() {
	
		int w = 350;
		
		int xPadding = 10;
		int yPadding = 10;

		int height = 24;
		int labelWidth = 50;
		int textboxWidth = 250;
		int smallButtonWidth = w/3 - (xPadding/2);
		
		int x = 0;
		int y = yPadding;

		int h = (yPadding * 4) + (height * 3);
		
		final JFrame jf = new JFrame("Create Entity");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h); 
		jf.setResizable(false);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width-w)/2;
		int screenY = (dim.height-h)/2;
		jf.setLocation(screenX, screenY);		
		
		jf.getContentPane().setLayout(null);
		
		JLabel textLabel = new JLabel("Name:");
 		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    	final JTextField textField = new JTextField();	
 		 		
 		textLabel.setBounds(new Rectangle(x, y, labelWidth, height));
 		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
 		x+= labelWidth + xPadding;
 		textField.setBounds(new Rectangle(x, y, textboxWidth, height)); 
 		
 		jf.getContentPane().add(textLabel);
 		jf.getContentPane().add(textField);
 		
 		x = w/2 - (xPadding + (smallButtonWidth*2))/2;
 		y += height + yPadding;
 		
		JButton okButton = new JButton();        
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
        		name = textField.getText();
        		name = SettingsUtil.removeSpecialChars(name);
            	if (name.equals("")){	
            		JOptionPane.showMessageDialog(jf, "No name is given.");
            	}else{
            		if (EntityManagerGUI.entities.keySet().contains(name)){
            			JOptionPane.showMessageDialog(jf, "This name is already in use.");
            		}else{
                		generateGenericMainScreenContent();            		
                    	jf.setVisible(false);
            		}      
            	}            	
            }
        });   
        okButton.setBounds(new Rectangle(x, y, smallButtonWidth, height));
        jf.getContentPane().add(okButton);
        
		x += smallButtonWidth + xPadding;
        
		JButton cancelButton = new JButton();        
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	jf.setVisible(false);
            }
        });   
        cancelButton.setBounds(new Rectangle(x, y, smallButtonWidth - xPadding, height));
        jf.getContentPane().add(cancelButton);
        
		jf.setVisible(true);		
	}

	private void generateGenericMainScreenContent(){
		
		if (entity == null){
			newEntity = true;
			location = MuseumAppPreferences.getContentFolder() + File.separator + EntityManager.ENTITIES + File.separator + name;
			new File(location).mkdir();
			entity = new Entity(location);			
		}else{
			location = entity.getLocation();
			
		}	
		
		jf = new JFrame(name);
		jf.getContentPane().setLayout(new BorderLayout());	
		
		jf.getContentPane().setLayout(null);		 		 		
        
	    JSeparator entitySeparatorOne = new JSeparator(); 
	    
		JButton viewContentsButton = new JButton();        
        viewContentsButton.setText("View Contents");
        
		final IgnoreDoubleClick contentClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
            	Desktop desktop = null;
            	File file = new File(location);
            	if (Desktop.isDesktopSupported()){
            		desktop = Desktop.getDesktop();
            	}
            	try {
            		desktop.open(file);
            	}catch (IOException e){}
			}
		};
        
        viewContentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	contentClicker.click(null);
            }
        });   
        
		JButton helpContent = SettingsUtil.generateHelpButton(jf, "Any visible media in this folder (.jpg, .png, .gif, .wav, .wma, .mp4, .mov, .avi) will be attatched to the entity.");
		
        poiCheckbox = new JCheckBox();
        poiCheckbox.setText("Show on Map");     
        boolean locationMode = ((entity.getType() == EntityType.LensedPOI) || (entity.getType() == EntityType.POI)) ;
        poiCheckbox.setSelected(locationMode);       
        
        lensCheckbox = new JCheckBox();
        lensCheckbox.setText("Visible only through the following lenses:");
        boolean eventMode = (entity.getType() == EntityType.LensedPOI);
        lensCheckbox.setSelected(eventMode);     
        
        poiCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	updateSelectedContents();
            }
        });
        
        lensCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	updateSelectedContents();
            }
        });
		
		JLabel textLabelX = new JLabel("X: ");
		poiControls.add(textLabelX);
 		textFieldX = new JLabel();
		textFieldX.setText("" + entity.getX());
		poiControls.add(textFieldX);		
		textLabelX.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel textLabelY = new JLabel("Y: ");
		poiControls.add(textLabelY);
 		textFieldY = new JLabel();
		textFieldY.setText("" + entity.getY()); 	
		poiControls.add(textFieldY);
 		textLabelY.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton locationMapperButton = new JButton();   
		poiControls.add(locationMapperButton);     
        locationMapperButton.setText("Locate");
        locationMapperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	float xIn = 0.5f;
            	float yIn = 0.5f;
            	try{
            		xIn = Float.parseFloat(textFieldX.getText());
            		yIn = Float.parseFloat(textFieldY.getText());
            	}catch (NumberFormatException e){}            	
            	new LocationMapper(xIn, yIn, instance, instance);
            }
        });	
        
		JButton helpMap = SettingsUtil.generateHelpButton(jf, "Click on the map to position the entity.");
		        
	    JSeparator entitySeparatorTwo = new JSeparator(); 
		
		JButton okButton = new JButton();        
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	
            	if (lensCheckbox.isSelected()){
            		entity.setType(EntityType.LensedPOI);
            		
           			float x = 0.5f;
            		float y = 0.5f;
            		
            		try{
            			x = Float.parseFloat(textFieldX.getText());
            		}catch(NumberFormatException e){}
            		
            		try{
            			y = Float.parseFloat(textFieldY.getText());
            		}catch(NumberFormatException e){}
            		
            		entity.setX(x);
            		entity.setY(y);
            		
            	}else if(poiCheckbox.isSelected()){
            		entity.setType(EntityType.POI);
            		
           			float x = 0.5f;
            		float y = 0.5f;
            		
            		try{
            			x = Float.parseFloat(textFieldX.getText());
            		}catch(NumberFormatException e){}
            		
            		try{
            			y = Float.parseFloat(textFieldY.getText());
            		}catch(NumberFormatException e){}
            		
            		entity.setX(x);
            		entity.setY(y);
            	}else{
            		entity.setType(EntityType.Free);
            	}
            	
        		entity.setName(name);
        		entity.saveXML();
        		EntityManagerGUI.entities.put(name, entity);    		
            	mainGUI.updateList();
            	jf.setVisible(false);
            	onClose();	            	
            }
        });   
        
		JButton cancelButton = new JButton();        
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if (newEntity){
            		File file = new File(location);
            		if (!file.delete()){
            			file.deleteOnExit();
            		}
            	}
            	jf.setVisible(false);
            	onClose();
            }
        });   
        
        
		int w = 1024;
		
		int xPadding = 10;
		int yPadding = 10;

		int height = 24;
		int panelWidth = w/EntityAttribute.values().length;
		int panelHeight = 250;
		int bigButtonWidth = w/2;
		int smallButtonWidth = bigButtonWidth/2 - (xPadding/2);
		int smallLabelWidth = 100;
		int textFieldWidth = 80;
	    int buttonWidth = 120;
	    int checkButtonWidth = 275;
	    
		int x = 0;
		int y = yPadding;
		
		for (EntityAttribute attribute : EntityAttribute.values()){
			JPanel panel = generateListBoxAndControls(attribute, panelWidth, panelHeight);
			panel.setBounds(new Rectangle(x, y, panelWidth, panelHeight));
			x += panelWidth;
			jf.getContentPane().add(panel);
		}
		
		x = xPadding;
		y += panelHeight + yPadding;	
		
	    entitySeparatorOne.setBounds(new Rectangle(0, y, w - (yPadding/2), height)); 
	    
		x = w/2 - bigButtonWidth/2;
	    y+= yPadding;  
        
        viewContentsButton.setBounds(new Rectangle(x, y, bigButtonWidth, height));
        
        x += bigButtonWidth + xPadding;
        
		helpContent.setBounds(new Rectangle(x, y, height, height));
		
		x = xPadding;
		y+= height + yPadding;
        
        poiCheckbox.setBounds(new Rectangle(x, y, checkButtonWidth, height)); 
        
        y+= yPadding + height;       
        x = xPadding;
        
		textLabelX.setBounds(new Rectangle(x, y, smallLabelWidth, height));
 		x+= smallLabelWidth + xPadding;
 		textFieldX.setBounds(new Rectangle(x, y, textFieldWidth, height)); 
 		x+= textFieldWidth + (xPadding*2);
		
		textLabelY.setBounds(new Rectangle(x, y, smallLabelWidth, height));
 		x+= smallLabelWidth + xPadding;
 		textFieldY.setBounds(new Rectangle(x, y, textFieldWidth, height));
 		x+= textFieldWidth + (xPadding*2);
        
 		locationMapperButton.setBounds(new Rectangle(x, y, buttonWidth, height));
 		
 		x+= buttonWidth + yPadding;
 		
 		helpMap.setBounds(new Rectangle(x, y, height, height));
 		
 		y += height + yPadding;
 		x = xPadding * 2;
 		
 		lensCheckbox.setBounds(new Rectangle(x, y, checkButtonWidth, height));
        poiControls.add(lensCheckbox); 
        x += checkButtonWidth + xPadding;
		JPanel lensPanel = generateLensBoxAndControls( w/4, height * 4);
		lensPanel.setBounds(x, y, w/4, height * 4);
               
		x = w/2 - (smallButtonWidth*2 + xPadding)/2;
		y+= (height * 4) + yPadding; 
		
	    entitySeparatorTwo.setBounds(new Rectangle(0, y, w - (yPadding/2), height)); 
	    
		y+= yPadding; 
		
 		x = (w/2) - (((smallButtonWidth * 2) + xPadding)/2);
 		
        okButton.setBounds(new Rectangle(x, y, smallButtonWidth, height));
        
		x += smallButtonWidth + xPadding;
		
        cancelButton.setBounds(new Rectangle(x, y, smallButtonWidth - xPadding, height));  
        
	    jf.getContentPane().add(entitySeparatorOne);    
        jf.getContentPane().add(viewContentsButton);
		jf.getContentPane().add(helpContent);
		jf.getContentPane().add(poiCheckbox);
		jf.getContentPane().add(textLabelX);
		jf.getContentPane().add(textFieldX);
		jf.getContentPane().add(textLabelY);
		jf.getContentPane().add(textFieldY);
		jf.getContentPane().add(locationMapperButton);
		jf.getContentPane().add(helpMap);		
		jf.getContentPane().add(lensCheckbox);
		jf.getContentPane().add(lensPanel);
		jf.getContentPane().add(entitySeparatorTwo);
		jf.getContentPane().add(okButton);
	    jf.getContentPane().add(cancelButton);	
        
        int h = y + height + (yPadding * 4);        
		jf.setSize(w, h); 
		jf.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width-w)/2;
		int screenY = (dim.height-h)/2;
		jf.setLocation(screenX, screenY);	 
		updateSelectedContents();
		jf.setVisible(true);
	}
	
	private void updateSelectedContents(){
    	boolean selected = poiCheckbox.isSelected();            	
    	for (JComponent control : poiControls){
    		if (lensControls.contains(control) && selected){
    			if (lensCheckbox.isSelected()){
        			control.setEnabled(selected);    				
    			}
    		}else{
    			control.setEnabled(selected);
    		}
    	}    	
    	
    	selected = lensCheckbox.isSelected();            	
    	for (JComponent control : lensControls){
    		control.setEnabled(selected);
    	}
	}

	private JPanel generateListBoxAndControls(final EntityAttribute attribute, int panelWidth, int panelHeight) {
		
		JLabel titleLabel = new JLabel(attribute.toString());	
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);	
        Font labelFont = new Font(titleLabel.getFont().getName(), Font.BOLD, (int)(titleLabel.getFont().getSize() * 1.5));         
        titleLabel.setFont(labelFont);
		
		JPanel panel = new JPanel();
		
		JButton createButton = new JButton();        
        createButton.setText("Create");
        
		final IgnoreDoubleClick createClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
    			switch (attribute){
					case Facts:
						new FactCreationGUI("", instance);
						break;
					case Links:
						new LinkCreationGUI("", instance);
						break;	
    			}
			}
		};
        
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {            	
            	createClicker.click(null);
            }
        });   
        
		JButton editButton = new JButton();        
        editButton.setText("Edit");
        
		final IgnoreDoubleClick editClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
            	if (listItems.get(attribute).getSelectedIndex() != -1){  
            		String selectedText = (String)listItems.get(attribute).getSelectedValue();
           			switch (attribute){
    					case Facts:
    						new FactCreationGUI(selectedText, instance);
    						break;
    					case Links:
    						new LinkCreationGUI(selectedText, instance);
    						break;	
        			}
            	}
			}
		};
        
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {  
            	editClicker.click(null);
            }
        });   
        
		JButton removeButton = new JButton();        
        removeButton.setText("Remove");
        
		final IgnoreDoubleClick removeClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
            	if (listItems.get(attribute).getSelectedIndex() != -1){           	
	          	    int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this entry from " + attribute.toString() + "?", "Remove?", JOptionPane.YES_NO_OPTION);
	                if (reply == JOptionPane.YES_OPTION) {   
	          			switch (attribute){
	    					case Facts:
	    						entity.getFacts().remove((String)listItems.get(attribute).getSelectedValue());
	    						break;
	    					case Links:
	    						entity.getLinked().remove((String)listItems.get(attribute).getSelectedValue());
	    						break;	
	        			}
	          			updateList(attribute);
	                }
            	}
			}
		};
        
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	removeClicker.click(null);
            }
        });   
        
        ArrayList<String> attributesArrayList = getArrayListOfAttributes(attribute);			

		String[] attributes = new String[attributesArrayList.size()];
		for (int i = 0; i < attributesArrayList.size(); i++){
			attributes[i] = attributesArrayList.get(i);
		}
		Arrays.sort(attributes);
		
		final DefaultListModel listModel = new DefaultListModel();
		for (String s : attributes){
			listModel.addElement(s);
		}
		
		JList list = new JList(listModel); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
				
		listItems.put(attribute, list);

		JScrollPane listScroller = new JScrollPane(list);
		
		jf.getContentPane().setLayout(null);
		
		int xPadding = 10;
		int yPadding = 10;
		
		int panelX = xPadding;
		int panelY = yPadding;	        
	       
        int height = 24;
        int buttonWidth = panelWidth/3;
		int listBoxWidth = (panelWidth/2) - xPadding;
		int listBoxHeight = panelHeight;
		
		panel.setLayout(null);
		
		panelX = listBoxWidth - buttonWidth - xPadding;
		panelY = yPadding;		

		titleLabel.setBounds(new Rectangle(panelX, panelY, buttonWidth, height));
		
		panelY += height + yPadding;	

		createButton.setBounds(new Rectangle(panelX, panelY, buttonWidth, height));
		
		panelY+= height + yPadding;		

		editButton.setBounds(new Rectangle(panelX, panelY, buttonWidth, height));
		
		panelY+= height + yPadding;		

		removeButton.setBounds(new Rectangle(panelX, panelY, buttonWidth, height));
		
		panelX += buttonWidth + xPadding;
		panelY = 0;
		
		listScroller.setBounds(new Rectangle(panelX, panelY, listBoxWidth, listBoxHeight));
		
		panel.add(titleLabel);
		panel.add(listScroller);
		panel.add(createButton);
		panel.add(editButton);
		panel.add(removeButton);
		
		return panel;
	}
	
	private JPanel generateLensBoxAndControls(int panelWidth, int panelHeight) {	
		
		JPanel panel = new JPanel();
		
        ArrayList<String> lensArray = entity.getLensValues();			

		String[] lenses = new String[lensArray.size()];
		for (int i = 0; i < lensArray.size(); i++){
			lenses[i] = lensArray.get(i);
		}
    	Arrays.sort(lenses, new Comparator<String>() {
    	    @Override
    	    public int compare(String o1, String o2) {              
    	        return o1.compareToIgnoreCase(o2);
    	    }
    	}); 
		
		lensList = new JList(lenses); 
		lensList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lensList.setLayoutOrientation(JList.VERTICAL);
		lensList.setVisibleRowCount(-1);
        lensControls.add(lensList);
		
		JScrollPane listScroller = new JScrollPane(lensList);
        
		JButton createButton = new JButton();        
        createButton.setText("Create");
        lensControls.add(createButton);
        
		final IgnoreDoubleClick createClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
    			new LensSelectorGUI(instance);
			}
		};
        
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {            	
            	createClicker.click(null);
            }
        });   
                
		JButton removeButton = new JButton();        
        removeButton.setText("Remove");
        lensControls.add(removeButton);
        
		final IgnoreDoubleClick removeClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
            	if (lensList.getSelectedIndex() != -1){           	
	          	    int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the POI being visible under this lens?", "Remove?", JOptionPane.YES_NO_OPTION);
	                if (reply == JOptionPane.YES_OPTION) {   
	                	entity.getLensValues().remove((String)lensList.getSelectedValue());
	          			updateLensList();
	                }
            	}
			}
		};
        
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	removeClicker.click(null);
            }
        });   				
        
		jf.getContentPane().setLayout(null);
		
		int xPadding = 10;
		int yPadding = 10;
		
		int panelX = xPadding;
		int panelY = yPadding;	        
	       
        int height = 24;
        int buttonWidth = panelWidth/3;
		int listBoxWidth = (panelWidth/2) - xPadding;
		int listBoxHeight = panelHeight;
		
		panel.setLayout(null);
		
		panelX = 0;
		panelY = 0;				

		listScroller.setBounds(new Rectangle(panelX, panelY, listBoxWidth, listBoxHeight));
		
		panelX += listBoxWidth + xPadding;
		panelY = (listBoxHeight - (height * 2)) - yPadding;
		
		createButton.setBounds(new Rectangle(panelX, panelY, buttonWidth, height));
		
		panelY+= height + yPadding;		

		removeButton.setBounds(new Rectangle(panelX, panelY, buttonWidth, height));
		panel.add(listScroller);
		panel.add(createButton);
		panel.add(removeButton);
		
		return panel;
	}
	
	public void updateLensList(){
        ArrayList<String> lensArray = entity.getLensValues();			

		String[] lenses = new String[lensArray.size()];
		for (int i = 0; i < lensArray.size(); i++){
			lenses[i] = lensArray.get(i);
		}
    	Arrays.sort(lenses, new Comparator<String>() {
    	    @Override
    	    public int compare(String o1, String o2) {              
    	        return o1.compareToIgnoreCase(o2);
    	    }
    	}); 
    	lensList.setListData(lenses);
	}
	
	private void onClose(){
		for (JFrame frame: relatedFrames){
			frame.setVisible(false);
		}
	}
	
	public void updateList(EntityAttribute attribute){
    	String[] contents = SettingsUtil.stringArrayListToStringArray(getArrayListOfAttributes(attribute)); 
    	Arrays.sort(contents);  
    	listItems.get(attribute).setListData(contents);
	}
	
	private ArrayList<String> getArrayListOfAttributes(EntityAttribute attribute){
		if (entity != null){
			switch (attribute){
				case Facts:
					return entity.getFacts();
				case Links:
					return entity.getLinked();	
			}
		}
		return new ArrayList<String>();	
	}
	
	protected JPanel generateUniqueContent(){
		return null;
	}

	@Override
	public void updateXandYFields(float xOnMap, float yOnMap) {
		textFieldX.setText("" + xOnMap);
		textFieldY.setText("" + yOnMap);	
	}
	
}
