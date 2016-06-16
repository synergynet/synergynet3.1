/*
 * ColorChannelOp.java Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import jpview.graphics.Vec3f;
import jpview.ptms.PTM;

/**
 * @author clyon
 */
public class NormalMapOp implements PixelTransformOp
{

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#clearCache()
	 */
	@Override
	public void clearCache()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#forceUpdate()
	 */
	@Override
	public void forceUpdate()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#release()
	 */
	@Override
	public void release()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM)
	 */
	@Override
	public void transformPixels(int[] pixels, PTM ptm)
	{
		int[] localPixels = pixels;
		// PTM localPtm = ptm;
		for (int i = 0; i < localPixels.length; i++)
		{
			Vec3f N = ptm.normal(i);
			if ((N.x() == 0) && (N.y() == 0) && (N.z() == 0))
			{
				localPixels[i] = 0;
			}
			else
			{
				localPixels[i] = N.toPixel();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM, int, int)
	 */
	@Override
	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY)
	{
		if (pixels.length < (ptm.getWidth() * ptm.getHeight()))
		{
			transformPixelsFast(pixels, ptm);
		}
		else
		{
			transformPixels(pixels, ptm);
		}
	}

	/**
	 * Transform pixels fast.
	 *
	 * @param pixels
	 *            the pixels
	 * @param ptm
	 *            the ptm
	 */
	public void transformPixelsFast(int[] pixels, PTM ptm)
	{
		final int[] localPixels = pixels;
		// final PTM localPtm = ptm;
		final int height = ptm.getHeight();
		final int width = ptm.getWidth();
		int pixelIndex = 0;

		for (int y = 0; y < height; y += 2)
		{
			for (int x = 0; x < width; x += 2)
			{
				int i = (y * width) + x;
				Vec3f N = ptm.normal(i);
				if ((N.x() == 0) && (N.y() == 0) && (N.z() == 0))
				{
					localPixels[pixelIndex] = 0;
				}
				else
				{
					localPixels[pixelIndex] = N.toPixel();
				}
				pixelIndex++;
			}
		}
	}

}
