/*
 * PTM.java
 *
 * Created on September 5, 2004, 5:07 PM
 */

package jpview.ptms;

import jpview.graphics.EnvironmentMap;
import jpview.graphics.Vec3f;

/**
 * 
 * @author clyon
 */
public interface PTM {

	public static final int LRGB = 1;

	public static final int RGB = 2;

	public static final int PRIMITIVE = 3;

	public void setKSpec(float f);

	public void setKDiff(float f);

	public void setExp(int i);

	public float getKSpec();

	public float getKDiff();

	public float getDGain();

	public void setDGain(float f);

	public int getExp();

	public int getZ();

	public void setZ(int z);

	public void setLuminance(float f);

	public float getLuminance();

	public void recache();

	public void release();

	public int getType();

	public int getWidth();

	public int getHeight();

	public int red(int i);

	public int green(int i);

	public int blue(int i);

	public void computeNormals();

	public Vec3f normal(int i);

	public Vec3f[] getNormals();

	public Vec3f normal(int x, int y);
	
	public void resize(int w, int h);

	public void setEnvironmentMap(EnvironmentMap em);

	public EnvironmentMap getEnvironmentMap();

	public int[] getEnvironmentMapCache();

	public int[] getEnvironmentMapMap();
	
	public boolean useEnv();

	public void useEnv(boolean b);
}
