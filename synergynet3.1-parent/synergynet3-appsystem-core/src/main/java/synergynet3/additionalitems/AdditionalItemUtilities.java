package synergynet3.additionalitems;

import multiplicity3.csys.factory.ContentTypeAlreadyBoundException;
import multiplicity3.csys.factory.ContentTypeInvalidException;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.IAudioContainer;
import synergynet3.additionalitems.interfaces.IAudioPlayer;
import synergynet3.additionalitems.interfaces.IAudioRecorder;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.ICachableLine;
import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.additionalitems.interfaces.IRadialMenu;
import synergynet3.additionalitems.interfaces.IScreenshotContainer;
import synergynet3.additionalitems.interfaces.IScrollContainer;
import synergynet3.additionalitems.interfaces.ISimpleKeyboard;
import synergynet3.additionalitems.interfaces.ISimpleKeypad;
import synergynet3.additionalitems.interfaces.ISimpleMediaPlayer;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.additionalitems.interfaces.IToggleButtonbox;
import synergynet3.additionalitems.jme.AudioContainer;
import synergynet3.additionalitems.jme.AudioPlayer;
import synergynet3.additionalitems.jme.AudioRecorder;
import synergynet3.additionalitems.jme.Buttonbox;
import synergynet3.additionalitems.jme.CachableImage;
import synergynet3.additionalitems.jme.CachableLine;
import synergynet3.additionalitems.jme.MediaPlayer;
import synergynet3.additionalitems.jme.RadialMenu;
import synergynet3.additionalitems.jme.ScreenshotContainer;
import synergynet3.additionalitems.jme.ScrollContainer;
import synergynet3.additionalitems.jme.SimpleKeyboard;
import synergynet3.additionalitems.jme.SimpleKeypad;
import synergynet3.additionalitems.jme.SimpleMediaPlayer;
import synergynet3.additionalitems.jme.Textbox;
import synergynet3.additionalitems.jme.ToggleButtonbox;

/**
 * The Class AdditionalItemUtilities.
 */
public class AdditionalItemUtilities
{

	/**
	 * Registers SynergyNet exclusive items (i.e. items not defined in
	 * multiplicity) with the content system.
	 **/
	public static void loadAdditionalItems(IStage stage)
	{
		try
		{
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
		}
		catch (ContentTypeAlreadyBoundException e)
		{
		}
		catch (ContentTypeInvalidException e)
		{
		}
	}

}
