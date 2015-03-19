package synergynet3.projector;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.fonts.FontColour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

/** Class to be run to produce a projection environment.*/
public class BottomRightProjectorMenu{	
	
    private static final Logger log = Logger.getLogger(BottomRightProjectorMenu.class.getName());
	
	protected static final String CORNER_ICON_LOC_OFF = "synergynet3/projector/corner_button.png";
	protected static final String CORNER_ICON_LOC_ON = "synergynet3/projector/corner_button_on.png";
	
	protected static final float BUTTON_SIZE = 40f, CONTROL_BORDER_WIDTH = 5f, OFFSET = 10f;
	
	private IContainer bottomRightControls = null;
	
	private SynergyNetProjector projector;
	private IStage stage;
	private IImage bottomRightCornerButton;
	
	public BottomRightProjectorMenu(SynergyNetProjector projector){
		this.stage = projector.getStage();
		this.projector = projector;
		try{
			generateBottomRightButton();	
			generateBottomRightMenu();
		} catch (ContentTypeNotBoundException e) {				
			log.warning("ContentTypeNotBoundException: " + e);
		}	
	}
	
	private void generateBottomRightButton() throws ContentTypeNotBoundException{
		bottomRightCornerButton = stage.getContentFactory().create(IImage.class, "cornerButton", UUID.randomUUID());
		bottomRightCornerButton.setImage(CORNER_ICON_LOC_OFF);		
		bottomRightCornerButton.setSize(BUTTON_SIZE*2, BUTTON_SIZE*2);
		bottomRightCornerButton.setRelativeLocation(new Vector2f(stage.getDisplayWidth()/2, -stage.getDisplayHeight()/2));
		
		bottomRightCornerButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {	
				if (bottomRightControls.isVisible()){						
					bottomRightControls.setVisible(false);						
					bottomRightCornerButton.setImage(CORNER_ICON_LOC_OFF);
				}else{						
					bottomRightControls.setVisible(true);	
					stage.getZOrderManager().bringToTop(bottomRightControls);
					stage.getZOrderManager().sendToBottom(bottomRightCornerButton);
					bottomRightCornerButton.setImage(CORNER_ICON_LOC_ON);
				}
			}
		});	
		
		stage.addItem(bottomRightCornerButton);	
	}
		
	private void generateBottomRightMenu() throws ContentTypeNotBoundException{	
		ArrayList<IItem> buttons = generateBottomRightButtons();
		
		bottomRightControls = stage.getContentFactory().create(IContainer.class, "wrapper", UUID.randomUUID());
		
		float width = 0, height = 0;
		
		width = OFFSET + (((BUTTON_SIZE*3) + OFFSET) * buttons.size());
		height = (OFFSET*2) + BUTTON_SIZE;
		
		IColourRectangle background = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
		background.setSolidBackgroundColour(ColorRGBA.Black);
		background.setSize(width, height);
				
		IRoundedBorder frameBorder = stage.getContentFactory().create(IRoundedBorder.class, "border", UUID.randomUUID());		
		frameBorder.setBorderWidth(CONTROL_BORDER_WIDTH);
		frameBorder.setSize(width, height);
		frameBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));
		
		bottomRightControls.addItem(background);
		bottomRightControls.addItem(frameBorder);
		
		float buttonLocX = -width/2 + OFFSET + (BUTTON_SIZE*3)/2;
		for (IItem button: buttons){
			button.setRelativeLocation(new Vector2f(buttonLocX, 0));
			bottomRightControls.addItem(button);
			buttonLocX += OFFSET + (BUTTON_SIZE*3);
		}
		
		bottomRightControls.setRelativeLocation(new Vector2f(stage.getDisplayWidth()/2 - width/2 - (BUTTON_SIZE - OFFSET), -stage.getDisplayHeight()/2 + height/2 +  (BUTTON_SIZE - OFFSET)));
		bottomRightControls.setVisible(false);
		bottomRightControls.getZOrderManager().setAutoBringToTop(false);
		
		stage.addItem(bottomRightControls);	
	}
	
	private ArrayList<IItem> generateBottomRightButtons() throws ContentTypeNotBoundException {
		ArrayList<IItem> buttons = new ArrayList<IItem>();
		
		IButtonbox alignButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		alignButton.setText("Align", ColorRGBA.Black, ColorRGBA.White, FontColour.White, BUTTON_SIZE*3, BUTTON_SIZE, stage);
		alignButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				projector.alignContents();						
				bottomRightControls.setVisible(false);	
				bottomRightCornerButton.setImage(CORNER_ICON_LOC_OFF);
			}
		});		
		buttons.add(alignButton);
		
		IButtonbox clearButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		clearButton.setText("Clear", ColorRGBA.Black, ColorRGBA.White, FontColour.White, BUTTON_SIZE*3, BUTTON_SIZE, stage);
		clearButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				projector.clearContents();						
				bottomRightControls.setVisible(false);	
				bottomRightCornerButton.setImage(CORNER_ICON_LOC_OFF);
			}
		});	
		buttons.add(clearButton);
		
		return buttons;
	}	
	
}