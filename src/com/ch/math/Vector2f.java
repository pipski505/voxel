package com.ch.math;

/**
 * Provides various methods and operations for working with 2D vectors. These include
 * calculating the length and magnitude of the vector, finding the maximum value, dot
 * product, cross product, and more. Additionally, it offers methods for adding,
 * subtracting, multiplying, dividing, and normalizing vectors, as well as providing
 * a convenient method for rotating a vector around an axis. The class also provides
 * high-level mathematical operations such as lerping and finding the absolute value
 * of a vector.
 */
public class Vector2f {
	
	private float x;
	private float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Computes the Euclidean distance of a point in two-dimensional space, as measured
	 * from its center, by squaring its coordinates and taking the square root.
	 * 
	 * @returns the square root of the sum of the squares of the `x` and `y` coordinates.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Computes the maximum value of two arguments, `x` and `y`, using the `Math.max()`
	 * method.
	 * 
	 * @returns the maximum value of `x` and `y`.
	 */
	public float max() {
		return Math.max(x, y);
	}

	/**
	 * Takes a `Vector2f` object `r` as input and returns the dot product of its `x` and
	 * `y` components with the values of the function's parameters `x` and `y`.
	 * 
	 * @param r 2D vector to be dot-producted with the current vector.
	 * 
	 * @returns a floating-point number representing the dot product of the input vector
	 * and another vector represented by `r`.
	 */
	public float dot(Vector2f r) {
		return x * r.getX() + y * r.getY();
	}

	/**
	 * Takes a `Vector2f` object and returns a new `Vector2f` object with the x-coordinate
	 * normalized to a fixed length and the y-coordinate preserved, scaled by the same
	 * factor as the length of the original vector.
	 * 
	 * @returns a normalized vector in the form of a `Vector2f` instance with magnitude
	 * equal to the length of the original vector and direction equal to the original
	 * vector's x-coordinate divided by the length, followed by the y-coordinate divided
	 * by the length.
	 */
	public Vector2f normalized() {
		float length = length();

		return new Vector2f(x / length, y / length);
	}

	/**
	 * Computes the dot product of two vectors and returns the result as a single scalar
	 * value.
	 * 
	 * @param r 2D vector that the function operates on, providing the second component
	 * of the vector to be multiplied with the `x` component of the function and the first
	 * component of the vector to be subtracted from the `y` component of the function.
	 * 
	 * @returns a floating-point number representing the cross product of two vectors.
	 */
	public float cross(Vector2f r) {
		return x * r.getY() - y * r.getX();
	}

