package com.ch.math;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

/**
 * Represents a quaternion mathematical object used for 3D rotations, providing methods
 * for creating, manipulating, and converting quaternions to and from rotation matrices
 * and Euler angles.
 */
public class Quaternion {

	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion() {
		this(0, 0, 0, 0);
	}

	
	public Quaternion(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quaternion(Vector3f axis, float angle) {
		float sinHalfAngle = (float) Math.sin(angle / 2);
		float cosHalfAngle = (float) Math.cos(angle / 2);

		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}

	/**
	 * Calculates the Euclidean distance of a point in 4-dimensional space. It takes the
	 * square root of the sum of squares of its coordinates (x, y, z, w) and returns the
	 * result as a floating-point number.
	 *
	 * @returns the Euclidean distance of a 4-dimensional point from the origin.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * Calculates the length of a quaternion, then divides each component by this length
	 * to normalize it.
	 *
	 * @returns a new Quaternion with components scaled by the reciprocal of the original
	 * Quaternion's length.
	 */
	public Quaternion normalized() {
		float length = length();

		return new Quaternion(w / length, x / length, y / length, z / length);
	}

	/**
	 * Returns the conjugate of the quaternion, which is a new quaternion with the same
	 * real part and opposite imaginary parts.
	 *
	 * @returns a Quaternion object with the same w component and negated x, y, and z components.
	 */
	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}

	/**
	 * Multiplies the current Quaternion by a scalar value, returning a new Quaternion
	 * with the components scaled by the given factor. The original Quaternion remains
	 * unchanged. The function takes a single float parameter, r.
	 *
	 * @param r scalar value to be multiplied with each component of the Quaternion.
	 *
	 * @returns a new quaternion resulting from the multiplication of the current quaternion
	 * by a scalar factor `r`.
	 */
	public Quaternion mul(float r) {
		return new Quaternion(w * r, x * r, y * r, z * r);
	}

	/**
	 * Calculates the product of two quaternions. It takes another quaternion as input,
	 * performs quaternion multiplication, and returns a new quaternion as a result. The
	 * product is computed based on the quaternion multiplication formula.
	 *
	 * @param r second quaternion to be multiplied with the current quaternion.
	 *
	 * @returns a new Quaternion object resulting from the multiplication of the input Quaternions.
	 */
	public Quaternion mul(Quaternion r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(w_, x_, y_, z_);
	}

