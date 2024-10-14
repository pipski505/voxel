package com.ch.math;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

/**
 * Represents a 3D rotation using quaternions, providing various methods for creation,
 * manipulation, and transformation of quaternions, as well as conversions to and
 * from other data types such as Euler angles and rotation matrices.
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
	 * Calculates the Euclidean norm of a 4-dimensional vector, returning the square root
	 * of the sum of squares of its components.
	 *
	 * @returns the Euclidean distance of a point in 4-dimensional space from the origin.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * Calculates the magnitude of the current Quaternion, then returns a new Quaternion
	 * where each component is divided by the magnitude, effectively normalizing the
	 * Quaternion to a unit length.
	 *
	 * @returns a new Quaternion with components normalized to a length of 1.
	 */
	public Quaternion normalized() {
		float length = length();

		return new Quaternion(w / length, x / length, y / length, z / length);
	}

	/**
	 * Returns a new `Quaternion` with the sign of the real part (`w`) changed and the
	 * signs of the imaginary parts (`x`, `y`, `z`) inverted.
	 *
	 * @returns a new Quaternion with the sign of the real part (w) unchanged and the
	 * signs of the imaginary parts (x, y, z) reversed.
	 */
	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}

	/**
	 * Multiplies the current Quaternion by a scalar value `r`, scaling each component
	 * (w, x, y, z) by the given factor. The result is a new Quaternion object with the
	 * scaled components. The original Quaternion remains unchanged.
	 *
	 * @param r scalar value by which the quaternion's components are multiplied.
	 *
	 * @returns a new Quaternion object scaled by the input float value r.
	 */
	public Quaternion mul(float r) {
		return new Quaternion(w * r, x * r, y * r, z * r);
	}

	/**
	 * Multiplies two quaternions and returns the resulting quaternion. It performs
	 * quaternion multiplication, a mathematical operation used in 3D rotations.
	 *
	 * @param r right-hand operand in a quaternion multiplication operation.
	 *
	 * @returns a new Quaternion object representing the product of two input Quaternions.
	 */
	public Quaternion mul(Quaternion r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(w_, x_, y_, z_);
	}

	/**
	 * Performs quaternion-vector multiplication, combining a quaternion and a 3D vector
	 * to produce a new quaternion. The result represents a rotation of the original
	 * vector by the quaternion's rotation. This operation is used in 3D graphics and
	 * physics simulations.
	 *
	 * @param r vector by which the quaternion is multiplied.
	 *
	 * @returns a new Quaternion object resulting from the multiplication of the input
	 * Quaternion and Vector3f.
	 */
	public Quaternion mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(w_, x_, y_, z_);
	}

	/**
	 * Performs a subtraction operation on two quaternions, returning a new quaternion
	 * with components that are the differences of the corresponding components of the
	 * input quaternions.
	 *
	 * @param r second quaternion to be subtracted from the first quaternion.
	 *
	 * @returns a new Quaternion object representing the difference between the current
	 * Quaternion and the input Quaternion.
	 */
	public Quaternion sub(Quaternion r) {
		return new Quaternion(w - r.getW(), x - r.getX(), y - r.getY(), z - r.getZ());
	}

	/**
	 * Calculates the sum of two quaternions and returns the result as a new quaternion.
	 * It combines corresponding components of the input quaternions, adding their w, x,
	 * y, and z parts. The result is a new quaternion with the combined values.
	 *
	 * @param r Quaternion to be added to the current Quaternion object.
	 *
	 * @returns a new Quaternion object with components summing the input Quaternion's components.
	 */
	public Quaternion add(Quaternion r) {
		return new Quaternion(w + r.getW(), x + r.getX(), y + r.getY(), z + r.getZ());
	}

	/**
	 * Converts a 4x4 homogeneous transformation matrix into a rotation matrix, which
	 * represents the orientation of an object in 3D space. It computes the forward, up,
	 * and right vectors from the given transformation matrix.
	 *
	 * @returns a 4x4 rotation matrix representing the transformation described by the
	 * input quaternion.
	 */
	public Matrix4f toRotationMatrix() {
		Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
		Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
		Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

		return new Matrix4f().initRotation(forward, up, right);
	}

	/**
	 * Calculates the dot product of two quaternions, combining the product of corresponding
	 * components. It multiplies corresponding elements of the two input quaternions and
	 * sums these products. The result is a scalar value.
	 *
	 * @param r right-hand side quaternion with which the dot product is being calculated.
	 *
	 * @returns the dot product of the calling quaternion and the input quaternion.
	 */
	public float dot(Quaternion r) {
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}

	/**
	 * Performs spherical linear interpolation between two quaternions, ensuring a smooth
	 * transition between rotations. It calculates the shortest path between the two
	 * quaternions if the `shortest` flag is set and the dot product of the two quaternions
	 * is negative.
	 *
	 * @param dest destination quaternion for the linear interpolation.
	 *
	 * @param lerpFactor amount of interpolation between the current quaternion and the
	 * destination quaternion.
	 *
	 * @param shortest option to choose the shortest path between two quaternions when
	 * performing linear interpolation.
	 *
	 * @returns a normalized quaternion resulting from linear interpolation between the
	 * current quaternion and the destination quaternion.
	 */
	public Quaternion NLerp(Quaternion dest, float lerpFactor, boolean shortest) {
		Quaternion correctedDest = dest;

		if (shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.getW(), -dest.getX(), -dest.getY(), -dest.getZ());

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
	}

	/**
	 * Interpolates between two quaternions while maintaining a constant rotation axis,
	 * ensuring a smooth transition between the two quaternions. It handles cases where
	 * the shortest path is not the most direct one.
	 *
	 * @param dest destination quaternion in a spherical linear interpolation, which is
	 * used to compute the interpolated quaternion.
	 *
	 * Destructure `dest` into its constituent parts:
	 * - `w`:
	 * - `x`:
	 * - `y`:
	 * - `z`:
	 *
	 * @param lerpFactor amount of interpolation between the initial quaternion and the
	 * destination quaternion.
	 *
	 * @param shortest option to return the shortest path between the current quaternion
	 * and the destination quaternion.
	 *
	 * @returns a quaternion interpolated between the current quaternion and the destination
	 * quaternion.
	 *
	 * The returned output is a Quaternion, which is a mathematical object representing
	 * a 3D rotation. It has four attributes: w, x, y, and z, which represent the rotation's
	 * components.
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
	 * Returns a new `Vector3f` pointing forward based on the object's rotation, calculated
	 * by rotating the standard forward vector (0, 0, 1) by the object's rotation. The
	 * resulting vector represents the direction the object is facing.
	 *
	 * @returns a rotated Vector3f object representing the forward direction of the
	 * specified object.
	 */
	public Vector3f getForward() {
		return new Vector3f(0, 0, 1).rotate(this);
	}

	/**
	 * Returns a vector representing the back direction from the current position, taking
	 * into account the object's rotation. The function uses a predefined vector (0, 0,
	 * -1) and applies the object's rotation to it.
	 *
	 * @returns a Vector3f object representing a vector pointing backward from the input
	 * vector.
	 */
	public Vector3f getBack() {
		return new Vector3f(0, 0, -1).rotate(this);
	}

	/**
	 * Rotates a given vector to point upwards by applying a rotation transformation to
	 * a predefined unit vector along the y-axis.
	 *
	 * @returns a Vector3f representing the direction vector of the current rotation,
	 * pointing upwards.
	 */
	public Vector3f getUp() {
		return new Vector3f(0, 1, 0).rotate(this);
	}

	/**
	 * Rotates a unit vector along the y-axis by the rotation of the current object and
	 * returns the resulting vector.
	 *
	 * @returns a Vector3f representing the direction down from the current object, rotated
	 * to its local coordinate system.
	 */
	public Vector3f getDown() {
		return new Vector3f(0, -1, 0).rotate(this);
	}

	/**
	 * Rotates a unit vector (1, 0, 0) around the object's current orientation and returns
	 * the resulting vector.
	 *
	 * @returns a Vector3f representing the local right axis of the object, rotated by
	 * its current orientation.
	 */
	public Vector3f getRight() {
		return new Vector3f(1, 0, 0).rotate(this);
	}

	/**
	 * Rotates a unit vector along the x-axis by the current object's rotation, effectively
	 * returning a vector pointing to the left in the object's coordinate system. The
	 * result is a new `Vector3f` object. The original object remains unchanged.
	 *
	 * @returns a Vector3f object representing the left direction, rotated from the
	 * original object.
	 */
	public Vector3f getLeft() {
		return new Vector3f(-1, 0, 0).rotate(this);
	}

	/**
	 * Initializes a Quaternion instance with the provided float values for x, y, z, and
	 * w coordinates and returns the same instance.
	 *
	 * @param x x component of a quaternion, which is set to the input value `x`.
	 *
	 * @param y y-component of a quaternion, which is a mathematical object used to
	 * represent 3D rotations and orientations.
	 *
	 * @param z imaginary part of the quaternion, specifying its rotation around the x-y
	 * plane.
	 *
	 * @param w imaginary component of the quaternion, which is a fundamental aspect of
	 * 3D rotations in mathematics.
	 *
	 * @returns a reference to the modified Quaternion object.
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
	 * Copies the values of a given Quaternion object to the current Quaternion object
	 * and returns the current object.
	 *
	 * @param r Quaternion object from which the values are being retrieved for assignment
	 * to this object.
	 *
	 * @returns the modified Quaternion object itself.
	 */
	public Quaternion set(Quaternion r) {
		set(r.getX(), r.getY(), r.getZ(), r.getW());
		return this;
	}

	/**
	 * Returns the value of a private float variable `x`.
	 *
	 * @returns the value of the instance variable `x`, a float.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the value of the instance variable `x` to the input `float` value. The new
	 * value replaces any existing value. The function has a single parameter, `x`, and
	 * modifies the object's state.
	 *
	 * @param x new value to be assigned to the instance variable `x`.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the current value of the `y` variable, which appears to be a float
	 * representing a coordinate or position.
	 *
	 * @returns the floating-point value of the instance variable `y`.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Updates the value of the instance variable `y` to the specified `float` value.
	 * This allows the `y` coordinate of an object to be modified externally. The change
	 * is then reflected in the object's state.
	 *
	 * @param y new value to be assigned to the instance variable `y`.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the value of the variable `z` as a floating-point number. This variable
	 * is likely a component of a point or a vector in 3D space. The function provides
	 * read-only access to the `z` value.
	 *
	 * @returns the value of the `z` variable, which is of type `float`.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the value of the `z` variable to the specified `z` parameter, updating the
	 * object's state. The `z` variable is a float and represents a coordinate in a 3D
	 * space. The function modifies the object's internal state directly.
	 *
	 * @param z value to be assigned to the instance variable `z`.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns the value of the `w` variable as a floating-point number.
	 *
	 * @returns the value of the instance variable `w`, a floating-point number.
	 */
	public float getW() {
		return w;
	}

	/**
	 * Sets a floating-point value for the `w` attribute, allowing it to be updated with
	 * a new value.
	 *
	 * @param w new width value to be assigned to the instance variable `w`.
	 */
	public void setW(float w) {
		this.w = w;
	}

	/**
	 * Compares two Quaternion objects for equality based on their component values.
	 * It returns true if all components (x, y, z, w) match between the two Quaternions.
	 *
	 * @param r Quaternion object with which the current object's components are compared
	 * for equality.
	 *
	 * @returns a boolean value indicating whether the current quaternion is equal to the
	 * input quaternion.
	 */
	public boolean equals(Quaternion r) {
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}
	
}
