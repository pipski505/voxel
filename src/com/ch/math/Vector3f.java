package com.ch.math;

/**
 * Represents a 3-dimensional vector with float components, providing various
 * mathematical operations for vector manipulation. It supports operations such as
 * addition, subtraction, multiplication, division, normalization, rotation, and interpolation.
 */
public class Vector3f {

	private float x;
	private float y;
	private float z;

	public Vector3f() {
		this(0, 0, 0);
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculates the Euclidean distance of a 3D point, where x, y, and z are coordinates.
	 * It returns the square root of the sum of squares of the coordinates. The result
	 * is a floating-point value representing the point's distance from the origin.
	 *
	 * @returns the Euclidean distance of a point from the origin in 3D space.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Calculates the square of the Euclidean distance between a point in three-dimensional
	 * space.
	 *
	 * @returns the square of the Euclidean distance between a point and the origin.
	 */
	public float squareLength() {
		return (float) x*x + y*y + z*z;
	}

	/**
	 * Returns the maximum value among three float variables x, y, and z.
	 *
	 * @returns the largest of the three values x, y, and z.
	 */
	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	/**
	 * Calculates the dot product of the current vector with the input vector `r`.
	 * The dot product is a scalar value resulting from the sum of the products of
	 * corresponding vector components.
	 *
	 * @param r vector to be dotted with the current vector.
	 *
	 * @returns the dot product of two 3D vectors.
	 */
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}

	/**
	 * Calculates the cross product of two 3D vectors. It takes a `Vector3f` object `r`
	 * as input and returns a new `Vector3f` object representing the cross product of the
	 * current vector and `r`.
	 *
	 * @param r second vector in a cross product operation.
	 *
	 * @returns a vector perpendicular to the input vectors.
	 */
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();

