/*
 * PTMDataBuffer.java Created on September 5, 2004, 5:04 PM
 */

package jpview.ptms;

import jpview.graphics.EnvironmentMap;
import jpview.graphics.Vec3f;

/**
 * @author clyon
 */
public class LRGBPTM implements PTM
{

	/** The dgain. */
	private float DGAIN = 3.5f;

	/** The exp. */
	private int EXP = 75;

	/** The height. */
	private int height = 0;

	/** The kdiff. */
	private float KDIFF = 0.4f;

	/** The kspec. */
	private float KSPEC = 0.78f;

	/** The lum. */
	private float LUM = 1.0f;

	/** normals */
	private Vec3f[] normals = null;

	/** The use env. */
	private boolean useEnv = false;

	/** PTM Attributes */
	private int width = 0;

	/** The z. */
	private int Z = 10000;

	/** The coefficients. */
	protected int[][] coefficients = null;

	/** map */
	protected EnvironmentMap em = null;

	/** The environment map cache. */
	protected int[] environmentMapCache = null;

	/** The environment map map. */
	protected int[] environmentMapMap = null;

	/** Creates a new instance of PTMDataBuffer */
	public LRGBPTM()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#blue(int)
	 */
	@Override
	public int blue(int i)
	{
		return coefficients[i][6] & 0xff;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#computeNormals()
	 */
	@Override
	public void computeNormals()
	{
		normals = HPNormals.getNormals(this);
	}

	/**
	 * This function updates the current PTM with the data of the given sub-ptm.
	 * The sub-ptm is stretched to the desired dimensions.
	 *
	 * @param offx
	 *            Horizontal offset
	 * @param offy
	 *            Vertical offset
	 * @param w
	 *            Target width
	 * @param h
	 *            Target height
	 * @param ptm
	 *            Sub-ptm
	 */
	public void drawSubPtm(int offx, int offy, int w, int h, PTM ptm)
	{
		// PTM must be of LRGB type
		assert (ptm.getType() == PTM.LRGB);
		assert ((offx + w) <= width);
		assert ((offy + h) <= height);
		assert (offx >= 0);
		assert (offy >= 0);

		float sx = (float) ptm.getWidth() / (float) w;
		float sy = (float) ptm.getHeight() / (float) h;

		LRGBPTM lrgbptm = (LRGBPTM) ptm;
		int[][] coeff = lrgbptm.getCoefficients();

		int r00, r01, r10, r11;
		int g00, g01, g10, g11;
		int b00, b01, b10, b11;
		int rgb00, rgb01, rgb10, rgb11;
		int r, g, b;
		int offset;
		float xp, yp;
		float alpha, beta;
		int xprime, yprime;
		int sourceW = ptm.getWidth();
		int sourceH = ptm.getHeight();
		for (int y = offy; y < (offy + h); y++)
		{
			for (int x = offx; x < (offx + w); x++)
			{
				xp = x * sx;
				yp = y * sy;

				alpha = xp - (float) Math.floor(xp);
				beta = yp - (float) Math.floor(yp);

				xprime = (int) xp;
				yprime = (int) yp;

				// Coefficients interpolation

				if ((xp < (sourceW - 1)) && (yp < (sourceH - 1)))
				{
					offset = xprime + (yprime * sourceW);

					for (int i = 0; i < 6; i++)
					{
						coefficients[x + (y * width)][i] = (int) (((1.0f - alpha) * (1.0f - beta) * coeff[offset][i]) + (alpha * (1.0f - beta) * coeff[offset + 1][i]) + ((1.0f - alpha) * beta * coeff[offset + sourceW][i]) + (alpha * beta * coeff[offset + 1 + sourceW][i]));
					}

					// RGB interpolation
					rgb00 = coeff[offset][6];
					rgb01 = coeff[offset + 1][6];
					rgb10 = coeff[offset + sourceW][6];
					rgb11 = coeff[offset + sourceW + 1][6];

					r00 = rgb00 & 0x00ff0000;
					r01 = rgb01 & 0x00ff0000;
					r10 = rgb10 & 0x00ff0000;
					r11 = rgb11 & 0x00ff0000;

					r = (int) (((1.0f - alpha) * (1.0f - beta) * r00) + (alpha * (1.0f - beta) * r01) + ((1.0f - alpha) * beta * r10) + (alpha * beta * r11));

					g00 = rgb00 & 0x0000ff00;
					g01 = rgb01 & 0x0000ff00;
					g10 = rgb10 & 0x0000ff00;
					g11 = rgb11 & 0x0000ff00;

					g = (int) (((1.0f - alpha) * (1.0f - beta) * g00) + (alpha * (1.0f - beta) * g01) + ((1.0f - alpha) * beta * g10) + (alpha * beta * g11));

					b00 = rgb00 & 0x000000ff;
					b01 = rgb01 & 0x000000ff;
					b10 = rgb10 & 0x000000ff;
					b11 = rgb11 & 0x000000ff;

					b = (int) (((1.0f - alpha) * (1.0f - beta) * b00) + (alpha * (1.0f - beta) * b01) + ((1.0f - alpha) * beta * b10) + (alpha * beta * b11));

					coefficients[x + (y * width)][6] = (r << 16) | (g << 8) | b;
				}
				else
				{
					if ((xprime + 1) >= sourceW)
					{
						xprime--;
					}

					if ((yprime + 1) >= sourceH)
					{
						yprime--;
					}

					for (int i = 0; i < 7; i++)
					{
						coefficients[x + (y * width)][i] = coeff[xprime + (yprime * sourceW)][i];
					}
				}
			}
		}
	}

	/**
	 * This functions updates the current PTM with the data of the given
	 * sub-ptm.
	 *
	 * @param offx
	 *            Horizontal offset
	 * @param offy
	 *            Vertical offset
	 * @param ptm
	 *            Sub-ptm
	 */
	public void drawSubPtm(int offx, int offy, PTM ptm)
	{
		assert (ptm.getType() == PTM.LRGB);
		assert (offx >= 0);
		assert (offy >= 0);

		LRGBPTM lrgbptm = (LRGBPTM) ptm;
		int[][] coeff = lrgbptm.getCoefficients();

		int offset = 0;
		// int w = ptm.getWidth();
		for (int y = offy; y < (offy + ptm.getHeight()); y++)
		{
			for (int x = offx; x < (offx + ptm.getWidth()); x++)
			{
				for (int i = 0; i < 7; i++)
				{
					coefficients[x + (y * width)][i] = coeff[offset][i];
				}

				offset++;
			}
		}
	}

	/**
	 * Gets the a0.
	 *
	 * @return the a0
	 */
	public int[] getA0()
	{
		return getA0(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the a0.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the a0
	 */
	public int[] getA0(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][0];
				offset++;
			}
		}

		return buffer;
	}

