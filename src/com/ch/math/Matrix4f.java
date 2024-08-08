package com.ch.math;

/**
 * Is a data structure for representing 4x4 matrices. It provides methods for
 * initializing the matrix with rotation and translation vectors, as well as methods
 * for transforming vectors and other matrices. The class also provides access to the
 * linear data of the matrix through its `getLinearData()` method.
 */
public class Matrix4f {
	
	private float[][] data;

	public Matrix4f() {
		data = new float[4][4];
	}

	/**
	 * Sets all elements of a `Matrix4f` object to identity values, i.e., matrix with
	 * diagonal elements equal to 1 and off-diagonal elements equal to 0.
	 * 
	 * @returns a matrix with all elements set to either 0 or 1, depending on their
	 * position in the matrix.
	 * 
	 * The `Matrix4f` object is initialized with identity matrix elements. Specifically,
	 * each element in the 16-element array is set to either 0 or 1, depending on its row
	 * and column index. The resulting matrix has a determinant of 1 and is equivalent
	 * to the identity matrix.
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
	 * Initializes a matrix with translation values `x`, `y`, and `z`. It sets the elements
	 * of the matrix to represent the position of an object in 3D space after translation.
	 * 
	 * @param x 3D translation amount in the x-axis direction, which is negated before
	 * being assigned to the matrix data.
	 * 
	 * @param y 2nd translation component, which is added to the `data` array at index 1.
	 * 
	 * @param z 3rd dimension of the matrix, which is translated by the value provided.
	 * 
	 * @returns a modified instance of the `Matrix4f` class.
	 * 
	 * 	- The returned object is a `Matrix4f` instance, representing a 4x4 homogeneous
	 * transformation matrix.
	 * 	- The matrix elements are updated to reflect the translation vector (x, y, z)
	 * from the origin. Specifically, the first column represents the x-component of the
	 * translation, the second column represents the y-component, and the third column
	 * represents the z-component.
	 * 	- The returned object retains the same properties as the original matrix, including
	 * its homogeneity and 4x4 structure.
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
	 * Initializes a rotation matrix based on three Euler angles (x, y, z). It returns
	 * the rotation matrix as a new object instance.
	 * 
	 * @param x 3D rotation angle around the z-axis, which is used to calculate the
	 * rotation matrix rz.
	 * 
	 * @param y 2D rotation axis around which the 3D rotation is performed, and it is
	 * used to calculate the rotation matrix using the rotation angles provided by the
	 * other two input parameters.
	 * 
	 * @param z 3D rotation axis around which the rotation is performed, and it is used
	 * to compute the rotations of the `rx`, `ry`, and `rz` matrices.
	 * 
	 * @returns a `Matrix4f` object containing the rotation matrix based on the given
	 * Euler angles.
	 * 
	 * 	- The `data` field is a 4x4 matrix representing the rotated coordinate system.
	 * 	- The elements of the matrix are floating-point values representing the individual
	 * components of the rotation.
	 * 	- The rotation is represented by three Euler angles (x, y, z) and their corresponding
	 * quaternion representations (rz, ry, rx).
	 * 	- The quaternions are multiplied together to form the final rotated coordinate system.
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
	 * Initializes a matrix with scaled values for the x, y, and z axes, returning the
	 * modified matrix instance.
	 * 
	 * @param x 3D scale factor along the x-axis of the matrix.
	 * 
	 * @param y 2D scaling factor for the matrix in the `initScale()` function.
	 * 
	 * @param z 3rd component of the scaling vector, which when multiplied with the
	 * matrix's elements, scales the matrix by the corresponding factor in the x, y, and
	 * z directions.
	 * 
	 * @returns a reference to the original matrix.
	 * 
	 * 1/ The matrix is a 4x4 identity matrix with all elements set to zero except for
	 * the top-left element, which is equal to the input parameter `x`.
	 * 2/ The matrix has been transformed by scaling along the x-axis by the factor `x`,
	 * while keeping the other axes unchanged.
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
	 * Initializes a matrix for perspective projection, calculating the necessary values
	 * based on the provided field of view (fov), aspect ratio, near and far distances.
	 * 
	 * @param fov field of view (FOV) of the camera, which determines the angle of the
	 * horizontal visual FOV.
	 * 
	 * @param aspectRatio 2D screen aspect ratio of the viewport, which is used to scale
	 * the near and far clipping planes along the x-axis to maintain the proper perspective
	 * in the resulting matrix.
	 * 
	 * @param zNear near clipping plane distance in the perspective projection matrix.
	 * 
	 * @param zFar 4th coordinate of the 4D vector returned by the `initPerspective`
	 * function, which is the farthest point from the origin that the perspective projection
	 * extends to.
	 * 
	 * @returns a reference to a matrix object containing the perspective projection parameters.
	 * 
	 * 	- The data array `data` has 16 elements, with each element representing a component
	 * of the perspective projection matrix.
	 * 	- The elements of the array are initialized with values that depend on the input
	 * parameters, specifically the field of view (fov), aspect ratio, near and far
	 * distances, and the tan of half of the fov.
	 * 	- The returned output is a matrix object that represents the perspective projection
	 * matrix, which is used to transform 3D points into screen coordinates in the viewport.
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
	 * Initializes a matrix for an orthographic projection, where the near and far planes
	 * are set to specified values, and the width, height, and depth of the image are
	 * calculated based on the aspect ratio of the near plane.
	 * 
	 * @param left 3D coordinate of the left edge of the orthographic projection, which
	 * determines the position of the origin of the projection.
	 * 
	 * @param right right edge of the orthographic projection, which is used to calculate
	 * the values for the matrix's elements.
	 * 
	 * @param bottom 2D coordinate of the bottom-left corner of the orthographic projection's
	 * viewport.
	 * 
	 * @param top 2D coordinate of the top edge of the orthographic projection, which is
	 * used to calculate the corresponding vertex position in the output matrix.
	 * 
	 * @param near near plane of the orthographic projection, which determines the distance
	 * from the viewer at which objects appear to be in focus.
	 * 
	 * @param far 3D far plane distance, which is used to compute the inverse perspective
	 * matrix elements for depth and near-far separation.
	 * 
	 * @returns a reference to the original Matrix4f object.
	 * 
	 * 	- The matrix's data array `data` has 16 elements, representing the 4x4 orthographic
	 * projection matrix.
	 * 	- Each element in the array is a float value between 0 and 1, representing the
	 * matrix coefficients.
	 * 	- The matrix preserves the identity transformation for points within the near and
	 * far clipping planes.
	 * 	- The near and far clipping planes are defined by the `near` and `far` parameters
	 * passed to the function, respectively.
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
	 * Computes a rotation matrix based on three input vectors: `forward`, `up`, and `r`.
	 * The rotation is computed by cross-producting `forward` and `up` to obtain the
	 * "forward" vector, then cross-producting `f` and `r` to obtain the "right" vector,
	 * finally returning the resulting rotation matrix.
	 * 
	 * @param forward 3D direction of the rotation axis.
	 * 
	 * @param up 2D cross product of two vectors and is used to compute the rotation axis.
	 * 
	 * @returns a Matrix4f object representing a rotation matrix based on the given forward
	 * and up vectors.
	 */
	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();

