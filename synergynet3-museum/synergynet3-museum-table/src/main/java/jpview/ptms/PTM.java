/*
 * PTM.java Created on September 5, 2004, 5:07 PM
 */

package jpview.ptms;

import jpview.graphics.EnvironmentMap;
import jpview.graphics.Vec3f;

/**
 * @author clyon
 */
public interface PTM
{

	/** The Constant LRGB. */
	public static final int LRGB = 1;

	/** The Constant PRIMITIVE. */
	public static final int PRIMITIVE = 3;

	/** The Constant RGB. */
	public static final int RGB = 2;

	/**
	 * Blue.
	 *
	 * @param i
	 *            the i
	 * @return the int
	 */
	public int blue(int i);

	/**
	 * Compute normals.
	 */
	public void computeNormals();

	/**
	 * Gets the d gain.
	 *
	 * @return the d gain
	 */
	public float getDGain();

	/**
	 * Gets the environment map.
	 *
	 * @return the environment map
	 */
	public EnvironmentMap getEnvironmentMap();

	/**
	 * Gets the environment map cache.
	 *
	 * @return the environment map cache
	 */
	public int[] getEnvironmentMapCache();

	/**
	 * Gets the environment map map.
	 *
	 * @return the environment map map
	 */
	public int[] getEnvironmentMapMap();

	/**
	 * Gets the exp.
	 *
	 * @return the exp
	 */
	public int getExp();

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight();

	/**
	 * Gets the k diff.
	 *
	 * @return the k diff
	 */
	public float getKDiff();

	/**
	 * Gets the k spec.
	 *
	 * @return the k spec
	 */
	public float getKSpec();

	/**
	 * Gets the luminance.
	 *
	 * @return the luminance
	 */
	public float getLuminance();

	/**
	 * Gets the normals.
	 *
	 * @return the normals
	 */
	public Vec3f[] getNormals();

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth();

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public int getZ();

	/**
	 * Green.
	 *
	 * @param i
	 *            the i
	 * @return the int
	 */
	public int green(int i);

	/**
	 * Normal.
	 *
	 * @param i
	 *            the i
	 * @return the vec3f
	 */
	public Vec3f normal(int i);

	/**
	 * Normal.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the vec3f
	 */
	public Vec3f normal(int x, int y);

	/**
	 * Recache.
	 */
	public void recache();

	/**
	 * Red.
	 *
	 * @param i
	 *            the i
	 * @return the int
	 */
	public int red(int i);

	/**
	 * Release.
	 */
	public void release();

	/**
	 * Resize.
	 *
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 */
	public void resize(int w, int h);

	/**
	 * Sets the d gain.
	 *
	 * @param f
	 *            the new d gain
	 */
	public void setDGain(float f);

	/**
	 * Sets the environment map.
	 *
	 * @param em
	 *            the new environment map
	 */
	public void setEnvironmentMap(EnvironmentMap em);

	/**
	 * Sets the exp.
	 *
	 * @param i
	 *            the new exp
	 */
	public void setExp(int i);

	/**
	 * Sets the k diff.
	 *
	 * @param f
	 *            the new k diff
	 */
	public void setKDiff(float f);

	/**
	 * Sets the k spec.
	 *
	 * @param f
	 *            the new k spec
	 */
	public void setKSpec(float f);

	/**
	 * Sets the luminance.
	 *
	 * @param f
	 *            the new luminance
	 */
	public void setLuminance(float f);

	/**
	 * Sets the z.
	 *
	 * @param z
	 *            the new z
	 */
	public void setZ(int z);

	/**
	 * Use env.
	 *
	 * @return true, if successful
	 */
	public boolean useEnv();

	/**
	 * Use env.
	 *
	 * @param b
	 *            the b
	 */
	public void useEnv(boolean b);
}
