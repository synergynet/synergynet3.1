/*
 * ColorChannelOp.java Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import jpview.ptms.LRGBPTM;
import jpview.ptms.PTM;
import jpview.ptms.RGBPTM;

/**
 * @author clyon
 */
public class DirectionalLightOp implements PixelTransformOp
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
		/** no local resources */
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM)
	 */
	@Override
	public void transformPixels(int[] pixels, PTM ptm)
	{
		transformPixels(pixels, ptm, ptm.getWidth() / 4, ptm.getHeight() / 4);
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM, int, int)
	 */
	@Override
	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY)
	{

		boolean beFast = false;

		if (pixels.length < (ptm.getWidth() * ptm.getHeight()))
		{
			beFast = true;
		}

		switch (ptm.getType())
		{
			case PTM.LRGB:
				if (!beFast)
				{
					if (!ptm.useEnv())
					{
						LRGBXform(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					}
					else
					{
						LRGBXformEnv(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					}
				}
				else
				{
					if (!ptm.useEnv())
					{
						LRGBXformFast(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					}
					else
					{
						LRGBXformEnvFast(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					}
				}
				break;
			case PTM.RGB:
				RGBXform(pixels, ((RGBPTM) ptm), mouseX, mouseY);
			case PTM.PRIMITIVE:
				/* TODO: simple grey color */
		}
	}

	/**
	 * LRGB xform.
	 *
	 * @param pixels
	 *            the pixels
	 * @param ptm
	 *            the ptm
	 * @param mouseX
	 *            the mouse x
	 * @param mouseY
	 *            the mouse y
	 */
	private void LRGBXform(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY)
	{
		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int intensity = 0, red = 0, green = 0, blue = 0, pixel = 0, m = 0;
		// int pix = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		// final LRGBPTM localPtm = (LRGBPTM) ptm;
		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][] localCache = ptm.getCoefficients();
		final int _lum = Math.round(ptm.getLuminance() * 256);

		for (int i = 0; i < localCache.length; i++)
		{

			/* no floats, nothing non-local */

			intensity = ((localCache[i][0] * _uu) >> 8) + ((localCache[i][1] * _vv) >> 8) + ((localCache[i][2] * _uv) >> 8) + ((localCache[i][3] * _u) >> 8) + ((localCache[i][4] * _v) >> 8) + (localCache[i][5]);

			pixel = localCache[i][6];

			m = _lum * intensity;

			red = (pixel >> 16) & 0xff;
			green = (pixel >> 8) & 0xff;
			blue = (pixel) & 0xff;

			red = (red * m) >> 16;
			green = (green * m) >> 16;
			blue = (blue * m) >> 16;

			if (red > 255)
			{
				red = 255;
			}
			if (green > 255)
			{
				green = 255;
			}
			if (blue > 255)
			{
				blue = 255;
			}

			if (red < 0)
			{
				red = 0;
			}
			if (green < 0)
			{
				green = 0;
			}
			if (blue < 0)
			{
				blue = 0;
			}

			localPixels[i] = (red << 16) | (green << 8) | blue;
		}
	}

	/**
	 * LRGB xform env.
	 *
	 * @param pixels
	 *            the pixels
	 * @param ptm
	 *            the ptm
	 * @param mouseX
	 *            the mouse x
	 * @param mouseY
	 *            the mouse y
	 */
	private void LRGBXformEnv(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY)
	{

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int intensity = 0, red = 0, green = 0, blue = 0, pixel = 0, m = 0, pix = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		// final LRGBPTM localPtm = (LRGBPTM) ptm;
		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][] localCache = ptm.getCoefficients();
		final int _lum = Math.round(ptm.getLuminance() * 256);

		int[] map = null;
		// boolean useEnv = false;
		int[] rotatedEnv = null;
		map = ptm.getEnvironmentMapMap();
		rotatedEnv = ptm.getEnvironmentMap().rotatedMap();

		if ((map == null) || (rotatedEnv == null))
		{
			LRGBXform(pixels, ptm, mouseX, mouseY);
			return;
		}

		int _er = 256, _eg = 256, _eb = 256;

		for (int i = 0; i < localCache.length; i++)
		{

			/* no floats, nothing non-local */

			intensity = ((localCache[i][0] * _uu) >> 8) + ((localCache[i][1] * _vv) >> 8) + ((localCache[i][2] * _uv) >> 8) + ((localCache[i][3] * _u) >> 8) + ((localCache[i][4] * _v) >> 8) + (localCache[i][5]);

			pixel = localCache[i][6];

			m = _lum * intensity;

			red = (pixel >> 16) & 0xff;
			green = (pixel >> 8) & 0xff;
			blue = (pixel) & 0xff;

			pix = rotatedEnv[map[i]];
			_er = (pix >> 16) & 0xff;
			_eg = (pix >> 8) & 0xff;
			_eb = pix & 0xff;

			red = (red * ((m * _er) >> 8)) >> 16;
			green = (green * ((m * _eg) >> 8)) >> 16;
			blue = (blue * ((m * _eb) >> 8)) >> 16;

			if (red > 255)
			{
				red = 255;
			}
			if (green > 255)
			{
				green = 255;
			}
			if (blue > 255)
			{
				blue = 255;
			}

			if (red < 0)
			{
				red = 0;
			}
			if (green < 0)
			{
				green = 0;
			}
			if (blue < 0)
			{
				blue = 0;
			}

			localPixels[i] = (red << 16) | (green << 8) | blue;
		}
	}

	/**
	 * LRGB xform env fast.
	 *
	 * @param pixels
	 *            the pixels
	 * @param ptm
	 *            the ptm
	 * @param mouseX
	 *            the mouse x
	 * @param mouseY
	 *            the mouse y
	 */
	private void LRGBXformEnvFast(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY)
	{

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int intensity = 0, red = 0, green = 0, blue = 0, pixel = 0, m = 0, pix = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][] localCache = ptm.getCoefficients();
		final int _lum = Math.round(ptm.getLuminance() * 256);

		int[] map = null;
		int[] rotatedEnv = null;
		map = ptm.getEnvironmentMapMap();
		rotatedEnv = ptm.getEnvironmentMap().rotatedMap();

		if ((map == null) || (rotatedEnv == null))
		{
			LRGBXformEnvFast(pixels, ptm, mouseX, mouseY);
			return;
		}

		int _er = 256, _eg = 256, _eb = 256;

		int pixelIndex = 0;

		int height = ptm.getHeight();
		int width = ptm.getWidth();

		for (int y = 0; y < height; y += 2)
		{
			for (int x = 0; x < width; x += 2)
			{
				int i = (y * width) + x;

				/* no floats, nothing non-local */

				intensity = ((localCache[i][0] * _uu) >> 8) + ((localCache[i][1] * _vv) >> 8) + ((localCache[i][2] * _uv) >> 8) + ((localCache[i][3] * _u) >> 8) + ((localCache[i][4] * _v) >> 8) + (localCache[i][5]);

				pixel = localCache[i][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				pix = rotatedEnv[map[i]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;

				red = (red * ((m * _er) >> 8)) >> 16;
				green = (green * ((m * _eg) >> 8)) >> 16;
				blue = (blue * ((m * _eb) >> 8)) >> 16;

				if (red > 255)
				{
					red = 255;
				}
				if (green > 255)
				{
					green = 255;
				}
				if (blue > 255)
				{
					blue = 255;
				}

				if (red < 0)
				{
					red = 0;
				}
				if (green < 0)
				{
					green = 0;
				}
				if (blue < 0)
				{
					blue = 0;
				}

				localPixels[pixelIndex++] = (red << 16) | (green << 8) | blue;
			}
		}
	}

	/**
	 * LRGB xform fast.
	 *
	 * @param pixels
	 *            the pixels
	 * @param ptm
	 *            the ptm
	 * @param mouseX
	 *            the mouse x
	 * @param mouseY
	 *            the mouse y
	 */
	private void LRGBXformFast(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY)
	{
		int[] localPixels = pixels;

		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int intensity = 0, red = 0, green = 0, blue = 0, pixel = 0, m = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][] localCache = ptm.getCoefficients();
		final int _lum = Math.round(ptm.getLuminance() * 256);

		int pixelIndex = 0;

		int height = ptm.getHeight();
		int width = ptm.getWidth();

		for (int y = 0; y < height; y += 2)
		{
			for (int x = 0; x < width; x += 2)
			{
				int i = (y * width) + x;
				/* no floats, nothing non-local */

				intensity = ((localCache[i][0] * _uu) >> 8) + ((localCache[i][1] * _vv) >> 8) + ((localCache[i][2] * _uv) >> 8) + ((localCache[i][3] * _u) >> 8) + ((localCache[i][4] * _v) >> 8) + (localCache[i][5]);

				pixel = localCache[i][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255)
				{
					red = 255;
				}
				if (green > 255)
				{
					green = 255;
				}
				if (blue > 255)
				{
					blue = 255;
				}

				if (red < 0)
				{
					red = 0;
				}
				if (green < 0)
				{
					green = 0;
				}
				if (blue < 0)
				{
					blue = 0;
				}

				localPixels[pixelIndex++] = (red << 16) | (green << 8) | blue;

			}
		}
	}

	/**
	 * RGB xform.
	 *
	 * @param pixels
	 *            the pixels
	 * @param ptm
	 *            the ptm
	 * @param mouseX
	 *            the mouse x
	 * @param mouseY
	 *            the mouse y
	 */
	private void RGBXform(int[] pixels, RGBPTM ptm, int mouseX, int mouseY)
	{

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int red = 0, green = 0, blue = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		final RGBPTM localPtm = ptm;
		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][][] localCache = localPtm.getCoefficients();
		// final int _lum = Math.round(ptm.getLuminance()*256);

		int RED = 0, GREEN = 1, BLUE = 2;

		for (int i = 0; i < localCache[RED].length; i++)
		{

			/* no floats, nothing non-local */

			red = ((localCache[RED][i][0] * _uu) >> 8) + ((localCache[RED][i][1] * _vv) >> 8) + ((localCache[RED][i][2] * _uv) >> 8) + ((localCache[RED][i][3] * _u) >> 8) + ((localCache[RED][i][4] * _v) >> 8) + (localCache[RED][i][5]);

			green = ((localCache[GREEN][i][0] * _uu) >> 8) + ((localCache[GREEN][i][1] * _vv) >> 8) + ((localCache[GREEN][i][2] * _uv) >> 8) + ((localCache[GREEN][i][3] * _u) >> 8) + ((localCache[GREEN][i][4] * _v) >> 8) + (localCache[GREEN][i][5]);

			blue = ((localCache[BLUE][i][0] * _uu) >> 8) + ((localCache[BLUE][i][1] * _vv) >> 8) + ((localCache[BLUE][i][2] * _uv) >> 8) + ((localCache[BLUE][i][3] * _u) >> 8) + ((localCache[BLUE][i][4] * _v) >> 8) + (localCache[BLUE][i][5]);

			if (red > 255)
			{
				red = 255;
			}
			if (green > 255)
			{
				green = 255;
			}
			if (blue > 255)
			{
				blue = 255;
			}

			if (red < 0)
			{
				red = 0;
			}
			if (green < 0)
			{
				green = 0;
			}
			if (blue < 0)
			{
				blue = 0;
			}

			localPixels[i] = (red << 16) | (green << 8) | blue;
		}
	}

}
