package com.ch.math;

/**
 * Represents a 2D vector with methods for basic vector operations, such as addition,
 * subtraction, multiplication, and division. It also includes methods for calculating
 * the length, maximum value, dot product, and cross product of two vectors.
 */
public class Vector2f {
	
	private float x;
	private float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculates the Euclidean distance of a point from the origin.
	 *
	 * @returns the Euclidean distance between the point (0, 0) and the point (x, y).
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns the maximum value between `x` and `y`.
	 * It utilizes the `Math.max` method from Java's standard library.
	 *
	 * @returns the greater value between `x` and `y`.
	 */
	public float max() {
		return Math.max(x, y);
	}

	/**
	 * Calculates the dot product of the current vector with the given `Vector2f` object,
	 * returning a scalar value representing the sum of the products of corresponding components.
	 *
	 * @param r right-hand side vector in a dot product operation, used to compute the
	 * dot product with the instance's vector.
	 *
	 * @returns the dot product of the calling object and the input vector r.
	 */
	public float dot(Vector2f r) {
		return x * r.getX() + y * r.getY();
	}

	/**
	 * Calculates the magnitude of a vector and returns a new vector with the same direction
	 * but with a length of 1.
	 *
	 * @returns a Vector2f object with components scaled to unit length.
	 */
	public Vector2f normalized() {
		float length = length();

		return new Vector2f(x / length, y / length);
	}

	/**
	 * Calculates the cross product of two 2D vectors. It returns a scalar value representing
	 * the magnitude of the perpendicular vector formed by the input vectors.
	 *
	 * @param r vector being crossed with the current vector, and its components are used
	 * to calculate the cross product.
	 *
	 * @returns the cross product of two vectors, a scalar value representing the magnitude
	 * of their perpendicular vector.
	 */
	public float cross(Vector2f r) {
		return x * r.getY() - y * r.getX();
	}

