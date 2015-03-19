package synergynet3.projector;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IToggleButtonbox;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.fonts.FontColour;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.web.core.AppSystemControlComms;
import synergynet3.web.shared.DevicesSelected;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class TopRightProjectorMenu {
	
    private static final Logger log = Logger.getLogger(TopRightProjectorMenu.class.getName());
	
	private static final float X_TABLE_BUTTONS_OFFSET = -75f;
	private static final float Y_TABLE_BUTTONS_OFFSET = 10f;
	
	private static final float X_TABLE_GAP = 150f;
	private static final float Y_TABLE_GAP = 60f;
	
	private static final int X_LIMIT = 2;
	private static final int Y_LIMIT = 2;
	
	private static final float WIDTH = 400;
	private static final float HEIGHT = 300;
	
	private static final String[] tableActions = {"Send content to", "Show content from", "Clear", "Freeze/Unfreeze", "View Snapshots of"};
	private static final String[] tableSelections = {"all tables", "these tables:"};

	private static final String SCROLL_BUTTON_IMAGE  = "synergynet3/additionalitems/scrollButton.png";
	
	protected static final String CORNER_ICON_LOC_OFF = "synergynet3/projector/corner_button.png";
	protected static final String CORNER_ICON_LOC_ON = "synergynet3/projector/corner_button_on.png";
	
	protected static final float BUTTON_SIZE = 40f, CONTROL_BORDER_WIDTH = 5f, OFFSET = 10f;
	
	private ArrayList<IToggleButtonbox> tableButtons = new ArrayList<IToggleButtonbox>();
	
	private IContainer topRightControls = null;
	
	private int tableActionIndex = 0;
	private int tableSelectionIndex = 0;	
	private int tableButtonFrame = 0;
	
	private ICachableImage tablesViewedScrollDown;
	private ICachableImage tablesViewedScrollUp;

	private IButtonbox actionsLabel;
	private IButtonbox selectionLabel;
	
	protected IStage stage;
	private IImage topRightCornerButton;
	
	public TopRightProjectorMenu(SynergyNetProjector projector){
		this.stage = projector.getStage();
		try{
			generateTopRightButton();
			generateTopRightMenu();
			generateActionsButtons();
			generateTablesSelectionButtons();
			generateTablesViewedButtons();
			generateConfirmationButtons();			 
			initialiseMenu();
		} catch (ContentTypeNotBoundException e) {				
			log.warning("ContentTypeNotBoundException: " + e);
		}	
	}
	
	private void generateTopRightButton() throws ContentTypeNotBoundException{
		topRightCornerButton = stage.getContentFactory().create(IImage.class, "cornerButton", UUID.randomUUID());
		topRightCornerButton.setImage(CORNER_ICON_LOC_OFF);		
		topRightCornerButton.setSize(BUTTON_SIZE*2, BUTTON_SIZE*2);
		topRightCornerButton.setRelativeLocation(new Vector2f(stage.getDisplayWidth()/2, stage.getDisplayHeight()/2));
		
		topRightCornerButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {	
				if (topRightControls.isVisible()){						
					topRightControls.setVisible(false);				
					topRightCornerButton.setImage(CORNER_ICON_LOC_OFF);
				}else{						
					topRightControls.setVisible(true);		
					manageTableButtonVisiblity();
					stage.getZOrderManager().bringToTop(topRightControls);
					topRightCornerButton.setImage(CORNER_ICON_LOC_ON);
				}
			}
		});				
		stage.addItem(topRightCornerButton);	
	}
	
	private void generateTopRightMenu()  throws ContentTypeNotBoundException{
						
		topRightControls = stage.getContentFactory().create(IContainer.class, "wrapper", UUID.randomUUID());
	
		IColourRectangle background = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
		background.setSolidBackgroundColour(ColorRGBA.Black);
		background.setSize(WIDTH, HEIGHT);
		background.setInteractionEnabled(false);
				
		IRoundedBorder frameBorder = stage.getContentFactory().create(IRoundedBorder.class, "border", UUID.randomUUID());		
		frameBorder.setBorderWidth(CONTROL_BORDER_WIDTH);
		frameBorder.setSize(WIDTH, HEIGHT);
		frameBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));
		
		topRightControls.addItem(background);
		topRightControls.addItem(frameBorder);
		
		stage.addItem(topRightControls);	
	}
	
	private void generateActionsButtons() throws ContentTypeNotBoundException {		
		float y = 125;
		
		actionsLabel = stage.getContentFactory().create(IButtonbox.class, "textLabel", UUID.randomUUID());
		actionsLabel.setText(tableActions[tableActionIndex], ColorRGBA.Black, ColorRGBA.Black, FontColour.White, 285, 40, stage);		
		actionsLabel.setRelativeLocation(new Vector2f(0, y));
		actionsLabel.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableActionIndex++;
				if (tableActionIndex >= tableActions.length)tableActionIndex = 0;
				actionsLabel.getTextLabel().setText(tableActions[tableActionIndex]);		
			}
		});	
		topRightControls.addItem(actionsLabel);	
		
		ICachableImage actionsScrollUp = stage.getContentFactory().create(ICachableImage.class, "scrollUp", UUID.randomUUID());
		actionsScrollUp.setImage(SCROLL_BUTTON_IMAGE);				
		actionsScrollUp.setSize(25, 25);	
		actionsScrollUp.setRelativeRotation(FastMath.DEG_TO_RAD*180);
		actionsScrollUp.setRelativeLocation(new Vector2f(160f, y));
		actionsScrollUp.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableActionIndex++;
				if (tableActionIndex >= tableActions.length)tableActionIndex = 0;
				actionsLabel.getTextLabel().setText(tableActions[tableActionIndex]);			
			}
		});
		topRightControls.addItem(actionsScrollUp);	
		
		ICachableImage actionsScrollDown = stage.getContentFactory().create(ICachableImage.class, "scrollUp", UUID.randomUUID());
		actionsScrollDown.setImage(SCROLL_BUTTON_IMAGE);				
		actionsScrollDown.setSize(25, 25);	
		actionsScrollDown.setRelativeLocation(new Vector2f(-160f, y));
		actionsScrollDown.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableActionIndex--;
				if (tableActionIndex < 0)tableActionIndex = tableActions.length - 1;
				actionsLabel.getTextLabel().setText(tableActions[tableActionIndex]);		
			}
		});
		topRightControls.addItem(actionsScrollDown);	
	}
	
	private void generateTablesSelectionButtons() throws ContentTypeNotBoundException {
		float y = 75;
		
		selectionLabel = stage.getContentFactory().create(IButtonbox.class, "textLabel", UUID.randomUUID());
		selectionLabel.setText(tableSelections[tableSelectionIndex], ColorRGBA.Black, ColorRGBA.Black, FontColour.White, 285, 40, stage);
		selectionLabel.setRelativeLocation(new Vector2f(0, y));
		selectionLabel.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableSelectionIndex++;
				if (tableSelectionIndex >= tableSelections.length)tableSelectionIndex = 0;
				addOrRemoveTableButtons();
			}
		});
		topRightControls.addItem(selectionLabel);	
		
		ICachableImage selectionScrollUp = stage.getContentFactory().create(ICachableImage.class, "scrollUp", UUID.randomUUID());
		selectionScrollUp.setImage(SCROLL_BUTTON_IMAGE);				
		selectionScrollUp.setSize(25, 25);	
		selectionScrollUp.setRelativeRotation(FastMath.DEG_TO_RAD*180);
		selectionScrollUp.setRelativeLocation(new Vector2f(160f, y));
		selectionScrollUp.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableSelectionIndex++;
				if (tableSelectionIndex >= tableSelections.length)tableSelectionIndex = 0;
				addOrRemoveTableButtons();
			}
		});
		topRightControls.addItem(selectionScrollUp);	
		
		ICachableImage selectionScrollDown = stage.getContentFactory().create(ICachableImage.class, "scrollUp", UUID.randomUUID());
		selectionScrollDown.setImage(SCROLL_BUTTON_IMAGE);				
		selectionScrollDown.setSize(25, 25);	
		selectionScrollDown.setRelativeLocation(new Vector2f(-160f, y));
		selectionScrollDown.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableSelectionIndex--;
				if (tableSelectionIndex < 0)tableSelectionIndex = tableSelections.length - 1;
				addOrRemoveTableButtons();
			}
		});		
		topRightControls.addItem(selectionScrollDown);	
	}	

	private void generateTablesViewedButtons() throws ContentTypeNotBoundException {
		float y = Y_TABLE_BUTTONS_OFFSET - (Y_TABLE_GAP * (Y_LIMIT - 1))/2;
		
		tablesViewedScrollUp = stage.getContentFactory().create(ICachableImage.class, "scrollUp", UUID.randomUUID());
		tablesViewedScrollUp.setImage(SCROLL_BUTTON_IMAGE);				
		tablesViewedScrollUp.setSize(25, 75);	
		tablesViewedScrollUp.setRelativeRotation(FastMath.DEG_TO_RAD*180);
		tablesViewedScrollUp.setRelativeLocation(new Vector2f(175f, y));
		tablesViewedScrollUp.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableButtonFrame++;
				if (tableButtonFrame > (int)(FastMath.ceil((tableButtons.size()-1)/(X_LIMIT * Y_LIMIT))))tableButtonFrame = 0;
				manageTableButtonVisiblity();
			}
		});		
		topRightControls.addItem(tablesViewedScrollUp);	
		
		tablesViewedScrollDown = stage.getContentFactory().create(ICachableImage.class, "scrollUp", UUID.randomUUID());
		tablesViewedScrollDown.setImage(SCROLL_BUTTON_IMAGE);				
		tablesViewedScrollDown.setSize(25, 75);	
		tablesViewedScrollDown.setRelativeLocation(new Vector2f(-175f, y));
		tablesViewedScrollDown.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				tableButtonFrame--;
				if (tableButtonFrame < 0)tableButtonFrame = (int)(FastMath.ceil((tableButtons.size()-1)/(X_LIMIT * Y_LIMIT)));
				manageTableButtonVisiblity();
			}
		});
		topRightControls.addItem(tablesViewedScrollDown);
	}
	
	private void generateConfirmationButtons() throws ContentTypeNotBoundException {
		float y = -115; 
		
		IButtonbox okButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		okButton.setText("OK", ColorRGBA.Black, ColorRGBA.White, FontColour.White, BUTTON_SIZE*3, BUTTON_SIZE, stage);
		okButton.setRelativeLocation(new Vector2f(-100f, y));
		okButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				performAction();						
				topRightControls.setVisible(false);	
				topRightCornerButton.setImage(CORNER_ICON_LOC_OFF);
			}
		});		
		topRightControls.addItem(okButton);
		
		IButtonbox cancelButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		cancelButton.setText("Cancel", ColorRGBA.Black, ColorRGBA.White, FontColour.White, BUTTON_SIZE*3, BUTTON_SIZE, stage);
		cancelButton.setRelativeLocation(new Vector2f(100f, y));
		cancelButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {	
				tableSelectionIndex = 0;
				addOrRemoveTableButtons();	
				topRightControls.setVisible(false);	
				topRightCornerButton.setImage(CORNER_ICON_LOC_OFF);
			}
		});		
		topRightControls.addItem(cancelButton);
	}
	
	private void initialiseMenu(){
		topRightControls.getZOrderManager().setAutoBringToTop(false);		
		topRightControls.setRelativeLocation(new Vector2f(stage.getDisplayWidth()/2 - WIDTH/2 - (BUTTON_SIZE - OFFSET), stage.getDisplayHeight()/2 - HEIGHT/2 -  (BUTTON_SIZE - OFFSET)));
		topRightControls.setVisible(false);
	}	
	
	private void addOrRemoveTableButtons() {		

		selectionLabel.getTextLabel().setText(tableSelections[tableSelectionIndex]);	
		
		float x = X_TABLE_BUTTONS_OFFSET;
		float y = Y_TABLE_BUTTONS_OFFSET;
		
		if (tableSelectionIndex == 1){
			for (final String table: AppSystemControlComms.get().getTablesList()){
				try {
					final IToggleButtonbox tableButton = stage.getContentFactory().create(IToggleButtonbox.class, "tableButtonOff", UUID.randomUUID());				
					tableButton.setText(table.toLowerCase(), ColorRGBA.Black, ColorRGBA.Gray, FontColour.White, 
							table.toLowerCase(), ColorRGBA.Gray, ColorRGBA.White, FontColour.White, BUTTON_SIZE*3, BUTTON_SIZE, stage);
					tableButton.setRelativeLocation(new Vector2f(x, y));
					tableButtons.add(tableButton);
					topRightControls.addItem(tableButton);						
					
					tableButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {	
							if (tableButton.isVisible()){								
								tableButton.toggle();
							}
						}
					});	
					
				} catch (ContentTypeNotBoundException e) {}				
				
				x += X_TABLE_GAP;
				if (x > X_TABLE_BUTTONS_OFFSET + (X_TABLE_GAP * (X_LIMIT - 1))){
					x = X_TABLE_BUTTONS_OFFSET;
					y -= Y_TABLE_GAP;
				}
				if (y < Y_TABLE_BUTTONS_OFFSET - (Y_TABLE_GAP * (Y_LIMIT - 1)))y = Y_TABLE_BUTTONS_OFFSET;				
				
			}	
			
			manageTableButtonVisiblity();
		}else{
			for (IToggleButtonbox button: tableButtons)topRightControls.removeItem(button);						
			tablesViewedScrollUp.setVisible(false);
			tablesViewedScrollDown.setVisible(false);
			tableButtonFrame = 0;
			tableButtons.clear();
		}
	}
	
	private void manageTableButtonVisiblity(){
		for (IToggleButtonbox button: tableButtons)button.setVisible(false);
		tablesViewedScrollUp.setVisible(false);
		tablesViewedScrollDown.setVisible(false);
		
		if (tableButtons.size() > (X_LIMIT * Y_LIMIT)){
			tablesViewedScrollUp.setVisible(true);
			tablesViewedScrollDown.setVisible(true);
		}
		
		for (int i = tableButtonFrame * (X_LIMIT * Y_LIMIT); i < (tableButtonFrame + 1) * (X_LIMIT * Y_LIMIT); i++){
			if (i >= tableButtons.size())break;
			tableButtons.get(i).setVisible(true);
		}
	}
	
	private void performAction() {	
		
		String[] tablesToSendTo = {DevicesSelected.ALL_TABLES_ID};
		String[] projectorsToSendTo = {SynergyNetCluster.get().getIdentity()};
		
		if (tableSelectionIndex == 1){
			ArrayList<String> selectedTables = new ArrayList<String>();
			
			for (IToggleButtonbox table: tableButtons){
				if (table.getToggleStatus()){
					selectedTables.add(table.getTextLabelOn().getText());
				}
			}
			tablesToSendTo = new String[selectedTables.size()];
			selectedTables.toArray(tablesToSendTo);			
		}
		
		switch (tableActionIndex){
			case 0:		
				final String[] finalTablesToSendTo = tablesToSendTo;
				Thread cachingThread = new Thread(new Runnable() {	  
					public void run() {	   
						ArrayList<ContentTransferedMessage> messages = ProjectorTransferUtilities.get().prepareToTransferAllContents(finalTablesToSendTo);		
						for (String table: finalTablesToSendTo){
							if (table.equals(DevicesSelected.ALL_TABLES_ID)){
								AppSystemControlComms.get().allTablesReceiveContent(messages);
							}else{
								AppSystemControlComms.get().specificTableReceiveContent(messages, table);
							}
						}	
					}    					
				});	
				cachingThread.start();
				break;		
			case 1:		
				for (String table: tablesToSendTo){
					if (table.equals(DevicesSelected.ALL_TABLES_ID)){
						AppSystemControlComms.get().allTablesSendContentsToProjectors(projectorsToSendTo);
					}else{
						AppSystemControlComms.get().specificTablesSendContentsToProjectors(projectorsToSendTo, table);
					}
				}
				break;	
			case 2:		
				for (String table: tablesToSendTo){
					if (table.equals(DevicesSelected.ALL_TABLES_ID)){
						AppSystemControlComms.get().allTablesRemoveAdditionalContent(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
					}else{
						AppSystemControlComms.get().specificTableRemoveAdditionalContent(new PerformActionMessage(MESSAGESTATE.ACTIVATE), table);
					}
				}	
				break;		
			case 3:		
				for (String table: tablesToSendTo){
					if (table.equals(DevicesSelected.ALL_TABLES_ID)){
						AppSystemControlComms.get().allTablesFreeze(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
					}else{
						AppSystemControlComms.get().specificTablesFreeze(new PerformActionMessage(MESSAGESTATE.ACTIVATE), table);
					}
				}	
				break;		
			case 4:		
				for (String table: tablesToSendTo){
					if (table.equals(DevicesSelected.ALL_TABLES_ID)){
						AppSystemControlComms.get().allTablesSendScreenshotsToProjectors(projectorsToSendTo);
					}else{
						AppSystemControlComms.get().specificTablesSendScreenshotsToProjectors(projectorsToSendTo, table);
					}
				}	
				break;	
		}
		
		tableSelectionIndex = 0;
		addOrRemoveTableButtons();		
	}
		
}