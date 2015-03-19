package synergynet3.apps.numbernet.ui.calculator;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity3.csys.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import synergynet3.apps.numbernet.ui.calculator.calckeydef.CalculatorKeyboardDefinition;
import synergynet3.apps.numbernet.ui.calculator.calckeydef.CalculatorKeyboardRenderer;
import synergynet3.apps.numbernet.validation.IValidationChecker.ValidationResult;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class Calculator implements IMultiTouchKeyboardListener{
	
	private static final int CHARACTER_DISPLAY_LIMIT = 8;
	
	private String currentOutputValue = "";
	
	//private IStage stage;	
	private IMutableLabel outputLabel;
	private IMutableLabel ownerLabel;
	private IItem keyboardWrapperFrame;
	private List<ICalculatorEventListener> listeners = new ArrayList<ICalculatorEventListener>();

	private String owner = "";
	private IColourRectangle outputLabelBackground;
	private IKeyboard keypadKeyboard;
	private IContainer parent;
	private BehaviourMaker behaviourMaker;
	private IContentFactory contentFactory;

	public Calculator(IContainer parent, IStage stage) {
		this.parent = parent;
		this.behaviourMaker = stage.getBehaviourMaker();
		this.contentFactory = stage.getContentFactory();
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public void setTextForCalculatorDisplay(String txt) {
		currentOutputValue = txt;
		if (txt.length() >= CHARACTER_DISPLAY_LIMIT + 3){
			outputLabel.setText("..." + txt.substring(txt.length() - (CHARACTER_DISPLAY_LIMIT), txt.length()));
		}else{
			outputLabel.setText(txt);
		}
		setDisplayStyle(ValidationResult.VALID);
	}
	
	public void setVisibility(Boolean visible) {
		keyboardWrapperFrame.setVisible(visible);
	}
	
	public boolean isVisible() {
		return keyboardWrapperFrame.isVisible();
	}
	
	public void addCalculatorEventListener(ICalculatorEventListener listener) {
		if(listeners.contains(listener)) return;		
		listeners.add(listener);
	}
	
	public void remove() {
		keyboardWrapperFrame.getParentItem().removeItem(keyboardWrapperFrame);
		listeners.clear();		
	}
	
	public IItem getDisplayItem() {
		return outputLabel;
	}

	public float getRotation() {
		return keyboardWrapperFrame.getRelativeRotation();
	}

	public void setRelativeLocation(float x, float y) {
		keyboardWrapperFrame.setRelativeLocation(new Vector2f(x, y));
	}

	public IItem getDragAndDropTarget() {
		return outputLabelBackground;
	}
	
	public void setDisplayStyle(ValidationResult result) {
		switch(result) {
		case VALID: {
			outputLabel.setFont(FontUtil.getFont(FontColour.White));
			break;
		}
		case INVALID: {
			outputLabel.setFont(FontUtil.getFont(FontColour.Red));
			break;
		}
		case DUPLICATE: {
			outputLabel.setFont(FontUtil.getFont(FontColour.Red));
			break;
		}
		case IDENTICAL: {
			outputLabel.setFont(FontUtil.getFont(FontColour.Red));
			break;
		}
		}
	}
	
	public void buildAndAttach() throws ContentTypeNotBoundException {
		float borderTransparency = 0.9f;
		float borderGreyValue = 0.5f;
		
		keyboardWrapperFrame = contentFactory.create(IContainer.class, "keywf", UUID.randomUUID());
		parent.addItem(keyboardWrapperFrame);
		
		keypadKeyboard = createKeypad(contentFactory);
		keyboardWrapperFrame.addItem(keypadKeyboard);
		Vector2f keypadSize = new Vector2f(
				(float)keypadKeyboard.getKeyboardDefinition().getBounds().getWidth(), 
				(float)keypadKeyboard.getKeyboardDefinition().getBounds().getHeight());
		
		KeyboardBehaviour keyboardBehaviour = behaviourMaker.addBehaviour(keypadKeyboard, KeyboardBehaviour.class);
		keyboardBehaviour.addListener(this);
		
		IRoundedBorder keyboardWrapperFrameBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());
		keyboardWrapperFrame.addItem(keyboardWrapperFrameBorder);
		keyboardWrapperFrameBorder.setBorderWidth(45f);
		keyboardWrapperFrameBorder.setSize(keypadSize.x, keypadSize.y);
		keyboardWrapperFrameBorder.setColor(new ColorRGBA(borderGreyValue, borderGreyValue, borderGreyValue, borderTransparency));	
		
		float outputLabelWidth = keypadSize.x + 60f;
		float outputLabelHeight = 60f;
		
		IRoundedBorder outputLabelBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());
		keyboardWrapperFrame.addItem(outputLabelBorder);
		outputLabelBorder.setBorderWidth(10f);
		outputLabelBorder.setSize(outputLabelWidth, outputLabelHeight);
		outputLabelBorder.setColor(new ColorRGBA(borderGreyValue, borderGreyValue, borderGreyValue, borderTransparency));
		
		outputLabelBackground = contentFactory.create(IColourRectangle.class, "labelbg", UUID.randomUUID());
		keyboardWrapperFrame.addItem(outputLabelBackground);
		ColorRGBA topGreen = new ColorRGBA(0.3f, 0.8f, 0.3f, 1f);
		ColorRGBA bottomGreen = new ColorRGBA(0.2f, 0.6f, 0.2f, 1f);
		Gradient greenGradient = new Gradient(topGreen, bottomGreen, GradientDirection.VERTICAL);
		outputLabelBackground.setGradientBackground(greenGradient);
		outputLabelBackground.setSize(outputLabelWidth, outputLabelHeight);
		
		outputLabel = createKeypadOutputLabel(contentFactory, outputLabelWidth);
		keyboardWrapperFrame.addItem(outputLabel);
		
		Vector2f outputLocation = new Vector2f(0, 252f);
		outputLabel.setRelativeLocation(outputLocation);
		outputLabel.setRelativeScale(1.5f);
		outputLabelBorder.setRelativeLocation(outputLocation);
		outputLabelBackground.setRelativeLocation(outputLocation);
		
		ownerLabel = createCalculatorOwnerLabel(contentFactory);
		keyboardWrapperFrame.addItem(ownerLabel);		
		
		keyboardWrapperFrame.getZOrderManager().setAutoBringToTop(false);
		keyboardWrapperFrame.getZOrderManager().setBringToTopPropagatesUp(true);
		keyboardWrapperFrame.getZOrderManager().bringToTop(outputLabel);
		
		keyboardWrapperFrame.setRelativeScale(0.5f);
		
		RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(
				keyboardWrapperFrameBorder, 
				RotateTranslateScaleBehaviour.class);		
		rts.setItemActingOn(keyboardWrapperFrame);
		rts.setScaleEnabled(false);		
	}
	
	private IMutableLabel createKeypadOutputLabel(IContentFactory cf, float outputLabelWidth) throws ContentTypeNotBoundException {
		IMutableLabel outputLabel = cf.create(IMutableLabel.class, "", UUID.randomUUID());		
		outputLabel.setFont(FontUtil.getFont(FontColour.White));
		outputLabel.setText("");	
		outputLabel.setBoxSize(outputLabelWidth / 1.6f, outputLabel.getTextSize().y);
		return outputLabel;
	}

	private IKeyboard createKeypad(IContentFactory cf) throws ContentTypeNotBoundException {
		IKeyboard kb = cf.create(IKeyboard.class, "kb", UUID.randomUUID());
		kb.setKeyboardDefinition(new CalculatorKeyboardDefinition());
		kb.setKeyboardRenderer(new CalculatorKeyboardRenderer(kb.getKeyboardDefinition()));		
		kb.reDraw();
		return kb;
	}

	private IMutableLabel createCalculatorOwnerLabel(IContentFactory cf) throws ContentTypeNotBoundException {
		IMutableLabel ownerLabel = cf.create(IMutableLabel.class, "", UUID.randomUUID());		
		ownerLabel.setFont(FontUtil.getFont(FontColour.Blue));
		ownerLabel.setText(this.owner);
		ownerLabel.setBoxSize((float)keypadKeyboard.getKeyboardDefinition().getBounds().getWidth(), ownerLabel.getTextSize().y);
		ownerLabel.setRelativeLocation(new Vector2f(0, -190f));		
		return ownerLabel;
	}
	
	public void setKeyVisible(String keyStringRepresentation, boolean state) {
		this.keypadKeyboard.getKeyboardDefinition().getKey(keyStringRepresentation).setEnabled(state);
		this.keypadKeyboard.reDraw();
	}

	@Override
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
			boolean ctlDown) {
	}

	@Override
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		setDisplayStyle(ValidationResult.VALID);
		if(k.getKeyCode() == KeyEvent.VK_C) {
			if (currentOutputValue.length() != 0){
				setTextForCalculatorDisplay(currentOutputValue.substring(0, currentOutputValue.length() - 1));
				notifyListenersCharacterRemoved();
			}
		}else if(k.getKeyCode() == KeyEvent.VK_BACK_QUOTE) {
			if(currentOutputValue.length() < 1) return;			
			notifyListenersEnterKeyPressed();
		}else{
			char character = k.getKeyStringRepresentation().charAt(0);
			setTextForCalculatorDisplay(currentOutputValue += character);
			notifyListenersCharacterAdded(character);
		}		
	}
	
	private void notifyListenersCharacterRemoved() {
		for(ICalculatorEventListener listener : listeners) {
			listener.characterRemoved(this, currentOutputValue);
		}
	}

	private void notifyListenersCharacterAdded(char character) {
		for(ICalculatorEventListener listener : listeners) {
			listener.characterAdded(character, this, currentOutputValue);
		}
	}

	private void notifyListenersEnterKeyPressed() {
		for(ICalculatorEventListener listener : listeners) {
			listener.enterKeyPressed(this, currentOutputValue);
		}
	}



}
