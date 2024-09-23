package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstract camera with various attributes and behaviors, including
 * transformation and projection matrices, and provides methods for calculating view
 * projections and adjusting to viewport sizes. It is designed to be extended by
 * concrete camera implementations. The class relies on a Transform object and a
 * Matrix4f representation of camera data.
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
	 * Returns a matrix representing the combined view and projection transformations.
	 * It recalculates these matrices as needed based on changes to the transform object,
	 * ensuring up-to-date results are returned when called. The calculated matrices are
	 * stored for subsequent use.
	 *
	 * @returns a 4x4 matrix representing combined view and projection transformation.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Computes a combined view-projection matrix by multiplying three matrices: a camera
	 * translation matrix, a camera rotation matrix, and a pre-existing projection matrix.
	 * The result is stored in a local variable `viewProjectionMat4`.
	 *
	 * @returns a view-projection matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Returns a translation matrix representing the negative position of the camera's
	 * transformed position. The camera's position is multiplied by -1 to invert its
	 * direction, and the resulting vector is used to initialize a translation matrix.
	 * The function returns this initialized matrix.
	 *
	 * @returns a translation matrix representing the camera's position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns an instance variable named `transform`. The returned object is assumed to
	 * be a `Transform` type and represents some form of transformation or conversion
	 * operation. It exposes its internal state through direct access, bypassing any encapsulation.
	 *
	 * @returns a `Transform` object instance.
	 * This object holds transformation data or references to it.
	 * Its implementation is encapsulated within the class containing this method.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract base class that provides a common structure for camera-related data.
	 * It defines a single abstract method getAsMatrix4() which returns a Matrix4f object
	 * representing the camera's state in matrix form. This allows subclasses to implement
	 * specific camera types with their own structure and logic.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
