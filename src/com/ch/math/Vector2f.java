package com.ch.math;

/**
 * Defines a 2-dimensional vector with methods for various mathematical operations,
 * transformations, and comparisons.
 */
public class Vector2f {
	
	private float x;
	private float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculates the Euclidean distance of a point from the origin, given its x and y
	 * coordinates, and returns the result as a floating-point number.
	 *
	 * @returns the Euclidean distance between the point and the origin.
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns the greater of two values, `x` and `y`, as a floating-point number.
	 *
	 * @returns the larger value between `x` and `y`.
	 */
	public float max() {
		return Math.max(x, y);
	}

	/**
	 * Calculates the dot product of two vectors.
	 * It takes a `Vector2f` object as input and returns a floating-point number representing
	 * the result of the dot product operation.
	 *
	 * @param r second vector in the dot product operation, whose components are used to
	 * calculate the result.
	 *
	 * @returns a scalar value representing the dot product of two vectors.
	 */
	public float dot(Vector2f r) {
		return x * r.getX() + y * r.getY();
	}

	/**
	 * Calculates the length of a vector and returns a new vector with the same direction
	 * but with a length of 1.
	 *
	 * @returns a new Vector2f object with normalized coordinates, where each coordinate
	 * is between -1 and 1.
	 */
	public Vector2f normalized() {
		float length = length();

		return new Vector2f(x / length, y / length);
	}

	/**
	 * Calculates the cross product of two vectors. It takes a `Vector2f` object `r` as
	 * input and returns a scalar value representing the area of the parallelogram formed
	 * by the two vectors.
	 *
	 * @param r second vector in a cross product operation, used to calculate the
	 * perpendicular vector.
	 *
	 * @returns the cross product of two vectors, a scalar value representing their perpendicularity.
	 */
	public float cross(Vector2f r) {
		return x * r.getY() - y * r.getX();
	}