	/**
	 * Calculates a linear interpolation between two vector values, `dest` and `this`,
	 * based on a provided factor `lerpFactor`. It returns a new vector value by combining
	 * the original vectors using the interpolation formula.
	 * 
	 * @param dest 2D destination point to which the linear interpolation is applied.
	 * 
	 * @param lerpFactor 0 to 1 value that the current vector will be interpolated towards
	 * from its current value, with values closer to 0 resulting in more rapid interpolation
	 * towards the destination vector and values closer to 1 resulting in more gradual interpolation.
	 * 
	 * @returns a vector that interpolates between two given vectors based on a provided
	 * factor.
	 */
	public Vector2f lerp(Vector2f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	/**
	 * Rotates a `Vector2f` instance by an angle in radians, returning a new `Vector2f`
	 * instance with the rotated coordinates.
	 * 
	 * @param angle angle of rotation in radians, which is used to calculate the cosine
	 * and sine of that angle.
	 * 
	 * @returns a rotated vector in the x-y plane, where the angle of rotation is specified
	 * as a float in radians.
	 */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	/**
	 * Takes a `Vector2f` argument `r` and returns a new `Vector2f` object with the sum
	 * of the current vector's components and the argument's components.
	 * 
	 * @param r 2D vector to be added to the current vector.
	 * 
	 * @returns a new `Vector2f` object representing the sum of the input vectors.
	 */
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.getX(), y + r.getY());
	}

	/**
	 * Adds a given scalar value to its input vector, returning a new vector with the
	 * updated coordinates.
	 * 
	 * @param r 2D vector to be added to the existing vector.
	 * 
	 * @returns a new `Vector2f` instance with the sum of the input `x` and `y` values
	 * and the input `r`.
	 */
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}

    /**
     * Adds two floating-point numbers to a `Vector2f` object, returning a new `Vector2f`
     * instance with the sum of the original object's coordinates and the input values.
     * 
     * @param x 2D coordinate to add to the current position of the vector.
     * 
     * @param y 2nd component of the resulting vector and combines it with the corresponding
     * component of the original vector to produce the new vector.
     * 
     * @returns a new `Vector2f` instance representing the sum of the input `x` and `y`
     * values.
     */
    public Vector2f add(float x, float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

	/**
	 * Takes a `Vector2f` argument `r` and returns a new `Vector2f` object representing
	 * the difference between the current vector's components and those of the provided
	 * vector.
	 * 
	 * @param r 2D vector that the function will subtract from the input vector `x` and
	 * `y`.
	 * 
	 * @returns a new `Vector2f` instance representing the difference between the input
	 * vector and the reference vector.
	 */
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.getX(), y - r.getY());
	}

	/**
	 * Subtracts a given `r` value from the `x` and `y` components of a `Vector2f` object,
	 * returning a new `Vector2f` object with the modified coordinates.
	 * 
	 * @param r 2D vector to subtract from the current vector.
	 * 
	 * @returns a vector with the difference between the input `r` and the current position
	 * of the vector.
	 */
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}

	/**
	 * Takes a `Vector2f` object `r` as input and returns a new `Vector2f` object with
	 * the product of `x` and `r.getX()` and `y` and `r.getY()`.
	 * 
	 * @param r 2D vector that is multiplied with the current vector.
	 * 
	 * @returns a vector with the product of the input vectors' x and y components.
	 */
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.getX(), y * r.getY());
	}

	/**
	 * Takes a scalar `r` and returns a new `Vector2f` object with the product of the
	 * component values of the original vector multiplied by `r`.
	 * 
	 * @param r Scalar value that is multiplied with the `Vector2f` components `x` and `y`.
	 * 
	 * @returns a vector with components scaled by the input value `r`.
	 */
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}

	/**
	 * Takes a `Vector2f` argument `r` and returns a new `Vector2f` instance with the
	 * components scaled by the reciprocals of the corresponding values in `r`.
	 * 
	 * @param r 2D vector to which the current vector should be divided.
	 * 
	 * @returns a vector with the same x-coordinate as the original vector, and a
	 * y-coordinate that is the reciprocal of the input vector's y-coordinate.
	 */
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.getX(), y / r.getY());
	}

	/**
	 * Takes a single floating-point argument `r` and returns a `Vector2f` object with x
	 * and y components scaled by the inverse of `r`.
	 * 
	 * @param r scale factor for the division operation performed on the `x` and `y`
	 * components of the `Vector2f` instance, resulting in a new `Vector2f` instance with
	 * the divided values.
	 * 
	 * @returns a vector with scaled X and Y components proportional to the input value
	 * `r`.
	 */
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}

	/**
	 * Calculates and returns a new `Vector2f` instance with the absolute values of its
	 * `x` and `y` components.
	 * 
	 * @returns a new `Vector2f` object containing the absolute values of the input
	 * vector's `x` and `y` components.
	 */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	/**
	 * Returns a string representation of an object by concatenating its `x` and `y` fields.
	 * 
	 * @returns a string representation of a point in Cartesian coordinates, consisting
	 * of two numbers separated by a space.
	 */
	public String toString() {
		return "(" + x + " " + y + ")";
	}

	/**
	 * Updates the `x` and `y` fields of the `Vector2f` instance, returning the modified
	 * instance for chaining.
	 * 
	 * @param x 2D coordinate of the vector's position in the set operation.
	 * 
	 * @param y 2nd component of the `Vector2f` object and assigns it the value passed
	 * as argument to update its value.
	 * 
	 * @returns a reference to the same `Vector2f` instance with updated `x` and `y` values.
	 */
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Sets the values of the vector to those of the given `Vector2f` reference.
	 * 
	 * @param r 2D vector that contains the x and y coordinates to be set for the current
	 * vector instance.
	 * 
	 * @returns a reference to the original vector with its components modified to match
	 * those of the provided vector.
	 */
	public Vector2f set(Vector2f r) {
		set(r.getX(), r.getY());
		return this;
	}

    /**
     * Transforms a `Vector2f` instance into a corresponding `Vector3f` instance, adding
     * an extra dimension with a value of 0.
     * 
     * @returns a new `Vector3f` object containing the values `x`, `y`, and `0` for the
     * `z` component.
     */
    public Vector3f as3DVector() {
        return new Vector3f(x, y, 0);
    }

	/**
	 * Retrieves the value of the `x` field, which represents a floating-point number.
	 * 
	 * @returns a float value representing the variable `x`.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the value of the `x` field of the object to which it belongs.
	 * 
	 * @param x float value that will be assigned to the `x` field of the class instance
	 * being manipulated by the `setX()` method.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Retrieves the value of the `y` field, which is a `float`. The function returns the
	 * value of `y`.
	 * 
	 * @returns a floating-point value representing the y-coordinate of the point.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the value of the `y` field of the current object to the provided floating-point
	 * value.
	 * 
	 * @param y 2D coordinate of a point in a graphical context, and its value is assigned
	 * directly to the `y` field of the object instance being passed to the function.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Compares a `Vector2f` object with another `Vector2f` object, returning `true` if
	 * both objects have the same x and y coordinates.
	 * 
	 * @param r 2D vector to be compared with the current vector for equality.
	 * 
	 * @returns a boolean value indicating whether the vector's coordinates are equal to
	 * those of the provided reference vector.
	 */
	public boolean equals(Vector2f r) {
		return x == r.getX() && y == r.getY();
	}
	
}
