package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class that handles camera-related tasks in a 3D graphics context.
 * It maintains a projection matrix and calculates view matrices based on transformations
 * and position of the camera. The class also provides methods to calculate view
 * projections, translate camera positions, and adjust the camera for viewport changes.
 */
public abstract class Camera {

	protected Matrix4f projection;
	protected Matrix4f viewProjectionMat4;
	protected CameraStruct values;
	protected Transform transform;

	protected Camera(Matrix4f projection) {
		this.projection = projection;
		transform = new Transform();
	}

	/**
	 * Calculates and returns a matrix representing the combined view and projection
	 * transformations when necessary, based on whether the `viewProjectionMat4` is null
	 * or if the transformation has changed. The calculation of the view matrix is performed
	 * only when required.
	 *
	 * @returns a Matrix4f object, either cached or recalculated.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by multiplying three matrices: a rotation matrix representing
	 * camera rotation, a translation matrix representing camera position, and a projection
	 * matrix. The result is then stored in the `viewProjectionMat4`.
	 *
	 * @returns a combined view-projection matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Computes a translation matrix from a given camera position. It multiplies the
	 * transformed position by -1, then initializes a new Matrix4f object with the
	 * translated x, y, and z coordinates.
	 *
	 * @returns a 4x4 matrix for translation based on the camera position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns a reference to a `transform` object. This allows other parts of the program
	 * to access and utilize the transformation data stored in the `transform` object.
	 * The function does not modify or manipulate the `transform` object in any way.
	 *
	 * @returns a reference to an object of type `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Defines an abstract class with one method. This abstract class is intended to serve
	 * as a base for other classes that represent camera data structures. It provides a
	 * way to get the data as a Matrix4f object.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
