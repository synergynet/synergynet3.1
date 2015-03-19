package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;

import multiplicity3.appsystem.MultiplicityClient;
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
import synergynet3.additionalitems.interfaces.IAudioPlayer;
import synergynet3.audio.IAudioItem;
import synergynet3.audio.SNAudioController;

import com.jme3.math.ColorRGBA;

@ImplementsContentItem(target = IAudioPlayer.class)
public class AudioPlayer extends AudioContainer implements IAudioItem, IAudioPlayer {
	
	private int width = 120;
	private int height = 120;
	
	private static final String PLAY_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioPlay.png";
	private static final String PLAY_DISABLED_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioPlayDisabled.png";
	private static final String STOP_BUTTON_IMAGE  = "synergynet3/additionalitems/audio/audioStop.png";
	
	private IColourRectangle background;
	private IStage stage;
	private ICachableImage playImage;
	private IRoundedBorder frameBorder;
		
	protected Logger log = Logger.getLogger(AudioPlayer.class.getName());
	protected SNAudioController audioControl;

	private String owner = "";
	private boolean playing;
	private boolean audioSet;

	public AudioPlayer(String name, UUID uuid) {
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
			
			playImage = contentFactory.create(ICachableImage.class, "play", UUID.randomUUID());
			playImage.setImage(PLAY_DISABLED_BUTTON_IMAGE);		
			playImage.setSize(100, 100);		
			
			playImage.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {
					if (audioSet){
						if (!playing){
							playImage.setImage(STOP_BUTTON_IMAGE);
							audioControl.startAudioPlayBack();
							playing = true;
						}else{	
							audioControl.stopAudioPlayBack();
						}
					}
				}
			});
			
			addItem(background);
			addItem(playImage);	
			addItem(frameBorder);				

			this.getZOrderManager().setAutoBringToTop(false);
			
		}catch(ContentTypeNotBoundException e){
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}
	}
	
	public void makeMovable(){
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker().addBehaviour(frameBorder, RotateTranslateScaleBehaviour.class);		
		rts.setItemActingOn(this);
		rts.setScaleEnabled(false);		
		
		NetworkFlickBehaviour nf = stage.getBehaviourMaker().addBehaviour(frameBorder, NetworkFlickBehaviour.class);
		nf.setItemActingOn(this);
		nf.setMaxDimension(width);
		nf.setDeceleration(100f);	
		
	}
	
	public void setAudioRecording(String fileName) {			
		audioControl.setAudioSource(MultiplicityClient.assetManager, fileName);
		playImage.setImage(PLAY_BUTTON_IMAGE);		
		audioSet = true;		
	}
	
	public void setAudioRecording(File recordingFile){	
		audioControl.setAudioSource(recordingFile);
		playImage.setImage(PLAY_BUTTON_IMAGE);		
		audioSet = true;		
	}
	
	public void setAudioRecording(File recordingFile, String audioLocation) {
		audioControl.setAudioSource(recordingFile, audioLocation);
		playImage.setImage(PLAY_BUTTON_IMAGE);		
		audioSet = true;		
	}
	
	public boolean isPlaying(){
		return playing;
	}

	@Override 
	public void stopPlay() {
		playImage.setImage(PLAY_BUTTON_IMAGE);	
		playing = false;		
	}

	@Override
	public void setAudioControlObject(SNAudioController audioControl) {
		this.audioControl = audioControl;		
	}

	@Override
	public void stopRecord(boolean success) {}

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
	

}
