package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.additionalitems.interfaces.IAudioContainer;
import synergynet3.additionalitems.interfaces.IAudioPlayer;
import synergynet3.audio.SNAudioController;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;

/**
 * The Class AudioContainer.
 */
@ImplementsContentItem(target = IAudioContainer.class)
public class AudioContainer extends JMEContainer implements IAudioContainer,
		IInitable, IItemCachable {

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_AUDIOWRAPPER";

	/** The audio controller. */
	private SNAudioController audioController;

	/** The cached. */
	private String cached = "";

	/**
	 * Instantiates a new audio container.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public AudioContainer(String name, UUID uuid) {
		super(name, uuid);
	}

	/**
	 * Reconstruct.
	 *
	 * @param galleryItem the gallery item
	 * @param stage the stage
	 * @param studentID the student id
	 * @return the audio player
	 */
	public static AudioPlayer reconstruct(
			GalleryItemDatabaseFormat galleryItem, IStage stage,
			String studentID) {
		AudioPlayer audioPlayItem = null;
		try {
			audioPlayItem = stage.getContentFactory().create(
					IAudioPlayer.class, "wrapper", UUID.randomUUID());
			String audioLocation = CacheOrganisation.getSpecificDir(studentID)
					+ File.separator + (String) galleryItem.getValues().get(0);
			audioPlayItem.setAudioRecording(new File(audioLocation),
					audioLocation);
			audioPlayItem.setCached(studentID);
		} catch (ContentTypeNotBoundException e) {
		}
		return audioPlayItem;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.cachecontrol.IItemCachable#deconstruct(java.lang.String)
	 */
	@Override
	public GalleryItemDatabaseFormat deconstruct(String loc) {
		GalleryItemDatabaseFormat galleryItem = new GalleryItemDatabaseFormat();
		galleryItem.setType(CACHABLE_TYPE);
		if (audioController == null) {
			return null;
		}
		if (audioController.getAudioFile() == null) {
			return null;
		}
		if (!audioController.getAudioFile().isFile()) {
			return null;
		}
		if (!cached.equalsIgnoreCase(loc)) {
			ItemCaching.cacheFile(audioController.getAudioFile(), loc);
		}
		galleryItem.addValue(audioController.getAudioFile().getName());
		return galleryItem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IAudioContainer#getAudioController
	 * ()
	 */
	public SNAudioController getAudioController() {
		return audioController;
	}

	/**
	 * @return the cached
	 */
	public String isCached() {
		return cached;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IAudioContainer#setAudioController
	 * (synergynet3.audio.SNAudioController)
	 */
	public void setAudioController(SNAudioController information) {
		this.audioController = information;
	}

	/**
	 * @param cached the cached to set
	 */
	public void setCached(String cached) {
		this.cached = cached;
	}

}
