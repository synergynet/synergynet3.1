package synergynet3.museum.table.mainapp.userrecorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.museum.table.mainapp.textentry.TextEntryGUI;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.utils.ImageUtils;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class MetricGUI {
	
	private final static int LIMIT = 8;
	
	private final static int TIME_OUT = 45000;
	
	private final static int WIDTH = 420;
	private final static float BOX_HEIGHT = 80;
	
	private static final float BUTTON_WIDTH = 128;		
	private static final float ITEM_HEIGHT = 64;		
	
	private final static float GAP = 10;
	private final static float TEXT_SCALE = 0.5f;	
	
	private final static int SETTINGS_HEIGHT= 60;

	private final static float BORDER_WIDTH = 3f;
	
	private static final ColorRGBA INVIS = new ColorRGBA(0,0,0,0);
	
	private float height;	
	
	private IContainer container;
	
	private boolean open = true;
	private Date lastUpdated = new Date();
	
	private String arrowImage = "";
	
	private int numberOfUsers = 1;
	
	private IItem[][] boxes;
	
	private IItem[] toAddBeforeListener;
	private IItem[] toAddAfterListener;
	
	private int toAddBeforeListenerCount = 0;
	private int toAddAfterListenerCount = 0;
	
	private String[] ageGroups = {"1 - 5", "6 - 12", "13 - 18", "18 - 25", "25 - 40", "40 - 60", "60 - 80", "80+"};
	private String[] genderGroups = {"Male", "Female"};
	
	private int[] ages = new int[LIMIT];
	private int[] genders = new int[LIMIT];
	
	private File originalRecording;
	private String id;
	
	private IColourRectangle background;
	private IRoundedBorder frameBorder;
	private ICachableImage listener;
	private IButtonbox okButton;
	private IButtonbox cancelButton;
	
	private String prompt;
	
	private void resize(){
		height = 200 + (numberOfUsers * BOX_HEIGHT);	
		
		float offset = -((numberOfUsers - 1) * BOX_HEIGHT)/2;
		
		background.setSize(WIDTH, height);
		background.setRelativeLocation(new Vector2f(0, offset));
		frameBorder.setSize(WIDTH, height);
		frameBorder.setRelativeLocation(new Vector2f(0, offset));
		listener.setSize(WIDTH, height);
		listener.setRelativeLocation(new Vector2f(0, offset));
		
		float buttonY = -((-offset) + (height/2)) + okButton.getHeight()/2 + GAP/2;
		
		okButton.setRelativeLocation(new Vector2f(-okButton.getWidth()/2 - GAP/2, buttonY));
		cancelButton.setRelativeLocation(new Vector2f(cancelButton.getWidth()/2 + GAP/2, buttonY));
	}
	 
	public MetricGUI(final IStage stage, final File folder, File originalRecording, String idIn, String promptIn){
		this.originalRecording = originalRecording;
		this.id = idIn;
		this.prompt = promptIn;
		
		height = 200 + (numberOfUsers * BOX_HEIGHT);	
		
		toAddBeforeListener = new IItem[LIMIT * 6];
		toAddAfterListener = new IItem[LIMIT * 4];
		
		try {
			
			Thread timeoutThread = new Thread(new Runnable() {	  
				public void run() {	   
					while (open){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (new Date().getTime() - lastUpdated.getTime() > TIME_OUT){
							close(stage);
						}
					}
				}
			});
			timeoutThread.start();
			
			container = stage.getContentFactory().create(IContainer.class, "container", UUID.randomUUID());	
			arrowImage = getArrowImage(MuseumAppPreferences.getMetricGUIButtonColourAsString());
			
			background = stage.getContentFactory().create(IColourRectangle.class, "recorderContainerBg", UUID.randomUUID());
			background.setSolidBackgroundColour(MuseumAppPreferences.getMetricGUIBackgroundColour());
			background.setSize(WIDTH, height);
	
			frameBorder = stage.getContentFactory().create(IRoundedBorder.class, "containerBorder", UUID.randomUUID());		
			frameBorder.setBorderWidth(BORDER_WIDTH);
			frameBorder.setSize(WIDTH, height);
			frameBorder.setColor(MuseumAppPreferences.getMetricGUIBorderColour());
			
			listener = stage.getContentFactory().create(ICachableImage.class, "listener", UUID.randomUUID());
			listener.setSize(WIDTH + (BORDER_WIDTH * 2), height + (BORDER_WIDTH * 2));
			
			float y = height/2;
						
			ITextbox textLabel = generateTextBox(stage, MuseumAppPreferences.getMetricGUIText(), TEXT_SCALE);
			y -= (GAP + ((textLabel.getHeight() * TEXT_SCALE)/2));
			textLabel.setRelativeLocation(new Vector2f(0,  y));			
			
			y -= (((textLabel.getHeight() * TEXT_SCALE)/2) + GAP + 1.5f);			
			ILine textLine = generateLine(y, 3f, stage);			
			
			ITextbox numberOfParticipantsLabel = generateTextBox(stage, "How many people took part in the recording?", TEXT_SCALE);	
			y -= (1.5f + GAP + ((numberOfParticipantsLabel.getHeight() * TEXT_SCALE)/2));
			numberOfParticipantsLabel.setRelativeLocation(new Vector2f(0, y));
			
			final ITextbox participantNumberLabel = generateTextBox(stage, "" + numberOfUsers, 0.75f);	
			y -= (((numberOfParticipantsLabel.getHeight() * TEXT_SCALE)/2) + GAP);	
			participantNumberLabel.setRelativeLocation(new Vector2f(0, y));
			
			ICachableImage leftButton = stage.getContentFactory().create(ICachableImage.class, "down", UUID.randomUUID());
			leftButton.setImage(arrowImage);		
			leftButton.setSize(SETTINGS_HEIGHT/2, SETTINGS_HEIGHT/2);	
			leftButton.setRelativeLocation(new Vector2f(-SETTINGS_HEIGHT, y));	
			
			final IgnoreDoubleClick clickerLeft = new IgnoreDoubleClick(500){
				@Override
				public void onAction(MultiTouchCursorEvent event) {
					if (numberOfUsers > 1){
						numberOfUsers--;
						participantNumberLabel.getTextLabel().setText("" + numberOfUsers);
						hideBox();
						resize();
					}
					lastUpdated = new Date();
				}
			};
			
			leftButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {					
					clickerLeft.click(event);
				}
			});	
			
			ICachableImage rightButton = stage.getContentFactory().create(ICachableImage.class, "down", UUID.randomUUID());
			rightButton.setImage(arrowImage);		
			rightButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			rightButton.setSize(SETTINGS_HEIGHT/2, SETTINGS_HEIGHT/2);	
			rightButton.setRelativeLocation(new Vector2f(SETTINGS_HEIGHT, y));	
			
			final IgnoreDoubleClick clickerRight= new IgnoreDoubleClick(500){
				@Override
				public void onAction(MultiTouchCursorEvent event) {
					if (numberOfUsers < LIMIT){
						numberOfUsers++;
						participantNumberLabel.getTextLabel().setText("" + numberOfUsers);
						showBox();
						resize();
					}
					lastUpdated = new Date();
				}
			};
			
			rightButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {					
					clickerRight.click(event);
				}
			});	
			
			y -= ((SETTINGS_HEIGHT/4) + GAP + 0.5f);

			generateBoxes(stage, y);
			
			ILine firstLine = generateLine(y, 1f, stage);
			
			okButton = stage.getContentFactory().create(IButtonbox.class, "enabledOkButton", UUID.randomUUID());  
			okButton.setText("Submit", MuseumAppPreferences.getMetricGUIBackgroundColour(), MuseumAppPreferences.getMetricGUIButtonColour(), 
					MuseumAppPreferences.getMetricGUIFontColour(), BUTTON_WIDTH, ITEM_HEIGHT/2, stage);
			okButton.setRelativeLocation(new Vector2f(-okButton.getWidth()/2 - GAP/2, -height/2 + okButton.getHeight()/2 + GAP/2));
			okButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {
					saveMetrics();
					close(stage);
					try {
						new TextEntryGUI(stage, folder, id, prompt);
					} catch (ContentTypeNotBoundException e) {
						e.printStackTrace();
					}
				}
			});
			
			cancelButton = stage.getContentFactory().create(IButtonbox.class, "cancelButton", UUID.randomUUID());
			cancelButton.setText("Cancel", MuseumAppPreferences.getMetricGUIBackgroundColour(), MuseumAppPreferences.getMetricGUIButtonColour(), 
					MuseumAppPreferences.getMetricGUIFontColour(), BUTTON_WIDTH, ITEM_HEIGHT/2, stage);
			cancelButton.setRelativeLocation(new Vector2f(cancelButton.getWidth()/2 + GAP/2, -height/2 + cancelButton.getHeight()/2 + GAP/2));
			cancelButton.setVisible(true);
			cancelButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {
					close(stage);
					try {
						new TextEntryGUI(stage, folder, id, prompt);
					} catch (ContentTypeNotBoundException e) {
						e.printStackTrace();
					}
				}
			});
			
			container.addItem(background);	
			container.addItem(frameBorder);	
			container.addItem(textLabel);
			container.addItem(textLine);
			container.addItem(numberOfParticipantsLabel);					
			container.addItem(participantNumberLabel);		
			container.addItem(firstLine);	
			for (IItem item: toAddBeforeListener){
				container.addItem(item);		
			}
			container.addItem(listener);	
			for (IItem item: toAddAfterListener){
				container.addItem(item);		
			}
			container.addItem(leftButton);
			container.addItem(rightButton);
			container.addItem(okButton);			
			container.addItem(cancelButton);
			
			RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(listener, RotateTranslateScaleBehaviour.class);		
			rts.setItemActingOn(container);
			rts.setScaleEnabled(false);			

			stage.addItem(container);		

			stage.getZOrderManager().bringToTop(container);
			
			container.getZOrderManager().setAutoBringToTop(false);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}		
	}
	
	private void generateBoxes(IStage stage, float y) throws ContentTypeNotBoundException{
		boxes = new IItem[LIMIT][];
		for (int i = 0; i < LIMIT; i++){
			boxes[i] = generateBox(stage, y, i);
			y -= BOX_HEIGHT;
			if (i > 0){
				for (IItem item: boxes[i]){
					item.setVisible(false);
				}
			}
		}
	}		
	
	private IItem[] generateBox(IStage stage, float y, final int i) throws ContentTypeNotBoundException {
		IItem[] toReturn = new IItem[10];		
		int person = i + 1;		
		genders[i] = 0;
		ages[i] = 0;		
		
		ITextbox personLabel = generateTextBox(stage, "Person " + person + ":", 0.75f);
		toReturn[0] = personLabel;
		addBeforeListener(personLabel);	
		
		y -= (0.5f + GAP + ((personLabel.getHeight() * TEXT_SCALE)/2))/2;
		personLabel.setRelativeLocation(new Vector2f(0,  y));	
		
		ITextbox genderPromptLabel = generateTextBox(stage, "Gender:", TEXT_SCALE);
		toReturn[1] = genderPromptLabel;
		addBeforeListener(genderPromptLabel);	
		
		y -= (((genderPromptLabel.getHeight() * TEXT_SCALE)/2) + GAP + ((personLabel.getHeight() * TEXT_SCALE)/2))/2;
		genderPromptLabel.setRelativeLocation(new Vector2f(-WIDTH/4,  y));		
		
		ITextbox agePromptLabel = generateTextBox(stage, "Age:", TEXT_SCALE);
		toReturn[5] = agePromptLabel;
		addBeforeListener(agePromptLabel);			

		agePromptLabel.setRelativeLocation(new Vector2f(WIDTH/4, y));	
		
		final ITextbox genderLabel = generateTextBox(stage, genderGroups[0], TEXT_SCALE);	
		toReturn[3] = genderLabel;
		addBeforeListener(genderLabel);
		
		y -= (((personLabel.getHeight() * TEXT_SCALE)/2) + GAP + ((genderLabel.getHeight() * TEXT_SCALE)/2))/2;
		genderLabel.setRelativeLocation(new Vector2f(-WIDTH/4,  y));		
		
		ICachableImage leftGenderButton = stage.getContentFactory().create(ICachableImage.class, "down", UUID.randomUUID());
		leftGenderButton.setImage(arrowImage);		
		leftGenderButton.setSize(SETTINGS_HEIGHT/2, SETTINGS_HEIGHT/2);	
		leftGenderButton.setRelativeLocation(new Vector2f((-WIDTH/4) - SETTINGS_HEIGHT, y));	
		toReturn[2] = leftGenderButton;
		addAfterListener(leftGenderButton);
		
		ICachableImage rightGenderButton = stage.getContentFactory().create(ICachableImage.class, "down", UUID.randomUUID());
		rightGenderButton.setImage(arrowImage);		
		rightGenderButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
		rightGenderButton.setSize(SETTINGS_HEIGHT/2, SETTINGS_HEIGHT/2);	
		rightGenderButton.setRelativeLocation(new Vector2f((-WIDTH/4) + SETTINGS_HEIGHT, y));
		toReturn[4] = rightGenderButton;
		addAfterListener(rightGenderButton);
		
		final ITextbox ageLabel = generateTextBox(stage, ageGroups[0], TEXT_SCALE);
		ageLabel.setRelativeLocation(new Vector2f(WIDTH/4,  y));	
		toReturn[7] = ageLabel;
		addBeforeListener(ageLabel);
		
		ICachableImage leftAgeButton = stage.getContentFactory().create(ICachableImage.class, "down", UUID.randomUUID());
		leftAgeButton.setImage(arrowImage);		
		leftAgeButton.setSize(SETTINGS_HEIGHT/2, SETTINGS_HEIGHT/2);	
		leftAgeButton.setRelativeLocation(new Vector2f((WIDTH/4) - SETTINGS_HEIGHT, y));	
		toReturn[6] = leftAgeButton;
		addAfterListener(leftAgeButton);
		
		ICachableImage rightAgeButton = stage.getContentFactory().create(ICachableImage.class, "down", UUID.randomUUID());
		rightAgeButton.setImage(arrowImage);		
		rightAgeButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
		rightAgeButton.setSize(SETTINGS_HEIGHT/2, SETTINGS_HEIGHT/2);	
		rightAgeButton.setRelativeLocation(new Vector2f((WIDTH/4) + SETTINGS_HEIGHT, y));
		toReturn[8] = rightAgeButton;
		addAfterListener(rightAgeButton);
		
		y -= (((genderLabel.getHeight() * TEXT_SCALE)/2) + GAP + 0.5f);
		ILine line = generateLine(y, 1f, stage);
		toReturn[9] = line;
		addBeforeListener(line);
		
		final IgnoreDoubleClick clickerGenderLeft = new IgnoreDoubleClick(500){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				if (genders[i] == 0){
					genders[i] = 1;
				}else{
					genders[i] = 0;
				}
				genderLabel.getTextLabel().setText(genderGroups[genders[i]]);
				lastUpdated = new Date();
			}
		};
		
		leftGenderButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {					
				clickerGenderLeft.click(event);
			}
		});	
		
		final IgnoreDoubleClick clickerGenderRight= new IgnoreDoubleClick(500){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				if (genders[i] == 0){
					genders[i] = 1;
				}else{
					genders[i] = 0;
				}
				genderLabel.getTextLabel().setText(genderGroups[genders[i]]);
				lastUpdated = new Date();
			}
		};
		
		rightGenderButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {					
				clickerGenderRight.click(event);
			}
		});	
		
		final IgnoreDoubleClick clickerAgeLeft = new IgnoreDoubleClick(500){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				int ageGroup = ages[i];
				ageGroup--;
				if (ageGroup == -1){
					ageGroup = ageGroups.length - 1;
				}
				ages[i] = ageGroup;
				ageLabel.getTextLabel().setText(ageGroups[ages[i]]);
				lastUpdated = new Date();
			}
		};
		
		leftAgeButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {					
				clickerAgeLeft.click(event);
			}
		});	
		
		final IgnoreDoubleClick clickerAgeRight= new IgnoreDoubleClick(500){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				int ageGroup = ages[i];
				ageGroup++;
				if (ageGroup == ageGroups.length){
					ageGroup = 0;
				}
				ages[i] = ageGroup;
				ageLabel.getTextLabel().setText(ageGroups[ages[i]]);
				lastUpdated = new Date();
			}
		};
		
		rightAgeButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {					
				clickerAgeRight.click(event);
			}
		});	
		
		return toReturn;
	}

	private void addBeforeListener(IItem item){
		toAddBeforeListener[toAddBeforeListenerCount] = item;
		toAddBeforeListenerCount++;
	}
	
	private void addAfterListener(IItem item){
		toAddAfterListener[toAddAfterListenerCount] = item;
		toAddAfterListenerCount++;
	}
	
	private ITextbox generateTextBox(IStage stage, String text, float scale) throws ContentTypeNotBoundException{
		ITextbox participantsCountLabel = stage.getContentFactory().create(ITextbox.class, "textLabel", UUID.randomUUID());
		participantsCountLabel.setColours(INVIS, INVIS, MuseumAppPreferences.getMetricGUIFontColour());
		participantsCountLabel.setWidth((WIDTH - GAP/2) / TEXT_SCALE);
		participantsCountLabel.setHeight(ITEM_HEIGHT);
		participantsCountLabel.setText(text, stage);		
		participantsCountLabel.setRelativeScale(scale);
		participantsCountLabel.setMovable(false);
		return participantsCountLabel;
	}
	
	private ILine generateLine(float y, float width, IStage stage) throws ContentTypeNotBoundException{
		ILine line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID()); 
		line.setLineWidth(width);
		line.setInteractionEnabled(false);
		line.setLineColour(MuseumAppPreferences.getMetricGUIButtonColour());
		line.setStartPosition(new Vector2f(-WIDTH/2, y));
		line.setEndPosition(new Vector2f(WIDTH/2, y));
		return line;
	}
	
	private void showBox(){
		int toShow = numberOfUsers - 1;
		for (IItem item: boxes[toShow]){
			item.setVisible(true);
		}
	}
	
	private void hideBox(){
		int toHide = numberOfUsers;
		for (IItem item: boxes[toHide]){
			item.setVisible(false);
		}
	}
	
	private String getArrowImage(String colour){
		return ImageUtils.getImage(colour, ImageUtils.RESOURCE_DIR + "sliderarrows/", "_slider_arrow.png");
	}
	
	private void close(IStage stage){
		open = false;
		stage.removeItem(container);
	}
	
	private void saveMetrics() {		
		String metricString = "Recording: " + id + ".wav";
		metricString += "\nWorkspace: " + MuseumAppPreferences.getContentFolder();
		metricString += "\n ";
		
		for (int i = 0; i < numberOfUsers; i++){
			int person = i+1;
			metricString += "\nPerson " + person;
			metricString += "\nGender: " + genderGroups[genders[i]];
			metricString += "\nAge: " + ageGroups[ages[i]];
			metricString += "\n ";
		}		
		
		File txtFile = new File(MuseumAppPreferences.getMetricsFolder() + File.separator + id + ".txt");
		
	    BufferedWriter writer = null;
	    try{
	    	writer = new BufferedWriter(new FileWriter(txtFile));
	        writer.write(metricString);
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
		
		try {
			SettingsUtil.copyFile(originalRecording, new File(MuseumAppPreferences.getMetricsFolder() + File.separator + id + ".wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
