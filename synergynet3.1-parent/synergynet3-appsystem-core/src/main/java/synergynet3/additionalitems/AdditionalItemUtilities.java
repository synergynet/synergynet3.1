package synergynet3.additionalitems;

import synergynet3.additionalitems.interfaces.*;
import synergynet3.additionalitems.jme.*;
import multiplicity3.csys.factory.ContentTypeAlreadyBoundException;
import multiplicity3.csys.factory.ContentTypeInvalidException;
import multiplicity3.csys.stage.IStage;

public class AdditionalItemUtilities {
	
	/**
	* Registers SynergyNet exclusive items (i.e. items not defined in multiplicity) with the content system.
	**/
	public static void loadAdditionalItems(IStage stage){
		try {
			stage.getContentFactory().register(ICachableImage.class, CachableImage.class);
			stage.getContentFactory().register(IScreenshotContainer.class, ScreenshotContainer.class);
			stage.getContentFactory().register(ITextbox.class, Textbox.class);
			stage.getContentFactory().register(IButtonbox.class, Buttonbox.class);
			stage.getContentFactory().register(IToggleButtonbox.class, ToggleButtonbox.class);
			stage.getContentFactory().register(IRadialMenu.class, RadialMenu.class);
			stage.getContentFactory().register(IScrollContainer.class, ScrollContainer.class);
			stage.getContentFactory().register(IAudioContainer.class, AudioContainer.class);
			stage.getContentFactory().register(IAudioPlayer.class, AudioPlayer.class);
			stage.getContentFactory().register(IAudioRecorder.class, AudioRecorder.class);
			stage.getContentFactory().register(ISimpleMediaPlayer.class, SimpleMediaPlayer.class);
			stage.getContentFactory().register(IMediaPlayer.class, MediaPlayer.class);
			stage.getContentFactory().register(ISimpleKeyboard.class, SimpleKeyboard.class);
			stage.getContentFactory().register(ISimpleKeypad.class, SimpleKeypad.class);
			stage.getContentFactory().register(ICachableLine.class, CachableLine.class);
		} catch (ContentTypeAlreadyBoundException e) {
		} catch (ContentTypeInvalidException e) {}
	}

}
