package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.additionalitems.interfaces.IAudioRecorder;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.audio.IAudioItem;
import synergynet3.audio.SNAudioController;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

@ImplementsContentItem(target = IAudioRecorder.class)
public class AudioRecorder extends AudioContainer implements IAudioItem, IAudioRecorder {
	
	private int width = 240;
	private int height = 120;
	
	private static final String PLAY_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioPlay.png";
	private static final String PLAY_DISABLED_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioPlayDisabled.png";
	private static final String STOP_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioStop.png";
	private static final String RECORD_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioRecord.png";
	private static final String DELETE_RECORD_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioDeleteRecording.png";
	private static final String DELETE_RECORD_DISABLE_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioDeleteRecordingDisabled.png";
	
	private IColourRectangle background;
	private ICachableImage playImage;
	private ICachableImage recordImage; 
	private IStage stage;
	private IRoundedBorder frameBorder;
	
	private boolean recorded = false;
	
	protected Logger log = Logger.getLogger(AudioRecorder.class.getName());
	protected SNAudioController audioControl;
	
	private String owner = "";
	private boolean recording;
	private boolean playing;
	
	public AudioRecorder(String name, UUID uuid) {
		super(name, uuid);	
		build();
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public void setBackgroundColour(ColorRGBA colour){
		background.setSolidBackgroundColour(colour);
	}

	private void build(){
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		try{
			final IContentFactory contentFactory = stage.getContentFactory();
			
			audioControl = new SNAudioController(log, this);			
			setAudioController(audioControl);
			
			background = contentFactory.create(IColourRectangle.class, "recorderbg", UUID.randomUUID());
			background.setSolidBackgroundColour(ColorRGBA.Black);
			background.setSize(width, height);
	
			frameBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());		
			frameBorder.setBorderWidth(15f);
			frameBorder.setSize(width, height);
			frameBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));
				
			recordImage = contentFactory.create(ICachableImage.class, "record", UUID.randomUUID());
			recordImage.setImage(RECORD_BUTTON_IMAGE);		
			recordImage.setSize(100, 100);	
			recordImage.setRelativeLocation(new Vector2f(-width/4, 0));
			
			recordImage.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {
					if (!playing){
						if (audioControl.hasAudioBeenSet()){
							audioControl.resetAudioRecording();
							recordImage.setImage(RECORD_BUTTON_IMAGE);
							playImage.setImage(PLAY_DISABLED_BUTTON_IMAGE);
							onCancelRecord();
						}else{
							if (!recording){
								if (!SNAudioController.recordingConcurrently){
									recording = true;
									recordImage.setImage(STOP_BUTTON_IMAGE);
									audioControl.startAudioCapture();	
								}
							}else{	
								audioControl.stopAudioCapture(true);
							}
						}
					}
				}
			});
			
			playImage = contentFactory.create(ICachableImage.class, "play", UUID.randomUUID());
			playImage.setImage(PLAY_DISABLED_BUTTON_IMAGE);		
			playImage.setSize(100, 100);	
			playImage.setRelativeLocation(new Vector2f(width/4, 0));
					
			playImage.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {
					if (audioControl.hasAudioBeenSet()){
						if (!playing){
							playing = true;
							playImage.setImage(STOP_BUTTON_IMAGE);
							recordImage.setImage(DELETE_RECORD_DISABLE_BUTTON_IMAGE);
							audioControl.startAudioPlayBack();
						} else{
							audioControl.stopAudioPlayBack();
						}
					}	
				}
			});
			
			addItem(background);
			addItem(recordImage);
			addItem(playImage);
			addItem(frameBorder);								

			this.getZOrderManager().setAutoBringToTop(false);

		}catch(ContentTypeNotBoundException e){
			log.log(Level.SEVERE, "Content Not Bound: ", e );
		}
	}
	
	
	public void makeMovable(){
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(frameBorder, RotateTranslateScaleBehaviour.class);		
		rts.setItemActingOn(this);
		rts.setScaleEnabled(false);		
	}
	
	public boolean isRecording(){
		return recording;
	}
	
	public boolean isRecorded(){
		return audioControl.hasAudioBeenSet();
	}
	
	public boolean isPlaying(){
		return playing;
	}

	@Override
	public void setAudioControlObject(SNAudioController audioControl) {
		this.audioControl = audioControl;		
	}


	@Override 
	public void stopPlay() {
		playing = false;
		playImage.setImage(PLAY_BUTTON_IMAGE);
		recordImage.setImage(DELETE_RECORD_BUTTON_IMAGE);		
	}

	@Override
	public void stopRecord(boolean success) {
		recording = false;
		if (success){
			recordImage.setImage(DELETE_RECORD_BUTTON_IMAGE);
			playImage.setImage(PLAY_BUTTON_IMAGE);
			onStopRecord();
		}else{
			recordImage.setImage(RECORD_BUTTON_IMAGE);
			onCancelRecord();
		}
		
	}
	
	public void onStopRecord(){
		recorded = true;
	}
	
	public void onCancelRecord() {
		recorded = false;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void makeImmovable(){
		frameBorder.setInteractionEnabled(false);
	}
	
	public void removeBorder(){
		removeItem(frameBorder);
	}
	
	public File getAudioFile(){
		return audioControl.getAudioFile();
	}

	@Override
	public boolean hasRecorded() {
		return recorded;
	}

}
