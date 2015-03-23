package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

/**
 * The Interface IScreenshotContainer.
 */
public interface IScreenshotContainer extends IItem {

	/**
	 * Sets the screen shot image.
	 *
	 * @param screenShotFile the screen shot file
	 * @param stage the stage
	 * @param width the width
	 * @param height the height
	 * @return true, if successful
	 */
	public boolean setScreenShotImage(File screenShotFile, IStage stage,
			float width, float height);

}
