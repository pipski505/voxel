package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract base class that encapsulates camera functionality in a game engine.
 * It manages projection and view matrices based on camera position and rotation. The
 * class can adjust to the viewport size and provides methods for calculating projection,
 * view, and translation matrices.
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
	 * Returns a matrix representing the combination of the camera's view and projection
	 * transformations. It recalculates the view matrix if necessary based on changes to
	 * the transform. The result is stored in the `viewProjectionMat4` variable for
	 * subsequent reuse.
	 *
	 * @returns a matrix representing the view and projection transformation.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by multiplying three matrices: a rotation matrix derived
	 * from the camera's transformed rotation, a translation matrix, and a pre-calculated
	 * projection matrix. The result is stored in the `viewProjectionMat4`.
	 *
	 * @returns a Matrix4f object representing the view matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Generates a translation matrix from a given camera position. It multiplies the
	 * transformed position by -1 and initializes a new Matrix4f with the resulting x,
	 * y, and z coordinates. The resulting matrix represents the translation of the camera
	 * position.
	 *
	 * @returns a 4x4 translation matrix.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns an instance of `transform`. It provides access to the transformed data
	 * without performing any transformation itself. The returned object is likely used
	 * by other parts of the program to access or manipulate the transformed data.
	 *
	 * @returns a reference to the existing `transform` object.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base type for other classes representing
	 * camera-related data structures. It provides a means to retrieve the camera data
	 * in matrix4 format through its getAsMatrix4() method.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
