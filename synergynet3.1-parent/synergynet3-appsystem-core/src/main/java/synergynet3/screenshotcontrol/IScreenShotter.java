package synergynet3.screenshotcontrol;

import java.io.File;

import com.jme3.math.Vector2f;

/**
 * The Interface IScreenShotter.
 */
public interface IScreenShotter
{

	/**
	 * Utilise screenshot.
	 *
	 * @param screenShotFile
	 *            the screen shot file
	 * @param loc
	 *            the loc
	 * @param rot
	 *            the rot
	 */
	public void utiliseScreenshot(File screenShotFile, Vector2f loc, float rot);

}
