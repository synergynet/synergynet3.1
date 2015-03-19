/*
 * ColorChannelOp.java
 *
 * Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import jpview.graphics.Vec3f;
import jpview.ptms.LRGBPTM;
import jpview.ptms.PTM;
import jpview.ptms.RGBPTM;

/**
 * 
 * @author clyon
 */
public class DiffuseGainOp implements PixelTransformOp {

	private int[][] dgain = null;

	private int[] rgb = null;

	private float DGAIN = 3.0f;

	private int[][][] dgain3;

	public void release() {
		dgain = null;
		dgain3 = null;
	}

	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		float f = ptm.getDGain();
		if (f != DGAIN) {
			DGAIN = f;
			dgain = null;
			dgain3 = null;
		}

		switch (ptm.getType()) {
		case PTM.LRGB:
			if (pixels.length < ptm.getHeight() * ptm.getWidth())
				LRGBXformFast(pixels, ptm, mouseX, mouseY);
			else
				LRGBXform(pixels, ptm, mouseX, mouseY);
			break;
		case PTM.RGB:
			RGBXform(pixels, ptm, mouseX, mouseY);
			break;
		case PTM.PRIMITIVE:
		/* TODO: simple grey color */
		}
	}

	public void transformPixels(int[] pixels, PTM ptm) {
		transformPixels(pixels, ptm, ptm.getWidth() / 4, ptm.getHeight() / 4);
	}

	private void LRGBXform(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		if (dgain == null)
			computeDGainCoeffLRGB((LRGBPTM) ptm);

		DGAIN = ptm.getDGain();

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

		final int[][] _dg = dgain;
		final int[] rgb = this.rgb;
		final int _lum = Math.round(ptm.getLuminance() * 256);

		for (int i = 0; i < localPixels.length; i++) {

			intensity = ((_dg[i][0] * _uu) >> 8) + ((_dg[i][1] * _vv) >> 8)
					+ ((_dg[i][2] * _uv) >> 8) + ((_dg[i][3] * _u) >> 8)
					+ ((_dg[i][4] * _v) >> 8) + (_dg[i][5]);

			pixel = rgb[i];

			m = _lum * intensity;

			red = (pixel >> 16) & 0xff;
			green = (pixel >> 8) & 0xff;
			blue = (pixel) & 0xff;

			red = (red * m) >> 16;
			green = (green * m) >> 16;
			blue = (blue * m) >> 16;

			if (red > 255)
				red = 255;
			if (green > 255)
				green = 255;
			if (blue > 255)
				blue = 255;

			if (red < 0)
				red = 0;
			if (green < 0)
				green = 0;
			if (blue < 0)
				blue = 0;

			localPixels[i] = (red << 16) | (green << 8) | blue;
		}
	}

	private void LRGBXformFast(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		if (dgain == null)
			computeDGainCoeffLRGB((LRGBPTM) ptm);

		DGAIN = ptm.getDGain();

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

		final int[][] _dg = dgain;
		final int[] rgb = this.rgb;
		final int _lum = Math.round(ptm.getLuminance() * 256);

		final int width = ptm.getWidth();
		final int height = ptm.getHeight();
		int pixelOffset = 0;

		for (int y = 0; y < height; y += 2) {
			for (int x = 0; x < width; x += 2) {
				int i = y * width + x;

				intensity = ((_dg[i][0] * _uu) >> 8) + ((_dg[i][1] * _vv) >> 8)
						+ ((_dg[i][2] * _uv) >> 8) + ((_dg[i][3] * _u) >> 8)
						+ ((_dg[i][4] * _v) >> 8) + (_dg[i][5]);

				pixel = rgb[i];

				m = _lum * intensity;

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				red = (red * m) >> 16;
				green = (green * m) >> 16;
				blue = (blue * m) >> 16;

				if (red > 255)
					red = 255;
				if (green > 255)
					green = 255;
				if (blue > 255)
					blue = 255;

				if (red < 0)
					red = 0;
				if (green < 0)
					green = 0;
				if (blue < 0)
					blue = 0;

				localPixels[pixelOffset++] = (red << 16) | (green << 8) | blue;
			}
		}
	}

	public void computeDGainCoeffLRGB(LRGBPTM ptm) {

		int[][] coeff = ptm.getCoefficients();
		Vec3f[] normals = ptm.getNormals();

		dgain = new int[coeff.length][];
		rgb = new int[coeff.length];

		for (int i = 0; i < dgain.length; i++) {
			int[] ap = new int[6];
			float lu = normals[i].x();
			float lv = normals[i].y();
			if (Math.sqrt(lu * lu + lv * lv) > 1) {
				lu /= (Math.sqrt(lu * lu + lv * lv));
				lv /= (Math.sqrt(lu * lu + lv * lv));
			}
			float lu2 = lu * lu;
			float lv2 = lv * lv;

			ap[0] = (int) (DGAIN * coeff[i][0]);
			ap[1] = (int) (DGAIN * coeff[i][1]);
			ap[2] = (int) (DGAIN * coeff[i][2]);
			ap[3] = (int) (((1 - DGAIN) * ((2 * coeff[i][0] * lu) + (coeff[i][2] * lv))) + coeff[i][3]);
			ap[4] = (int) (((1 - DGAIN) * ((2 * coeff[i][1] * lv) + (coeff[i][2] * lu))) + coeff[i][4]);
			ap[5] = (int) (((1 - DGAIN) * (coeff[i][0] * lu2 + coeff[i][1]
					* lv2 + coeff[i][2] * lu * lv))
					+ ((coeff[i][3] - ap[3]) * lu)
					+ ((coeff[i][4] - ap[4]) * lv) + coeff[i][5]);
			dgain[i] = ap;
			rgb[i] = coeff[i][6];
		}
	}

	public void forceUpdate() {
	}

	public void clearCache() {
	}

	public void computeDGainCoeffRGB(RGBPTM ptm) {

		int[][][] coeff = ptm.getCoefficients();
		Vec3f[][] normals = ptm.getChannelNormals();
		final int RED = 0, BLUE = 2;
		final int SIZE = ptm.getWidth() * ptm.getHeight();
		dgain3 = new int[SIZE][3][6];

		for (int i = 0; i < dgain3.length; i++) {

			/* the array of new coeff */
			int[][] ap = new int[3][6];

			for (int color = RED; color <= BLUE; color++) {

				float lu = normals[color][i].x();
				float lv = normals[color][i].y();

				if (Math.sqrt(lu * lu + lv * lv) > 1) {
					lu /= (Math.sqrt(lu * lu + lv * lv));
					lv /= (Math.sqrt(lu * lu + lv * lv));
				}
				float lu2 = lu * lu;
				float lv2 = lv * lv;

				ap[color][0] = (int) (DGAIN * coeff[color][i][0]);
				ap[color][1] = (int) (DGAIN * coeff[color][i][1]);
				ap[color][2] = (int) (DGAIN * coeff[color][i][2]);
				ap[color][3] = (int) (((1 - DGAIN) * ((2 * coeff[color][i][0] * lu) + (coeff[color][i][2] * lv))) + coeff[color][i][3]);
				ap[color][4] = (int) (((1 - DGAIN) * ((2 * coeff[color][i][1] * lv) + (coeff[color][i][2] * lu))) + coeff[color][i][4]);
				ap[color][5] = (int) (((1 - DGAIN) * (coeff[color][i][0] * lu2
						+ coeff[color][i][1] * lv2 + coeff[color][i][2] * lu
						* lv))
						+ ((coeff[color][i][3] - ap[color][3]) * lu)
						+ ((coeff[color][i][4] - ap[color][4]) * lv) + coeff[color][i][5]);
				dgain3[i] = ap;

			}
		}
	}

	private void RGBXform(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		if (dgain3 == null)
			computeDGainCoeffRGB((RGBPTM) ptm);

		DGAIN = ptm.getDGain();

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int red = 0, green = 0, blue = 0;
		int[] intensity = new int[3];

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

		final int[][][] _dg = dgain3;
		// final int _lum = Math.round(ptm.getLuminance()*256);

		for (int i = 0; i < localPixels.length; i++) {

			for (int j = 0; j < 3; j++) {

				intensity[j] = ((_dg[i][j][0] * _uu) >> 8)
						+ ((_dg[i][j][1] * _vv) >> 8)
						+ ((_dg[i][j][2] * _uv) >> 8)
						+ ((_dg[i][j][3] * _u) >> 8)
						+ ((_dg[i][j][4] * _v) >> 8) + (_dg[i][j][5]);
			}

			red = intensity[0];
			blue = intensity[1];
			green = intensity[2];

			if (red > 255)
				red = 255;
			if (green > 255)
				green = 255;
			if (blue > 255)
				blue = 255;

			if (red < 0)
				red = 0;
			if (green < 0)
				green = 0;
			if (blue < 0)
				blue = 0;

			localPixels[i] = (red << 16) | (green << 8) | blue;
		}
	}
}
