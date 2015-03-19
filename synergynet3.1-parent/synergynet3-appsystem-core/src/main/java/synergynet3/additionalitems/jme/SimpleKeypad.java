package synergynet3.additionalitems.jme;

import java.util.UUID;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ISimpleKeypad;
import synergynet3.fonts.FontColour;
import synergynet3.keyboard.KeyboardOutput;
import synergynet3.keyboard.KeyboardSpecialKeys;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;

@ImplementsContentItem(target = ISimpleKeypad.class)
public class SimpleKeypad extends JMEContainer implements IInitable, ISimpleKeypad{
	
	private float buttonSize = 40;
	private float buttonSpacing = 50;
	
	private float width = buttonSpacing * 4;
	private float height = buttonSpacing * 5;
		
	private KeyboardOutput keyboardOutput;
	private ColorRGBA bgColour = ColorRGBA.Black;
	private ColorRGBA keyColour = ColorRGBA.Black;
	private ColorRGBA keyBorderColour = ColorRGBA.Gray;
	private ColorRGBA boardBorderColour = ColorRGBA.Gray;
	
	private boolean movable = true;
	
	private IStage stage;
	
	private RotateTranslateScaleBehaviour rtsBackground, rtsBorder;
	private float minScale, maxScale = 1;
	private boolean scaleLimitsSet = false;
	
	private float x = -width/2;
	private float y = height/2;
	
	private FontColour fontColour = FontColour.White;
	
	public SimpleKeypad(String name, UUID uuid) {
		super(name, uuid);			
	}
	
	@Override
	public void generateKeys(IStage stage, KeyboardOutput keyboardOutput){
		this.stage = stage;
		this.keyboardOutput = keyboardOutput;
		
		try{						
			IColourRectangle background = stage.getContentFactory().create(IColourRectangle.class, "keyboardBg", UUID.randomUUID());
			background.enableTransparency();
			background.setSolidBackgroundColour(bgColour);
			background.setSize(width, height);
			
			IRoundedBorder keyboardBorder = stage.getContentFactory().create(IRoundedBorder.class, "keyboardBorder", UUID.randomUUID());		
			keyboardBorder.setBorderWidth(10f);
			keyboardBorder.setSize(width, height);
			keyboardBorder.setColor(boardBorderColour);
			
			this.addItem(background);
			this.addItem(keyboardBorder);	
			
			x += buttonSpacing;
			y -= buttonSpacing;
			
			generateKey("7");			
			generateKey("8");			
			generateKey("9");			
			
			x = -width/2;
			
			x += buttonSpacing;			
			y -= buttonSpacing;
			
			generateKey("4");			
			generateKey("5");			
			generateKey("6");	
			
			x = -width/2;
			
			x += buttonSpacing;		
			y -= buttonSpacing;
			
			generateKey("1");			
			generateKey("2");			
			generateKey("3");	
			
			x = -width/2;
			
			x += buttonSpacing;		
			y -= buttonSpacing;
			
			generateKey("0");			
			generateBackspaceKey();
			
			this.zOrderManager.setAutoBringToTop(false);
			
			if (movable){				
				rtsBackground = stage.getBehaviourMaker().addBehaviour(background, RotateTranslateScaleBehaviour.class);				
				rtsBackground.setItemActingOn(this);
				rtsBorder = stage.getBehaviourMaker().addBehaviour(keyboardBorder, RotateTranslateScaleBehaviour.class);
				rtsBorder.setItemActingOn(this);	
				if (scaleLimitsSet){
					rtsBackground.setScaleLimits(minScale, maxScale);
					rtsBorder.setScaleLimits(minScale, maxScale);
				}
			}

		}catch(ContentTypeNotBoundException e){}	
	}
	
	private IButtonbox generateBackspaceKey() throws ContentTypeNotBoundException{

		x -= buttonSpacing;

		float backspaceKeyWidth = (buttonSize * 2) + (buttonSpacing - buttonSize);
		x += ((buttonSize/2) + (backspaceKeyWidth/2)) + (buttonSpacing - buttonSize);
		
		IButtonbox button = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		button.setText("<", keyColour, keyBorderColour, fontColour, backspaceKeyWidth, buttonSize, stage);		
		
		final IgnoreDoubleClick clicker = new IgnoreDoubleClick(500){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				keyboardOutput.onKeyboardOutput(KeyboardSpecialKeys.BACKSPACE.toString());
			}
		};
		button.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {					
				clicker.click(event);
			}
		});		
		button.setRelativeLocation(new Vector2f(x, y));
		
		this.addItem(button);
		return button;
	}
	
	public void setButtonSizeAndSpacing(float buttonSize, float buttonSpacing){
		this.buttonSize = buttonSize;
		this.buttonSpacing = buttonSpacing;
		
		width = buttonSpacing * 4;
		height = buttonSpacing * 5;
		
		x = -width/2;
		y = height/2;
	}	
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	private void generateKey(final String key) throws ContentTypeNotBoundException{
		
		IButtonbox button = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		button.setText(key, keyColour, keyBorderColour, fontColour, buttonSize, buttonSize, stage);
		
		final IgnoreDoubleClick clicker = new IgnoreDoubleClick(500){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				keyboardOutput.onKeyboardOutput(key);
			}
		};
		
		button.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {	
				clicker.click(event);
			}
		});		

		button.setRelativeLocation(new Vector2f(x, y));
		this.addItem(button);
		x += buttonSpacing;
	}
	
	public void setScaleLimits(float minScale, float maxScale){
		this.minScale = minScale;
		this.maxScale = maxScale;
		if (rtsBackground != null){
			rtsBackground.setScaleLimits(minScale, maxScale);
		}
		if (rtsBorder != null){
			rtsBorder.setScaleLimits(minScale, maxScale);
		}
		scaleLimitsSet = true;		
	}


	@Override
	public void setColours(ColorRGBA bgColour, ColorRGBA keyColour, ColorRGBA keyBorderColour, ColorRGBA boardBorderColour, FontColour fontColour) {
		this.bgColour = bgColour;
		this.keyColour = keyColour;
		this.keyBorderColour = keyBorderColour;
		this.boardBorderColour = boardBorderColour;
		this.fontColour = fontColour;
	}

	@Override
	public void setMovable(boolean movable) {
		this.movable = movable;		
	}

}
