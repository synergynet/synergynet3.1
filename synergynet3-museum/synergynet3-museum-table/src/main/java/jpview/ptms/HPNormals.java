/*
 * ColorChannelOp.java
 *
 * Created on September 5, 2004, 9:41 PM
 */

package jpview.ptms;

import jpview.Utils;
import jpview.graphics.GraphicsGems;
import jpview.graphics.Vec3f;

/**
 * Original code by Hans Wolters Java port by Cliff Lyon
 */
public class HPNormals {

	public static final double zerotol = 1.0e-5;

	public static final double eps = zerotol;

	public static double evalPoly(double[] a, double t) {
		double u = 2 * t / (1 + t * t);
		double v = (1 - t * t) / (1 + t * t);
		return a[0] * u * u + a[1] * v * v + a[2] * u * v + a[3] * u + a[4] * v
				+ a[5];
	}

	public static int computeMaximumOnCircle(double[] a, double[] normal) {
		double db0, db1, db2, db3, db4;
		double val1;
		double[] zeros = new double[4];
		double u, v, maxval, maxu = -1, maxv = -1, inc, arg, polyval;
		int index, nroots;

		index = -1;
		nroots = -1;

		db0 = a[2] - a[3];
		db1 = 4 * a[1] - 2 * a[4] - 4 * a[0];
		db2 = -6 * a[2];
		db3 = -4 * a[1] - 2 * a[4] + 4 * a[0];
		db4 = a[2] + a[3];

		/** polynomial is constant on circle, pick (0,1) as a solution */
		if (Math.abs(db0) < zerotol && Math.abs(db1) < zerotol
				&& Math.abs(db2) < zerotol && Math.abs(db3) < zerotol) {
			normal[0] = 0.0;
			normal[1] = 1.0;
			return 1;
		}

		if (db0 != 0) {
			double[] c = new double[] { db4, db3, db2, db1, db0 };
			nroots = GraphicsGems.SolveQuartic(c, zeros);
		} else if (db1 != 0) {
			double[] c = new double[] { db4, db3, db2, db1 };
			nroots = GraphicsGems.SolveCubic(c, zeros);
		} else /** TODO case where db2 is zero */
		{
			double[] c = new double[] { db4, db3, db2 };
			nroots = GraphicsGems.SolveQuadric(c, zeros);
		}
		if (nroots <= 0) {
			return -1;
		}

		switch (nroots) {
		case 1:
			index = 0;
			break;
		default:
			double[] vals = new double[nroots];
			for (int i = 0; i < vals.length; i++) {
				vals[i] = evalPoly(a, zeros[i]);
			}
			index = Utils.indexOfMax(vals);
		}

		/**
		 * I noticed that the fact that the pont (0,-1) on the circle can only
		 * be attained in the limit causes it to be missed in case it really is
		 * the maximum. Hence it is necessary to investigate a neighboring
		 * region to find the potential maximum there, we look at the segment
		 * from 260 degress to 280 degrees (270 degrees being the limit point).
		 */

		normal[0] = 2 * zeros[index] / (1 + zeros[index] * zeros[index]);
		normal[1] = (1 - zeros[index] * zeros[index])
				/ (1 + zeros[index] * zeros[index]);

		/**
		 * test the correctness of solution:
		 */

		maxval = -1000;

		for (int k = 0; k <= 20; k++) {
			inc = (1 / 9.0) / 20 * k;
			arg = Math.PI * (26.0 / 18.0 + inc);
			u = Math.cos(arg);
			v = Math.sin(arg);
			polyval = a[0] * u * u + a[1] * v * v + a[2] * u * v + a[3] * u
					+ a[4] * v + a[5];
			if (maxval < polyval) {
				maxval = polyval;
				maxu = u;
				maxv = v;
			}
		}

		val1 = evalPoly(a, zeros[index]);
		if (maxval > val1) {
			normal[0] = maxu;
			normal[1] = maxv;
		}
		return 1;
	}

	public static Vec3f[] getNormals(LRGBPTM ptm) {
		return HPNormals.getNormals(ptm.getCoefficients());
	}