		Vector3f r = up.normalized();
		r = r.cross(f);

		Vector3f u = f.cross(r);

		return initRotation(f, u, r);
	}

	/**
	 * Initializes a rotation matrix based on three vectors: `forward`, `right`, and `up`.
	 * It sets the corresponding elements of the matrix to the dot products of the vectors.
	 * 
	 * @param forward 3D direction of rotation relative to the origin, which is used to
	 * set the corresponding elements of the matrix's data array.
	 * 
	 * 	- It is a vector with three components (x, y, and z) representing a direction in
	 * 3D space.
	 * 
	 * @param up 3D direction of upward motion in the rotation, which is used to initialize
	 * the Y-axis of the rotation matrix.
	 * 
	 * 	- It is a Vector3f object representing the upward direction.
	 * 	- Its components are typically non-zero and may vary depending on the context.
	 * 
	 * @param right 3D right vector of the rotation axis, which is used to initialize the
	 * components of the rotation matrix.
	 * 
	 * 	- `right` is a vector representing the right-hand coordinate system axis.
	 * 	- It has three components: `x`, `y`, and `z`.
	 * 
	 * @returns a Matrix4f object representing the rotation matrix.
	 * 
	 * The `Matrix4f` object returned by the function is an instance of the Matrix4f class
	 * in Java, representing a 4x4 homogeneous transformation matrix.
	 * The elements of the matrix are determined by the input vectors `forward`, `up`,
	 * and `right`, which represent the orientation of the rotation axis.
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
	 * Takes a `Vector3f` object `r` and returns a new `Vector3f` object with the components
	 * multiplied by the corresponding values from an array `data`.
	 * 
	 * @param r 3D vector to be transformed, which is multiplied element-wise with the
	 * elements of the `data` array to produce the transformed vector.
	 * 
	 * @returns a new Vector3f object containing the result of multiplying each element
	 * of the input Vector3f by the corresponding element of a given array, followed by
	 * addition of the scalars.
	 */
	public Vector3f transform(Vector3f r) {
		return new Vector3f(data[0][0] * r.getX() + data[0][1] * r.getY() + data[0][2] * r.getZ() + data[0][3], data[1][0] * r.getX() + data[1][1] * r.getY() + data[1][2]
				* r.getZ() + data[1][3], data[2][0] * r.getX() + data[2][1] * r.getY() + data[2][2] * r.getZ() + data[2][3]);
	}

	/**
	 * Multiplies a matrix by another matrix, element-wise multiplying the corresponding
	 * elements of each matrix and storing the result in a new matrix.
	 * 
	 * @param r 4x4 matrix that will be multiplied with the current matrix, resulting in
	 * a new 4x4 matrix that represents the product of the two matrices.
	 * 
	 * 	- It is an instance of the `Matrix4f` class, representing a 4x4 matrix.
	 * 
	 * @returns a new Matrix4f object containing the result of multiplying the input
	 * matrix `r` with the current matrix.
	 * 
	 * The `res` variable is initialized as a new `Matrix4f` object to hold the result
	 * of the multiplication.
	 * 
	 * The multiplication operation is performed element-wise between the elements of the
	 * input matrices `data` and `r`. The resulting elements in `res` represent the product
	 * of the corresponding elements in `data` and `r`.
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
	 * Generates a 4x4 array of floats, using the values from an array `data`. The returned
	 * array is used to store and manipulate data.
	 * 
	 * @returns an array of 4x4 floating-point values.
	 */
	public float[][] getData() {
		float[][] res = new float[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res[i][j] = data[i][j];

		return res;
	}
	
	/**
	 * Returns an array of floats containing the values of a linear data set at multiple
	 * points.
	 * 
	 * @returns an array of 12 floating-point values.
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
	 * Retrieves a value from a two-dimensional array `data`. The value is located at
	 * position `(x, y)` and is returned as a float.
	 * 
	 * @param x 2D coordinates of a point in the data array.
	 * 
	 * @param y 2nd dimension of the data array being accessed by the function.
	 * 
	 * @returns a floating-point value representing the data at the specified coordinates
	 * within a two-dimensional array.
	 */
	public float get(int x, int y) {
		return data[x][y];
	}

	/**
	 * Sets the value of the member `data` to a provided array of floats.
	 * 
	 * @param data 2D array of float values to be stored as the instance variable `data`.
	 */
	public void SetM(float[][] data) {
		this.data = data;
	}

	/**
	 * Sets a value for a specific position in a two-dimensional array `data`.
	 * 
	 * @param x 0-based index of the row in the 2D array `data`.
	 * 
	 * @param y 2D array index at which the value `value` is assigned to the corresponding
	 * element.
	 * 
	 * @param value 4-byte floating-point number that is assigned to the corresponding
	 * element in the 2D array `data`.
	 */
	public void set(int x, int y, float value) {
		data[x][y] = value;
	}

	/**
	 * Transforms an array of floats `data` into a new array with the same dimensions,
	 * where each element is the transpose of its original position.
	 */
	public void transposeSelf() {
		float[][] tr = new float[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				tr[i][j] = data[j][i];
		this.data = tr;
	}
	
}
