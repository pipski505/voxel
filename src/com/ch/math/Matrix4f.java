package com.ch.math;

/**
 * Represents a 4x4 matrix with various initialization methods for transformations
 * such as translation, rotation, scaling, and projection.
 */
public class Matrix4f {
	
	private float[][] data;

	public Matrix4f() {
		data = new float[4][4];
	}

	/**
	 * Sets the elements of a 4x4 matrix to their identity matrix values, where the
	 * diagonal elements are 1 and the off-diagonal elements are 0. This is a common
	 * initialization for matrices used in linear transformations. The function returns
	 * the initialized matrix.
	 *
	 * @returns a 4x4 matrix representing the identity transformation.
	 *
	 * Initialized as a 4x4 identity matrix.
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
	 * Sets the translation values in a 4x4 matrix, allowing for 3D movement in the x,
	 * y, and z axes, and returns the modified matrix. The translation values are stored
	 * in the last column of the matrix.
	 *
	 * @param x translation value along the x-axis of a 3D object.
	 *
	 * @param y y-coordinate of the translation, which is stored in the second column of
	 * the translation matrix.
	 *
	 * @param z translation along the z-axis, affecting the position of the object in the
	 * third dimension.
	 *
	 * @returns a 4x4 translation matrix with specified x, y, and z values.
	 *
	 * The output is a 4x4 homogeneous matrix representing a translation transformation.
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
	 * Constructs a 4x4 rotation matrix from three Euler angles (x, y, z) and multiplies
	 * them together in the order rz, ry, rx to produce a single rotation matrix.
	 *
	 * @param x rotation angle around the X-axis in the rotation matrix.
	 *
	 * @param y rotation angle around the y-axis.
	 *
	 * @param z rotation around the z-axis.
	 *
	 * @returns a 4x4 rotation matrix representing the specified rotation around X, Y,
	 * and Z axes.
	 *
	 * The returned output is a 4x4 matrix representing the rotation transformation.
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
	 * Initializes a 4x4 matrix for scaling transformations. It sets the diagonal elements
	 * to the specified scale factors (x, y, z) and the rest to zero, preserving the
	 * identity matrix's properties.
	 *
	 * @param x scale factor applied to the x-axis of the matrix.
	 *
	 * @param y scale factor applied to the y-axis.
	 *
	 * @param z scale factor along the z-axis.
	 *
	 * @returns a 4x4 matrix representing a scale transformation with the specified x,
	 * y, and z values.
	 *
	 * The output is a 4x4 homogenous transformation matrix. It represents a scaling
	 * transformation with scale factors x, y, and z along the x, y, and z axes, respectively.
	 * The matrix has a determinant of x*y*z.
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
	 * Initializes a 4x4 matrix representing a perspective projection transformation based
	 * on the provided field of view, aspect ratio, and near and far clipping planes. It
	 * returns a reference to the modified object.
	 *
	 * @param fov field of view in the perspective projection, which determines the angle
	 * at which objects are visible.
	 *
	 * @param aspectRatio ratio of the horizontal to the vertical field of view in the
	 * perspective matrix.
	 *
	 * @param zNear distance between the camera and the nearest point in the scene that
	 * is still visible.
	 *
	 * @param zFar maximum distance in the z-axis from the viewer where objects are
	 * considered to be at infinity.
	 *
	 * @returns a pre-configured 4x4 matrix representing a perspective projection.
	 *
	 * The output is a 4x4 matrix. It is a perspective projection matrix used for 3D
	 * rendering, specifically for transforming objects from 3D space to 2D screen space.
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
	 * Calculates and initializes the elements of a 4x4 matrix to represent an orthographic
	 * projection. The matrix is configured based on the provided parameters for the left,
	 * right, bottom, top, near, and far clipping planes.
	 *
	 * @param left x-coordinate of the left edge of the viewing volume in an orthographic
	 * projection.
	 *
	 * @param right x-coordinate of the right edge of the orthographic projection frustum,
	 * used to calculate the matrix elements that scale and translate the x-axis.
	 *
	 * @param bottom minimum y-coordinate of the view volume in an orthographic projection.
	 *
	 * @param top y-coordinate of the top of the viewing frustum, and is used to calculate
	 * the vertical scale factor in the orthographic projection matrix.
	 *
	 * @param near distance from the viewer to the nearest point in the scene that can
	 * be projected onto the screen, affecting the depth of the orthographic projection.
	 *
	 * @param far maximum distance from the viewer in the negative z-axis direction.
	 *
	 * @returns a 4x4 matrix representing an orthographic projection.
	 *
	 * It is a 4x4 matrix.
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
	 * Calculates a rotation matrix based on the given forward and up vectors. It normalizes
	 * the input vectors, computes the right vector by crossing the forward and up vectors,
	 * and then computes the up vector by crossing the forward and right vectors.
	 *
	 * @param forward direction in which the rotation is applied, and it is used to
	 * calculate the new coordinate system's forward axis.
	 *
	 * @param up orientation of the object's local up direction, which is used to calculate
	 * the object's rotation matrix.
	 *
	 * @returns a rotation matrix representing the rotation from the default coordinate
	 * system to the specified coordinate system.
	 */
	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();

