/*
 * Vec3f.java
 *
 * Created on September 11, 2004, 3:54 PM
 */

package jpview.graphics;

import java.text.DecimalFormat;

import jpview.Utils;

/**
 * The Vec3f class provides a vector object for 3 dimensional locations or RGB
 * pixel values. The class offers static methods for standard vector operations.
 * 
 * @author clyon
 */
public class Vec3f {

	private float[] _f = null;

	/** Creates a new instance of Vec3 */
	public Vec3f() {
		_f = new float[3];
	}

	/**
	 * Converts a 32 bit pixel value with RGB in the lower 24 bits to a Vec3f
	 * object
	 * 
	 * @return a new Vec3f object with the pixels r,b,g values as the elements
	 *         of the Vec3f
	 * @param pixel
	 *            the 32 bit pixel value
	 */
	public static Vec3f convertPixel(int pixel) {
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		float[] a = new float[] { (((float) red) * 2.0f - 255.0f) / 255.0f,
				(((float) green) * 2.0f - 255.0f) / 255.0f, (((float) blue) * 2.0f - 255.0f) / 255.0f };
		Vec3f v = new Vec3f(a);
		// v.normalize();
		return v;
	}

	/**
	 * Returns the underlying float array for the Vec3f object.
	 * 
	 * @return the underlying float [] used for the object.
	 */
	public float[] toFloat() {
		return _f;
	}

	/**
	 * Creates a new Vec3f object using the provided float [] as the underlying
	 * values.
	 * 
	 * @param f
	 *            an array of 3 float values.
	 */
	public Vec3f(float[] f) {
		_f = f;
	}

	/**
	 * Creates an new Vec3f object using the provided float values
	 * 
	 * @param x
	 *            first float value
	 * @param y
	 *            second float value
	 * @param z
	 *            third float value
	 */
	public Vec3f(float x, float y, float z) {
		_f = new float[] { x, y, z };
	}

	/**
	 * Returns the length of the Vec3f vector
	 * 
	 * @return the length of the vector as a float
	 */
	public float len() {
		return (float) Math.sqrt(dot(this));
	}

	/**
	 * Normalizes the vector. For convenience, returns itself.
	 * 
	 * @return the normalized vector.
	 */
	public Vec3f normalize() {
		float len = len();
		_f[0] /= len;
		_f[1] /= len;
		_f[2] /= len;
		return this;
	}

	/**
	 * Returns the x component of the vector (x,y,z)
	 * 
	 * @return the x component of the vector (x,y,z)
	 */
	public float x() {
		return _f[0];
	}

	/**
	 * Returns the y component of the vector (x,y,z)
	 * 
	 * @return the y component of the vector (x,y,z)
	 */
	public float y() {
		return _f[1];
	}

	/**
	 * Returns the z component of the vector (x,y,z)
	 * 
	 * @return the z component of the vector (x,y,z)
	 */
	public float z() {
		return _f[2];
	}

	/**
	 * Sets the x component of the vector (x,y,z) to the provided value
	 * 
	 * @param f
	 *            the x component of the vector (x,y,z)
	 */
	public void x(float f) {
		_f[0] = f;
	}

	/**
	 * Sets the y component of the vector (x,y,z) to the provided value
	 * 
	 * @param f
	 *            the y component of the vector (x,y,z)
	 */
	public void y(float f) {
		_f[1] = f;
	}

	/**
	 * Sets the z component of the vector (x,y,z) to the provided value
	 * 
	 * @param f
	 *            the z component of the vector (x,y,z)
	 */
	public void z(float f) {
		_f[2] = f;
	}

	/**
	 * Returns the dot product of this Vec3f and the Vec3f provided to the
	 * method
	 * 
	 * @param vector
	 *            The Vec3f object representing the right-hand side of the dot
	 *            product
	 * @return The dot product of this Vec3f and the Vec3f provided to the
	 *         method.
	 */
	public float dot(Vec3f vector) {
		return (_f[0] * vector.x() + _f[1] * vector.y() + _f[2] * vector.z());
	}