	/**
	 * Calculates a new position by interpolating between the current position and a
	 * target position, with the interpolation factor controlling the amount of change.
	 * The result is a position that is a weighted average of the current and target positions.
	 *
	 * @param dest destination point in the linear interpolation operation.
	 *
	 * @param lerpFactor percentage of the distance between `this` and `dest` to be
	 * traversed towards `dest`.
	 *
	 * @returns a vector that is the linear interpolation of the original vector and the
	 * destination vector.
	 */
	public Vector2f lerp(Vector2f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	/**
	 * Rotates a 2D vector by a specified angle around the origin, returning a new vector
	 * with the updated x and y coordinates based on the rotation matrix. The input vector
	 * is not modified.
	 *
	 * @param angle angle of rotation in degrees that is applied to the vector.
	 *
	 * @returns a new 2D vector resulting from rotating the original vector by the specified
	 * angle.
	 */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	/**
	 * Calculates and returns a new vector by adding the current vector to the input
	 * vector `r`. The resulting vector has components that are the sum of the corresponding
	 * components of the two input vectors.
	 *
	 * @param r second vector to be added to the current vector, whose components are
	 * accessed via the `getX()` and `getY()` methods.
	 *
	 * @returns a new Vector2f object representing the sum of the current Vector2f and
	 * the input Vector2f.
	 */
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.getX(), y + r.getY());
	}

	/**
	 * Adds a specified value `r` to the x and y components of a 2D vector, returning a
	 * new vector with the updated coordinates. The original vector remains unchanged.
	 * This operation represents a translation of the vector by the specified amount.
	 *
	 * @param r amount to be added to the x and y coordinates of the vector.
	 *
	 * @returns a new `Vector2f` instance with elements incremented by the specified `r`
	 * value.
	 */
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}

    /**
     * Calculates the sum of the current vector's components and the input values, creating
     * a new vector with the resulting components. The original vector remains unchanged.
     * The new vector's x component is the sum of the original x component and the input
     * x value.
     *
     * @param x x-coordinate value to be added to the current x-coordinate of the vector.
     *
     * @param y vertical component of a vector to be added to the current vector's y-coordinate.
     *
     * @returns a new Vector2f object with its x and y components incremented by the
     * provided values.
     */
    public Vector2f add(float x, float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

	/**
	 * Subtracts the components of the given `Vector2f` `r` from the current vector and
	 * returns the resulting vector. The resulting vector has its x-component calculated
	 * as `x - r.getX()` and its y-component as `y - r.getY()`.
	 *
	 * @param r vector from which the current vector's components are subtracted.
	 *
	 * @returns a new Vector2f object representing the difference between the current
	 * vector and the input vector r.
	 */
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.getX(), y - r.getY());
	}

	/**
	 * Subtracts a given float value from both the x and y components of a 2D vector,
	 * returning a new vector with the resulting values.
	 *
	 * @param r amount to subtract from both the x and y coordinates.
	 *
	 * @returns a new Vector2f object with x and y coordinates reduced by r.
	 */
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}

	/**
	 * Performs element-wise multiplication of the current vector with the input vector
	 * `r`, returning a new vector with the product of corresponding elements.
	 *
	 * @param r vector to be multiplied with the current vector, with the result returned
	 * as a new Vector2f object.
	 *
	 * @returns a new Vector2f object representing the element-wise product of the input
	 * Vector2f and the receiver.
	 */
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.getX(), y * r.getY());
	}

	/**
	 * Scales a 2D vector by a given scalar value, multiplying each component (x and y)
	 * by the scalar.
	 *
	 * @param r scale factor for the vector, where each component of the vector is
	 * multiplied by this factor.
	 *
	 * @returns a new Vector2f object with scaled components, where x and y are multiplied
	 * by the input float r.
	 */
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}

	/**
	 * Divides the current vector by the specified vector `r`, component-wise, returning
	 * a new vector with the result. The division operation is performed on the x and y
	 * components separately.
	 *
	 * @param r vector by which the current vector is divided.
	 *
	 * @returns a new vector with components resulting from element-wise division of the
	 * current vector by the input vector.
	 */
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.getX(), y / r.getY());
	}

	/**
	 * Divides the vector's components by a scalar value, returning a new vector with the
	 * result. The division operation is element-wise, affecting both the x and y components
	 * of the vector.
	 *
	 * @param r divisor for the division operation, scaling the current vector's components
	 * by its reciprocal.
	 *
	 * @returns a new Vector2f object with its x and y components divided by the input
	 * float r.
	 */
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}

	/**
	 * Returns a new `Vector2f` object with the absolute values of its components. The
	 * original vector remains unchanged. The function takes no parameters.
	 *
	 * @returns a new Vector2f object with absolute values of x and y components.
	 */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	/**
	 * Returns a string representation of the object, formatted as a pair of coordinates
	 * (x, y). The string includes the values of the x and y variables, separated by a
	 * space and enclosed in parentheses. This allows the object to be easily represented
	 * as a string.
	 *
	 * @returns a string representation of a point in the format "(x y)" where x and y
	 * are coordinates.
	 */
	public String toString() {
		return "(" + x + " " + y + ")";
	}

	/**
	 * Sets the x and y coordinates of a Vector2f object with the given values, allowing
	 * for method chaining through a return reference to the object itself.
	 *
	 * @param x new x-coordinate for the object being set.
	 *
	 * @param y y-coordinate of a vector.
	 *
	 * @returns a Vector2f object with the specified x and y coordinates.
	 */
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Updates the current vector with the values from the input vector `r`, and returns
	 * the current vector instance.
	 *
	 * @param r vector to be copied into the current object.
	 *
	 * @returns a Vector2f object with updated x and y values.
	 */
	public Vector2f set(Vector2f r) {
		set(r.getX(), r.getY());
		return this;
	}

    /**
     * Converts a 3D vector with a specified x and y component into a 3D vector with an
     * additional z component of 0. It returns a new Vector3f object with the modified
     * components. The original vector remains unchanged.
     *
     * @returns a 3D vector with the x and y components of the current object and a z
     * component of 0.
     */
    public Vector3f as3DVector() {
        return new Vector3f(x, y, 0);
    }

	/**
	 * Returns a float value representing the x-coordinate.
	 * The function provides direct access to the x attribute.
	 *
	 * @returns the value of the instance variable `x`.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets a float value to the instance variable `x`, allowing the value to be updated.
	 *
	 * @param x new value to be assigned to the instance variable `x`.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the value of the variable `y` as a floating-point number. This value is
	 * likely a coordinate or position in a graphical context. The function provides
	 * direct access to the `y` attribute.
	 *
	 * @returns the value of the instance variable `y` of type `float`.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Assigns a specified `float` value to the `y` attribute of the class, updating its
	 * current value. This allows the `y` attribute to be modified after its initial
	 * assignment. The new value is stored directly in the `y` field.
	 *
	 * @param y new y-coordinate value to be assigned to the object's y property.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Compares two Vector2f objects for equality based on their x and y coordinates,
	 * returning true if both coordinates match and false otherwise.
	 *
	 * @param r right-hand side of the equality comparison, a Vector2f object to be
	 * compared with the instance's coordinates.
	 *
	 * @returns a boolean value indicating whether the current vector is equal to the
	 * given vector.
	 */
	public boolean equals(Vector2f r) {
		return x == r.getX() && y == r.getY();
	}
	
}