		Vector3f r = up.normalized();
		r = r.cross(f);

		Vector3f u = f.cross(r);

		return initRotation(f, u, r);
	}

	/**
	 * Sets the elements of a 4x4 matrix to represent a rotation transformation based on
	 * the provided forward, up, and right vectors. The matrix is populated with the x,
	 * y, and z components of these vectors in the first three rows, and the fourth row
	 * is set to [0, 0, 0, 1].
	 *
	 * @param forward direction of the forward axis in the resulting rotation matrix.
	 *
	 * Extracts the X, Y, and Z components of the input vector.
	 *
	 * @param up direction of the up vector in a 3D coordinate system, which is used to
	 * construct a rotation matrix.
	 *
	 * Assign.
	 * `up` is a 3D vector, specifically a Vector3f object, representing a unit vector
	 * in the direction of up.
	 *
	 * @param right direction of the x-axis in the rotation matrix, which is stored in
	 * the first column of the matrix.
	 *
	 * Destructure: Vector3f r = right, with components r.getX(), r.getY(), and r.getZ().
	 * Main properties: r is a 3D vector representing the direction of the local right axis.
	 *
	 * @returns a 4x4 rotation matrix populated with the provided forward, up, and right
	 * vectors.
	 *
	 * The returned output is a 4x4 matrix, specifically a rotation matrix.
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
	 * Performs a 3D affine transformation on a given vector. It multiplies the input
	 * vector by a 3x4 matrix represented by `data`, resulting in a transformed vector.
	 *
	 * @param r vector to be transformed by the given matrix, with its components (x, y,
	 * z) used in the transformation computation.
	 *
	 * @returns a Vector3f object representing the transformed vector.
	 */
	public Vector3f transform(Vector3f r) {
		return new Vector3f(data[0][0] * r.getX() + data[0][1] * r.getY() + data[0][2] * r.getZ() + data[0][3], data[1][0] * r.getX() + data[1][1] * r.getY() + data[1][2]
				* r.getZ() + data[1][3], data[2][0] * r.getX() + data[2][1] * r.getY() + data[2][2] * r.getZ() + data[2][3]);
	}

	/**
	 * Calculates the product of two 4x4 matrices. It takes a `Matrix4f` object as input,
	 * performs a matrix multiplication, and returns the resulting matrix. The result is
	 * a new matrix representing the product of the input matrices.
	 *
	 * @param r right-hand side matrix in a matrix multiplication operation.
	 *
	 * Extract its elements.
	 *
	 * @returns a new 4x4 matrix resulting from the matrix multiplication of the input matrices.
	 *
	 * The returned output is a 4x4 matrix representing the product of two input matrices.
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
	 * Copies data from a 2D array called `data` into a new 4x4 2D array and returns it.
	 * The new array is initialized with default values, which are then replaced with the
	 * actual data from `data`. The function returns the copied data.
	 *
	 * @returns a 4x4 2D array of float values.
	 */
	public float[][] getData() {
		float[][] res = new float[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res[i][j] = data[i][j];

		return res;
	}
	
	/**
	 * Returns an array of 16 float values, specifically the first element of each sub-array
	 * in the 4x4 `data` array, ordered in a linear fashion.
	 *
	 * @returns an array of 16 float values representing linear data points.
	 *
	 * The returned output is an array of 16 float elements.
	 * It represents a flattened version of a 4x4 matrix, with values from each row
	 * concatenated in sequence.
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
	 * Accesses and returns a 2D array element at the specified coordinates. It takes two
	 * integer parameters, x and y, to determine the array position. The function returns
	 * a float value.
	 *
	 * @param x row index of the 2D array `data` accessed by the function.
	 *
	 * @param y column index in a 2D array.
	 *
	 * @returns the value at the specified 2D array index (x, y).
	 */
	public float get(int x, int y) {
		return data[x][y];
	}

	/**
	 * Assigns a 2D float array to the object's `data` variable. The input array is stored
	 * as is, without any modifications. This function is likely used to update or replace
	 * the existing data.
	 *
	 * @param data 2D array to be assigned to the class's `data` field.
	 */
	public void SetM(float[][] data) {
		this.data = data;
	}

	/**
	 * Assigns a specified `value` to a location in a 2D array, identified by `x` and `y`
	 * coordinates. The `data` array is modified in place. The function takes three
	 * parameters: `x`, `y`, and `value`.
	 *
	 * @param x row index of the 2D array where the value will be set.
	 *
	 * @param y column index in a two-dimensional array, used to access and modify a
	 * specific element.
	 *
	 * @param value numeric value to be assigned to the specified location in the `data`
	 * array.
	 */
	public void set(int x, int y, float value) {
		data[x][y] = value;
	}

	/**
	 * Performs a matrix transpose operation on the data matrix, swapping its rows with
	 * columns, and assigns the result back to the data matrix. The resulting data matrix
	 * is a transposed version of the original. The process is performed in-place, modifying
	 * the original data.
	 */
	public void transposeSelf() {
		float[][] tr = new float[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				tr[i][j] = data[j][i];
		this.data = tr;
	}
	
}