	/**
	 * Returns the cross product of this Vec3f and the Vec3f provided to the
	 * method
	 * 
	 * @return The cross product of this Vec3f and the Vec3f provided to the
	 *         method.
	 * @param v
	 *            The Vec3f object representing the right-hand side of the cross
	 *            product
	 */
	public Vec3f cross(Vec3f v) {
		return new Vec3f(y() * v.z() - z() * v.y(), z() * v.x() - x() * v.z(),
				x() * v.y() - y() * v.x());
	}

	/**
	 * Scales this vector by the provided float value
	 * 
	 * @param s
	 *            the amount by which each component of the vector is multiplied
	 * @return a new scaled vector (the underlying vector does not change)
	 */
	public Vec3f scale(float s) {
		return new Vec3f(_f[0] * s, _f[1] * s, _f[2] * s);
	}

	/**
	 * Adds two Vec3f objects together
	 * 
	 * @param v
	 *            The vector to which this vector will be added
	 * @return a new vector representing the sum of this vector and the provided
	 *         vector. (this vector is unchanged)
	 */
	public Vec3f sum(Vec3f v) {
		return new Vec3f(x() + v.x(), y() + v.y(), z() + v.z());
	}

	/**
	 * Returns the difference of two vectors.
	 * 
	 * @return This difference of this vector and the provided vector
	 * @param v
	 *            The vector representing the right-hand side of the difference.
	 */
	public Vec3f diff(Vec3f v) {
		return new Vec3f(x() - v.x(), y() - v.y(), z() - v.z());
	}

	/**
	 * Returns a string representation of this vector.
	 * 
	 * @return a human-readable String representation of this vector
	 */
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.000");
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (int i = 0; i < _f.length; i++) {
			if (i > 0)
				sb.append(", ");
			if (_f[i] >= 0)
				sb.append(" ");
			sb.append(format.format(_f[i]));
		}
		sb.append(")");
		return new String(sb);
	}

	/**
	 * Returns a vector representing the reflection of the provided incedent
	 * vector.
	 * 
	 * @param normal
	 *            The normal vector for the particular poiont.
	 * @param incedent
	 *            The vector representing incedent light.
	 * @return The reflection of the incendent vector.
	 */
	public static Vec3f reflect(Vec3f normal, Vec3f incedent) {
		// R = 2 * N.I * N - I
		if (normal == null || incedent == null)
			return null;
		Vec3f lhs = normal.scale(normal.dot(incedent) * 2);
		return lhs.diff(incedent);

	}

	/**
	 * Returns true if this vector is the same as the provided vector
	 * 
	 * @param that
	 *            The vector to be compared with this vector
	 * @return true if the this vector and the provided vector are equivalent.
	 */
	public boolean equals(Vec3f that)
	{
		return (this.x() == that.x() && this.y() == that.y() && this.z() == that.z());
	}

	/**
	 * Returns a representation of this vector as a colored pixel value, with
	 * RGB in the lower 24 bits.
	 * 
	 * @return The 32 bit pixel representation of this vector.
	 */
	public int toPixel() {
		float r = ((_f[0] + 1.0f) / 2.0f) * 255.0f;
		float g = ((_f[1] + 1.0f) / 2.0f) * 255.0f;
		float b = ((_f[2] + 1.0f) / 2.0f) * 255.0f;
		int red = Utils.clamp(Math.round(r));
		int green = Utils.clamp(Math.round(g));
		int blue = Utils.clamp(Math.round(b));
		return red << 16 | green << 8 | blue;
	}

	/**
	 * Perturbs the normal slightly
	 */
	public void perturb() {
		_f[0] += _f[0] * (Math.random() - 0.5f) / 10.0f;
		_f[1] += _f[1] * (Math.random() - 0.5f) / 10.0f;
		_f[2] += _f[2] * (Math.random() - 0.5f) / 10.0f;
		this.normalize();
	}
}
