package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Provides an abstraction for cameras in a graphics system. It manages projection
 * and view matrices through its `calculateViewMatrix` method, which incorporates the
 * camera's rotation and translation from its transform. The class also allows for
 * custom calculation of projection matrices and adjustment to viewport sizes.
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
	 * Returns a matrix representing the product of the current view and projection
	 * transformations, unless the transformation has changed or no cached result exists,
	 * in which case it recalculates the view matrix using the `calculateViewMatrix` method.
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
	 * Calculates a view matrix by multiplying a rotation matrix and a translation matrix,
	 * both derived from camera properties. The result is then multiplied with a pre-existing
	 * projection matrix to produce a combined view-projection matrix.
	 *
	 * @returns a transformed matrix combining camera rotation and translation.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Creates a translation matrix from a given camera position. It multiplies the
	 * original camera position by -1 and then initializes a new Matrix4f object with the
	 * resulting x, y, z values to form the translation matrix.
	 *
	 * @returns a matrix representing the translation of a position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns a reference to an object of type `Transform`. This allows access to the
	 * internal state of the `transform` object. The returned value is not modified by
	 * the function, it simply provides a read-only view of the object's properties.
	 *
	 * @returns an instance of a class extending `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that defines a base structure for camera-related data. It
	 * contains a single abstract method getAsMatrix4() that returns a Matrix4f object.
	 * This class serves as a blueprint for implementing specific camera settings or configurations.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
