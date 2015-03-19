package synergynet3.museum.table.mainapp.userrecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

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

import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.audio.IAudioItem;
import synergynet3.audio.SNAudioController;
import synergynet3.museum.table.mainapp.EntityManager;
import synergynet3.museum.table.mainapp.textentry.TextEntryGUI;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;

public class UserRecorder implements IAudioItem{
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH-mm-ss");
	
	private final static int WIDTH= 380;
	private final static int HEIGHT= 300;
	
	public static final float BUTTON_WIDTH = 128;
	public static final float BUTTON_HEIGHT = 128;
	
	private final static float BUTTON_GAP = 30;
	private final static float TEXT_SCALE = 0.5f;	

	private final static float BORDER_WIDTH = 3f;
	
	private static final ColorRGBA INVIS = new ColorRGBA(0,0,0,0);
	
	private IStage stage;
	
	private IContainer container;
	private ILine line;
	private EntityManager entityManager;
	private File folder;
	private UserRecordingPromptLabel userRecordingPromptLabel;
	
	private String name;
	
	private Logger log = Logger.getLogger(UserRecorder.class.getName());
	
	private SNAudioController audioController;
	
	private RecorderButton enabledRecordStartButton;
	private RecorderButton disabledRecordStartButton;	
	private RecorderButton enabledRecordStopButton;
	private RecorderButton disabledRecordStopButton;
	private RecorderButton enabledPlaybackStartButton;
	private RecorderButton disabledPlaybackStartButton;	
	private RecorderButton enabledPlaybackStopButton;
	private RecorderButton disabledPlaybackStopButton;
	
	private IButtonbox enabledOkButton;
	private IButtonbox disabledOkButton;	
	
	private boolean recording = false;
	private boolean playing = false;

	private String prompt = "What are your thoughts?";
	
