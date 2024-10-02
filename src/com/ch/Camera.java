package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Defines an abstract camera class providing functionality for 3D camera transformations
 * and projections. It supports dynamic camera adjustments based on viewport dimensions
 * and rotation/translation transformations. The class is designed to be extended by
 * concrete camera implementations.
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
	 * Returns the combined view and projection matrix, recalculating the view matrix if
	 * necessary due to changes in the transform, and updates the `viewProjectionMat4`
	 * if it is null.
	 *
	 * @returns a Matrix4f object representing the combined view and projection matrices.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates the view matrix of a camera by multiplying the camera's rotation and
	 * translation matrices with a projection matrix. The result is stored in the
	 * `viewProjectionMat4` variable. The function returns the resulting view matrix.
	 *
	 * @returns a 4x4 matrix representing the combined view projection transformation.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Calculates a translation matrix based on the camera's position,
	 * then returns this matrix,
	 * resulting in a translation of the camera's position.
	 *
	 * @returns a 4x4 matrix representing the translation from the camera position to the
	 * origin.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the value of the `transform` object.
	 *
	 * @returns an instance of the `Transform` class.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Defines an abstract base class for camera-related data structures, providing a
	 * method to retrieve the data as a Matrix4f.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
