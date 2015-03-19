package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public interface IScreenshotContainer extends IItem {
	
	public boolean setScreenShotImage(File screenShotFile, IStage stage, float width, float height);

}
