package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstract camera with various properties and behaviors related to
 * transformation, projection, and viewport adjustment. It maintains a reference to
 * its transformation state and provides methods for calculating the view-projection
 * matrix based on this transformation. The class also defines an interface for custom
 * camera-specific settings.
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
	 * Calculates a combined view and projection matrix if it is null or if the transformation
	 * has changed. It returns the resulting view-projection matrix. The calculation
	 * involves retrieving the view matrix from the transformation object, multiplying
	 * it with the projection matrix, and storing the result.
	 *
	 * @returns a Matrix4f object.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix by multiplying three matrices: the camera's rotation
	 * matrix, its translation matrix, and the projection matrix. The result is stored
	 * in the `viewProjectionMat4`.
	 *
	 * @returns a matrix that combines camera rotation and translation.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Returns a translation matrix that represents the position of the camera in world
	 * coordinates. The camera position is calculated by multiplying the current transformed
	 * position with -1. The result is used to initialize a new Matrix4f object for
	 * translation purposes.
	 *
	 * @returns a 4x4 matrix representing the translation.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns an instance of `Transform`. It provides read-only access to the internal
	 * `transform` object, allowing other parts of the program to retrieve its value
	 * without modifying it directly.
	 *
	 * @returns a reference to an instance of the `Transform` class named `transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base for camera-specific data structures.
	 * It contains an abstract method getAsMatrix4(), which returns a Matrix4f object
	 * representing the camera's structural information in a matrix form. This class
	 * provides a framework for custom camera configurations and settings.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
