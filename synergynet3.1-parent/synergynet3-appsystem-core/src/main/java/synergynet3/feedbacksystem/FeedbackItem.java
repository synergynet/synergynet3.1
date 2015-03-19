package synergynet3.feedbacksystem;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;

import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.ZManager;
import synergynet3.additionalitems.interfaces.IScrollContainer;
import synergynet3.cachecontrol.IFeedbackItemCachable;
import synergynet3.personalcontentcontrol.StudentIconGenerator;
import synergynet3.personalcontentcontrol.StudentRepresentation;
import synergynet3.studentmenucontrol.StudentMenu;
import multiplicity3.csys.factory.ContentTypeNotBoundException;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public abstract class FeedbackItem implements IFeedbackItemCachable{
	
	protected Logger log;
	private IStage stage;
	protected IItem userIcon;
	
	protected StudentRepresentation student;
	protected String studentID;
	
	protected IScrollContainer setter;
	private StudentMenu menu;
	
	private ILine line;
	
	public String getIcon(){
		return "synergynet3/feedbacksystem/feedbackIcon.png";
	}
	
	public void createSetter(Vector2f position, float rotation, StudentRepresentation student, StudentMenu menuIn, IStage stage, Logger log) {
		
		this.stage = stage;
		menu = menuIn;
		
		menu.setFeedbackModeSelect(this);
		
		if (log == null){
			this.log = Logger.getLogger(FeedbackItem.class.getName());
		}else{
			this.log = log;
		}	
		
		this.student = student;
		studentID = student.getStudentId();
	
		try {
			
			setter = stage.getContentFactory().create(IScrollContainer.class, "menu", UUID.randomUUID());			
			setter.setDimensions(stage, log, 512, 300);	
			stage.addItem(setter);
			setter.setRelativeLocation(new Vector2f(position.x, position.y));
			setter.setRelativeRotation(rotation);
			setter.setFrameColour(student.getStudentColour());
			
			stage.getDragAndDropSystem().registerDragDestinationListener(setter.getBackground(), new FeedbackDragAndDropListener(this, stage, log));
			
			line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());	
			line.setLineColour(student.getStudentColour());
			line.setLineWidth(10f);		
			stage.addItem(line);		
			
			ZManager.manageLineOrderFull(stage, line, menu.getRadialMenu(), setter);
				
			
			IItem userIcon = StudentIconGenerator.generateIcon(stage, 40, 40, 3, false, studentID);
			
			new PerformActionOnAllDescendents(userIcon, false, false){
				@Override
				protected void actionOnDescendent(IItem child){	
					child.setInteractionEnabled(true);
					child.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {					
							tidyAwayFeedbackSetter();
						}
					});
				}
			};
			
			setter.addToAllFrames(userIcon, (int)(setter.getWidth()/2 - 40/2 - 5), 
					(int)(-setter.getHeight()/2 + 40/2 + 5));
			
			addSettings();
			
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e );
		}		
	}
	
	protected void addSettings() throws ContentTypeNotBoundException{}
	
	public boolean addFeedbackItem(IItem item){
		
		if (FeedbackSystem.isItemFeedbackEligible(item) && getAllSettingsMade()){	
		
			FeedbackContainer feedbackContainer;
			
			boolean correctVisibility = false;
			
			if (FeedbackSystem.isItemFeedbackContainer(item)){
				feedbackContainer = FeedbackContainer.feedbackContainers.get(item);
			}else{
				feedbackContainer = addContainerToItem(item);		
				correctVisibility = true;
			}
			feedbackContainer.getFeedbackViewer().addFeedback(this);			
			tidyAwayFeedbackSetter();
			
			if (correctVisibility){
				feedbackContainer.getFeedbackViewer().getContainer().setVisibility(true);
				feedbackContainer.getFeedbackViewer().getContainer().setVisibility(false);
			}			
			return true;
		}else{
			return false;
		}
	}
	
	protected boolean getAllSettingsMade() {
		return false;
	}

	private FeedbackContainer addContainerToItem(IItem item){
		FeedbackContainer feedbackContainer = new FeedbackContainer(stage, log);
		feedbackContainer.setItem(item);		
		feedbackContainer.getFeedbackViewer().addToStage(stage);
		feedbackContainer.getIcon().setVisible(true);
		return feedbackContainer;		
	}
	
	public void tidyAwayFeedbackSetter(){
		if (menu != null)menu.turnFeedbackModeOff();
		stage.removeItem(setter);
		stage.removeItem(line);
	}
	
	public void addFeedbackViewFrame(final FeedbackViewer feedbackViewer, int frameNo) {		
		try {
			
			userIcon = StudentIconGenerator.generateIcon(stage, 50, 50, 4, false, studentID);	
			feedbackViewer.getContainer().addToFrame(userIcon, frameNo, feedbackViewer.getContainer().getWidth()/4, -feedbackViewer.getContainer().getHeight()/2+60);
			
			new PerformActionOnAllDescendents(userIcon, false, false){
				@Override
				protected void actionOnDescendent(IItem child){	
					child.setInteractionEnabled(true);
					child.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {					
							feedbackViewer.toggleVisibility();
						}
					});
				}
			};
			
			generateFeedbackView(feedbackViewer, frameNo);
			
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e );
		}
	}
	
	protected void generateFeedbackView(FeedbackViewer feedbackViewer, int frameNo) throws ContentTypeNotBoundException{}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(IStage stage) {
		this.stage = stage;
	}

	/**
	 * @return the stage
	 */
	public IStage getStage() {
		return stage;
	}

	/**
	 * @param studentID the studentID to set
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return the studentID
	 */
	public String getStudentID() {
		return studentID;
	}

}
