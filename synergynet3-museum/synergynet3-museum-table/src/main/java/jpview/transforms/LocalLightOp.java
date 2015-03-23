/*
 * ColorChannelOp.java Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import jpview.ptms.LRGBPTM;
import jpview.ptms.PTM;

/**
 * @author clyon
 */
public class LocalLightOp implements PixelTransformOp {

	/** The __height. */
	private int __height;

	/** The __width. */
	private int __width;

	/** The is flash light. */
	private boolean isFlashLight = false;

	/** The l_lookup. */
	private int[] l_lookup = null;

	/** The z. */
	private int Z = 10000;

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#clearCache()
	 */
	public void clearCache() {
		;
	}

	/**
	 * Creates the length lookup.
	 */
	public void createLengthLookup() {
		l_lookup = new int[__width * __height * 5];
		for (int i = 0; i < __width; i++) {
			for (int j = 0; j < __height; j++) {
				float lx = i;
				float ly = j;

				float tanLen;

				if (!isFlashLight) {
					tanLen = 1 / (float) Math.sqrt((lx * lx) + (ly * ly) + Z);
				} else {
					tanLen = 1 / (float) Math.sqrt(lx + lx + ly + ly + Z);
				}
				lx *= tanLen;
				ly *= -tanLen; /* y oriented in reverse */
				l_lookup[(((i * __width) + j) * 5) + 0] = (int) (lx * 256);
				l_lookup[(((i * __width) + j) * 5) + 1] = (int) (ly * 256);
				l_lookup[(((i * __width) + j) * 5) + 2] = (int) (lx * lx * 256);
				l_lookup[(((i * __width) + j) * 5) + 3] = (int) (ly * ly * 256);
				l_lookup[(((i * __width) + j) * 5) + 4] = (int) (lx * ly * 256);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#forceUpdate()
	 */
	public void forceUpdate() {
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#release()
	 */
	public void release() {
		l_lookup = null;
	}

	/**
	 * Sets the flashlight.
	 *
	 * @param b the new flashlight
	 */
	public void setFlashlight(boolean b) {
		isFlashLight = b;
		l_lookup = null;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM)
	 */
	public void transformPixels(int[] pixels, PTM ptm) {
		transformPixels(pixels, ptm, ptm.getWidth() / 4, ptm.getHeight() / 4);
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM, int, int)
	 */
	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		boolean beFast = false;

		if (pixels.length < (ptm.getWidth() * ptm.getHeight())) {
			beFast = true;
		}

		switch (ptm.getType()) {
			case PTM.LRGB:
				if (!beFast) {
					if (!ptm.useEnv()) {
						LRGBXform(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					} else {
						LRGBXformEnv(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					}
				} else {
					if (!ptm.useEnv()) {
						LRGBXformFast(pixels, ((LRGBPTM) ptm), mouseX, mouseY);
					} else {
						LRGBXformEnvFast(pixels, ((LRGBPTM) ptm), mouseX,
								mouseY);
					}
				}
				break;
			case PTM.RGB:
				/* Hook for other type */
			case PTM.PRIMITIVE:
				/* TODO: simple grey color */
		}
	}

	/**
	 * LRGB xform.
	 *
	 * @param pixels the pixels
	 * @param ptm the ptm
	 * @param mouseX the mouse x
	 * @param mouseY the mouse y
	 */
	private void LRGBXform(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY) {

		__width = ptm.getWidth();
		__height = ptm.getHeight();

		if ((l_lookup == null) || (Z != ptm.getZ())) {
			Z = ptm.getZ();
			createLengthLookup();

		}

		int offset = 0;
		final int[] myPixels = pixels;
		final int myW = __width;
		final int myH = __height;
		final int[][] myC = ptm.getCoefficients();

		int xoff = 0, yoff = 0;
		// int dOff = 0;
		int red, green, blue, pixel;
		final int _lightX = mouseX;
		final int _lightY = mouseY;

		final int[] local2 = l_lookup;
		int intensity = 0;
		final int _lum = Math.round(ptm.getLuminance() * 256);
		int m = 0;

		/** -x, -y * */

		for (int x = myW; --x >= _lightX;) {
			xoff = x - _lightX;
			for (int y = myH; --y >= _lightY;) {
				offset = (y * myW) + x;
				yoff = y - _lightY;
				int i = ((xoff * __width) + yoff) * 5;
				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * local2[i + 4]) >> 8)
						+ ((myC[offset][3] * -local2[i + 0]) >> 8)
						+ ((myC[offset][4] * -local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}

		/** +x, -y * */

		for (int x = _lightX; --x >= 0;) {
			xoff = _lightX - x;
			for (int y = myH; --y >= _lightY;) {
				offset = (y * myW) + x;
				yoff = y - _lightY;
				int i = ((xoff * __width) + yoff) * 5;
				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * -local2[i + 4]) >> 8)
						+ ((myC[offset][3] * local2[i + 0]) >> 8)
						+ ((myC[offset][4] * -local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}
				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}

		/** -x, +y * */

		for (int x = myW; --x >= _lightX;) {

			xoff = x - _lightX;

			for (int y = _lightY; --y >= 0;) {
				offset = (y * myW) + x;
				yoff = _lightY - y;

				int i = ((xoff * __width) + yoff) * 5;

				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * -local2[i + 4]) >> 8)
						+ ((myC[offset][3] * -local2[i + 0]) >> 8)
						+ ((myC[offset][4] * local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}
				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}

		/** +x, +y * */

		for (int x = _lightX; --x >= 0;) {
			xoff = _lightX - x;

			for (int y = _lightY; --y >= 0;) {
				offset = (y * myW) + x;
				yoff = _lightY - y;

				int i = ((xoff * __width) + yoff) * 5;

				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * local2[i + 4]) >> 8)
						+ ((myC[offset][3] * local2[i + 0]) >> 8)
						+ ((myC[offset][4] * local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}
	}

	/**
	 * LRGB xform env.
	 *
	 * @param pixels the pixels
	 * @param ptm the ptm
	 * @param mouseX the mouse x
	 * @param mouseY the mouse y
	 */
	private void LRGBXformEnv(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY) {

		__width = ptm.getWidth();
		__height = ptm.getHeight();

		if ((l_lookup == null) || (Z != ptm.getZ())) {
			Z = ptm.getZ();
			createLengthLookup();
		}

		int offset = 0;
		final int[] myPixels = pixels;
		final int myW = __width;
		final int myH = __height;
		final int[][] myC = ptm.getCoefficients();

		int xoff = 0, yoff = 0;
		// int dOff = 0;
		int red, green, blue, pixel;
		final int _lightX = mouseX;
		final int _lightY = mouseY;

		final int[] local2 = l_lookup;
		int intensity = 0;
		final int _lum = Math.round(ptm.getLuminance() * 256);
		int m = 0;

		int[] map = null;
		// boolean useEnv = false;
		int[] rotatedEnv = null;
		map = ptm.getEnvironmentMapMap();
		rotatedEnv = ptm.getEnvironmentMap().rotatedMap();

		if ((map == null) || (rotatedEnv == null)) {
			LRGBXform(pixels, ptm, mouseX, mouseY);
			return;
		}

		int _er = 256, _eg = 256, _eb = 256, pix = 0;

		/** -x, -y * */

		for (int x = myW; --x >= _lightX;) {
			xoff = x - _lightX;
			for (int y = myH; --y >= _lightY;) {
				offset = (y * myW) + x;
				yoff = y - _lightY;
				int i = ((xoff * __width) + yoff) * 5;
				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * local2[i + 4]) >> 8)
						+ ((myC[offset][3] * -local2[i + 0]) >> 8)
						+ ((myC[offset][4] * -local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				pix = rotatedEnv[map[offset]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;

				red = (red * ((m * _er) >> 8)) >> 16;
				green = (green * ((m * _eg) >> 8)) >> 16;
				blue = (blue * ((m * _eb) >> 8)) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}

		/** +x, -y * */

		for (int x = _lightX; --x >= 0;) {
			xoff = _lightX - x;
			for (int y = myH; --y >= _lightY;) {
				offset = (y * myW) + x;
				yoff = y - _lightY;
				int i = ((xoff * __width) + yoff) * 5;
				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * -local2[i + 4]) >> 8)
						+ ((myC[offset][3] * local2[i + 0]) >> 8)
						+ ((myC[offset][4] * -local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				pix = rotatedEnv[map[offset]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;

				red = (red * ((m * _er) >> 8)) >> 16;
				green = (green * ((m * _eg) >> 8)) >> 16;
				blue = (blue * ((m * _eb) >> 8)) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}
				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}

		/** -x, +y * */

		for (int x = myW; --x >= _lightX;) {

			xoff = x - _lightX;

			for (int y = _lightY; --y >= 0;) {
				offset = (y * myW) + x;
				yoff = _lightY - y;

				int i = ((xoff * __width) + yoff) * 5;

				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * -local2[i + 4]) >> 8)
						+ ((myC[offset][3] * -local2[i + 0]) >> 8)
						+ ((myC[offset][4] * local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				pix = rotatedEnv[map[offset]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;

				red = (red * ((m * _er) >> 8)) >> 16;
				green = (green * ((m * _eg) >> 8)) >> 16;
				blue = (blue * ((m * _eb) >> 8)) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}
				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}

		/** +x, +y * */

		for (int x = _lightX; --x >= 0;) {
			xoff = _lightX - x;

			for (int y = _lightY; --y >= 0;) {
				offset = (y * myW) + x;
				yoff = _lightY - y;

				int i = ((xoff * __width) + yoff) * 5;
				intensity = ((myC[offset][0] * local2[i + 2]) >> 8)
						+ ((myC[offset][1] * local2[i + 3]) >> 8)
						+ ((myC[offset][2] * local2[i + 4]) >> 8)
						+ ((myC[offset][3] * local2[i + 0]) >> 8)
						+ ((myC[offset][4] * local2[i + 1]) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				pix = rotatedEnv[map[offset]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;

				red = (red * ((m * _er) >> 8)) >> 16;
				green = (green * ((m * _eg) >> 8)) >> 16;
				blue = (blue * ((m * _eb) >> 8)) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[offset] = (red << 16) | (green << 8) | blue;
			}
		}
	}

	/**
	 * LRGB xform env fast.
	 *
	 * @param pixels the pixels
	 * @param ptm the ptm
	 * @param mouseX the mouse x
	 * @param mouseY the mouse y
	 */
	private void LRGBXformEnvFast(int[] pixels, LRGBPTM ptm, int mouseX,
			int mouseY) {

		__width = ptm.getWidth();
		__height = ptm.getHeight();

		if ((l_lookup == null) || (Z != ptm.getZ())) {
			Z = ptm.getZ();
			createLengthLookup();

		}

		int offset = 0;
		final int[] myPixels = pixels;
		final int myW = __width;
		final int myH = __height;
		final int[][] myC = ptm.getCoefficients();

		int xoff = 0, yoff = 0;
		// int dOff = 0;
		int red, green, blue, pixel;
		final int _lightX = mouseX;
		final int _lightY = mouseY;

		final int[] local2 = l_lookup;
		int intensity = 0;
		final int _lum = Math.round(ptm.getLuminance() * 256);
		int m = 0;

		int[] map = null;
		// boolean useEnv = false;
		int[] rotatedEnv = null;
		map = ptm.getEnvironmentMapMap();
		rotatedEnv = ptm.getEnvironmentMap().rotatedMap();

		if ((map == null) || (rotatedEnv == null)) {
			LRGBXform(pixels, ptm, mouseX, mouseY);
			return;
		}

		int _er = 256, _eg = 256, _eb = 256, pix = 0;
		int _x = 0, _y = 0, _off = 0;
		int s1 = 1, s2 = 1, s3 = 1, s4 = 1, s5 = 1;

		for (int x = 0; x < myW; x += 2) {
			xoff = Math.abs(x - _lightX);
			if ((x - _lightX) > 0) {
				s4 = -1;
			} else {
				s4 = 1;
			}

			_y = 0;
			for (int y = 0; y < myH; y += 2) {
				if ((y - _lightY) > 0) {
					s5 = -1;
				} else {
					s5 = 1;
				}

				if (s4 != s5) {
					s3 = -1;
				} else {
					s3 = 1;
				}

				offset = (y * myW) + x;
				_off = ((_y * myW) / 2) + _x;
				yoff = Math.abs(y - _lightY);
				int i = ((xoff * __width) + yoff) * 5;

				intensity = ((myC[offset][0] * local2[i + 2] * s1) >> 8)
						+ ((myC[offset][1] * local2[i + 3] * s2) >> 8)
						+ ((myC[offset][2] * local2[i + 4] * s3) >> 8)
						+ ((myC[offset][3] * local2[i + 0] * s4) >> 8)
						+ ((myC[offset][4] * local2[i + 1] * s5) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				pix = rotatedEnv[map[offset]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;

				red = (red * ((m * _er) >> 8)) >> 16;
				green = (green * ((m * _eg) >> 8)) >> 16;
				blue = (blue * ((m * _eb) >> 8)) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[_off] = (red << 16) | (green << 8) | blue;

				_y++;
			}
			_x++;
		}
	}

	/**
	 * LRGB xform fast.
	 *
	 * @param pixels the pixels
	 * @param ptm the ptm
	 * @param mouseX the mouse x
	 * @param mouseY the mouse y
	 */
	private void LRGBXformFast(int[] pixels, LRGBPTM ptm, int mouseX, int mouseY) {
		__width = ptm.getWidth();
		__height = ptm.getHeight();

		if ((l_lookup == null) || (Z != ptm.getZ())) {
			Z = ptm.getZ();
			createLengthLookup();
		}

		int offset = 0;
		final int[] myPixels = pixels;
		final int myW = __width;
		final int myH = __height;
		final int[][] myC = ptm.getCoefficients();

		int xoff = 0, yoff = 0;
		// int dOff = 0;
		int red, green, blue, pixel;
		final int _lightX = mouseX;
		final int _lightY = mouseY;

		final int[] local2 = l_lookup;
		int intensity = 0;
		final int _lum = Math.round(ptm.getLuminance() * 256);
		int m = 0;
		int _x = 0, _y = 0, _off = 0;

		int s1 = 1, s2 = 1, s3 = 1, s4 = 1, s5 = 1;

		for (int x = 0; x < myW; x += 2) {
			xoff = Math.abs(x - _lightX);
			if ((x - _lightX) > 0) {
				s4 = -1;
			} else {
				s4 = 1;
			}

			_y = 0;
			for (int y = 0; y < myH; y += 2) {
				if ((y - _lightY) > 0) {
					s5 = -1;
				} else {
					s5 = 1;
				}

				if (s4 != s5) {
					s3 = -1;
				} else {
					s3 = 1;
				}

				offset = (y * myW) + x;
				_off = ((_y * myW) / 2) + _x;
				yoff = Math.abs(y - _lightY);
				int i = ((xoff * __width) + yoff) * 5;

				intensity = ((myC[offset][0] * local2[i + 2] * s1) >> 8)
						+ ((myC[offset][1] * local2[i + 3] * s2) >> 8)
						+ ((myC[offset][2] * local2[i + 4] * s3) >> 8)
						+ ((myC[offset][3] * local2[i + 0] * s4) >> 8)
						+ ((myC[offset][4] * local2[i + 1] * s5) >> 8)
						+ (myC[offset][5]);

				pixel = myC[offset][6];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255) {
					red = 255;
				}
				if (green > 255) {
					green = 255;
				}
				if (blue > 255) {
					blue = 255;
				}

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				myPixels[_off] = (red << 16) | (green << 8) | blue;

				_y++;
			}
			_x++;
		}
	}

}
