package com.ch.math;

/**
 * Represents a 4x4 matrix for 3D transformation, providing methods for initialization,
 * multiplication, and transformation of vectors.
 */
public class Matrix4f {
	
	private float[][] data;

	public Matrix4f() {
		data = new float[4][4];
	}

	/**
	 * Sets the elements of a 4x4 matrix to their identity matrix values. The identity
	 * matrix is a special square matrix with 1s on the main diagonal and 0s elsewhere,
	 * used for matrix multiplication and transformations. The function returns the matrix
	 * instance itself.
	 *
	 * @returns a 4x4 matrix representing the identity transformation with diagonal
	 * elements equal to 1.
	 *
	 * The returned output is a 4x4 matrix with diagonal elements set to 1 and off-diagonal
	 * elements set to 0.
	 */
	public Matrix4f initIdentity() {
		data[0][0] = 1;
		data[0][1] = 0;
		data[0][2] = 0;
		data[0][3] = 0;
		data[1][0] = 0;
		data[1][1] = 1;
		data[1][2] = 0;
		data[1][3] = 0;
		data[2][0] = 0;
		data[2][1] = 0;
		data[2][2] = 1;
		data[2][3] = 0;
		data[3][0] = 0;
		data[3][1] = 0;
		data[3][2] = 0;
		data[3][3] = 1;

		return this;
	}

	/**
	 * Sets the translation values in a 4x4 matrix. It populates the last column of the
	 * matrix with the given translation coordinates (x, y, z), which represents a 3D translation.
	 *
	 * @param x translation along the x-axis, and its value is used to set the corresponding
	 * element in the matrix to define the translation.
	 *
	 * @param y vertical translation of the matrix, specifying the amount of movement
	 * along the y-axis.
	 *
	 * @param z translation along the z-axis in 3D space.
	 *
	 * @returns a 4x4 translation matrix initialized with the specified x, y, and z coordinates.
	 *
	 * The returned output is a 4x4 matrix with translation properties. It is an identity
	 * matrix with the last column modified to represent a translation. The last column's
	 * values at indices (0, 3), (1, 3), and (2, 3) are x, y, and z respectively.
	 */
	public Matrix4f initTranslation(float x, float y, float z) {
//        x = -x;
		data[0][0] = 1;
		data[0][1] = 0;
		data[0][2] = 0;
		data[0][3] = x;
		data[1][0] = 0;
		data[1][1] = 1;
		data[1][2] = 0;
		data[1][3] = y;
		data[2][0] = 0;
		data[2][1] = 0;
		data[2][2] = 1;
		data[2][3] = z;
		data[3][0] = 0;
		data[3][1] = 0;
		data[3][2] = 0;
		data[3][3] = 1;

		return this;
	}

	/**
	 * Calculates and initializes a 4x4 rotation matrix based on the input angles x, y,
	 * and z, and stores it in the `data` field of the current object.
	 *
	 * @param x rotation angle around the x-axis in degrees.
	 *
	 * @param y rotation around the y-axis.
	 *
	 * @param z rotation around the z-axis.
	 *
	 * @returns a 4x4 rotation matrix representing the combined rotation around x, y, and
	 * z axes.
	 *
	 * Constructed as a 4x4 matrix, the output represents a composite rotation transformation
	 * in 3D space. It combines rotations around the x, y, and z axes.
	 */
	public Matrix4f initRotation(float x, float y, float z) {
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();

		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);

		rz.data[0][0] = (float) Math.cos(z);
		rz.data[0][1] = -(float) Math.sin(z);
		rz.data[0][2] = 0;
		rz.data[0][3] = 0;
		rz.data[1][0] = (float) Math.sin(z);
		rz.data[1][1] = (float) Math.cos(z);
		rz.data[1][2] = 0;
		rz.data[1][3] = 0;
		rz.data[2][0] = 0;
		rz.data[2][1] = 0;
		rz.data[2][2] = 1;
		rz.data[2][3] = 0;
		rz.data[3][0] = 0;
		rz.data[3][1] = 0;
		rz.data[3][2] = 0;
		rz.data[3][3] = 1;

