package com.ch.math;

/**
 * Is a representation of a 3D vector with three components (x, y, and z) that can
 * be used to perform mathematical operations such as addition, subtraction,
 * multiplication, and division. It also provides methods for normalizing the vector,
 * rotating it around an axis, and lerping between two vectors. Additionally, it has
 * getters and setters for each component of the vector.
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
	 * Computes the Euclidean distance of a 3D point from origin using the square root
	 * of the sum of its coordinates.
	 * 
	 * @returns the square root of the sum of the squares of the three cartesian coordinates.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Calculates the length of a point in three-dimensional space by squaring the
	 * coordinates and summing the results.
	 * 
	 * @returns a value representing the length of the square of the provided coordinates.
	 */
	public float squareLength() {
		return (float) x*x + y*y + z*z;
	}

	/**
	 * Computes and returns the maximum value of its three arguments, `x`, `y`, and `z`.
	 * 
	 * @returns the maximum of three floating-point values: `x`, `y`, and `z`.
	 */
	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	/**
	 * Computes the dot product of a vector and another vector, returning a scalar value.
	 * 
	 * @param r 3D vector that the dot product is being calculated for.
	 * 
	 * @returns a floating-point number representing the dot product of the input vector
	 * and the provided vector.
	 */
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}

	/**
	 * Computes the cross product of two vectors in 3D space, returning a new vector with
	 * the resultant magnitude and direction.
	 * 
	 * @param r 3D vector to cross with the current vector.
	 * 
	 * @returns a new vector with the cross product of the input vectors.
	 */
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();

		return new Vector3f(x_, y_, z_);
	}

	/**
	 * Normalizes a vector by dividing its components by their magnitude, resulting in a
	 * unitized vector representation.
	 * 
	 * @returns a normalized version of the input vector.
	 */
	public Vector3f normalized() {
		float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}

	/**
	 * Rotates a vector by an angle around a specified axis, returning the resulting vector.
	 * 
	 * @param axis 3D rotation axis around which the object is rotated.
	 * 
	 * @param angle 3D rotation angle around the specified `axis`.
	 * 
	 * @returns a rotated version of the input vector, based on the specified axis and
	 * angle of rotation.
	 */
	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);

		return this.cross(axis.mul(sinAngle)).add((this.mul(cosAngle)).add(axis.mul(this.dot(axis.mul(1 - cosAngle)))));
	}

	/**
	 * Takes a quaternion representation of rotation and applies it to a `Vector3f` object,
	 * returning the rotated vector.
	 * 
	 * @param rotation 3D rotation transformation that is applied to the `Vector3f` object.
	 * 
	 * @returns a vector representing the rotated position of the original vector.
	 */
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}

	/**
	 * Interpolates between two vectors, `dest` and `this`, based on a lerping factor
	 * `lerpFactor`. It returns the resulting vector by subtracting `this`, multiplying
	 * by `lerpFactor`, and adding `this` again.
	 * 
	 * @param dest 3D vector that the function will blend or interpolate between the
	 * current vector and.
	 * 
	 * @param lerpFactor linear interpolation factor used to blend the current vector
	 * value with the destination vector value.
	 * 
	 * @returns a new vector that is a linear interpolation of the `dest` vector and the
	 * current vector, with the specified lerp factor.
	 */
	public Vector3f lerp(Vector3f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	/**
	 * Adds two `Vector3f` objects together, returning a new vector with the sum of the
	 * corresponding components.
	 * 
	 * @param r 3D vector to be added to the current vector.
	 * 
	 * @returns a new vector with the sum of the input vectors' components.
	 */
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	/**
	 * Adds the components of a vector object (`r`) to the corresponding components of
	 * the current object.
	 * 
	 * @param r 3D vector that adds to the current position of the object.
	 */
	public void addSelf(Vector3f r) {
		this.x += r.x;
		this.y += r.y;
		this.z += r.z;
	}

	/**
	 * Adds a floating-point value to a `Vector3f` object, returning a new `Vector3f`
	 * instance with the modified coordinates.
	 * 
	 * @param r 3D vector to be added to the existing vector.
	 * 
	 * @returns a new `Vector3f` object representing the sum of the original vector and
	 * the specified floating-point value.
	 */
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	/**
	 * Adds a vector to the current vector with a scaling factor applied to it. The
	 * resulting vector is returned.
	 * 
	 * @param v 3D vector to be added to the current vector, multiplied by a scaling factor.
	 * 
	 * @param scale scalar value by which the input vector is multiplied before being
	 * added to the current vector.
	 * 
	 * @returns a new vector that is the sum of the input vector multiplied by the scale
	 * factor.
	 */
	public Vector3f addScaledVector(Vector3f v, float scale) {
		return this.add(v.mul(scale));
	}
	
	/**
	 * Multiplies a `Vector3f` object by a scalar value and adds it to the current vector
	 * instance, scaling the existing values accordingly.
	 * 
	 * @param v 3D vector to be scaled.
	 * 
	 * @param scale 3D vector multiplication factor applied to the `v` argument.
	 */
	public void addSelfScaledVector(Vector3f v, float scale) {
		this.addSelf(v.mul(scale));
	}

	/**
	 * Subtracts a vector from another, returning a new vector representing the difference.
	 * 
	 * @param r 3D vector to be subtracted from the current vector.
	 * 
	 * @returns a new `Vector3f` object representing the difference between the input
	 * vector and the reference vector.
	 */
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}

	/**
	 * Takes a single float argument `r` and returns a new `Vector3f` instance representing
	 * the difference between the original vector and the provided value.
	 * 
	 * @param r 3D position of the offset from the original vector.
	 * 
	 * @returns a new `Vector3f` instance representing the difference between the original
	 * vector and the given value.
	 */
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}

	/**
	 * Multiplies two `Vector3f` objects, returning a new vector with the product of the
	 * corresponding components.
	 * 
	 * @param r 3D vector to multiply with the current vector.
	 * 
	 * @returns a vector with the product of the input vectors' coordinates.
	 */
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}

	/**
	 * Multiplies a vector by a scalar value, returning a new vector with the product.
	 * 
	 * @param r scalar value to be multiplied with the vector components of the `Vector3f`
	 * object.
	 * 
	 * @returns a vector with the product of the input `r` and each component of the
	 * original vector.
	 */
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}

	/**
	 * Takes a `Vector3f` argument `r` and returns a new `Vector3f` instance with the
	 * components scaled by the reciprocal of the corresponding values of `r`.
	 * 
	 * @param r 3D vector that the `x`, `y`, and `z` components of the returned vector
	 * are divided by.
	 * 
	 * @returns a vector with the same components as the input, but scaled by the reciprocal
	 * of the corresponding component of the argument vector.
	 */
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}

	/**
	 * Takes a scalar `r` and returns a new `Vector3f` instance with each component divided
	 * by `r`.
	 * 
	 * @param r scalar value used to divide each component of the `Vector3f` instance
	 * being manipulated.
	 * 
	 * @returns a vector with the x, y, and z components scaled by the input value `r`.
	 */
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}

	/**
	 * Computes and returns a new `Vector3f` instance with the absolute values of its
	 * input components.
	 * 
	 * @returns a new vector with the absolute value of the input values for `x`, `y`,
	 * and `z`.
	 */
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/**
	 * Returns a string representation of an object, combining its three instance variables
	 * `x`, `y`, and `z`.
	 * 
	 * @returns a string representation of a point in 3D space, consisting of three
	 * separated values.
	 */
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}

	/**
	 * Returns a `Vector2f` instance representing the position of an entity with `x` and
	 * `y` coordinates.
	 * 
	 * @returns a `Vector2f` object containing the x and y coordinates of the point.
	 */
	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	/**
	 * Returns a `Vector2f` object representing the y- and z-components of a point.
	 * 
	 * @returns a `Vector2f` object representing the Y and Z coordinates of a point.
	 */
	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}

	/**
	 * Returns a `Vector2f` object containing the `z` and `x` components of an unknown input.
	 * 
	 * @returns a `Vector2f` object containing the `z` and `x` coordinates of a point.
	 */
	public Vector2f getZX() {
		return new Vector2f(z, x);
	}

	/**
	 * Returns a `Vector2f` object containing the x and y coordinates of a point.
	 * 
	 * @returns a `Vector2f` object containing the values of `y` and `x`.
	 */
	public Vector2f getYX() {
		return new Vector2f(y, x);
	}

	/**
	 * Returns a `Vector2f` object containing the `z` and `y` coordinates of a point.
	 * 
	 * @returns a vector with x-coordinate `z` and y-coordinate `y`.
	 */
	public Vector2f getZY() {
		return new Vector2f(z, y);
	}

	/**
	 * Returns a `Vector2f` object containing the `x` and `z` components of an unknown quantity.
	 * 
	 * @returns a `Vector2f` object representing the position of an entity in a 2D space,
	 * with `x` and `z` components.
	 */
	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}

	/**
	 * Sets the `x`, `y`, and `z` components of the `Vector3f` object to the input values,
	 * returning the modified object.
	 * 
	 * @param x 3D position of the vector along the x-axis.
	 * 
	 * @param y 2D coordinate of the vector, which is updated to match the value provided.
	 * 
	 * @param z 3D position of the vector along the z-axis, which is updated to match the
	 * value provided by the user.
	 * 
	 * @returns a reference to the modified `Vector3f` instance.
	 */
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Sets the values of the `Vector3f` object to those of a given `Vector3f` reference.
	 * 
	 * @param r 3D vector to be set as the value of the `Vector3f` instance.
	 * 
	 * @returns a reference to the original `Vector3f` object, unchanged.
	 */
	public Vector3f set(Vector3f r) {
		set(r.getX(), r.getY(), r.getZ());
		return this;
	}

	/**
	 * Retrieves the value of a field named `x`. It returns the stored value as a `float`
	 * type.
	 * 
	 * @returns a floating-point value representing the x coordinate of an object.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the value of the instance field `x` to the input `float` value provided in
	 * the method invocation.
	 * 
	 * @param x float value to be assigned to the `x` field of the class instance being
	 * manipulated by the function.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the value of the `y` field.
	 * 
	 * @returns a floating-point value representing the current value of the `y` field.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the value of the `y` field of the object it is called on to the provided
	 * floating-point value.
	 * 
	 * @param y 2D coordinate of a point to be assigned to the `y` field of the object,
	 * which is then updated to reflect the new value.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Retrieves and returns the value of a `z` field, which is stored as a `float`.
	 * 
	 * @returns the value of the `z` field.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the value of an object's `z` field to the inputted float value.
	 * 
	 * @param z 3D position of an object in the Java code's scope, and its value is
	 * assigned to the `z` field of the code's class.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Compares the `Vector3f` object with another provided `Vector3f` object by checking
	 * the equivalence of its `x`, `y`, and `z` components.
	 * 
	 * @param r 3D vector to be compared with the current vector.
	 * 
	 * @returns a boolean value indicating whether the vector's components are equal to
	 * those of the provided reference vector.
	 */
	public boolean equals(Vector3f r) {
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}

}
