package synergynet3.museum.table.settingsapp.lensmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import multiplicity3.input.events.MultiTouchCursorEvent;


import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.utils.LensUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LensManagerGUI {

	private static String contentFolderLoc;	

	private JList lensList;
	
	private JFrame jf;
	
	private LensManagerGUI instance;

	public LensManagerGUI(){
		instance = this;
		contentFolderLoc = MuseumAppPreferences.getContentFolder();
		
		File f = new File(contentFolderLoc);
		if (!f.isDirectory() || contentFolderLoc.equals("")){
			JOptionPane.showMessageDialog(jf, "The shared content folder is not set up correctly.");
			return;
		}
		
		int w = 512;
		int h = 300;
		
		jf = new JFrame("Lenses Manager");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h); 
		jf.setResizable(false);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width-w)/2;
		int screenY = (dim.height-h)/2;
		jf.setLocation(screenX, screenY);		
		
		jf.getContentPane().setLayout(null);
					
		int xPadding = 10;
		int yPadding = 10;
		
		int panelWidth = w - (xPadding * 2);
		int panelHeight = h - (yPadding * 2);		

		JPanel panel = generateListBoxAndControls(panelWidth, panelHeight);
		panel.setBounds(new Rectangle(0, 0, panelWidth, panelHeight));
		jf.getContentPane().add(panel);
		
		jf.setVisible(true);
	}
	
	private JPanel generateListBoxAndControls(int panelWidth, int panelHeight) {
		
		JPanel panel = new JPanel();
		String[] lensArray = LensUtils.getLenses();		
		Arrays.sort(lensArray);
			
		lensList = new JList(lensArray); 
		lensList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lensList.setLayoutOrientation(JList.VERTICAL);
		lensList.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(lensList);
		
		JButton createButton = new JButton();        
        createButton.setText("Create");
        
		final IgnoreDoubleClick createClicker = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				new LensCreatorGUI("", instance);
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
            	if (lensList.getSelectedIndex() != -1){
                	String lensName = (String)lensList.getSelectedValue();     
    				new LensCreatorGUI(lensName, instance);         
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
				if (lensList.getSelectedIndex() != -1){
                	String lensName = (String)lensList.getSelectedValue();        
	          	    int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove \n " + lensName + "?", "Remove from lenses", JOptionPane.YES_NO_OPTION);
	                if (reply == JOptionPane.YES_OPTION) {
	                	File file = new File(contentFolderLoc + File.separator + LensUtils.LENSES_FOLDER + File.separator + lensName + ".xml");
	    				if (!file.delete()){
	    					file.deleteOnExit();
	    				}
	                	updateList();
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
		
		int x = 0;
		int y = 0;	        
	       
        int height = 24;
        int buttonWidth = (panelWidth/2) - xPadding;
		int listBoxWidth = panelWidth/2;
		int listBoxHeight = panelHeight;
		
		panel.setLayout(null);
			
		listScroller.setBounds(new Rectangle(x, y, listBoxWidth, listBoxHeight));
		
		x = panelWidth - buttonWidth;
		y = panelHeight/4;		

		createButton.setBounds(new Rectangle(x, y, buttonWidth, height));
		
		y+= height + yPadding;		

		editButton.setBounds(new Rectangle(x, y, buttonWidth, height));
		
		y+= height + yPadding;		

		removeButton.setBounds(new Rectangle(x, y, buttonWidth, height));
		
		panel.add(listScroller);
		panel.add(createButton);
		panel.add(editButton);
		panel.add(removeButton);
		
		updateList();
		
		return panel;
	}
	
	public boolean listContainsValue(String newValue){
        for (int i = 0; i < lensList.getModel().getSize(); i++) {
            String item = (String)lensList.getModel().getElementAt(i);
            if (newValue.equals(item)){
            	return true;
            }
        }
		return false;
	}
	
	public void show(){
		if (jf != null){
			jf.setVisible(true);
		}
	}
	
	public void updateList(){
    	String[] contents = LensUtils.getLenses();
    	Arrays.sort(contents, new Comparator<String>() {
    	    @Override
    	    public int compare(String o1, String o2) {              
    	        return o1.compareToIgnoreCase(o2);
    	    }
    	}); 
    	lensList.setListData(contents);
	}

	public boolean isVisible() {
		if (jf == null){
			return false;
		}
		return jf.isVisible();
	}

}