		rx.data[0][0] = 1;
		rx.data[0][1] = 0;
		rx.data[0][2] = 0;
		rx.data[0][3] = 0;
		rx.data[1][0] = 0;
		rx.data[1][1] = (float) Math.cos(x);
		rx.data[1][2] = -(float) Math.sin(x);
		rx.data[1][3] = 0;
		rx.data[2][0] = 0;
		rx.data[2][1] = (float) Math.sin(x);
		rx.data[2][2] = (float) Math.cos(x);
		rx.data[2][3] = 0;
		rx.data[3][0] = 0;
		rx.data[3][1] = 0;
		rx.data[3][2] = 0;
		rx.data[3][3] = 1;

		ry.data[0][0] = (float) Math.cos(y);
		ry.data[0][1] = 0;
		ry.data[0][2] = -(float) Math.sin(y);
		ry.data[0][3] = 0;
		ry.data[1][0] = 0;
		ry.data[1][1] = 1;
		ry.data[1][2] = 0;
		ry.data[1][3] = 0;
		ry.data[2][0] = (float) Math.sin(y);
		ry.data[2][1] = 0;
		ry.data[2][2] = (float) Math.cos(y);
		ry.data[2][3] = 0;
		ry.data[3][0] = 0;
		ry.data[3][1] = 0;
		ry.data[3][2] = 0;
		ry.data[3][3] = 1;

		data = rz.mul(ry.mul(rx)).getData();

