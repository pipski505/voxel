package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstract camera with a projection and a transform, allowing for view
 * projection calculation and adjustment to a viewport.
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
	 * Returns a Matrix4f object representing the combined view and projection transforms.
	 * It recalculates the view matrix if the transform has changed or if the view
	 * projection matrix is null.
	 *
	 * @returns a 4x4 matrix representing the combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by combining camera rotation and translation with a
	 * projection matrix. The result is stored in the `viewProjectionMat4` variable.
	 *
	 * @returns a view-projection matrix, resulting from the multiplication of camera
	 * rotation, translation, and projection matrices.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Returns a 4x4 translation matrix based on the inverted position of a camera. The
	 * matrix is initialized with the negative x, y, and z coordinates of the camera's position.
	 *
	 * @returns a 4x4 translation matrix representing the camera's position in 3D space.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the value of the `transform` variable.
	 * It provides direct access to the transform object.
	 *
	 * @returns an object of type `Transform` which is stored in the `transform` variable.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Defines an abstract data structure that represents camera-specific settings and
	 * is used to calculate the projection matrix. It provides a method to get the data
	 * as a Matrix4f.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
