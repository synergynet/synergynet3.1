package synergynet3.screenshotcontrol;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.IScreenshotContainer;
import synergynet3.projector.network.ProjectorTransferUtilities;

/**
 * The Class ScreenshotUtilities.
 */
public class ScreenshotUtilities
{

	/**
	 * Send screenshot of current contents of the app to a the list of
	 * projectors provided. * @param screenShotFile File to generate screenshot
	 * from.
	 */
	public static void transferScreenShot(final File screenShotFile, final String[] projectorsToSendTo, final IStage stage)
	{
		Thread cachingThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{

					int displayWidth = (int) (stage.getWorldLocation().x * 2);
					int displayHeight = (int) (stage.getWorldLocation().y * 2);

					IContentFactory contentFactory = stage.getContentFactory();

					IScreenshotContainer item = contentFactory.create(IScreenshotContainer.class, screenShotFile.getName(), UUID.randomUUID());

					item.setScreenShotImage(screenShotFile, stage, displayWidth / 4, displayHeight / 4);

					int angle = (int) ((Math.random() * (11)) + 0.5);
					if (Math.random() > 0.5)
					{
						angle = -angle;
					}
					item.setRelativeRotation((float) Math.toRadians(angle));

					ProjectorTransferUtilities.get().addToTransferableContents(item, new Float(displayWidth / 4), new Float(displayHeight / 4), screenShotFile.getName());
					ProjectorTransferUtilities.get().transferIndividualItem(item, projectorsToSendTo, new Float(displayWidth / 4), new Float(displayHeight / 4));
					ProjectorTransferUtilities.get().removeFromTransferableContents(item);
					stage.removeItem(item);

				}
				catch (ContentTypeNotBoundException e)
				{
					AdditionalSynergyNetUtilities.log(Level.SEVERE, "Unable to transfer Screenshot", e);
				}

			}
		});
		cachingThread.start();
	}

}