	/**
	 * Calculates a linear interpolation between the current vector and the destination
	 * vector,
	 * resulting in a new vector that is a specified factor away from the current vector
	 * towards the destination vector.
	 * It uses vector subtraction, scalar multiplication, and vector addition to achieve
	 * this.
	 *
	 * @param dest destination vector to which the current vector is interpolated.
	 *
	 * @param lerpFactor percentage of the distance between the current position and the
	 * destination position.
	 *
	 * @returns a Vector2f that is the interpolated result of the current position and
	 * the destination, scaled by the lerpFactor.
	 */
	public Vector2f lerp(Vector2f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	/**
	 * Rotates a 2D vector represented by `x` and `y` components by a specified angle.
	 * The result is a new vector with components transformed according to the rotation
	 * matrix. The original vector remains unchanged.
	 *
	 * @param angle rotation angle in degrees that is applied to the vector.
	 *
	 * @returns a new vector resulting from rotating the original vector by the specified
	 * angle clockwise.
	 */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	/**
	 * Calculates a new vector by adding corresponding components of two input vectors.
	 * It takes a `Vector2f` object as input, adds its components to the current vector's
	 * components, and returns a new `Vector2f` object with the resulting components.
	 *
	 * @param r vector to be added to the current vector.
	 *
	 * @returns a new Vector2f object with elements being the sum of the input Vector2f's
	 * elements.
	 */
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.getX(), y + r.getY());
	}

	/**
	 * Adds a specified value to the x and y coordinates of a 2D vector, creating a new
	 * vector with the updated coordinates.
	 *
	 * @param r amount to be added to each component of the vector.
	 *
	 * @returns a new `Vector2f` object with x and y coordinates incremented by the given
	 * float value `r`.
	 */
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}

    /**
     * Adds a specified vector to the current vector, returning a new vector with the
     * resulting coordinates. The function takes two float parameters, x and y, which
     * represent the components of the vector to be added.
     *
     * @param x horizontal component to be added to the current vector's x-coordinate.
     *
     * @param y y-coordinate of a point to be added to the current vector.
     *
     * @returns a new `Vector2f` object with added x and y components based on the input
     * values.
     */
    public Vector2f add(float x, float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

	/**
	 * Subtracts one vector from another, performing element-wise subtraction of corresponding
	 * components, resulting in a new vector with the difference between the two input vectors.
	 *
	 * @param r vector from which the current vector's components are subtracted.
	 *
	 * @returns a new Vector2f instance with components (x - r.x, y - r.y).
	 */
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.getX(), y - r.getY());
	}

	/**
	 * Subtracts a specified floating-point value from the x and y components of a 2D vector.
	 *
	 * @param r amount to be subtracted from the x and y coordinates of the vector.
	 *
	 * @returns a new `Vector2f` object with x and y coordinates reduced by the specified
	 * float value r.
	 */
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}

	/**
	 * Performs element-wise multiplication of two vectors, resulting in a new vector
	 * with corresponding components multiplied together.
	 *
	 * @param r vector to multiply with the current vector.
	 *
	 * @returns a new Vector2f object with components equal to the product of the input
	 * Vector2f's components.
	 */
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.getX(), y * r.getY());
	}

	/**
	 * Scales a 2D vector by a given scalar factor, returning a new vector with the scaled
	 * x and y components.
	 *
	 * @param r scalar value by which the components of the vector are multiplied.
	 *
	 * @returns a new Vector2f object with components scaled by the input parameter r.
	 */
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}

	/**
	 * Performs element-wise division of the current vector by the input vector `r`,
	 * returning a new vector with the result.
	 *
	 * @param r vector to divide the current vector by, with its components used for
	 * division operations.
	 *
	 * @returns a new Vector2f instance with components resulting from the division of
	 * the current vector by the input vector r.
	 */
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.getX(), y / r.getY());
	}

	/**
	 * Divides a vector by a scalar value and returns a new vector with the result. The
	 * division operation is performed element-wise, separately on the x and y components
	 * of the vector. The original vector remains unchanged.
	 *
	 * @param r divisor for scalar division, used to normalize the vector's magnitude.
	 *
	 * @returns a new `Vector2f` object with components divided by the input scalar `r`.
	 */
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}

	/**
	 * Returns a new `Vector2f` object with the absolute values of its corresponding x
	 * and y components. The original vector remains unchanged. This method is used to
	 * obtain the magnitude of the vector without direction.
	 *
	 * @returns A new `Vector2f` object with absolute values of its components x and y.
	 */
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	/**
	 * Converts the object's state into a string representation, specifically a string
	 * in the format "(x y)" where x and y are the object's coordinates.
	 *
	 * @returns a string representation of the object in the format "(x y)".
	 */
	public String toString() {
		return "(" + x + " " + y + ")";
	}

	/**
	 * Sets the x and y coordinates of the Vector2f object and returns the object itself,
	 * allowing for method chaining.
	 *
	 * @param x new x-coordinate value to be assigned to the object's x-component.
	 *
	 * @param y y-coordinate of the vector.
	 *
	 * @returns a Vector2f object with the specified x and y coordinates.
	 */
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Updates the current Vector2f object with the coordinates from the input Vector2f
	 * object `r`.
	 *
	 * @param r vector to be assigned to the current object.
	 *
	 * @returns a Vector2f object with its x and y coordinates set to the input values.
	 */
	public Vector2f set(Vector2f r) {
		set(r.getX(), r.getY());
		return this;
	}

    /**
     * Creates a new 3D vector from the current object's x and y coordinates, with the z
     * coordinate set to 0.
     *
     * @returns A 3D vector with x and y components from the original object, and a z
     * component of 0.
     */
    public Vector3f as3DVector() {
        return new Vector3f(x, y, 0);
    }

	/**
	 * Returns the value of the `x` variable, which is a float. This suggests it is part
	 * of a class or object with a floating-point coordinate. It provides read-only access
	 * to the `x` value.
	 *
	 * @returns the value of the instance variable `x` of type `float`.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the value of a float variable `x` to a specified value. It takes one parameter,
	 * `x`, and assigns it to the instance variable `x`.
	 *
	 * @param x new value to be assigned to the instance variable `x`.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the value of the variable `y` as a floating-point number. The function
	 * does not modify the `y` variable and simply provides access to its current value.
	 *
	 * @returns the value of the instance variable `y`, which is of type `float`.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Updates the `y` field of the current object to the specified value.
	 *
	 * @param y new y-coordinate value to be assigned to the object's y property.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Compares two Vector2f objects for equality by checking if their x and y coordinates
	 * are equal. It returns true if the coordinates match and false otherwise. The
	 * comparison is based on exact equality, not approximate equality.
	 *
	 * @param r right-hand side vector being compared for equality.
	 *
	 * @returns a boolean value indicating whether the current object's coordinates match
	 * the input vector's coordinates.
	 */
	public boolean equals(Vector2f r) {
		return x == r.getX() && y == r.getY();
	}
	
}
