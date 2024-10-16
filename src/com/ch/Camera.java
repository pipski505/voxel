package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Defines an abstract base class for camera objects, providing functionality for
 * view and projection matrix calculations, and camera transformation management. It
 * abstracts away concrete camera implementation details, allowing for various camera
 * types to be created.
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
	 * Returns the view projection matrix, recalculating it if the view projection matrix
	 * is null or the transform has changed.
	 *
	 * @returns a Matrix4f containing the combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by combining a camera's rotation and translation. It uses
	 * the camera's rotation to transform the translation matrix, then multiplies the
	 * result by a projection matrix to obtain the final view projection matrix.
	 *
	 * @returns a 4x4 matrix representing the combined view and projection transformations.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Calculates a translation matrix based on the camera position. It negates the
	 * transformed position of an object and uses the resulting vector to initialize a
	 * translation matrix. The function returns this translation matrix.
	 *
	 * @returns a 4x4 translation matrix representing the camera's position in 3D space.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the `transform` object.
	 *
	 * @returns an object of type `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Defines a base structure for camera-related data,
	 * with a requirement to convert it into a Matrix4f.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