		return new Vector3f(x_, y_, z_);
	}

	/**
	 * Normalizes a 3D vector by dividing its components by its magnitude, resulting in
	 * a vector with a length of 1. The original vector's direction is preserved, but its
	 * scale is reduced. This is a common operation in vector mathematics.
	 *
	 * @returns a new vector with the same direction as the original but length of 1.
	 */
	public Vector3f normalized() {
		float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}

	/**
	 * Rotates a 3D vector around a specified axis by a given angle, utilizing the
	 * Rodrigues' rotation formula for efficient computation. It takes a vector axis and
	 * an angle as input, then returns the rotated vector.
	 *
	 * @param axis axis of rotation, around which the rotation transformation is applied.
	 *
	 * @param angle angle of rotation around the specified axis.
	 *
	 * @returns the rotated vector based on the given axis and angle.
	 */
	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);

		return this.cross(axis.mul(sinAngle)).add((this.mul(cosAngle)).add(axis.mul(this.dot(axis.mul(1 - cosAngle)))));
	}

	/**
	 * Rotates a 3D vector by a specified quaternion, returning a new vector resulting
	 * from the rotation.
	 *
	 * @param rotation rotational transformation applied to the object.
	 *
	 * @returns a Vector3f representing the rotation of the current object by the given
	 * Quaternion.
	 */
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}

	/**
	 * Calculates a vector by smoothly interpolating between the current vector and the
	 * destination vector, based on the specified linear interpolation factor. The result
	 * is a vector that is a weighted average of the two input vectors. The interpolation
	 * factor controls the amount of interpolation.
	 *
	 * @param dest destination vector, from which the current vector is interpolated towards.
	 *
	 * @param lerpFactor fraction of the distance between the current position and the
	 * destination that is traveled.
	 *
	 * @returns a vector that represents the interpolated position between the current
	 * object and the destination, scaled by the lerp factor.
	 */
	public Vector3f lerp(Vector3f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	/**
	 * Performs element-wise vector addition, taking another vector as input and returning
	 * a new vector with components that are the sum of the corresponding components of
	 * the two input vectors.
	 *
	 * @param r vector to be added to the current vector, with its components being
	 * accessed using the getX(), getY(), and getZ() methods.
	 *
	 * @returns a new `Vector3f` object with components summing the input vector's
	 * components and the receiver's components.
	 */
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	/**
	 * Adds the components of the input vector `r` to the corresponding components of the
	 * current vector, effectively increasing the current vector's magnitude.
	 *
	 * @param r vector to be added to the current vector.
	 */
	public void addSelf(Vector3f r) {
		this.x += r.x;
		this.y += r.y;
		this.z += r.z;
	}

	/**
	 * Adds a scalar value to each component of a 3D vector.
	 * It returns a new vector with the updated components.
	 *
	 * @param r amount to be added to each component of the Vector3f.
	 *
	 * @returns a new Vector3f object with its x, y, and z components incremented by the
	 * input float r.
	 */
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	/**
	 * Adds the scaled vector to the current vector, where the scaling factor is applied
	 * to the input vector before addition. The scaled input vector is calculated by
	 * multiplying the input vector by the specified scale factor.
	 *
	 * @param v vector to be scaled and added to the current vector.
	 *
	 * @param scale factor by which the input vector `v` is multiplied before being added
	 * to the current vector.
	 *
	 * @returns the sum of the current vector and the scaled vector `v`.
	 */
	public Vector3f addScaledVector(Vector3f v, float scale) {
		return this.add(v.mul(scale));
	}
	
	/**
	 * Adds a scaled vector to the current object, where the scaling factor is applied
	 * to the input vector before addition, and the result is added to the object's current
	 * state.
	 *
	 * @param v vector to be scaled and added to the current object.
	 *
	 * @param scale factor by which the vector `v` is multiplied.
	 */
	public void addSelfScaledVector(Vector3f v, float scale) {
		this.addSelf(v.mul(scale));
	}

	/**
	 * Performs vector subtraction, returning a new Vector3f object with components that
	 * are the differences between the current vector's components and the corresponding
	 * components of the input vector `r`.
	 *
	 * @param r vector to be subtracted from the current vector.
	 *
	 * @returns a new Vector3f object representing the difference of the current vector
	 * and the input vector r.
	 */
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}

	/**
	 * Subtracts a specified value `r` from each component (x, y, z) of a 3D vector.
	 *
	 * @param r quantity to be subtracted from the current vector's components.
	 *
	 * @returns a new Vector3f instance with each component decreased by the specified value.
	 */
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}

	/**
	 * Performs element-wise multiplication of two 3D vectors. It takes another `Vector3f`
	 * object `r` as input and returns a new `Vector3f` object with components that are
	 * the products of corresponding components from the input vectors.
	 *
	 * @param r right-hand side vector for multiplication of the current vector.
	 *
	 * @returns a new Vector3f object containing the product of the current vector and
	 * the input vector r.
	 */
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}

	/**
	 * Multiplies the components of a 3D vector by a scalar value, returning a new vector
	 * with the scaled components.
	 *
	 * @param r scale factor by which the vector's components are multiplied.
	 *
	 * @returns a new Vector3f object with components scaled by the input float r.
	 */
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}

	/**
	 * Performs element-wise division of the current vector by the input vector `r`,
	 * returning a new vector with the results. It divides corresponding x, y, and z
	 * components of the two vectors.
	 *
	 * @param r divisor for the division operation, which is performed element-wise on
	 * the x, y, and z components of the input vector.
	 *
	 * @returns a new Vector3f object representing the division of the current vector by
	 * the input vector r.
	 */
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}

	/**
	 * Performs element-wise division of a 3D vector by a scalar value. It returns a new
	 * vector with each component divided by the scalar. The original vector remains unchanged.
	 *
	 * @param r denominator for division of the vector's components.
	 *
	 * @returns a new Vector3f object with each component divided by the input float r.
	 */
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}

	/**
	 * Calculates the absolute values of the x, y, and z components of a 3D vector. Returns
	 * a new vector with the absolute values.
	 *
	 * @returns a new Vector3f object with absolute values of its components.
	 */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/**
	 * Converts object data into a string representation, specifically a formatted string
	 * containing the values of `x`, `y`, and `z` enclosed in parentheses.
	 *
	 * @returns a string representation of coordinates in the format "(x y z)".
	 */
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}

	/**
	 * Returns a new `Vector2f` object with the values of `x` and `y` properties. The
	 * function creates a copy of the current vector's coordinates. It does not modify
	 * the original object.
	 *
	 * @returns a Vector2f object with the current values of x and y.
	 */
	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	/**
	 * Returns a new `Vector2f` object with its x-coordinate set to the y-coordinate of
	 * the original vector and its y-coordinate set to the z-coordinate of the original
	 * vector.
	 *
	 * @returns a new Vector2f object with y and z coordinates.
	 */
	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}

	/**
	 * Returns a new `Vector2f` object with its x-coordinate set to the value of the
	 * original object's z-coordinate and its y-coordinate set to the original object's
	 * x-coordinate.
	 *
	 * @returns a new Vector2f object containing the z-coordinate and x-coordinate values.
	 */
	public Vector2f getZX() {
		return new Vector2f(z, x);
	}

	/**
	 * Reverses the order of the coordinates in a 2D vector, returning a new vector with
	 * y as the x-coordinate and x as the y-coordinate.
	 *
	 * @returns a new Vector2f object with y-coordinate first, followed by x-coordinate.
	 */
	public Vector2f getYX() {
		return new Vector2f(y, x);
	}

	/**
	 * Returns a new instance of a 2D vector with its x-component set to the z-component
	 * of the original vector and its y-component set to the y-component of the original
	 * vector.
	 *
	 * @returns a new Vector2f object with coordinates (z, y).
	 */
	public Vector2f getZY() {
		return new Vector2f(z, y);
	}

	/**
	 * Returns a new 2D vector with x and z components of the original 3D vector.
	 *
	 * @returns a Vector2f object with x and z components of the original object.
	 */
	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}

	/**
	 * Sets the x, y, and z components of a 3D vector to specified values and returns the
	 * updated vector.
	 *
	 * @param x x-coordinate of a 3D vector, which is being set to the specified value.
	 *
	 * @param y y-coordinate in a 3D space.
	 *
	 * @param z z-coordinate of a 3D vector and sets the corresponding value in the
	 * object's state.
	 *
	 * @returns a `Vector3f` object with the specified x, y, and z coordinates.
	 */
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Sets the vector's components to the values from the provided `Vector3f` object `r`
	 * and returns the current vector instance.
	 *
	 * @param r new value to be set for the Vector3f object.
	 *
	 * @returns a reference to the modified `Vector3f` object.
	 */
	public Vector3f set(Vector3f r) {
		set(r.getX(), r.getY(), r.getZ());
		return this;
	}

	/**
	 * Returns the value of the `x` variable, which is a float. The function provides
	 * read-only access to the `x` property. It allows other parts of the program to
	 * retrieve the current value of `x`.
	 *
	 * @returns the value of the `x` variable.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the value of the instance variable `x` to the provided `float` value. It
	 * updates the internal state of the object. The change is persistent, allowing the
	 * object to reflect the new value.
	 *
	 * @param x value to be assigned to the instance variable `x`.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the value of the `y` variable as a floating-point number.
	 * This value represents a coordinate or a position.
	 *
	 * @returns the value of the `y` field, a float representing a vertical coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Updates the `y` field with a specified `float` value.
	 *
	 * @param y new value for the object's y-coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the value of the `z` variable as a floating-point number.
	 *
	 * @returns the value of the `z` variable, which is of type `float`.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets a float value to a private variable `z` within the same class.
	 *
	 * @param z value to be assigned to the instance variable `z`.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Compares two `Vector3f` objects for equality based on their x, y, and z coordinates.
	 * It returns `true` if the coordinates match exactly, `false` otherwise.
	 *
	 * @param r right-hand side of the vector being compared for equality.
	 *
	 * @returns a boolean value indicating whether the current object is equal to the
	 * input `Vector3f`.
	 */
	public boolean equals(Vector3f r) {
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}

}
