package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import synergynet3.additionalitems.interfaces.IAudioRecorder;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.audio.IAudioItem;
import synergynet3.audio.SNAudioController;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class AudioRecorder.
 */
@ImplementsContentItem(target = IAudioRecorder.class)
public class AudioRecorder extends AudioContainer implements IAudioItem,
		IAudioRecorder {

	/** The Constant DELETE_RECORD_BUTTON_IMAGE. */
	private static final String DELETE_RECORD_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioDeleteRecording.png";

	/** The Constant DELETE_RECORD_DISABLE_BUTTON_IMAGE. */
	private static final String DELETE_RECORD_DISABLE_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioDeleteRecordingDisabled.png";

	/** The Constant PLAY_BUTTON_IMAGE. */
	private static final String PLAY_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioPlay.png";

	/** The Constant PLAY_DISABLED_BUTTON_IMAGE. */
	private static final String PLAY_DISABLED_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioPlayDisabled.png";

	/** The Constant RECORD_BUTTON_IMAGE. */
	private static final String RECORD_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioRecord.png";

	/** The Constant STOP_BUTTON_IMAGE. */
	private static final String STOP_BUTTON_IMAGE = "synergynet3/additionalitems/audio/audioStop.png";

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

	/** The recorded. */
	private boolean recorded = false;

	/** The record image. */
	private ICachableImage recordImage;

	/** The recording. */
	private boolean recording;

	/** The stage. */
	private IStage stage;

	/** The width. */
	private int width = 240;

	/** The audio control. */
	protected SNAudioController audioControl;

	/** The log. */
	protected Logger log = Logger.getLogger(AudioRecorder.class.getName());

	/**
	 * Instantiates a new audio recorder.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public AudioRecorder(String name, UUID uuid) {
		super(name, uuid);
		build();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IAudioRecorder#getAudioFile()
	 */
	public File getAudioFile() {
		return audioControl.getAudioFile();
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

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IAudioRecorder#hasRecorded()
	 */
	@Override
	public boolean hasRecorded() {
		return recorded;
	}

	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Checks if is recorded.
	 *
	 * @return true, if is recorded
	 */
	public boolean isRecorded() {
		return audioControl.hasAudioBeenSet();
	}

	/**
	 * Checks if is recording.
	 *
	 * @return true, if is recording
	 */
	public boolean isRecording() {
		return recording;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#makeImmovable()
	 */
	@Override
	public void makeImmovable() {
		frameBorder.setInteractionEnabled(false);
	}

	/**
	 * Make movable.
	 */
	public void makeMovable() {
		RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker()
				.addBehaviour(frameBorder, RotateTranslateScaleBehaviour.class);
		rts.setItemActingOn(this);
		rts.setScaleEnabled(false);
	}

	/**
	 * On cancel record.
	 */
	public void onCancelRecord() {
		recorded = false;
	}

	/**
	 * On stop record.
	 */
	public void onStopRecord() {
		recorded = true;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IAudioRecorder#removeBorder()
	 */
	public void removeBorder() {
		removeItem(frameBorder);
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
		playing = false;
		playImage.setImage(PLAY_BUTTON_IMAGE);
		recordImage.setImage(DELETE_RECORD_BUTTON_IMAGE);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.audio.IAudioItem#stopRecord(boolean)
	 */
	@Override
	public void stopRecord(boolean success) {
		recording = false;
		if (success) {
			recordImage.setImage(DELETE_RECORD_BUTTON_IMAGE);
			playImage.setImage(PLAY_BUTTON_IMAGE);
			onStopRecord();
		} else {
			recordImage.setImage(RECORD_BUTTON_IMAGE);
			onCancelRecord();
		}

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

			recordImage = contentFactory.create(ICachableImage.class, "record",
					UUID.randomUUID());
			recordImage.setImage(RECORD_BUTTON_IMAGE);
			recordImage.setSize(100, 100);
			recordImage.setRelativeLocation(new Vector2f(-width / 4, 0));

			recordImage.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							if (!playing) {
								if (audioControl.hasAudioBeenSet()) {
									audioControl.resetAudioRecording();
									recordImage.setImage(RECORD_BUTTON_IMAGE);
									playImage
											.setImage(PLAY_DISABLED_BUTTON_IMAGE);
									onCancelRecord();
								} else {
									if (!recording) {
										if (!SNAudioController.recordingConcurrently) {
											recording = true;
											recordImage
													.setImage(STOP_BUTTON_IMAGE);
											audioControl.startAudioCapture();
										}
									} else {
										audioControl.stopAudioCapture(true);
									}
								}
							}
						}
					});

			playImage = contentFactory.create(ICachableImage.class, "play",
					UUID.randomUUID());
			playImage.setImage(PLAY_DISABLED_BUTTON_IMAGE);
			playImage.setSize(100, 100);
			playImage.setRelativeLocation(new Vector2f(width / 4, 0));

			playImage.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							if (audioControl.hasAudioBeenSet()) {
								if (!playing) {
									playing = true;
									playImage.setImage(STOP_BUTTON_IMAGE);
									recordImage
											.setImage(DELETE_RECORD_DISABLE_BUTTON_IMAGE);
									audioControl.startAudioPlayBack();
								} else {
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

		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "Content Not Bound: ", e);
		}
	}

}