	/**
	 * Performs a quaternion-vector multiplication, updating the quaternion's components
	 * based on the input vector's components. The resulting quaternion represents the
	 * rotation of the input vector by the original quaternion. The function returns a
	 * new quaternion object with the updated components.
	 *
	 * @param r vector to be multiplied with the quaternion, affecting the rotation of
	 * the quaternion.
	 *
	 * @returns a Quaternion resulting from the multiplication of the input Quaternion
	 * and Vector3f.
	 */
	public Quaternion mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(w_, x_, y_, z_);
	}

	/**
	 * Subtracts the corresponding components of the given Quaternion `r` from the current
	 * Quaternion, returning a new Quaternion with the resulting components.
	 *
	 * @param r right-hand side operand in the subtraction operation of two quaternions.
	 *
	 * @returns a new Quaternion representing the difference between the current Quaternion
	 * and the passed Quaternion.
	 */
	public Quaternion sub(Quaternion r) {
		return new Quaternion(w - r.getW(), x - r.getX(), y - r.getY(), z - r.getZ());
	}

	/**
	 * Performs element-wise addition of two quaternions w, x, y, z and r.w, r.x, r.y,
	 * r.z. It returns a new quaternion with the sum of corresponding elements. The
	 * original quaternions remain unchanged.
	 *
	 * @param r second quaternion to be added to the first quaternion.
	 *
	 * @returns a new Quaternion object representing the sum of the input Quaternion and
	 * the parameter Quaternion r.
	 */
	public Quaternion add(Quaternion r) {
		return new Quaternion(w + r.getW(), x + r.getX(), y + r.getY(), z + r.getZ());
	}

	/**
	 * Converts a 4x4 homogeneous transformation matrix into a rotation matrix, extracting
	 * the forward, up, and right vectors from it.
	 *
	 * @returns a rotation matrix representing the orientation of a 4D vector.
	 */
	public Matrix4f toRotationMatrix() {
		Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
		Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
		Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

		return new Matrix4f().initRotation(forward, up, right);
	}

	/**
	 * Calculates the dot product of two quaternions, combining their corresponding components.
	 *
	 * @param r Quaternion object with which the current Quaternion object's components
	 * are being dotted.
	 *
	 * @returns the dot product of the current quaternion and the input quaternion.
	 */
	public float dot(Quaternion r) {
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}

	/**
	 * Performs spherical linear interpolation between two quaternions. It returns the
	 * interpolated quaternion based on a given lerp factor and an optional shortest arc
	 * flag.
	 *
	 * @param dest destination quaternion value used in the spherical linear interpolation.
	 *
	 * @param lerpFactor amount of interpolation between the current quaternion and the
	 * destination quaternion.
	 *
	 * @param shortest option to find the shortest arc between two quaternions, which is
	 * achieved by inverting the destination quaternion if they are in opposite directions.
	 *
	 * @returns a normalized quaternion resulting from a spherical linear interpolation
	 * of the input quaternion.
	 */
	public Quaternion NLerp(Quaternion dest, float lerpFactor, boolean shortest) {
		Quaternion correctedDest = dest;

		if (shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.getW(), -dest.getX(), -dest.getY(), -dest.getZ());

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
	}

	/**
	 * Performs spherical linear interpolation between two quaternions. It calculates the
	 * interpolation factor based on the angle between the two quaternions and the specified
	 * lerp factor, ensuring a smooth interpolation with minimal distortion.
	 *
	 * @param dest destination quaternion in the spherical linear interpolation (Slerp)
	 * operation.
	 *
	 * Define. The `dest` is a Quaternion object, which has four properties: w, x, y, z.
	 *
	 * @param lerpFactor fraction of the angular distance between the two quaternions to
	 * interpolate.
	 *
	 * @param shortest option to select the shortest path between the current quaternion
	 * and the destination quaternion in the 3D space.
	 *
	 * @returns a quaternion interpolated between the current quaternion and the destination
	 * quaternion.
	 *
	 * The returned output is a Quaternion object.
	 */
	public Quaternion SLerp(Quaternion dest, float lerpFactor, boolean shortest) {
		final float EPSILON = 1e3f;

		float cos = this.dot(dest);
		Quaternion correctedDest = dest;

		if (shortest && cos < 0) {
			cos = -cos;
			correctedDest = new Quaternion(-dest.getW(), -dest.getX(), -dest.getY(), -dest.getZ());
		}

		if (Math.abs(cos) >= 1 - EPSILON)
			return NLerp(correctedDest, lerpFactor, false);

		float sin = (float) Math.sqrt(1.0f - cos * cos);
		float angle = (float) Math.atan2(sin, cos);
		float invSin = 1.0f / sin;

		float srcFactor = (float) Math.sin((1.0f - lerpFactor) * angle) * invSin;
		float destFactor = (float) Math.sin((lerpFactor) * angle) * invSin;

		return this.mul(srcFactor).add(correctedDest.mul(destFactor));
	}

	// From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
	public Quaternion(Matrix4f rot) {
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

		if (trace > 0) {
			float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		} else {
			if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			} else if (rot.get(1, 1) > rot.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			} else {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0)) / s;
				x = (rot.get(2, 0) + rot.get(0, 2)) / s;
				y = (rot.get(1, 2) + rot.get(2, 1)) / s;
				z = 0.25f * s;
			}
		}

		float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}

	/**
	 * Rotates a unit vector (0, 0, 1) by the current object's rotation, returning the
	 * resulting forward vector. The resulting vector represents the direction the object
	 * is facing. The vector is created anew each time the function is called.
	 *
	 * @returns a Vector3f object representing the direction forward from the current
	 * object's orientation.
	 */
	public Vector3f getForward() {
		return new Vector3f(0, 0, 1).rotate(this);
	}

	/**
	 * Rotates a given vector by the instance's rotation and returns a new vector
	 * representing the direction behind the instance, equivalent to (0, 0, -1) in the
	 * world space.
	 *
	 * @returns a `Vector3f` representing the unit vector pointing in the backward direction
	 * relative to the object's current orientation.
	 */
	public Vector3f getBack() {
		return new Vector3f(0, 0, -1).rotate(this);
	}

	/**
	 * Rotates a given Vector3f around the object this is referring to, resulting in a
	 * new Vector3f representing the upward direction. The rotation is based on the
	 * object's orientation.
	 *
	 * @returns a Vector3f representing the direction of the up vector relative to the
	 * object's current orientation.
	 */
	public Vector3f getUp() {
		return new Vector3f(0, 1, 0).rotate(this);
	}

	/**
	 * Rotates a Vector3f down by the current object's rotation, returning the resulting
	 * Vector3f.
	 *
	 * @returns a Vector3f object representing the down direction relative to the object's
	 * orientation.
	 */
	public Vector3f getDown() {
		return new Vector3f(0, -1, 0).rotate(this);
	}

	/**
	 * Returns a new Vector3f that is the right vector of the current object, calculated
	 * by rotating the unit right vector (1, 0, 0) by the object's rotation.
	 *
	 * @returns a Vector3f object representing the right direction of the input vector
	 * after rotation.
	 */
	public Vector3f getRight() {
		return new Vector3f(1, 0, 0).rotate(this);
	}

	/**
	 * Returns a new Vector3f instance that is the result of rotating the local x-axis
	 * by the object's rotation, effectively giving the left direction. This is done by
	 * creating a Vector3f instance representing the local x-axis and then applying the
	 * object's rotation to it. The resulting vector represents the left direction.
	 *
	 * @returns a Vector3f object rotated by the current object's rotation.
	 */
	public Vector3f getLeft() {
		return new Vector3f(-1, 0, 0).rotate(this);
	}

	/**
	 * Assigns the specified values to the quaternion's components (x, y, z, w) and returns
	 * the current object.
	 *
	 * @param x x-component of a quaternion, assigning it to the `x` field of the current
	 * Quaternion instance.
	 *
	 * @param y y-coordinate of the quaternion, which is a fundamental component in 3D
	 * mathematical transformations.
	 *
	 * @param z third component of the quaternion, typically representing rotation or
	 * transformation along the z-axis.
	 *
	 * @param w real component of the quaternion, which is a mathematical object used to
	 * describe 3D rotations.
	 *
	 * @returns an instance of the Quaternion class with the specified values.
	 */
	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	/**
	 * @param eulerAngles - @see <a href="https://en.wikipedia.org/wiki/Euler_angles#Proper_Euler_angles">Wikipedia's Article on Euler Angles</a> for a description
	 *                    of their usage/definition.
	 * @return The {@link Quaternion} associated with the Euler angles.
	 */
	public static Quaternion fromEuler(Vector3f eulerAngles) {
		//eulerAngles = [phi, theta, yota]
		float phi = eulerAngles.getX();
		float theta = eulerAngles.getY();
		float yota = eulerAngles.getZ();


		//locally store all cos/sin so we don't have to calculate them twice each
		float cos_half_phi = (float) Math.cos(phi / 2.0f);
		float sin_half_phi = (float) Math.sin(phi / 2.0f);
		float cos_half_theta = (float) Math.cos(theta / 2.0f);
		float sin_half_theta = (float) Math.sin(theta / 2.0f);
		float cos_half_yota = (float) Math.cos(yota / 2.0f);
		float sin_half_yota = (float) Math.sin(yota / 2.0f);

		float q0 = cos_half_phi * cos_half_theta * cos_half_yota + sin_half_phi * sin_half_theta * sin_half_yota;
		float q1 = sin_half_phi * cos_half_theta * cos_half_yota - cos_half_phi * sin_half_theta * sin_half_yota;
		float q2 = cos_half_phi * sin_half_theta * cos_half_yota + sin_half_phi * cos_half_theta * sin_half_yota;
		float q3 = cos_half_phi * cos_half_theta * sin_half_yota - sin_half_phi * sin_half_theta * cos_half_yota;

		return new Quaternion(q0, q1, q2, q3);

	}

	/**
	 * Assigns the components of a given Quaternion `r` to the current Quaternion instance,
	 * allowing for chaining method calls.
	 *
	 * @param r Quaternion object from which the components are to be retrieved.
	 *
	 * @returns this quaternion, with its values updated to match the input quaternion `r`.
	 */
	public Quaternion set(Quaternion r) {
		set(r.getX(), r.getY(), r.getZ(), r.getW());
		return this;
	}

	/**
	 * Returns the value of the `x` variable as a float.
	 *
	 * @returns the value of the `x` variable, a float.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Assigns a float value to the instance variable `x` of the class. The value is
	 * directly stored in the variable without any validation or modification. This
	 * function updates the state of the object.
	 *
	 * @param x new value for the object's instance variable `x`, which is updated to
	 * this value.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the value of the variable `y` as a float.
	 *
	 * @returns the value of the private float variable `y`.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Updates the object's y-coordinate with a specified floating-point value. The new
	 * value replaces the existing y-coordinate, allowing for dynamic modification.
	 *
	 * @param y value to be assigned to the instance variable `y`.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the value of the variable `z` as a floating-point number.
	 *
	 * @returns the value of the instance variable `z` of type `float`.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * updates a floating-point value representing the z-coordinate.
	 * It assigns the provided `z` value to the instance variable `z`.
	 *
	 * @param z value to be assigned to the instance variable `z`.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns the value of the variable `w` as a floating-point number.
	 *
	 * @returns a float value representing the attribute `w`.
	 */
	public float getW() {
		return w;
	}

	/**
	 * Sets a float value to the `w` instance variable. It assigns the input `w` parameter
	 * to the `w` field. This allows modification of the `w` value.
	 *
	 * @param w width that is being set.
	 */
	public void setW(float w) {
		this.w = w;
	}

	/**
	 * Compares two Quaternion objects for equality based on their components. It checks
	 * if the x, y, z, and w values of the current object match the corresponding values
	 * of the passed Quaternion object.
	 *
	 * @param r Quaternion object being compared for equality with the current object.
	 *
	 * @returns a boolean value indicating whether the Quaternion object is equal to the
	 * parameter Quaternion.
	 */
	public boolean equals(Quaternion r) {
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}
	
}
