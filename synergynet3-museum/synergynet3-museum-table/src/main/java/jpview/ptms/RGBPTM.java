/*
 * Created on Jul 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jpview.ptms;

import jpview.graphics.EnvironmentMap;
import jpview.graphics.Vec3f;

/**
 * @author clyon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RGBPTM implements PTM
{
	private int[][][] coefficients;

	private int width;
	
	private int height;
	
	private int EXP = 75;

	private float KDIFF = 0.25f;

	private float KSPEC = 0.78f;

	private float LUM = 1.0f;

	private float m_dGain = 3.5f;

	private Vec3f[][] normals;

	public RGBPTM()
	{
		super();
	}

	public void computeNormals()
	{
		if (null == normals)
		{
			normals = new Vec3f[3][];
			
			for (int i = 0; i < normals.length; i++)
			{
				normals[i] = HPNormals.getNormals(coefficients[i]);
			}
		}
	}

	public Vec3f[][] getChannelNormals()
	{
		computeNormals();
		return normals;
	}

	/**
	 * The coefficients are organized as follows:
	 * 
	 *    coefficients[block][offset][i]
	 *    
	 * where:
	 * 
	 *    block  is the color channel (RED=0, GREEN=1, BLUE=2)
	 *    offset is the index of the pixel
	 *    i      is the index of the coefficients (a0...a5) 
	 */
	public int[][][] getCoefficients()
	{
		return coefficients;
	}

	public float getDGain()
	{
		return m_dGain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#getEnvironmentMap()
	 */
	public EnvironmentMap getEnvironmentMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#getEnvironmentMapCache()
	 */
	public int[] getEnvironmentMapCache()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#getEnvironmentMapMap()
	 */
	public int[] getEnvironmentMapMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getExp()
	{
		return EXP;
	}

	public int getHeight()
	{
		return height;
	}

	public float getKDiff()
	{
		return KDIFF;
	}

	public float getKSpec()
	{
		return KSPEC;
	}

	public float getLuminance()
	{
		return LUM;
	}

	public Vec3f[] getNormals()
	{
		return null;
	}

	public Vec3f[] getNormalsRed()
	{
		return normals[0];
	}
	
	public Vec3f[] getNormalsGreen()
	{
		return normals[1];
	}
	
	public Vec3f[] getNormalsBlue()
	{
		return normals[2];
	}

	public int getType()
	{
		return PTM.RGB;
	}
	
	public int getWidth()
	{
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#getZ()
	 */
	public int getZ()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int red(int i)
	{
		return 0;
	}

	public int green(int i)
	{
		return 0;
	}
	
	public int blue(int i)
	{
		return 0;
	}

	public Vec3f normal(int i)
	{
		return null;
	}

	public Vec3f normal(int x, int y)
	{
		return null;
	}
	
	/**
	 * Resize the PTM.
	 * 
	 * @param w   Width after resizing. 
	 * @param h   Height after resizing.
	 */
	public void resize(int w, int h)
	{
		int[][][] coeffs = new int[3][w*h][6]; 
		
		int xp,yp;
		float sx = (float)width / (float)w;
		float sy = (float)height / (float)h;
		
		// resize coefficients 
		for (int channel = 0; channel < 3; channel++)
		{
			for (int i = 0; i < 6; i++)
			{
				for (int y = 0; y < h; y++)
				{
					for (int x = 0; x < w; x++)
					{	
						xp = (int)(sx * x);
						yp = (int)(sy * y);
					
						coeffs[channel][x + y * w][i] = coefficients[channel][xp + yp * width][i];
					}
				}
			}
		}
		
		// assign new size
		coefficients = coeffs;
		width = w;
		height = h;
		
		// invoke garbage collector
		System.gc();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#recache()
	 */
	public void recache()
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#release()
	 */
	public void release()
	{
		// TODO Auto-generated method stub
	}

	public void setCoeff(int[][][] c)
	{
		coefficients = c;
	}

	public void setDGain(float f)
	{
		m_dGain = f;
	}

	public void setExp(int i)
	{
		EXP = i;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setKDiff(float f)
	{
		KDIFF = f;
	}

	public void setKSpec(float f)
	{
		KSPEC = f;
	}

	public void setLuminance(float f)
	{
		LUM = f;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#setZ(int)
	 */
	public void setZ(int z)
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#setEnvironmentMap(jpview.graphics.EnvironmentMap)
	 */
	public void setEnvironmentMap(EnvironmentMap em)
	{
		// TODO Auto-generated method stub
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#useEnv()
	 */
	public boolean useEnv()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jpview.ptms.PTM#useEnv(boolean)
	 */
	public void useEnv(boolean b)
	{
		// TODO Auto-generated method stub
	}
}
