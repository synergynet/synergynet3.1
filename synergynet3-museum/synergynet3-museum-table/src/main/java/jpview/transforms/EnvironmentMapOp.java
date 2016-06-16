/*
 * ColorChannelOp.java Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

// import java.awt.image.BufferedImage;

// import jpview.Utils;
// import jpview.graphics.Vec3f;
import jpview.ptms.PTM;

/**
 * @author clyon
 */
public class EnvironmentMapOp implements PixelTransformOp
{

	/** Creates a new instance of ColorChannelOp */
	public EnvironmentMapOp()
	{
	}

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
		PTM localPtm = ptm;
		int length = localPixels.length;
		// Vec3f eye = new Vec3f(0, 0, 1);
		// BufferedImage tmp = new BufferedImage(ptm.getWidth(),
		// ptm.getHeight(), BufferedImage.TYPE_INT_RGB);
		// int[] buf = Utils.grabPixels(tmp);
		// int[] map = null;
		// int[] rotatedEnv = ptm.getEnvironmentMap().rotatedMap();
		// if (ptm.getEnvironmentMap() != null) {
		// map = ptm.getEnvironmentMapMap();
		// }

		for (int i = 0; i < length; i++)
		{
			// localPixels[i] = rotatedEnv[map[i]];
			// N.perturb();
			// Vec3f R = Vec3f.reflect(ptm.normal(i),new Vec3f(0,0,1));
			// float m = (float) Math.sqrt(2*(R.z()+1));
			// float u = -R.x()/m;
			// float v = -R.y()/m;
			// int u1 = Math.round((u+1)*ptm.getWidth()/2);
			// int v1 = Math.round((v+1)*ptm.getHeight()/2);
			localPixels[i] = localPtm.getEnvironmentMap().getPixel(ptm.normal(i));

			// Vec3f R = Vec3f.reflect(localPtm.normal(x,y),eye);
			// // float m = (float) Math.sqrt(2*(R.z()+1));
			// // float u = R.x()/m;
			// // float v = -R.y()/m;
			// // int pixel = localPtm.getEnvironmentMap().getPixel(u,v);
			// buf[i] = R.toPixel();
			// }
			//
			// float sum = (2+4+5+4+2)*2 + (4+9+12+9+4)*2 + 5+12+15+12+5;
			//
			// float[] elements = {
			// 2f/sum, 4f/sum, 5f/sum, 4.0f/sum, 2.0f/sum,
			// 4f/sum, 9f/sum, 12f/sum, 9f/sum, 4f/sum,
			// 5f/sum, 12f/sum, 15f/sum, 12f/sum, 5f/sum,
			// 4f/sum, 9f/sum, 12f/sum, 9f/sum, 4f/sum,
			// 2.0f/sum, 4.0f/sum, 5.0f/sum, 4.0f/sum, 2.0f/sum };
			//
			// Kernel kernel = new Kernel(5,5,elements);
			// ConvolveOp cop = new ConvolveOp(kernel);
			// BufferedImage tmp2 = new
			// BufferedImage(ptm.getWidth(),ptm.getHeight(),BufferedImage.TYPE_INT_RGB);
			// cop.filter(tmp,tmp2);
			// int [] buf2 = Utils.grabPixels(tmp2);
			// int unchanged = 0;
			//
			// // buf2 contains blurred reflection vectors
			//
			// for ( int i = 0; i < length; i++ ) {
			// int w = localPtm.getWidth();
			// int h = localPtm.getHeight();
			// int x = i % w;
			// int y = i / w;
			// Vec3f R = Vec3f.convertPixel(buf[i]);
			// float m = (float) Math.sqrt(2*(R.z()+1));
			// float u = R.x()/m;
			// float v = -R.y()/m;
			// localPixels[i] = localPtm.getEnvironmentMap().getPixel(u,v);
			// //localPixels[i] = localPtm.getEnvironmentMap().getPixel(i);
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
		transformPixels(pixels, ptm);

	}

}
