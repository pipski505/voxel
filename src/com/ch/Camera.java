package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents an abstraction of a camera with features for projection and view
 * transformation. It maintains matrices for the camera's view and projection, as
 * well as its translation and rotation. The class also provides methods for adjusting
 * the camera's viewport and calculating its matrix transformations.
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
	 * Calculates and returns a view-projection matrix, which transforms world coordinates
	 * to screen coordinates. It does so by checking if the view projection matrix is
	 * null or if the transformation has changed, and if so, recalculates the view matrix
	 * before returning it.
	 *
	 * @returns a Matrix4f object representing combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix for a camera by multiplying three matrices: the conjugate
	 * of the camera's rotational transformation, its translation transformation, and the
	 * projection matrix. The result is then assigned to the `viewProjectionMat4`.
	 *
	 * @returns a combined view-projection matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Initializes a translation matrix based on the negated position of a camera. The
	 * negation is achieved by multiplying the transformed position vector by -1. The
	 * resulting translation matrix is then returned.
	 *
	 * @returns a matrix representing the translation from the origin to a negative camera
	 * position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves and returns an instance of `Transform`. It provides access to the current
	 * transformation state without modifying it. The returned object can be used for
	 * further processing or manipulation.
	 *
	 * @returns a reference to the internal `transform` object.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that represents some sort of camera data structure. It provides
	 * a way to retrieve the data as a Matrix4f object through its getAsMatrix4 method.
	 * The class itself does not contain any concrete implementation, but rather serves
	 * as a template for subclasses to implement their own specific behavior.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
