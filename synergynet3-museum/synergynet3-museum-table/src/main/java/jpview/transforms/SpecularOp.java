/*
 * ColorChannelOp.java
 *
 * Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import java.awt.Point;

import jpview.Utils;
import jpview.graphics.Vec3f;
import jpview.ptms.LRGBPTM;
import jpview.ptms.PTM;
import jpview.ptms.RGBPTM;

/**
 * 
 * @author clyon
 */
public final class SpecularOp implements PixelTransformOp {

	public Vec3f[] normals = null;

	private float[] kCache = null;

	private Point cachedMouse = new Point(-1, -1);

	private boolean forceUpdate = false;

	private int cachedExp = -1;

	private float[] phong = new float[1000];

	public void forceUpdate() {
		forceUpdate = true;
	}

	public void clearCache() {
		kCache = null;
		cachedMouse = null;
	}

	public void release() {
		if (normals != null) {
			for (int i = 0; i < normals.length; i++)
				normals[i] = null;
			normals = null;
		}
		kCache = null;
		cachedMouse = null;
	}

	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		boolean beFast = false;
		if (pixels.length < ptm.getHeight() * ptm.getWidth())
			beFast = true;

		if (normals == null)
			normals = ptm.getNormals();

		switch (ptm.getType()) {
		case PTM.LRGB:
			if (!beFast)
				LRGBXform(pixels, ptm, mouseX, mouseY);
			else
				LRGBXformFast(pixels, ptm, mouseX, mouseY);
			break;
		case PTM.RGB:
			RGBXform(pixels, ptm, mouseX, mouseY);
		case PTM.PRIMITIVE:
		/* TODO: simple grey color */
		}
	}

	private void RGBXform(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int red = 0, green = 0, blue = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		final float KSPEC = ptm.getKSpec() * 3;
		final int KDIFF = Math.round(ptm.getKDiff() * 256);
		final int EXP = ptm.getExp();

		if (EXP != this.cachedExp) {
			for (int i = 0; i < phong.length; i++) {
				phong[i] = (float) Math.pow(((float) i) / 1000, EXP);
			}
			cachedExp = EXP;
		}

		final RGBPTM localPtm = (RGBPTM) ptm;

		Vec3f[][] __normals = localPtm.getChannelNormals();

		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);

		final int[][][] coeff = localPtm.getCoefficients();

		float inner = 1 - uu - vv;

		float _w = (inner > 0) ? (float) Math.sqrt(inner) : 0f;

		final float[] L = Utils.normalize3(new float[] { u, v, _w });

		final Vec3f _L = new Vec3f(L);

		final float[] V = new float[] { 0, 0, 1 };

		final float[] H = Utils.normalize3
				(Utils.scalarMult3(Utils.vecSum3(L, V), 0.5f));

		final Vec3f _H = new Vec3f(H);

		final int _lum = Math.round(ptm.getLuminance() * 256);

		float nDotH;

		for (int i = 0; i < localPixels.length; i++) {

			float k_red, k_green, k_blue;

			Vec3f N_red = __normals[0][i];
			nDotH = Math.min(1f, Math.max(0f, N_red.dot(_H)));
			/* k = (float) Math.pow(nDotH,EXP); */
			k_red = phong[Math.round(nDotH * 999)];

			Vec3f N_green = __normals[1][i];
			nDotH = Math.min(1f, Math.max(0f, N_green.dot(_H)));
			k_green = phong[Math.round(nDotH * 999)];

			Vec3f N_blue = __normals[2][i];
			nDotH = Math.min(1f, Math.max(0f, N_blue.dot(_H)));
			k_blue = phong[Math.round(nDotH * 999)];

			red = ((coeff[0][i][0] * _uu) >> 8) + ((coeff[0][i][1] * _vv) >> 8)
					+ ((coeff[0][i][2] * _uv) >> 8)
					+ ((coeff[0][i][3] * _u) >> 8)
					+ ((coeff[0][i][4] * _v) >> 8) + (coeff[0][i][5]);

			green = ((coeff[1][i][0] * _uu) >> 8)
					+ ((coeff[1][i][1] * _vv) >> 8)
					+ ((coeff[1][i][2] * _uv) >> 8)
					+ ((coeff[1][i][3] * _u) >> 8)
					+ ((coeff[1][i][4] * _v) >> 8) + (coeff[1][i][5]);

			blue = ((coeff[2][i][0] * _uu) >> 8)
					+ ((coeff[2][i][1] * _vv) >> 8)
					+ ((coeff[2][i][2] * _uv) >> 8)
					+ ((coeff[2][i][3] * _u) >> 8)
					+ ((coeff[2][i][4] * _v) >> 8) + (coeff[2][i][5]);

			red = Math.round(((red * KDIFF) >> 8) * N_red.dot(_L)
					+ (k_red * KSPEC * red));

			green = Math.round(((green * KDIFF) >> 8) * N_green.dot(_L)
					+ (k_green * KSPEC * green));

			blue = Math.round(((blue * KDIFF) >> 8) * N_blue.dot(_L)
					+ (k_blue * KSPEC * blue));

			red = (red * _lum) >> 8;
			green = (green * _lum) >> 8;
			blue = (blue * _lum) >> 8;

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
		forceUpdate = false;
	}

	public void transformPixels(int[] pixels, PTM ptm) {
		transformPixels(pixels, ptm, ptm.getWidth() / 4, ptm.getHeight() / 4);
	}

	private void LRGBXform(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		boolean useCache = false;
		Point mousePos = new Point(mouseX, mouseY);
		if (mousePos.equals(cachedMouse) && (kCache != null)) {
			useCache = true;
		} else {
			cachedMouse = mousePos; /* next time */
			kCache = null;
			kCache = new float[pixels.length];
		}

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int intensity = 0, red = 0, green = 0, blue = 0, pixel = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		final float KSPEC = ptm.getKSpec();
		final int KDIFF = Math.round(ptm.getKDiff() * 256);
		final int EXP = ptm.getExp();

		if (EXP != this.cachedExp) {
			for (int i = 0; i < phong.length; i++) {
				phong[i] = (float) Math.pow(((float) i) / 1000, EXP);
			}
			cachedExp = EXP;
		}

		final LRGBPTM localPtm = (LRGBPTM) ptm;
		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][] coeff = localPtm.getCoefficients();

		float inner = 1 - uu - vv;
		float _w = (inner > 0) ? (float) Math.sqrt(inner) : 0f;

		final float[] L = Utils.normalize3(new float[] { u, v, _w });
		final float[] V = new float[] { 0, 0, 1 };
		final float[] H = Utils.normalize3(Utils.scalarMult3(Utils
				.vecSum3(L, V), 0.5f));
		final Vec3f _H = new Vec3f(H);
		final int _lum = Math.round(ptm.getLuminance() * 256);

		float nDotH;
		float k;
		int[] map = null;
		boolean useEnv = false;
		int[] rotatedEnv = null; 
		if (ptm.getEnvironmentMap() != null) {
			useEnv = true;
			map = ((LRGBPTM) ptm).getEnvironmentMapMap();
			rotatedEnv = ptm.getEnvironmentMap().rotatedMap();
		}
		int _er = 255, _eg = 255, _eb = 255;

		if (useCache && kCache == null) {
			kCache = new float[localPixels.length];
		}

		for (int i = 0; i < localPixels.length; i++) {

			if (useCache && kCache != null && !forceUpdate) {
				k = kCache[i];
			} else {
				Vec3f N = normals[i];
				nDotH = Math.min(1f, Math.max(0f, N.dot(_H)));
				/* k = (float) Math.pow(nDotH,EXP); */
				k = phong[Math.round(nDotH * 999)];
				kCache[i] = k;

			}

			intensity = ((coeff[i][0] * _uu) >> 8) + ((coeff[i][1] * _vv) >> 8)
					+ ((coeff[i][2] * _uv) >> 8) + ((coeff[i][3] * _u) >> 8)
					+ ((coeff[i][4] * _v) >> 8) + (coeff[i][5]);

			pixel = coeff[i][6];

			red = (pixel >> 16) & 0xff;
			green = (pixel >> 8) & 0xff;
			blue = (pixel) & 0xff;

			if (useEnv) {
				int pix = rotatedEnv[map[i]];
				_er = (pix >> 16) & 0xff;
				_eg = (pix >> 8) & 0xff;
				_eb = pix & 0xff;
			}

			red = Math.round(((((red * intensity * KDIFF) >> 8) * _er) >> 8)
					+ (_er * k * KSPEC * intensity));

			green = Math
					.round(((((green * intensity * KDIFF) >> 8) * _eg) >> 8)
							+ (_eg * k * KSPEC * intensity));
			blue = Math.round(((((blue * intensity * KDIFF) >> 8) * _eb) >> 8)
					+ (_eb * k * KSPEC * intensity));

			red = (red * _lum) >> 8;
			green = (green * _lum) >> 8;
			blue = (blue * _lum) >> 8;

			red >>= 8;
			green >>= 8;
			blue >>= 8;

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
		forceUpdate = false;
	}

	private void LRGBXformFast(int[] pixels, PTM ptm, int mouseX, int mouseY) {

		boolean useCache = false;
		Point mousePos = new Point(mouseX, mouseY);
		if (mousePos.equals(cachedMouse) && (kCache != null)) {
			useCache = true;
		} else {
			cachedMouse = mousePos; /* next time */
			kCache = null;
			kCache = new float[pixels.length];
		}

		int[] localPixels = pixels;
		int w = ptm.getWidth() / 2;
		int h = ptm.getHeight() / 2;
		int intensity = 0, red = 0, green = 0, blue = 0, pixel = 0;

		float u = ((float) mouseX - w) / w;
		float v = -((float) mouseY - h) / h;
		float uu = u * u;
		float vv = v * v;
		float uv = u * v;

		final float KSPEC = ptm.getKSpec();
		final int KDIFF = Math.round(ptm.getKDiff() * 256);
		final int EXP = ptm.getExp();

		if (EXP != this.cachedExp) {
			for (int i = 0; i < phong.length; i++) {
				phong[i] = (float) Math.pow(((float) i) / 1000, EXP);
			}
			cachedExp = EXP;
		}

		final LRGBPTM localPtm = (LRGBPTM) ptm;
		final int _u = Math.round(u * 256);
		final int _v = Math.round(v * 256);
		final int _uu = Math.round(uu * 256);
		final int _vv = Math.round(vv * 256);
		final int _uv = Math.round(uv * 256);
		final int[][] coeff = localPtm.getCoefficients();

		float inner = 1 - uu - vv;
		float _w = (inner > 0) ? (float) Math.sqrt(inner) : 0f;

		final float[] L = Utils.normalize3(new float[] { u, v, _w });
		final float[] V = new float[] { 0, 0, 1 };
		final float[] H = Utils.normalize3(Utils.scalarMult3(Utils
				.vecSum3(L, V), 0.5f));
		final Vec3f _H = new Vec3f(H);
		final int _lum = Math.round(ptm.getLuminance() * 256);

		float nDotH;
		float k;
		int[] map = null;
		boolean useEnv = false;
		int[] rotatedEnv = null; 
		if (ptm.getEnvironmentMap() != null) {
			useEnv = true;
			map = ((LRGBPTM) ptm).getEnvironmentMapMap();
			rotatedEnv = ptm.getEnvironmentMap().rotatedMap();
		}
		int _er = 255, _eg = 255, _eb = 255;

		if (useCache && kCache == null) {
			kCache = new float[localPixels.length];
		}

		if (kCache.length < localPixels.length) {
			kCache = new float[localPixels.length];
		}

		int pixelIndex = 0;

		int height = ptm.getHeight();
		int width = ptm.getWidth();

		for (int y = 0; y < height; y += 2) {
			for (int x = 0; x < width; x += 2) {
				int i = y * width + x;

				if (useCache && kCache != null && !forceUpdate) {
					k = kCache[pixelIndex];
				} else {
					Vec3f N = normals[i];
					nDotH = Math.min(1f, Math.max(0f, N.dot(_H)));
					k = phong[Math.round(nDotH * 999)];
					kCache[pixelIndex] = k;
				}

				intensity = ((coeff[i][0] * _uu) >> 8)
						+ ((coeff[i][1] * _vv) >> 8)
						+ ((coeff[i][2] * _uv) >> 8)
						+ ((coeff[i][3] * _u) >> 8) + ((coeff[i][4] * _v) >> 8)
						+ (coeff[i][5]);

				pixel = coeff[i][6];

				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				if (useEnv) {
					int pix = rotatedEnv[map[i]];
					_er = (pix >> 16) & 0xff;
					_eg = (pix >> 8) & 0xff;
					_eb = pix & 0xff;
				}

				red = Math
						.round(((((red * intensity * KDIFF) >> 8) * _er) >> 8)
								+ (_er * k * KSPEC * intensity));
				green = Math
						.round(((((green * intensity * KDIFF) >> 8) * _eg) >> 8)
								+ (_eg * k * KSPEC * intensity));
				blue = Math
						.round(((((blue * intensity * KDIFF) >> 8) * _eb) >> 8)
								+ (_eb * k * KSPEC * intensity));

				red = (red * _lum) >> 8;
				green = (green * _lum) >> 8;
				blue = (blue * _lum) >> 8;

				red >>= 8;
				green >>= 8;
				blue >>= 8;

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

				localPixels[pixelIndex++] = (red << 16) | (green << 8) | blue;
			}
		}
		forceUpdate = false;
	}
}