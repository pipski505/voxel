package com.ch.math;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

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
	 * Computes the Euclidean length of a vector represented by its component values,
	 * returning it as a float value.
	 * 
	 * @returns a floating-point representation of the length of the vector.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * Normalizes a provided quaternion by dividing each component by its corresponding
	 * length, resulting in a quaternion with a length of 1.
	 * 
	 * @returns a normalized quaternion representation of the input quaternion.
	 */
	public Quaternion normalized() {
		float length = length();

		return new Quaternion(w / length, x / length, y / length, z / length);
	}

	/**
	 * Generates a quaternion with the same scalar part but conjugated axis.
	 * 
	 * @returns a new quaternion with the opposite orientation of the original quaternion.
	 */
	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}

	/**
	 * Multiplies a quaternion by a scalar value `r`. It returns a new quaternion with
	 * the product of the quaternion's components and the scalar value.
	 * 
	 * @param r 3D rotation angle of the quaternion product.
	 * 
	 * @returns a new Quaternion instance with multiplied scalar value and the original
	 * quaternion's components.
	 */
	public Quaternion mul(float r) {
		return new Quaternion(w * r, x * r, y * r, z * r);
	}

	/**
	 * Multiplies a quaternion by another quaternion, computing the new quaternion's
	 * values based on the multiplication of the two componentwise.
	 * 
	 * @param r 4-vector quaternion to be multiplied with the current quaternion.
	 * 
	 * @returns a new Quaternion instance with the product of the two input quaternions.
	 */
	public Quaternion mul(Quaternion r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(w_, x_, y_, z_);
	}

	/**
	 * Multiplies a quaternion by a vector, producing another quaternion with the dot
	 * product of the original quaternion and the vector as its magnitude.
	 * 
	 * @param r 3D vector to be multiplied with the quaternion.
	 * 
	 * @returns a Quaternion object representing the product of the given vector and the
	 * quaternion.
	 */
	public Quaternion mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(w_, x_, y_, z_);
	}

	/**
	 * Computes the quaternion `q1 - q2`, where `q1` and `q2` are passed as arguments.
	 * It returns a new quaternion with the components `w1 - w2`, `x1 - x2`, `y1 - y2`,
	 * and `z1 - z2`.
	 * 
	 * @param r 4-element quaternion that is being subtracted from the original quaternion.
	 * 
	 * @returns a new quaternion with the same magnitude as the input quaternion, but
	 * with the specified elements subtracted.
	 */
	public Quaternion sub(Quaternion r) {
		return new Quaternion(w - r.getW(), x - r.getX(), y - r.getY(), z - r.getZ());
	}

	/**
	 * Takes a quaternion object `r` as input and returns a new quaternion object with
	 * the sum of its values.
	 * 
	 * @param r 4D vector to which the current quaternion will be added.
	 * 
	 * @returns a new Quaternion object representing the sum of the input quaternions.
	 */
	public Quaternion add(Quaternion r) {
		return new Quaternion(w + r.getW(), x + r.getX(), y + r.getY(), z + r.getZ());
	}

	/**
	 * Converts a quaternion representation of a rotation into a matrix representation.
	 * It takes a `Vector3f` object representing the forward, up, and right vectors as
	 * input and returns a `Matrix4f` object containing the rotation matrix.
	 * 
	 * @returns a rotation matrix represented as a 4x4 homogeneous matrix.
	 */
	public Matrix4f toRotationMatrix() {
		Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
		Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
		Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

		return new Matrix4f().initRotation(forward, up, right);
	}

	/**
	 * Multiplies each component of a quaternion by a corresponding component of another
	 * quaternion, and returns their product.
	 * 
	 * @param r 4-dimensional vector that multiplies with the `x`, `y`, `z`, and `w`
	 * components of the current quaternion, resulting in the dot product.
	 * 
	 * @returns a float value representing the dot product of two quaternions.
	 */
	public float dot(Quaternion r) {
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}

	/**
	 * Interpolates between two quaternions using the lerp factor and returns the resulting
	 * quaternion after normalization.
	 * 
	 * @param dest 4-component quaternion towards which the interpolation will be performed.
	 * 
	 * @param lerpFactor 0 to 1 value used for interpolating between the source quaternion
	 * and the destinaion quaternion during linear interpolation.
	 * 
	 * @param shortest direction of rotation of the destination quaternion to avoid
	 * over-rotation or under-rotation during the interpolation.
	 * 
	 * @returns a Quaternion representing the intermediate result of linearly interpolating
	 * between two provided Quaternions using the nearest neighbor search algorithm.
	 */
	public Quaternion NLerp(Quaternion dest, float lerpFactor, boolean shortest) {
		Quaternion correctedDest = dest;

		if (shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.getW(), -dest.getX(), -dest.getY(), -dest.getZ());

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
	}

	/**
	 * Calculates a smooth transition between two Quaternion values based on the given
	 * factor and shortest path parameter. It takes into account the magnitude of the
	 * cosine of the angle between the source and destination Quaternions, and uses inverse
	 * sine functions to compute the appropriate factors for interpolation. The resulting
	 * Quaternion is returned.
	 * 
	 * @param dest 4-vector destiation point to which the quaternion interpolation will
	 * be applied.
	 * 
	 * @param lerpFactor 0 to 1 factor by which the Quaternion is smoothly interpolated
	 * from its original value to the destination value.
	 * 
	 * @param shortest shortest arc interpolation mode, which when set to `true`, calculates
	 * the interpolation in the shortest distance between the two given points instead
	 * of using the usual straight-line path between them.
	 * 
	 * @returns a new quaternion that represents the smooth interpolation between two
	 * given quaternions based on the given factor.
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
	 * Rotates a reference vector by the angle of the `Object` it is called on, resulting
	 * in a vector pointing in the forward direction of the `Object`.
	 * 
	 * @returns a rotation of the input vector by 90 degrees around the z-axis.
	 */
	public Vector3f getForward() {
		return new Vector3f(0, 0, 1).rotate(this);
	}

	/**
	 * Rotates a vector by 90 degrees around the Z-axis, effectively "moving" it towards
	 * the negative Z direction.
	 * 
	 * @returns a rotated vector with a magnitude of -1, resulting from the multiplication
	 * of the current object and a rotation matrix.
	 */
	public Vector3f getBack() {
		return new Vector3f(0, 0, -1).rotate(this);
	}

	/**
	 * Rotates a vector by 90 degrees around the x-axis to produce a vertical component
	 * in the y-axis direction, while keeping the z-component unchanged.
	 * 
	 * @returns a rotated vector in the upward direction.
	 */
	public Vector3f getUp() {
		return new Vector3f(0, 1, 0).rotate(this);
	}

	/**
	 * Rotates a provided vector by 90 degrees clockwise, resulting in a vector pointing
	 * downwards.
	 * 
	 * @returns a rotation of the original vector by 90 degrees around the z-axis.
	 */
	public Vector3f getDown() {
		return new Vector3f(0, -1, 0).rotate(this);
	}

	/**
	 * Rotates a vector by 90 degrees clockwise to produce a vector that points "right".
	 * 
	 * @returns a rotated vector representing the rightward direction of the transform's
	 * local coordinates.
	 */
	public Vector3f getRight() {
		return new Vector3f(1, 0, 0).rotate(this);
	}

	/**
	 * Rotates a vector by 90 degrees clockwise to generate a left-handed coordinate
	 * system version of the original vector.
	 * 
	 * @returns a rotated vector pointing to the left direction.
	 */
	public Vector3f getLeft() {
		return new Vector3f(-1, 0, 0).rotate(this);
	}

	/**
	 * Updates the elements of a Quaternion object, setting its `x`, `y`, `z`, and `w`
	 * fields to the input values.
	 * 
	 * @param x 3D position of the quaternion in the x-axis direction.
	 * 
	 * @param y 2D projection of the quaternion's orientation in the y-z plane.
	 * 
	 * @param z 3D rotation axis around which the quaternion rotates.
	 * 
	 * @param w 4th component of the quaternion, which is used to store the value of `w`.
	 * 
	 * @returns a new instance of the `Quaternion` class with updated values for `x`,
	 * `y`, `z`, and `w`.
	 */
	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	/**
	 * Takes a vector of Euler angles and returns a quaternion representation of that
	 * rotation. It calculates the quaternion's components by combining sine and cosine
	 * values of the input angles using the appropriate multipliers.
	 * 
	 * @param eulerAngles 3D orientation of an object or entity in the form of a Vector3f
	 * containing the angle values for roll, pitch, and yaw.
	 * 
	 * @returns a quaternion representing the rotation expressed by the provided Euler angles.
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
	 * Sets the values of this quaternion to match the provided quaternion `r`.
	 * 
	 * @param r 4-component quaternion to which the current quaternion will be set.
	 * 
	 * @returns a reference to the same Quaternion object.
	 */
	public Quaternion set(Quaternion r) {
		set(r.getX(), r.getY(), r.getZ(), r.getW());
		return this;
	}

	/**
	 * Returns the value of `x`.
	 * 
	 * @returns a value of type `float`, representing the `x` component of an object.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the value of its argument to the `x` field of its receiver, updating the field
	 * accordingly.
	 * 
	 * @param x xtensor to which the `this.x` field will be assigned the value passed as
	 * argument.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the value of the `y` field.
	 * 
	 * @returns a float value representing the y-coordinate of a point.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Updates the instance field 'y' by passing a float value as an argument.
	 * 
	 * @param y 3rd component of a vector that is being modified by the function.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the value of the `z` field of an object.
	 * 
	 * @returns the value of the `z` field.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the value of the `z` field of an object to the provided floating-point value.
	 * 
	 * @param z 3D coordinate of an object's position, which is being set to the specified
	 * value by calling the `setZ()` method.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns the value of a member variable `w`.
	 * 
	 * @returns a float value representing the width of the shape.
	 */
	public float getW() {
		return w;
	}

	/**
	 * Sets the value of the instance field `w`.
	 * 
	 * @param w 𝚹 value of the object being modified by the `setW()` function.
	 */
	public void setW(float w) {
		this.w = w;
	}

	/**
	 * Compares a quaternion `this` with another quaternion `r`, returning `true` if all
	 * components of both quaternions are equal, and `false` otherwise.
	 * 
	 * @param r Quaternion to be compared with the current instance of the Quaternion
	 * class for equality determination.
	 * 
	 * @returns `true` if the given quaternion has the same coordinates as the original
	 * quaternion, and `false` otherwise.
	 */
	public boolean equals(Quaternion r) {
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}
	
}
