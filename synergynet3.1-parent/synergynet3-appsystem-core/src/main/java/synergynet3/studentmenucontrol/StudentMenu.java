package synergynet3.studentmenucontrol;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;

import synergynet3.SynergyNetApp;
import synergynet3.additionalitems.RadialMenuOption;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IRadialMenu;
import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackSelect;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.personalcontentcontrol.StudentGallery;
import synergynet3.personalcontentcontrol.StudentIconGenerator;
import synergynet3.personalcontentcontrol.StudentRepresentation;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class StudentMenu {
	
	private static String SCREENSHOT_MENU_ICON  = "synergynet3/studentmenucontrol/screenshotMenuIcon.png";
	private static String GALLERY_MENU_ICON  = "synergynet3/studentmenucontrol/galleryMenuIcon.png";
	private static String FEEDBACK_MENU_ICON  = "synergynet3/studentmenucontrol/feedbackMenuIcon.png";
	
	private int screenshotOptionIndex = -1;
	private int galleryOptionIndex = -1;
	private int feedbackOptionIndex = -1;
	
	private IRadialMenu radialMenu;
	private IStage stage;
	private Logger log;
	private StudentRepresentation student;
	
	private StudentGallery gallery;
	private SynergyNetApp app;
	
	private boolean galleryMode = false;
	private int feedbackMode = 0;
	
	private FeedbackSelect selector;
	private FeedbackItem setter;
	
	public enum DefaultStudentMenuOptions{SCREENSHOT, GALLERY, FEEDBACK} 
	
	public StudentMenu(StudentRepresentation student, IStage stage, Logger log, SynergyNetApp app) {		
		if (log == null){
			this.log = Logger.getLogger(StudentMenu.class.getName());
		}else{
			this.log = log;
		}
		this.stage = stage;
		this.student = student;
		this.app = app;
				
		IItem studentIcon = StudentIconGenerator.generateIcon(stage, 75, 75, 6, true, student.getStudentId());
		try {
			radialMenu = stage.getContentFactory().create(IRadialMenu.class, "menu", UUID.randomUUID());
			radialMenu.setRootItem(studentIcon, stage, log, student.getStudentColour());
			radialMenu.setRelativeLocation(new Vector2f(-stage.getDisplayWidth() - 300,0));
			radialMenu.setRadius(150);
			stage.addItem(radialMenu);
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
		
		for (IItem child: studentIcon.getChildItems()){
			if (child.getName().contains("userImage")){
				child.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
					@Override
					public void cursorClicked(MultiTouchCursorEvent event) {
						onRootItemClickAction();					
					}
				});		
			}
		}
		
		generateScreenshotOption();
		generateGalleryOption();
		generateFeedbackOption();
		radialMenu.toggleOptionVisibility();			
	}
	
	private void generateScreenshotOption(){			
		try {
			ICachableImage screenshotImage = stage.getContentFactory().create(ICachableImage.class, "play", UUID.randomUUID());
			screenshotImage.setImage(SCREENSHOT_MENU_ICON);		
			screenshotImage.setSize(75, 75);	
			
			IItem wrapperOption = createOptionWrapper(stage, screenshotImage, log, 5);
			
			RadialMenuOption option = new RadialMenuOption(wrapperOption){
				@Override
				public void onOptionSelect() {
					radialMenu.toggleOptionVisibility();
					app.createScreenShotItem(radialMenu.getRelativeLocation(), radialMenu.getRelativeRotation());
				}
			};
			screenshotOptionIndex = addOption(option);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}
	}

	private void generateGalleryOption(){
		try {
			ICachableImage galleryImage = stage.getContentFactory().create(ICachableImage.class, "play", UUID.randomUUID());
			galleryImage.setImage(GALLERY_MENU_ICON);		
			galleryImage.setSize(75, 75);	
			
			IItem wrapperOption = createOptionWrapper(stage, galleryImage, log, 5);
			
			RadialMenuOption option = new RadialMenuOption(wrapperOption){
				@Override
				public void onOptionSelect() {					
					radialMenu.toggleOptionVisibility();	
					
					if (gallery == null){
						gallery = student.getGallery();
						gallery.generateGallery(stage);	
					}
						
					gallery.setVisibility(true);
														
					gallery.asItem().setRelativeLocation(radialMenu.getRelativeLocation());
					gallery.asItem().setRelativeRotation(radialMenu.getRelativeRotation());					
					
					galleryMode = true;
										
				}
			};
			galleryOptionIndex = addOption(option);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}
	}
	
	private void generateFeedbackOption(){
		try {
			ICachableImage feedbackImage = stage.getContentFactory().create(ICachableImage.class, "play", UUID.randomUUID());
			feedbackImage.setImage(FEEDBACK_MENU_ICON);		
			feedbackImage.setSize(75, 75);	
			
			IItem wrapperOption = createOptionWrapper(stage, feedbackImage, log, 5);
			
			RadialMenuOption option = new RadialMenuOption(wrapperOption){
				@Override
				public void onOptionSelect() {
					onFeedbackGenerate();
				}
			};
			feedbackOptionIndex = addOption(option);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}
	}
	
	private void onFeedbackGenerate(){
		radialMenu.toggleOptionVisibility();
		feedbackMode = 1;
		selector = FeedbackSystem.createSetter(stage, log, app.getFeedbackTypes(), student, this);
	}
	
	private IItem createOptionWrapper(IStage stage, ICachableImage item, Logger log, int borderWidth) {
		try {			
			
			IContainer wrapperFrame = stage.getContentFactory().create(IContainer.class, "userIconWrap", UUID.randomUUID());
			
			wrapperFrame.addItem(item);
			
			IRoundedBorder frameBorder = stage.getContentFactory().create(IRoundedBorder.class, "border", UUID.randomUUID());		
			frameBorder.setBorderWidth(borderWidth);
			frameBorder.setSize(item.getWidth(), item.getHeight());
			frameBorder.setColor(student.getStudentColour());
			wrapperFrame.addItem(frameBorder);
									
			return wrapperFrame;
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
			return null;
		}
	}
	
	public void onRootItemClickAction(){
		if (!galleryMode){
			if(feedbackMode == 0){
				radialMenu.toggleOptionVisibility();
			}else if(feedbackMode == 1){
				if (selector != null) selector.tidyAway();
				feedbackMode = 0;
			}else if(feedbackMode == 2){
				if (setter != null)setter.tidyAwayFeedbackSetter();
			}
		}else{
			gallery.setVisibility(false);
			galleryMode = false;
		}
	}
	
	private void removeScreenshotOption(){
		removeOption(screenshotOptionIndex);
		screenshotOptionIndex = -1;
	}
	
	private void removeGalleryOption(){
		removeOption(galleryOptionIndex);
		galleryOptionIndex = -1;
	}
	
	private void removeFeedbackOption(){
		removeOption(feedbackOptionIndex);
		feedbackOptionIndex = -1;
	}
	
	public int addOption(RadialMenuOption option){
		return radialMenu.addOption(option);
	}
	
	public void removeOption(int optionIndex){
		radialMenu.removeOption(optionIndex);
	}
	
	public void removeDefaultOption(DefaultStudentMenuOptions defaultOption){
		switch (defaultOption){
			case SCREENSHOT: removeScreenshotOption(); break;
			case GALLERY: removeGalleryOption(); break;
			case FEEDBACK: removeFeedbackOption(); break;
		}
	}
	
	public void setVisibility(boolean b){
		radialMenu.setVisible(b);
	}
	
	public void reAddDefaultOption(DefaultStudentMenuOptions defaultOption){
		switch (defaultOption){
			case SCREENSHOT: generateScreenshotOption(); break;
			case GALLERY: generateGalleryOption(); break;
			case FEEDBACK: generateFeedbackOption(); break;
		}
	}
	
	public IRadialMenu getRadialMenu(){
		return radialMenu;
	}

	public void removeMenu(IStage stage) {
		radialMenu.setOptionVisibility(false);	
		stage.removeItem(radialMenu);		
		if (gallery != null)gallery.removeFrom(stage);
	}
	
	public void turnFeedbackModeOff(){
		feedbackMode = 0;
	}
	
	public void setFeedbackModeSelect(FeedbackItem setter){
		this.setter = setter;
		feedbackMode = 2;
	}
	
}