	/**
	 * Gets the a1.
	 *
	 * @return the a1
	 */
	public int[] getA1()
	{
		return getA1(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the a1.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the a1
	 */
	public int[] getA1(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][1];
				offset++;
			}
		}

		return buffer;
	}

	/**
	 * Gets the a2.
	 *
	 * @return the a2
	 */
	public int[] getA2()
	{
		return getA2(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the a2.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the a2
	 */
	public int[] getA2(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][2];
				offset++;
			}
		}

		return buffer;
	}

	/**
	 * Gets the a3.
	 *
	 * @return the a3
	 */
	public int[] getA3()
	{
		return getA3(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the a3.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the a3
	 */
	public int[] getA3(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][3];
				offset++;
			}
		}

		return buffer;
	}

	/**
	 * Gets the a4.
	 *
	 * @return the a4
	 */
	public int[] getA4()
	{
		return getA4(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the a4.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the a4
	 */
	public int[] getA4(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][4];
				offset++;
			}
		}

		return buffer;
	}

	/**
	 * Gets the a5.
	 *
	 * @return the a5
	 */
	public int[] getA5()
	{
		return getA5(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the a5.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the a5
	 */
	public int[] getA5(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][5];
				offset++;
			}
		}

		return buffer;
	}

	/**
	 * Get LRGBPTM coefficients. Coefficients are organized as follows:
	 * coefficients[i][0] --> coefficient a0 of the i-th pixel
	 * coefficients[i][1] --> coefficient a1 of the i-th pixel ...
	 * coefficients[i][5] --> coefficient a5 of the i-th pixel
	 * coefficients[i][6] --> RGB color encoded as 0x00RRGGBB
	 */
	public int[][] getCoefficients()
	{
		return coefficients;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getDGain()
	 */
	@Override
	public float getDGain()
	{
		return DGAIN;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getEnvironmentMap()
	 */
	@Override
	public EnvironmentMap getEnvironmentMap()
	{
		return em;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getEnvironmentMapCache()
	 */
	@Override
	public int[] getEnvironmentMapCache()
	{
		if (this.environmentMapCache == null)
		{
			createEnvironmentMapCache();
		}
		return this.environmentMapCache;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getEnvironmentMapMap()
	 */
	@Override
	public int[] getEnvironmentMapMap()
	{
		if (this.environmentMapMap == null)
		{
			createEnvironmentMapMap();
		}
		return this.environmentMapMap;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getExp()
	 */
	@Override
	public int getExp()
	{
		return EXP;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getHeight()
	 */
	@Override
	public int getHeight()
	{
		return height;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getKDiff()
	 */
	@Override
	public float getKDiff()
	{
		return KDIFF;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getKSpec()
	 */
	@Override
	public float getKSpec()
	{
		return KSPEC;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getLuminance()
	 */
	@Override
	public float getLuminance()
	{
		return LUM;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getNormals()
	 */
	@Override
	public Vec3f[] getNormals()
	{
		if (normals == null)
		{
			computeNormals();
		}

		return normals;
	}

	/**
	 * Gets the rgb.
	 *
	 * @return the rgb
	 */
	public int[] getRGB()
	{
		return getRGB(0, 0, width - 1, height - 1);
	}

	/**
	 * Gets the rgb.
	 *
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 * @return the rgb
	 */
	public int[] getRGB(int left, int top, int right, int bottom)
	{
		int[] buffer = new int[((right - left) + 1) * ((bottom - top) + 1)];

		int x, y, offset = 0, offset2;
		for (y = top; y <= bottom; y++)
		{
			for (x = left; x <= right; x++)
			{
				offset2 = x + (y * width);
				buffer[offset] = coefficients[offset2][6];
				offset++;
			}
		}

		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getType()
	 */
	@Override
	public int getType()
	{
		return PTM.LRGB;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getWidth()
	 */
	@Override
	public int getWidth()
	{
		return width;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#getZ()
	 */
	@Override
	public int getZ()
	{
		return Z;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#green(int)
	 */
	@Override
	public int green(int i)
	{
		return (coefficients[i][6] >> 8) & 0xff;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#normal(int)
	 */
	@Override
	public Vec3f normal(int i)
	{
		return normals[i];
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#normal(int, int)
	 */
	@Override
	public Vec3f normal(int x, int y)
	{
		Vec3f n = null;

		try
		{
			return normals[(y * width) + x];
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e)
		{
		}

		return n;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#recache()
	 */
	@Override
	public void recache()
	{
		this.environmentMapCache = null;
		this.environmentMapMap = null;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#red(int)
	 */
	@Override
	public int red(int i)
	{
		return (coefficients[i][6] >> 16) & 0xff;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#release()
	 */
	@Override
	public void release()
	{
		if (em != null)
		{
			em.release();
		}
		em = null;
		environmentMapCache = null;
		environmentMapMap = null;
		if (normals != null)
		{
			for (int i = 0; i < normals.length; i++)
			{
				normals[i] = null;
			}
			normals = null;
		}
		coefficients = null;
	}

	/**
	 * Resize the PTM.
	 *
	 * @param w
	 *            Width after resizing
	 * @param h
	 *            Height after resizing
	 */
	@Override
	public void resize(int w, int h)
	{
		if (coefficients != null)
		{
			int[][] coeffs = new int[w * h][7];

			int xp, yp;
			float sx = (float) width / (float) w;
			float sy = (float) height / (float) h;

			// resize coefficients only
			for (int i = 0; i < 6; i++)
			{
				for (int y = 0; y < h; y++)
				{
					for (int x = 0; x < w; x++)
					{
						xp = (int) (sx * x);
						yp = (int) (sy * y);

						coeffs[x + (y * w)][i] = coefficients[xp + (yp * width)][i];
					}
				}
			}

			// resize RGB plane
			for (int y = 0; y < h; y++)
			{
				for (int x = 0; x < w; x++)
				{
					xp = (int) (sx * x);
					yp = (int) (sy * y);

					coeffs[x + (y * w)][6] = coefficients[xp + (yp * width)][6];
				}
			}

			// assign new size
			coefficients = coeffs;
			width = w;
			height = h;
		}
	}

	/** settors */
	public void setA0(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][0] = a[i];
		}
	}

	/**
	 * Sets the a1.
	 *
	 * @param a
	 *            the new a1
	 */
	public void setA1(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][1] = a[i];
		}

	}

	/**
	 * Sets the a2.
	 *
	 * @param a
	 *            the new a2
	 */
	public void setA2(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][2] = a[i];
		}

	}

	/**
	 * Sets the a3.
	 *
	 * @param a
	 *            the new a3
	 */
	public void setA3(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][3] = a[i];
		}
	}

	/**
	 * Sets the a4.
	 *
	 * @param a
	 *            the new a4
	 */
	public void setA4(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][4] = a[i];
		}
	}

	/**
	 * Sets the a5.
	 *
	 * @param a
	 *            the new a5
	 */
	public void setA5(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][5] = a[i];
		}
	}

	/**
	 * Set LRGBPTM coefficients. If coeff is null the coefficients are
	 * allocated.
	 */
	public void setCoefficients(int[][] coeff)
	{
		if (coeff == null)
		{
			coefficients = new int[width * height][7];
		}
		else
		{
			assert ((width * height * 7) == coeff.length);
			coefficients = coeff;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setDGain(float)
	 */
	@Override
	public void setDGain(float f)
	{
		DGAIN = Math.max(f, 1.0f);
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setEnvironmentMap(jpview.graphics.EnvironmentMap)
	 */
	@Override
	public void setEnvironmentMap(EnvironmentMap map)
	{
		em = map;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setExp(int)
	 */
	@Override
	public void setExp(int i)
	{
		EXP = i;
	}

	/**
	 * Sets the height.
	 *
	 * @param h
	 *            the new height
	 */
	public void setHeight(int h)
	{
		height = h;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setKDiff(float)
	 */
	@Override
	public void setKDiff(float f)
	{
		KDIFF = f;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setKSpec(float)
	 */
	@Override
	public void setKSpec(float f)
	{
		KSPEC = f;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setLuminance(float)
	 */
	@Override
	public void setLuminance(float f)
	{
		LUM = f;
	}

	/**
	 * Sets the rgb.
	 *
	 * @param a
	 *            the new rgb
	 */
	public void setRGB(int[] a)
	{
		if (coefficients == null)
		{
			coefficients = new int[a.length][7];
		}

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i][6] = a[i];
		}
	}

	/**
	 * Sets the width.
	 *
	 * @param w
	 *            the new width
	 */
	public void setWidth(int w)
	{
		width = w;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#setZ(int)
	 */
	@Override
	public void setZ(int z)
	{
		Z = z;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#useEnv()
	 */
	@Override
	public boolean useEnv()
	{
		return useEnv;
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.ptms.PTM#useEnv(boolean)
	 */
	@Override
	public void useEnv(boolean b)
	{
		useEnv = b;
	}

	/**
	 * Creates the environment map cache.
	 */
	private void createEnvironmentMapCache()
	{
		environmentMapCache = new int[width * height];

		if (normals == null)
		{
			this.computeNormals();
		}

		if (this.em == null)
		{
			System.out.println("Can't compute environment map cache, no environment map");
		}
		for (int i = 0; i < normals.length; i++)
		{
			environmentMapCache[i] = this.em.getPixel(normals[i]);
		}
	}

	/**
	 * Creates the environment map map.
	 */
	private void createEnvironmentMapMap()
	{
		environmentMapMap = new int[width * height];

		if (normals == null)
		{
			this.computeNormals();
		}

		if (this.em == null)
		{
			System.out.println("Can't compute environment map cache, no environment map");
		}

		for (int i = 0; i < normals.length; i++)
		{
			environmentMapMap[i] = this.em.getMapIndex(normals[i]);
		}
	}

	/**
	 * X.
	 *
	 * @param i
	 *            the i
	 * @return the int
	 */
	protected int x(int i)
	{
		return i % width;
	}

	/**
	 * Y.
	 *
	 * @param i
	 *            the i
	 * @return the int
	 */
	protected int y(int i)
	{
		return i / height;
	}

}
