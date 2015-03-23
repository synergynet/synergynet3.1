package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.audio.IAudioItem;
import synergynet3.audio.SNAudioController;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;

import com.jme3.math.ColorRGBA;

/**
 * The Class AudioPlayer.
 */
@ImplementsContentItem(target = IAudioPlayer.class)
public class AudioPlayer extends AudioContainer implements IAudioItem,
		IAudioPlayer {

	/** The Constant PLAY_BUTTON_IMAGE. */
	private static final String PLAY_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioPlay.png";

	/** The Constant PLAY_DISABLED_BUTTON_IMAGE. */
	private static final String PLAY_DISABLED_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioPlayDisabled.png";

	/** The Constant STOP_BUTTON_IMAGE. */
	private static final String STOP_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioStop.png";

	/** The audio set. */
	private boolean audioSet;

	/** The background. */
	private IColourRectangle background;

	/** The frame border. */
	private IRoundedBorder frameBorder;

	/** The height. */
	private int height = 120;

	/** The owner. */
	private String owner = "";

	/** The play image. */
	private ICachableImage playImage;

	/** The playing. */
	private boolean playing;

	/** The stage. */
	private IStage stage;

	/** The width. */
	private int width = 120;

	/** The audio control. */
	protected SNAudioController audioControl;

	/** The log. */
	protected Logger log = Logger.getLogger(AudioPlayer.class.getName());

	/**
	 * Instantiates a new audio player.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public AudioPlayer(String name, UUID uuid) {
		super(name, uuid);
		build();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#getOwner()
	 */
	public String getOwner() {
		return this.owner;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	public boolean isPlaying() {
		return playing;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#makeImmovable()
	 */
	@Override
	public void makeImmovable() {
		frameBorder.setInteractionEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IAudioPlayer#makeMovable()
	 */
	public void makeMovable() {
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker()
				.addBehaviour(frameBorder, RotateTranslateScaleBehaviour.class);
		rts.setItemActingOn(this);
		rts.setScaleEnabled(false);

		NetworkFlickBehaviour nf = stage.getBehaviourMaker().addBehaviour(
				frameBorder, NetworkFlickBehaviour.class);
		nf.setItemActingOn(this);
		nf.setMaxDimension(width);
		nf.setDeceleration(100f);

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.audio.IAudioItem#setAudioControlObject(synergynet3.audio.
	 * SNAudioController)
	 */
	@Override
	public void setAudioControlObject(SNAudioController audioControl) {
		this.audioControl = audioControl;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IAudioPlayer#setAudioRecording
	 * (java.io.File)
	 */
	public void setAudioRecording(File recordingFile) {
		audioControl.setAudioSource(recordingFile);
		playImage.setImage(PLAY_BUTTON_IMAGE);
		audioSet = true;
	}

	/**
	 * Sets the audio recording.
	 *
	 * @param recordingFile the recording file
	 * @param audioLocation the audio location
	 */
	public void setAudioRecording(File recordingFile, String audioLocation) {
		audioControl.setAudioSource(recordingFile, audioLocation);
		playImage.setImage(PLAY_BUTTON_IMAGE);
		audioSet = true;
	}

	/**
	 * Sets the audio recording.
	 *
	 * @param fileName the new audio recording
	 */
	public void setAudioRecording(String fileName) {
		audioControl.setAudioSource(MultiplicityClient.assetManager, fileName);
		playImage.setImage(PLAY_BUTTON_IMAGE);
		audioSet = true;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.audio.IAudioItem#setBackgroundColour(com.jme3.math.ColorRGBA)
	 */
	public void setBackgroundColour(ColorRGBA colour) {
		background.setSolidBackgroundColour(colour);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#setOwner(java.lang.String)
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#stopPlay()
	 */
	@Override
	public void stopPlay() {
		playImage.setImage(PLAY_BUTTON_IMAGE);
		playing = false;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#stopRecord(boolean)
	 */
	@Override
	public void stopRecord(boolean success) {
	}

	/**
	 * Builds the.
	 */
	private void build() {
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		try {
			final IContentFactory contentFactory = stage.getContentFactory();

			audioControl = new SNAudioController(log, this);

			setAudioController(audioControl);

			background = contentFactory.create(IColourRectangle.class,
					"recorderbg", UUID.randomUUID());
			background.setSolidBackgroundColour(ColorRGBA.Black);
			background.setSize(width, height);

			frameBorder = contentFactory.create(IRoundedBorder.class, "border",
					UUID.randomUUID());
			frameBorder.setBorderWidth(15f);
			frameBorder.setSize(width, height);
			frameBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));

			playImage = contentFactory.create(ICachableImage.class, "play",
					UUID.randomUUID());
			playImage.setImage(PLAY_DISABLED_BUTTON_IMAGE);
			playImage.setSize(100, 100);

			playImage.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							if (audioSet) {
								if (!playing) {
									playImage.setImage(STOP_BUTTON_IMAGE);
									audioControl.startAudioPlayBack();
									playing = true;
								} else {
									audioControl.stopAudioPlayBack();
								}
							}
						}
					});

			addItem(background);
			addItem(playImage);
			addItem(frameBorder);

			this.getZOrderManager().setAutoBringToTop(false);

		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}
	}

}