	public UserRecorder(String prompt, IStage stage, File folder, UserRecordingPromptLabel userRecordingPromptLabel, ITextbox entityItem, EntityManager entityManager) throws ContentTypeNotBoundException{
		this.entityManager = entityManager;
		this.folder = folder;
		this.userRecordingPromptLabel = userRecordingPromptLabel;
		this.stage = stage;
		this.name = entityItem.getTextLabel().getText();
		this.prompt = prompt;
		container = stage.getContentFactory().create(IContainer.class, "container", UUID.randomUUID());
		
		audioController = new SNAudioController(log, this);
		audioController.setMaxRecordingTime(MuseumAppPreferences.getMaxRecordingTime());
		
		IColourRectangle background = stage.getContentFactory().create(IColourRectangle.class, "recorderContainerBg", UUID.randomUUID());
		background.setSolidBackgroundColour(MuseumAppPreferences.getRecorderBackgroundColour());
		background.setSize(WIDTH, HEIGHT);

		IRoundedBorder frameBorder = stage.getContentFactory().create(IRoundedBorder.class, "containerBorder", UUID.randomUUID());		
		frameBorder.setBorderWidth(BORDER_WIDTH);
		frameBorder.setSize(WIDTH, HEIGHT);
		frameBorder.setColor(MuseumAppPreferences.getEntityBorderColour());
		
		enabledRecordStartButton = new RecorderButton(stage, "enabledRecordStartButton", "Start Recording", true);
		enabledRecordStartButton.getButtonContainer().setRelativeLocation(new Vector2f(-BUTTON_WIDTH/2 - BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		enabledRecordStartButton.getButtonContainer().setVisible(false);
		
		final IgnoreDoubleClick clickerRecordStart = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				audioController.startAudioCapture();
				recordingButtonsSet();
			}
		};
		
		enabledRecordStartButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				clickerRecordStart.click(event);
			}
		});
		
		disabledRecordStartButton = new RecorderButton(stage, "disabledRecordStartButton", "Start Recording", false);	
		disabledRecordStartButton.getButtonContainer().setRelativeLocation(new Vector2f(-BUTTON_WIDTH/2 - BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		disabledRecordStartButton.getButtonContainer().setVisible(false);
		
		enabledRecordStopButton = new RecorderButton(stage, "enabledRecordStopButton", "Stop Recording", true);
		enabledRecordStopButton.getButtonContainer().setRelativeLocation(new Vector2f(BUTTON_WIDTH/2 + BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		enabledRecordStopButton.getButtonContainer().setVisible(false);
		
		final IgnoreDoubleClick clickerRecordStop = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				audioController.stopAudioCapture(true);
				playbackReadyButtonsSet();
			}
		};
		
		enabledRecordStopButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				clickerRecordStop.click(event);
			}
		});
		
		disabledRecordStopButton = new RecorderButton(stage, "disabledRecordStopButton", "Stop Recording", false);
		disabledRecordStopButton.getButtonContainer().setRelativeLocation(new Vector2f(BUTTON_WIDTH/2 + BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		disabledRecordStopButton.getButtonContainer().setVisible(false);
		
		enabledPlaybackStartButton = new RecorderButton(stage, "enabledPlaybackStartButton", "Start Playback", true);
		enabledPlaybackStartButton.getButtonContainer().setRelativeLocation(new Vector2f(-BUTTON_WIDTH/2 - BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		enabledPlaybackStartButton.getButtonContainer().setVisible(false);
		
		final IgnoreDoubleClick clickerPlaybackStart = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				audioController.startAudioPlayBack();
				playingButtonsSet();
			}
		};
		
		enabledPlaybackStartButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				clickerPlaybackStart.click(event);
			}
		});
		
		disabledPlaybackStartButton = new RecorderButton(stage, "disabledPlaybackStartButton", "Start Playback", false);
		disabledPlaybackStartButton.getButtonContainer().setRelativeLocation(new Vector2f(-BUTTON_WIDTH/2 - BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		disabledPlaybackStartButton.getButtonContainer().setVisible(false);
			
		enabledPlaybackStopButton = new RecorderButton(stage, "enabledRecordStopButton", "Stop Playback", true);
		enabledPlaybackStopButton.getButtonContainer().setRelativeLocation(new Vector2f(BUTTON_WIDTH/2 + BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		enabledPlaybackStopButton.getButtonContainer().setVisible(false);
		
		final IgnoreDoubleClick clickerPlaybackStop = new IgnoreDoubleClick(){
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				try{
					audioController.stopAudioPlayBack();
				}catch(Exception e){}
				playbackReadyButtonsSet();
			}
		};
		
		enabledPlaybackStopButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				clickerPlaybackStop.click(event);
			}
		});
		
		disabledPlaybackStopButton = new RecorderButton(stage, "disabledRecordStopButton", "Stop Playback", false);
		disabledPlaybackStopButton.getButtonContainer().setRelativeLocation(new Vector2f(BUTTON_WIDTH/2 + BUTTON_GAP/2, HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_GAP/2));
		disabledPlaybackStopButton.getButtonContainer().setVisible(false);	
		
		line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID()); 
		line.setLineWidth(6f);
		line.setInteractionEnabled(false);
		line.setLineColour(MuseumAppPreferences.getEntityBorderColour());
		line.setSourceItem(entityItem);
		line.setDestinationItem(container);		
		line.setStartPosition(entityItem.getRelativeLocation());
		line.setEndPosition(container.getRelativeLocation());
		stage.addItem(line);
		
		enabledOkButton = stage.getContentFactory().create(IButtonbox.class, "enabledOkButton", UUID.randomUUID());  
		enabledOkButton.setText("Submit", MuseumAppPreferences.getRecorderBackgroundColour(), MuseumAppPreferences.getRecorderActiveButtonBorderColour(), 
				MuseumAppPreferences.getRecorderActiveButtonFontColour(), BUTTON_WIDTH, BUTTON_HEIGHT/2, stage);
		enabledOkButton.setRelativeLocation(new Vector2f(-enabledOkButton.getWidth()/2 - BUTTON_GAP/2, -HEIGHT/2 + enabledOkButton.getHeight()/2 + BUTTON_GAP/2));
		enabledOkButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				saveRecording();
			}
		});
		enabledOkButton.setVisible(false);
		
		disabledOkButton = stage.getContentFactory().create(IButtonbox.class, "disabledOkButton", UUID.randomUUID());  
		disabledOkButton.setText("Submit", MuseumAppPreferences.getRecorderBackgroundColour(), MuseumAppPreferences.getRecorderInactiveButtonBorderColour(), 
				MuseumAppPreferences.getRecorderInactiveButtonFontColour(), BUTTON_WIDTH, BUTTON_HEIGHT/2, stage);
		disabledOkButton.setRelativeLocation(new Vector2f(-disabledOkButton.getWidth()/2 - BUTTON_GAP/2, -HEIGHT/2 + disabledOkButton.getHeight()/2 + BUTTON_GAP/2));
		disabledOkButton.setVisible(false);
		
		IButtonbox cancelButton = stage.getContentFactory().create(IButtonbox.class, "cancelButton", UUID.randomUUID());
		cancelButton.setText("Cancel", MuseumAppPreferences.getRecorderBackgroundColour(), MuseumAppPreferences.getRecorderActiveButtonBorderColour(),
				MuseumAppPreferences.getRecorderActiveButtonFontColour(), BUTTON_WIDTH, BUTTON_HEIGHT/2, stage);
		cancelButton.setRelativeLocation(new Vector2f(cancelButton.getWidth()/2 + BUTTON_GAP/2, -HEIGHT/2 + cancelButton.getHeight()/2 + BUTTON_GAP/2));
		cancelButton.setVisible(true);
		cancelButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				close();
			}
		});
		
		float remainingHeight = HEIGHT - ((BUTTON_GAP/2) * 4) - (BUTTON_HEIGHT * 1.5f);
		
		ITextbox textLabel = stage.getContentFactory().create(ITextbox.class, "textLabel", UUID.randomUUID());
		textLabel.setColours(INVIS, INVIS, MuseumAppPreferences.getRecorderFontColour());
		textLabel.setWidth(WIDTH / TEXT_SCALE);
		textLabel.setHeight(remainingHeight);
		textLabel.setText(MuseumAppPreferences.getRecorderText(), stage);		
		textLabel.setRelativeScale(TEXT_SCALE);
		textLabel.setRelativeLocation(new Vector2f(0, -HEIGHT/2 + enabledOkButton.getHeight() + BUTTON_GAP + remainingHeight/2));
		textLabel.setMovable(false);
		
		ICachableImage listener = stage.getContentFactory().create(ICachableImage.class, "listener", UUID.randomUUID());
		listener.setSize(WIDTH + (BORDER_WIDTH * 2), HEIGHT + (BORDER_WIDTH * 2));

		container.addItem(background);
		container.addItem(frameBorder);
		container.addItem(textLabel);	
		container.addItem(listener);
		container.addItem(enabledRecordStartButton.getButtonContainer());
		container.addItem(disabledRecordStartButton.getButtonContainer());
		container.addItem(enabledRecordStopButton.getButtonContainer());
		container.addItem(disabledRecordStopButton.getButtonContainer());
		container.addItem(enabledPlaybackStartButton.getButtonContainer());
		container.addItem(disabledPlaybackStartButton.getButtonContainer());
		container.addItem(enabledPlaybackStopButton.getButtonContainer());
		container.addItem(disabledPlaybackStopButton.getButtonContainer());	
		container.addItem(enabledOkButton);
		container.addItem(disabledOkButton);
		container.addItem(cancelButton);
		
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(listener, RotateTranslateScaleBehaviour.class);		
		rts.setItemActingOn(container);
		rts.setScaleEnabled(false);			

		stage.addItem(container);		

		stage.getZOrderManager().bringToTop(entityItem);
		stage.getZOrderManager().bringToTop(container);
		
		container.getZOrderManager().setAutoBringToTop(false);
		line.getZOrderManager().setAutoBringToTop(false);
		
		line.getZOrderManager().setItemZOrder(background.getZOrder()-1);
		
		initialButtonsSet();
	}
	
	private void saveRecording(){
		File recordingsFolder = new File(folder.getAbsolutePath() + File.separator + EntityManager.RECORDINGS);
		if (!recordingsFolder.isDirectory()){
			recordingsFolder.mkdir();
		}
		File approvedRecordingsFolder = new File(recordingsFolder.getAbsolutePath() + File.separator + EntityManager.APPROVED);
		if (!approvedRecordingsFolder.isDirectory()){
			approvedRecordingsFolder.mkdir();
		}
		
		String id = generateName();
		File userGeneratedFile = new File(recordingsFolder.getAbsolutePath() + File.separator + id + ".wav");
		
		try {
			SettingsUtil.copyFile(audioController.getAudioFile(), userGeneratedFile);
			audioController.getAudioFile().deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		close();
		
		if (MuseumAppPreferences.areMetricsEnabled()){
			new MetricGUI(stage, new File(recordingsFolder.getAbsolutePath()), userGeneratedFile, id, prompt);
		}else{
			try {
				new TextEntryGUI(stage, new File(recordingsFolder.getAbsolutePath()), id, prompt);
			} catch (ContentTypeNotBoundException e) {
				e.printStackTrace();
			}			
		}
	}
	
	private String generateName(){
		Date date = new Date();
		return DATE_FORMAT.format(date) + " " + name + " " + TIME_FORMAT.format(date);
	}
	
	private void initialButtonsSet(){
		enabledRecordStartButton.getButtonContainer().setVisible(true);
		disabledRecordStartButton.getButtonContainer().setVisible(false);	
		enabledRecordStopButton.getButtonContainer().setVisible(false);
		disabledRecordStopButton.getButtonContainer().setVisible(true);
		enabledPlaybackStartButton.getButtonContainer().setVisible(false);
		disabledPlaybackStartButton.getButtonContainer().setVisible(false);	
		enabledPlaybackStopButton.getButtonContainer().setVisible(false);
		disabledPlaybackStopButton.getButtonContainer().setVisible(false);
		enabledOkButton.setVisible(false);
		disabledOkButton.setVisible(true);
		recording = false;
		playing = false;
	}
	
	private void recordingButtonsSet(){
		enabledRecordStartButton.getButtonContainer().setVisible(false);
		disabledRecordStartButton.getButtonContainer().setVisible(true);	
		enabledRecordStopButton.getButtonContainer().setVisible(true);
		disabledRecordStopButton.getButtonContainer().setVisible(false);
		enabledPlaybackStartButton.getButtonContainer().setVisible(false);
		disabledPlaybackStartButton.getButtonContainer().setVisible(false);	
		enabledPlaybackStopButton.getButtonContainer().setVisible(false);
		disabledPlaybackStopButton.getButtonContainer().setVisible(false);
		enabledOkButton.setVisible(false);
		disabledOkButton.setVisible(true);
		recording = true;
		playing = false;
	}
	
	private void playbackReadyButtonsSet(){
		enabledRecordStartButton.getButtonContainer().setVisible(false);
		disabledRecordStartButton.getButtonContainer().setVisible(false);	
		enabledRecordStopButton.getButtonContainer().setVisible(false);
		disabledRecordStopButton.getButtonContainer().setVisible(false);
		enabledPlaybackStartButton.getButtonContainer().setVisible(true);
		disabledPlaybackStartButton.getButtonContainer().setVisible(false);	
		enabledPlaybackStopButton.getButtonContainer().setVisible(false);
		disabledPlaybackStopButton.getButtonContainer().setVisible(true);
		enabledOkButton.setVisible(true);
		disabledOkButton.setVisible(false);
		recording = false;
		playing = false;
	}
	
	private void playingButtonsSet(){
		enabledRecordStartButton.getButtonContainer().setVisible(false);
		disabledRecordStartButton.getButtonContainer().setVisible(false);	
		enabledRecordStopButton.getButtonContainer().setVisible(false);
		disabledRecordStopButton.getButtonContainer().setVisible(false);
		enabledPlaybackStartButton.getButtonContainer().setVisible(false);
		disabledPlaybackStartButton.getButtonContainer().setVisible(true);	
		enabledPlaybackStopButton.getButtonContainer().setVisible(true);
		disabledPlaybackStopButton.getButtonContainer().setVisible(false);
		enabledOkButton.setVisible(true);
		disabledOkButton.setVisible(false);
		recording = false;
		playing = true;
	}
	
	public void close(){
		
		try{
			if (recording){
				audioController.stopAudioCapture(false);
			}else if (playing){
				audioController.stopAudioPlayBack();
			}
		}catch(Exception e){}
		
		container.setVisible(false);
		line.setVisible(false);	
		entityManager.setUserRecordingPromptLabelsVisibility(true);
		userRecordingPromptLabel.onRecorderClose();
	}
	
	public IItem asItem(){
		return container;
	}

	@Override
	public void setOwner(String owner) {}

	@Override
	public String getOwner() {
		return "";
	}

	@Override
	public void setBackgroundColour(ColorRGBA colour) {}

	@Override
	public void stopPlay() {
		playbackReadyButtonsSet();		
	}

	@Override
	public void setAudioControlObject(SNAudioController audioController) {
		this.audioController = audioController;		
	}

	@Override
	public void stopRecord(boolean success) {
		playbackReadyButtonsSet();		
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public void makeImmovable() {}
	
}