	public static Vec3f[] getNormals(int[][] coeff) {

		Vec3f[] normals = new Vec3f[coeff.length];
		double[] a;
		double length2d;
		double disc;
		int stat;
		int maxfound;

		for (int i = 0; i < normals.length; i++) {
			double[] normal = new double[3];
			a = new double[6];
			a[0] = (double) coeff[i][0];
			a[1] = (double) coeff[i][1];
			a[2] = (double) coeff[i][2];
			a[3] = (double) coeff[i][3];
			a[4] = (double) coeff[i][4];
			a[5] = (double) coeff[i][5];

			/* now remove factor of 256 in scaling values */
			a[0] /= 256;
			a[1] /= 256;
			a[2] /= 256;
			a[3] /= 256;
			a[4] /= 256;
			a[5] /= 256;

			/*
			 * These coordinates are where the first deriviative of the
			 * polynominal is zero
			 */
			/* Derivation in lab notebook 2951-43 */
			/*
			 * normal[0] = (2 * a[1] * a[3] - (a[4] / a[2]) ) / (1.0 - 4 * a[0] *
			 * a[1]);
			 */

			/** zero denominator in upcoming computations */
			if (Math.abs(a[2] * a[2] - 4 * a[1] * a[0]) < zerotol) {
				normal[0] = 0;
				normal[1] = 0;
			} else {
				if (Math.abs(a[2]) < zerotol) {
					normal[0] = -1.0 * a[3] / (2.0 * a[0]);
					normal[1] = -1.0 * a[4] / (2.0 * a[1]);
				} else {
					normal[0] = (2.0 * a[1] * a[3] - a[2] * a[4])
							/ (a[2] * a[2] - 4.0 * a[1] * a[0]);
					normal[1] = (-2.0 * a[0] * normal[0] - a[3]) / a[2];
				}
			}

			/** polynomial is constant we are done, set normal to be at 0,0,1 */
			if (Math.abs(a[0]) < zerotol && Math.abs(a[1]) < zerotol
					&& Math.abs(a[2]) < zerotol && Math.abs(a[3]) < zerotol
					&& Math.abs(a[4]) < zerotol) {
				normal[0] = 0.0;
				normal[1] = 0.0;
				normal[2] = 1.0;
			}
			/**
			 * clip normal[0], normal[1] values - these can both be unbounded
			 * theoretically. first check if the vector (normal[0],normal[1]) is
			 * greater than 1
			 */
			else {
				length2d = normal[0] * normal[0] + normal[1] * normal[0];

				/*
				 * Add check for saddle or minimum. if p_uu >0 and/or
				 * p_uu*p_vv-p_uv*p_uv <0 -> saddle or minimum if this is the
				 * case then we should always look at boundary
				 */
				if (4 * a[0] * a[1] - a[2] * a[2] > eps && a[0] < -eps)
					maxfound = 1;
				else
					maxfound = 0;

				/**
				 * Changed by Hans J. Wolters 12-11-99: instead of clipping, we
				 * attempt to find the minimum value on the circle. we know that
				 * since the polynomial is monotonous in the interior the
				 * extrema must be attained at the boundary. The circle will be
				 * parametrized by x = 2t/(1+t^2), y = (1-t^2)/(1+t^2). by using
				 * Maple we derived the polynomial in t and subsequently its
				 * derivative wrt t, a quartic. The task is to find the real
				 * root(s) of this polynomial. We denote the 3 coefficients as
				 * db0,db1,db2,db3, db4 For precise derivation see Hans Wolters'
				 * lab book
				 */

				if (length2d > 1 - eps || maxfound == 0) {
					stat = computeMaximumOnCircle(a, normal);
					if (stat == -1) // failed
					{
						length2d = Math.sqrt(length2d);
						if (length2d > zerotol) {
							normal[0] /= length2d;
							normal[1] /= length2d;
						}
					}
				}
				disc = 1.0 - normal[0] * normal[0] - normal[1] * normal[1];
				if (disc < 0.0) {
					normal[2] = 0;
				} else {
					normal[2] = (float) Math.sqrt(disc);
				}
			}
			normal = Utils.normalize3(normal);
			normals[i] = new Vec3f((float) normal[0], (float) normal[1],
					(float) normal[2]);
		}
		return normals;
	}
}
