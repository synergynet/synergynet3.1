package synergynet3.feedbacksystem.defaultfeedbacktypes;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import synergynet3.additionalitems.interfaces.IAudioPlayer;
import synergynet3.additionalitems.interfaces.IAudioRecorder;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackViewer;

/**
 * The Class AudioFeedback.
 */
public class AudioFeedback extends FeedbackItem {

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_FEEDBACK_AUDIO";

	/** The audio recorder. */
	private IAudioRecorder audioRecorder;

	/** The cached. */
	private String cached = "";

	/** The recording. */
	private File recording;

	/**
	 * Reconstruct.
	 *
	 * @param feedbackItem the feedback item
	 * @return the audio feedback
	 */
	public static AudioFeedback reconstruct(Object[] feedbackItem) {
		AudioFeedback feedback = new AudioFeedback();
		String studentID = (String) feedbackItem[1];
		feedback.setStudentID(studentID);
		feedback.setRecordingLocation(CacheOrganisation
				.getSpecificDir(studentID)
				+ File.separatorChar
				+ (String) feedbackItem[2]);
		feedback.setCached(studentID);
		return feedback;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.cachecontrol.IFeedbackItemCachable#deconstruct(java.lang.
	 * String)
	 */
	@Override
	public Object[] deconstruct(String studentIDin) {
		Object[] feedbackItem = new Object[3];
		feedbackItem[0] = CACHABLE_TYPE;
		feedbackItem[1] = studentID;
		if (recording == null) {
			return null;
		}
		if (!recording.isFile()) {
			return null;
		}
		if (!cached.equalsIgnoreCase(studentIDin)) {
			ItemCaching.cacheFile(recording, studentIDin);
		}
		feedbackItem[2] = recording.getName();
		return feedbackItem;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.feedbacksystem.FeedbackItem#getIcon()
	 */
	@Override
	public String getIcon() {
		return "synergynet3/feedbacksystem/defaultfeedbacktypes/audioFeedback.png";
	}

	/**
	 * @return the cached
	 */
	public String isCached() {
		return cached;
	}

	/**
	 * @param cached the cached to set
	 */
	public void setCached(String cached) {
		this.cached = cached;
	}

	/**
	 * Sets the recording location.
	 *
	 * @param value the new recording location
	 */
	public void setRecordingLocation(String value) {
		recording = new File(value);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.feedbacksystem.FeedbackItem#addSettings()
	 */
	@Override
	protected void addSettings() throws ContentTypeNotBoundException {
		audioRecorder = getStage().getContentFactory().create(
				IAudioRecorder.class, "bg", UUID.randomUUID());
		audioRecorder.makeImmovable();
		setter.addToFrame(audioRecorder, 0, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.feedbacksystem.FeedbackItem#generateFeedbackView(synergynet3
	 * .feedbacksystem.FeedbackViewer, int)
	 */
	@Override
	protected void generateFeedbackView(FeedbackViewer feedbackViewer,
			int frameNo) throws ContentTypeNotBoundException {

		if (recording == null) {
			recording = audioRecorder.getAudioFile();
		}

		IAudioPlayer audioPlayer = getStage().getContentFactory().create(
				IAudioPlayer.class, "bg", UUID.randomUUID());
		audioPlayer.setVisible(false);
		audioPlayer.setAudioRecording(recording);

		feedbackViewer.addToFeedbackFrame(audioPlayer, frameNo, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.feedbacksystem.FeedbackItem#getAllSettingsMade()
	 */
	@Override
	protected boolean getAllSettingsMade() {
		return audioRecorder.hasRecorded();
	}

}
