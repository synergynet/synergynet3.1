package synergynet3.museum.table.mainapp.textentry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.ISimpleKeyboard;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.keyboard.KeyboardOutput;
import synergynet3.keyboard.KeyboardSpecialKeys;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

public class TextEntryGUI implements KeyboardOutput{
	
	private final static int TIME_OUT = 40000;
	
	private final static int WIDTH = 512;
	private final static int HEIGHT = 360;
	
	private static final float BUTTON_WIDTH = 128;		
	private static final float ITEM_HEIGHT = 64;	
	
	private final static float GAP = 32;
	private final static float TEXT_SCALE = 0.5f;	

	private final static float BORDER_WIDTH = 3f;
	
	private static final ColorRGBA INVIS = new ColorRGBA(0,0,0,0);
	
	private IContainer container;
	private TextScrollBox textScrollbox;	
	
	private String id;

	private File folder;
	
	private boolean open = true;
	private Date lastUpdated = new Date();
	
	private String prompt;
	
	public TextEntryGUI(IStage stage, File folder, String id, String prompt) throws ContentTypeNotBoundException{
		this.prompt = prompt;
		this.folder = folder;
		this.id = id;
		container = stage.getContentFactory().create(IContainer.class, "textentryContainer", UUID.randomUUID());
		
		Thread timeoutThread = new Thread(new Runnable() {	  
			public void run() {	   
				while (open){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (new Date().getTime() - lastUpdated.getTime() > TIME_OUT){
						close();
					}
				}
			}
		});
		timeoutThread.start();
		
		IColourRectangle background = stage.getContentFactory().create(IColourRectangle.class, "recorderContainerBg", UUID.randomUUID());
		background.setSolidBackgroundColour(MuseumAppPreferences.getTextInputBackgroundColour());
		background.setSize(WIDTH, HEIGHT);

		IRoundedBorder frameBorder = stage.getContentFactory().create(IRoundedBorder.class, "containerBorder", UUID.randomUUID());		
		frameBorder.setBorderWidth(BORDER_WIDTH);
		frameBorder.setSize(WIDTH, HEIGHT);
		frameBorder.setColor(MuseumAppPreferences.getEntityBorderColour());
		
		ITextbox textLabel = stage.getContentFactory().create(ITextbox.class, "textLabel", UUID.randomUUID());
		textLabel.setColours(INVIS, INVIS, MuseumAppPreferences.getTextInputFontColour());
		textLabel.setWidth((WIDTH - GAP/2) / TEXT_SCALE);
		textLabel.setHeight(ITEM_HEIGHT);
		textLabel.setText(MuseumAppPreferences.getTextInputInstructions(), stage);		
		textLabel.setRelativeScale(TEXT_SCALE);
		textLabel.setRelativeLocation(new Vector2f(0, HEIGHT/2 - ((ITEM_HEIGHT/2) * TEXT_SCALE) - GAP/2));
		textLabel.setMovable(false);
			
		textScrollbox = new TextScrollBox(stage, WIDTH, ITEM_HEIGHT, GAP, TEXT_SCALE);
		textScrollbox.getTextLabel().setRelativeLocation(new Vector2f(0, HEIGHT/2 - (ITEM_HEIGHT * TEXT_SCALE) - GAP - ((ITEM_HEIGHT/2) * TEXT_SCALE)));
		
		ISimpleKeyboard keyboard = stage.getContentFactory().create(ISimpleKeyboard.class, "keyboard", UUID.randomUUID());		
		keyboard.setColours(INVIS, MuseumAppPreferences.getTextInputKeyboardButtonBackgroundColour(), MuseumAppPreferences.getTextInputKeyboardButtonBorderColour(), 
				INVIS, MuseumAppPreferences.getTextInputKeyboardButtonFontColour());
		keyboard.setMovable(false);
		keyboard.generateKeys(stage, this);		
		float keyboardScale = (HEIGHT - ((ITEM_HEIGHT  * TEXT_SCALE) * 3) - (GAP * 1.5f))/keyboard.getHeight();
		keyboard.setRelativeScale(keyboardScale);
		keyboard.setRelativeLocation(new Vector2f(0, (HEIGHT/2) - ((ITEM_HEIGHT * 2) * TEXT_SCALE) - GAP - (keyboard.getHeight()/2) * keyboardScale));
				
		IButtonbox okButton = stage.getContentFactory().create(IButtonbox.class, "enabledOkButton", UUID.randomUUID());  
		okButton.setText("Submit", MuseumAppPreferences.getTextInputKeyboardButtonBackgroundColour(), MuseumAppPreferences.getTextInputKeyboardButtonBorderColour(), 
				MuseumAppPreferences.getTextInputKeyboardButtonFontColour(), BUTTON_WIDTH, ITEM_HEIGHT/2, stage);
		okButton.setRelativeLocation(new Vector2f(-okButton.getWidth()/2 - GAP/2, -HEIGHT/2 + okButton.getHeight()/2 + GAP/2));
		okButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				saveText();
			}
		});
		
		IButtonbox cancelButton = stage.getContentFactory().create(IButtonbox.class, "cancelButton", UUID.randomUUID());
		cancelButton.setText("Cancel", MuseumAppPreferences.getTextInputKeyboardButtonBackgroundColour(), MuseumAppPreferences.getTextInputKeyboardButtonBorderColour(),
				MuseumAppPreferences.getTextInputKeyboardButtonFontColour(), BUTTON_WIDTH, ITEM_HEIGHT/2, stage);
		cancelButton.setRelativeLocation(new Vector2f(cancelButton.getWidth()/2 + GAP/2, -HEIGHT/2 + cancelButton.getHeight()/2 + GAP/2));
		cancelButton.setVisible(true);
		cancelButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				saveText();
			}
		});
		
		ICachableImage listener = stage.getContentFactory().create(ICachableImage.class, "listener", UUID.randomUUID());
		listener.setSize(WIDTH + (BORDER_WIDTH * 2), HEIGHT + (BORDER_WIDTH * 2));

		container.addItem(background);
		container.addItem(frameBorder);
		container.addItem(textLabel);	
		container.addItem(textScrollbox.getTextLabel());
		container.addItem(listener);
		container.addItem(keyboard);
		container.addItem(okButton);
		container.addItem(cancelButton);
		
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(listener, RotateTranslateScaleBehaviour.class);		
		rts.setItemActingOn(container);
		rts.setScaleLimits(0.5f, 1.5f);

		stage.addItem(container);		

		stage.getZOrderManager().bringToTop(container);
		
		container.getZOrderManager().setAutoBringToTop(false);
	}

	private void saveText() {
		outputTextToFile(folder.getAbsolutePath() + File.separator + id + ".txt");
		close();
	}
		
	private void outputTextToFile(String file){
	    BufferedWriter writer = null;
	    try{
	    	writer = new BufferedWriter(new FileWriter(file));
			if (!textScrollbox.getText().equals("")){
				writer.write(textScrollbox.getText());
				writer.newLine();
			}
	        writer.write("Prompt: " + prompt);
	    }catch (IOException e) {
	        System.err.println(e);
	    }finally{
	        if (writer != null){
	        	try {
	                writer.close();
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	        }		    
		}
	}
	
	public void close(){
		open = false;
		container.setVisible(false);
	}
	
	public IItem asItem(){
		return container;
	}

	@Override
	public void onKeyboardOutput(String letter) {
		lastUpdated = new Date();
		if (letter.equalsIgnoreCase(KeyboardSpecialKeys.BACKSPACE.toString())){
			textScrollbox.backspace();
		}else if (letter.equalsIgnoreCase(KeyboardSpecialKeys.ENTER.toString())){
			saveText();
		}else{
			textScrollbox.addChar(letter);
		}		
	}
	
}