		return this;
	}

	/**
	 * Sets the scale values for a 4x4 matrix, specifying the x, y, and z axes independently,
	 * and returns the updated matrix instance. The function initializes the matrix with
	 * the given scale values, leaving other elements unchanged.
	 *
	 * @param x scale factor applied to the x-axis of the matrix.
	 *
	 * @param y scale factor along the y-axis of the matrix.
	 *
	 * @param z scale factor along the z-axis.
	 *
	 * @returns a 4x4 matrix representing a scale transformation with specified x, y, and
	 * z values.
	 *
	 * The returned output is a 4x4 Matrix4f object with diagonal elements representing
	 * scale factors (x, y, z) and off-diagonal elements set to 0, except for the
	 * bottom-right element which is 1.
	 */
	public Matrix4f initScale(float x, float y, float z) {
		data[0][0] = x;
		data[0][1] = 0;
		data[0][2] = 0;
		data[0][3] = 0;
		data[1][0] = 0;
		data[1][1] = y;
		data[1][2] = 0;
		data[1][3] = 0;
		data[2][0] = 0;
		data[2][1] = 0;
		data[2][2] = z;
		data[2][3] = 0;
		data[3][0] = 0;
		data[3][1] = 0;
		data[3][2] = 0;
		data[3][3] = 1;

		return this;
	}

	/**
	 * Calculates and initializes a 4x4 perspective projection matrix for 3D rendering.
	 * It takes four parameters: field of view, aspect ratio, near clipping plane, and
	 * far clipping plane, and populates the matrix elements accordingly.
	 *
	 * @param fov field of view, determining the angle of vision in the perspective projection.
	 *
	 * @param aspectRatio ratio of the horizontal to vertical screen size, affecting the
	 * projection matrix's horizontal and vertical field of view.
	 *
	 * @param zNear near clipping plane distance, used to calculate the perspective
	 * matrix's elements.
	 *
	 * @param zFar far clipping plane, determining the depth at which objects are no
	 * longer rendered.
	 *
	 * @returns a 4x4 matrix representing a perspective projection.
	 *
	 * The output is a 4x4 matrix. It represents a perspective projection matrix, which
	 * is used to map 3D points onto a 2D image plane.
	 */
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float) Math.tan(Math.toRadians(fov) / 2);
		float zRange = zNear - zFar;

		data[0][0] = 1.0f / (tanHalfFOV * aspectRatio);
		data[0][1] = 0;
		data[0][2] = 0;
		data[0][3] = 0;
		data[1][0] = 0;
		data[1][1] = 1.0f / tanHalfFOV;
		data[1][2] = 0;
		data[1][3] = 0;
		data[2][0] = 0;
		data[2][1] = 0;
		data[2][2] = (-zNear - zFar) / zRange;
		data[2][3] = 2 * zFar * zNear / zRange;
		data[3][0] = 0;
		data[3][1] = 0;
		data[3][2] = 1;
		data[3][3] = 0;

		return this;
	}

	/**
	 * Sets up an orthographic projection matrix. It calculates the matrix elements based
	 * on the provided view volume parameters (left, right, bottom, top, near, far) to
	 * transform 3D coordinates into 2D screen coordinates.
	 *
	 * @param left coordinate of the left edge of the orthographic view volume.
	 *
	 * @param right x-coordinate of the right edge of the viewing frustum in an orthographic
	 * projection.
	 *
	 * @param bottom the y-coordinate of the near plane in the orthographic projection,
	 * affecting the position and scaling of objects in the y-axis.
	 *
	 * @param top y-coordinate of the top edge of the viewing frustum, which affects the
	 * vertical scaling of the orthographic projection matrix.
	 *
	 * @param near distance to the nearest point in the scene that is visible, affecting
	 * the orthographic projection's depth scaling.
	 *
	 * @param far maximum distance from the viewer, determining the depth range of the
	 * orthographic projection.
	 *
	 * @returns a pre-populated 4x4 matrix representing an orthographic projection.
	 *
	 * The output is a 4x4 matrix with specific elements. The main properties are its
	 * diagonal elements, which are non-zero, and its non-diagonal elements, which are zero.
	 */
	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far) {
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		data[0][0] = 2 / width;
		data[0][1] = 0;
		data[0][2] = 0;
		data[0][3] = -(right + left) / width;
		data[1][0] = 0;
		data[1][1] = 2 / height;
		data[1][2] = 0;
		data[1][3] = -(top + bottom) / height;
		data[2][0] = 0;
		data[2][1] = 0;
		data[2][2] = -2 / depth;
		data[2][3] = -(far + near) / depth;
		data[3][0] = 0;
		data[3][1] = 0;
		data[3][2] = 0;
		data[3][3] = 1;

		return this;
	}

	/**
	 * Calculates a rotation matrix from a given forward and up vector. It normalizes the
	 * forward vector and calculates a right vector by taking the cross product of the
	 * up and forward vectors.
	 *
	 * @param forward direction of the object's forward axis, used to calculate the
	 * object's rotation matrix.
	 *
	 * @param up direction of the local up axis in a 3D space.
	 *
	 * @returns a 4x4 rotation matrix representing the rotation based on the specified
	 * forward and up vectors.
	 */
	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();

		Vector3f r = up.normalized();
		r = r.cross(f);

		Vector3f u = f.cross(r);

		return initRotation(f, u, r);
	}

	/**
	 * Populates a 4x4 matrix with the given right, up, and forward vectors, effectively
	 * creating a rotation matrix in 3D space. The resulting matrix can be used for
	 * transformations such as rotation and orientation.
	 *
	 * @param forward direction of the forward axis of the rotation matrix, which is set
	 * to the third column of the matrix.
	 *
	 * Extract.
	 * The `forward` is a Vector3f with x, y, and z components representing the direction
	 * in which the rotation is applied.
	 *
	 * @param up direction of the local up axis in the 3D space, used to construct a
	 * rotation matrix.
	 *
	 * Assign the x, y, and z components of the input vector `up` to the corresponding
	 * rows of the matrix. The `up` vector represents the direction of the matrix's y-axis.
	 *
	 * @param right direction of the x-axis in the rotation matrix.
	 *
	 * Extract.
	 * It is a 3D vector.
	 *
	 * @returns a Matrix4f object representing a rotation matrix based on the input vectors.
	 *
	 * The returned output is a 4x4 rotation matrix, specifically an orthonormal matrix.
	 */
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		Vector3f f = forward;
		Vector3f r = right;
		Vector3f u = up;

		data[0][0] = r.getX();
		data[0][1] = r.getY();
		data[0][2] = r.getZ();
		data[0][3] = 0;
		data[1][0] = u.getX();
		data[1][1] = u.getY();
		data[1][2] = u.getZ();
		data[1][3] = 0;
		data[2][0] = f.getX();
		data[2][1] = f.getY();
		data[2][2] = f.getZ();
		data[2][3] = 0;
		data[3][0] = 0;
		data[3][1] = 0;
		data[3][2] = 0;
		data[3][3] = 1;

		return this;
	}

	/**
	 * Performs a matrix-vector multiplication, transforming a given 3D vector `r` by a
	 * 3x3 matrix represented by the `data` array. The result is a new 3D vector.
	 *
	 * @param r vector being transformed by the function, with its x, y, and z components
	 * being multiplied by corresponding elements of the data array.
	 *
	 * @returns a transformed Vector3f object resulting from matrix multiplication.
	 */
	public Vector3f transform(Vector3f r) {
		return new Vector3f(data[0][0] * r.getX() + data[0][1] * r.getY() + data[0][2] * r.getZ() + data[0][3], data[1][0] * r.getX() + data[1][1] * r.getY() + data[1][2]
				* r.getZ() + data[1][3], data[2][0] * r.getX() + data[2][1] * r.getY() + data[2][2] * r.getZ() + data[2][3]);
	}

	/**
	 * Multiplies two 4x4 matrices, `this` and `r`, element-wise, and returns the resulting
	 * product matrix.
	 *
	 * @param r right-hand side matrix in a matrix multiplication operation.
	 *
	 * Depict its main properties.
	 * It is a 4x4 matrix.
	 *
	 * @returns a new 4x4 matrix representing the product of the input matrices.
	 *
	 * The output is a 4x4 matrix.
	 */
	public Matrix4f mul(Matrix4f r) {
		Matrix4f res = new Matrix4f();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res.set(i, j, data[i][0] * r.get(0, j) + data[i][1] * r.get(1, j) + data[i][2] * r.get(2, j) + data[i][3] * r.get(3, j));
			}
		}

		return res;
	}

	/**
	 * Copies a 4x4 two-dimensional array named `data` and returns a copy of it as a float
	 * array.
	 *
	 * @returns a 4x4 two-dimensional array of float values.
	 */
	public float[][] getData() {
		float[][] res = new float[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res[i][j] = data[i][j];

		return res;
	}
	
	/**
	 * Returns an array of 16 float values, extracted from a 4x4 data array, with each
	 * value repeated four times in the order of first row, first column to fourth row,
	 * first column.
	 *
	 * @returns an array of 16 float values representing linear data from a 4x4 matrix.
	 *
	 * Expose the returned array as a 2D array with 4 rows and 4 columns.
	 */
	public float[] getLinearData() {
		return new float[] {
			data[0][0],
			data[1][0],
			data[2][0],
			data[3][0],
			data[0][1],
			data[1][1],
			data[2][1],
			data[3][1],
			data[0][2],
			data[1][2],
			data[2][2],
			data[3][2],
			data[0][3],
			data[1][3],
			data[2][3],
			data[3][3],
		};
	}

	/**
	 * Retrieves a float value from a 2D array at a specified x and y index, returning
	 * it as the function's result.
	 *
	 * @param x row index in a 2D data array.
	 *
	 * @param y column index in a 2D array.
	 *
	 * @returns a float value representing data at the specified 2D coordinates.
	 */
	public float get(int x, int y) {
		return data[x][y];
	}

	/**
	 * Assigns a 2D array of floating-point numbers to an instance variable named `data`.
	 *
	 * @param data two-dimensional array of floating-point numbers to be assigned to the
	 * class's `data` field.
	 */
	public void SetM(float[][] data) {
		this.data = data;
	}

	/**
	 * Updates a specific cell in a 2D data array at coordinates `x` and `y` with a given
	 * `value`. The data array is assumed to be a 2D array of floats.
	 *
	 * @param x row index in a 2D array, specifying the position where a value is to be
	 * set.
	 *
	 * @param y column index in a two-dimensional array `data`.
	 *
	 * @param value new value to be assigned to the specified data point at coordinates
	 * x and y.
	 */
	public void set(int x, int y, float value) {
		data[x][y] = value;
	}

	/**
	 * Transposes a 4x4 matrix by swapping its rows with columns, creating a new matrix,
	 * and then replacing the original matrix with the transposed one.
	 */
	public void transposeSelf() {
		float[][] tr = new float[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				tr[i][j] = data[j][i];
		this.data = tr;
	}
	
}
