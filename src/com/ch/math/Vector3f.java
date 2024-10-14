package com.ch.math;

/**
 * Represents a 3D vector with floating-point components, providing various mathematical
 * operations and transformations. It supports vector addition, subtraction,
 * multiplication, division, and normalization. Additionally, it offers methods for
 * calculating length, dot product, cross product, and rotation.
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
	 * Calculates the Euclidean distance of a 3D point from the origin. It uses the
	 * Pythagorean theorem to compute the square root of the sum of the squares of the
	 * point's coordinates.
	 *
	 * @returns the Euclidean distance of a point in 3D space, represented as a floating-point
	 * number.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Calculates the square of the Euclidean distance between a point in three-dimensional
	 * space, where the coordinates are represented by `x`, `y`, and `z`.
	 *
	 * @returns the square of the Euclidean distance between a point in 3D space.
	 */
	public float squareLength() {
		return (float) x*x + y*y + z*z;
	}

	/**
	 * Returns the maximum value among the three variables `x`, `y`, and `z`. It uses the
	 * `Math.max` method to recursively find the maximum value, starting with `y` and
	 * `z`, then compares the result with `x`.
	 *
	 * @returns the maximum value among the variables `x`, `y`, and `z`.
	 */
	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	/**
	 * Calculates the dot product of the current vector and the input vector `r`. The dot
	 * product is a scalar value resulting from the sum of the products of corresponding
	 * components of the two vectors.
	 *
	 * @param r other vector with which the dot product is calculated.
	 *
	 * @returns the dot product of two vectors, a scalar value representing their magnitude
	 * multiplied by cosine of angle between them.
	 */
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}

	/**
	 * Calculates the cross product of two 3D vectors. It takes a `Vector3f` object `r`
	 * as input and returns a new `Vector3f` object representing the perpendicular vector
	 * to both input vectors. The cross product is computed using the determinant formula.
	 *
	 * @param r second vector for the cross product calculation.
	 *
	 * @returns a vector perpendicular to the input vectors, calculated using the cross
	 * product operation.
	 */
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();

		return new Vector3f(x_, y_, z_);
	}

	/**
	 * Calculates the magnitude of a 3D vector and returns a new vector with the same
	 * direction but a length of 1, achieved by dividing each component of the original
	 * vector by its magnitude.
	 *
	 * @returns a new normalized vector with the same direction as the original vector.
	 */
	public Vector3f normalized() {
		float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}

	/**
	 * Rotates a 3D vector around a specified axis by a given angle, utilizing both cross
	 * and dot product operations to achieve the rotation.
	 *
	 * @param axis axis of rotation, around which the current vector is rotated by the
	 * specified angle.
	 *
	 * @param angle angle of rotation around the specified axis.
	 *
	 * @returns a rotated version of the input vector by the specified angle around the
	 * given axis.
	 */
	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);

		return this.cross(axis.mul(sinAngle)).add((this.mul(cosAngle)).add(axis.mul(this.dot(axis.mul(1 - cosAngle)))));
	}

	/**
	 * Rotates a Vector3f around a specified axis defined by a Quaternion rotation. The
	 * rotation is performed by first conjugating the rotation Quaternion, then multiplying
	 * the input Vector3f by the rotation Quaternion and its conjugate, and finally
	 * returning the resulting Vector3f.
	 *
	 * @param rotation rotation to be applied to the current object, which is a quaternion
	 * describing a 3D rotation.
	 *
	 * @returns a Vector3f representing the rotation of the current object around its origin.
	 */
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}

	/**
	 * Calculates a new vector by interpolating between the current vector and the
	 * destination vector, based on the specified lerp factor. The interpolation is linear,
	 * with the factor controlling the proportion of the destination vector to add to the
	 * current vector.
	 *
	 * @param dest destination vector for the linear interpolation between the current
	 * vector and the specified `dest` vector.
	 *
	 * @param lerpFactor percentage of the distance between the current position and the
	 * destination position to be interpolated.
	 *
	 * @returns a vector interpolated between the original vector and the destination vector.
	 */
	public Vector3f lerp(Vector3f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	/**
	 * Calculates the sum of two 3D vectors by adding corresponding components, returning
	 * a new vector with the result. It takes a `Vector3f` object as input and returns a
	 * new `Vector3f` object with the added components.
	 *
	 * @param r vector to be added to the current vector, with its components used for
	 * element-wise addition.
	 *
	 * @returns a new Vector3f object with elements x, y, z incremented by the corresponding
	 * elements of input Vector3f r.
	 */
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	/**
	 * Adds a 3D vector to the current vector's components, incrementing the x, y, and z
	 * values accordingly. This operation is element-wise, combining corresponding
	 * components of the two vectors. The resulting vector is stored in the current
	 * object's state.
	 *
	 * @param r vector to be added to the current object.
	 */
	public void addSelf(Vector3f r) {
		this.x += r.x;
		this.y += r.y;
		this.z += r.z;
	}

	/**
	 * Adds a specified float value to each component of a Vector3f object, returning a
	 * new Vector3f object with the updated values.
	 *
	 * @param r amount by which the x, y, and z components of the Vector3f are incremented.
	 *
	 * @returns a new `Vector3f` instance with values incremented by the specified float
	 * `r`.
	 */
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	/**
	 * Adds a scaled version of a given vector to the current vector, where the scaling
	 * factor is applied to the vector before addition. The scaled vector is created by
	 * multiplying the given vector by a specified scale value.
	 *
	 * @param v vector to be scaled and added to the current vector.
	 *
	 * @param scale factor by which the input vector `v` is multiplied before being added
	 * to the current vector.
	 *
	 * @returns a Vector3f object resulting from scaling the input vector by the specified
	 * scale and adding it to the current vector.
	 */
	public Vector3f addScaledVector(Vector3f v, float scale) {
		return this.add(v.mul(scale));
	}
	
	/**
	 * Adds a scaled version of a given vector to itself, effectively multiplying the
	 * vector's components by a specified scale factor. The scaled vector is then added
	 * to the original vector using the `addSelf` method. This operation modifies the
	 * original vector.
	 *
	 * @param v vector to be scaled and added to the current object.
	 *
	 * @param scale factor by which the input vector `v` is multiplied.
	 */
	public void addSelfScaledVector(Vector3f v, float scale) {
		this.addSelf(v.mul(scale));
	}

	/**
	 * Subtracts the components of a given `Vector3f` object from the current object's
	 * components, resulting in a new `Vector3f` object with the difference in each dimension.
	 *
	 * @param r vector being subtracted from the current vector.
	 *
	 * @returns a new Vector3f object representing the difference between the current and
	 * input vectors.
	 */
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}

	/**
	 * Subtracts a specified value from the x, y, and z components of a 3D vector.
	 *
	 * @param r value to be subtracted from each component of the vector.
	 *
	 * @returns a new `Vector3f` object with each component reduced by the specified value
	 * `r`.
	 */
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}

	/**
	 * Performs element-wise multiplication of two 3D vectors, returning a new vector
	 * with components obtained by multiplying corresponding components of the input
	 * vectors. The function takes another Vector3f object as input and returns a new
	 * Vector3f object.
	 *
	 * @param r factor by which the current vector's components are multiplied.
	 *
	 * @returns a new Vector3f object representing the element-wise product of the input
	 * Vector3f and r.
	 */
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}

	/**
	 * Multiplies each component of a 3D vector by a scalar value. It takes a single float
	 * parameter, scales the vector's x, y, and z components by this value, and returns
	 * a new scaled vector. The original vector remains unchanged.
	 *
	 * @param r scale factor used to multiply the current vector's components.
	 *
	 * @returns a new Vector3f object with components scaled by the input float r.
	 */
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}

	/**
	 * Divides a vector by another vector component-wise, creating a new vector with
	 * elements being the quotients of corresponding components of the input vectors.
	 *
	 * @param r vector being divided from, which is divided element-wise by the current
	 * vector's components.
	 *
	 * @returns a new Vector3f instance with components divided by the corresponding
	 * components of the input Vector3f r.
	 */
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}

	/**
	 * Performs element-wise division of a 3D vector by a scalar value, resulting in a
	 * new 3D vector with each component divided by the scalar.
	 *
	 * @param r divisor for the division operation applied to the vector components.
	 *
	 * @returns a new Vector3f object with each component divided by the input float r.
	 */
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}

	/**
	 * Calculates the absolute value of a 3D vector by taking the absolute value of its
	 * components, resulting in a new vector with the same magnitude but with all components
	 * being positive.
	 *
	 * @returns a new Vector3f object with absolute values of its x, y, and z components.
	 */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/**
	 * Returns a string representation of the object, consisting of the values of its
	 * attributes `x`, `y`, and `z`, enclosed in parentheses.
	 *
	 * @returns a string representation of the object in the format "(x y z)".
	 */
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}

	/**
	 * Returns a new instance of a `Vector2f` object, initialized with the current `x`
	 * and `y` values. The original `x` and `y` values remain unchanged. A copy of the
	 * coordinates is returned.
	 *
	 * @returns a new Vector2f object with the x and y coordinates.
	 */
	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	/**
	 * Returns a new `Vector2f` object containing the y and z coordinates of the original
	 * vector.
	 *
	 * @returns a Vector2f object with y and z coordinates.
	 */
	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}

	/**
	 * Returns a new Vector2f object with its x-coordinate set to the original z-coordinate
	 * and its y-coordinate set to the original x-coordinate.
	 *
	 * @returns a Vector2f object with z-coordinate first and x-coordinate second.
	 */
	public Vector2f getZX() {
		return new Vector2f(z, x);
	}

	/**
	 * Reverses the coordinates of a 2D vector, swapping the y and x values.
	 *
	 * @returns a new Vector2f object with y-coordinate and x-coordinate swapped.
	 */
	public Vector2f getYX() {
		return new Vector2f(y, x);
	}

	/**
	 * Returns a new Vector2f instance with its x-coordinate set to the z-coordinate and
	 * its y-coordinate set to the y-coordinate of the underlying object.
	 *
	 * @returns a Vector2f object containing the z and y coordinates.
	 */
	public Vector2f getZY() {
		return new Vector2f(z, y);
	}

	/**
	 * Returns a new Vector2f object with the x and z coordinates of the original object.
	 * The function creates a copy of the x and z components, leaving y unchanged. The
	 * returned vector is a two-dimensional representation of the original object's x and
	 * z coordinates.
	 *
	 * @returns a new Vector2f object with components x and z.
	 */
	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}

	/**
	 * Sets the x, y, and z coordinates of a Vector3f object and returns the current
	 * object instance, allowing for method chaining.
	 *
	 * @param x x-coordinate of a 3D vector, setting the corresponding member variable
	 * of the class.
	 *
	 * @param y y-coordinate of a 3D point.
	 *
	 * @param z third coordinate of a 3D vector, setting its value in the `Vector3f` object.
	 *
	 * @returns a Vector3f object with the specified x, y, and z coordinates.
	 */
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Assigns the values of the input `Vector3f` object `r` to the current object's
	 * components and returns the current object, allowing for method chaining.
	 *
	 * @param r vector to be copied into the current object.
	 *
	 * @returns a reference to the instance itself, allowing for method chaining.
	 */
	public Vector3f set(Vector3f r) {
		set(r.getX(), r.getY(), r.getZ());
		return this;
	}

	/**
	 * Returns the value of the instance variable `x` as a `float`.
	 *
	 * @returns the value of the instance variable `x`, which is of type `float`.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Assigns a floating-point value to the instance variable `x`. The value is directly
	 * stored without any validation or modification. The function modifies the state of
	 * the object by updating its `x` attribute.
	 *
	 * @param x value to be assigned to the instance variable `x`.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the current value of the `y` variable. This value represents a point's or
	 * object's vertical position.
	 *
	 * @returns the value of the instance variable `y`, a float.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Assigns a specified `float` value to the instance variable `y`. This allows the
	 * value of `y` to be modified externally. The change is persisted within the object.
	 *
	 * @param y new Y-coordinate value to be assigned to the object's y property.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the value of the `z` variable, a float representing a 3D coordinate. This
	 * is a getter method that allows access to the private `z` field. The returned value
	 * is a float.
	 *
	 * @returns the value of the instance variable `z`, a float.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the value of a floating-point variable `z` to a specified value.
	 *
	 * @param z value to be assigned to the instance variable `z`.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Compares two instances of a 3D vector, returning true if all corresponding coordinates
	 * (x, y, z) are equal.
	 *
	 * @param r vector to be compared with the current vector for equality.
	 *
	 * @returns a boolean value indicating whether two Vector3f objects have identical
	 * x, y, and z coordinates.
	 */
	public boolean equals(Vector3f r) {
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}

}
