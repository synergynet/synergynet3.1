package synergynet3.screenshotcontrol;

import java.io.File;

import com.jme3.math.Vector2f;

public interface IScreenShotter {
	
	public void utiliseScreenshot(File screenShotFile, Vector2f loc, float rot);

}
