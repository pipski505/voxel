package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Defines an abstract camera system with projection and view matrices, which are
 * updated based on the transformation of the camera. The class provides methods to
 * retrieve the view projection matrix, calculate the view matrix, and get the camera's
 * transform.
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
	 * Retrieves and returns a matrix representing the view projection transformation,
	 * which combines the camera's view and projection transformations into a single
	 * transformation. If the view projection matrix is not present or has changed, it
	 * recalculates the matrix based on the current camera transformation.
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
	 * Calculates a view matrix by multiplying a projection matrix with a rotation matrix
	 * and a translation matrix, both derived from camera transform data. The result is
	 * stored in the `viewProjectionMat4` variable.
	 *
	 * @returns a matrix representing the combined view and projection transformations.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Creates a translation matrix based on the camera position, which is obtained by
	 * multiplying the original position with -1. The resulting translation matrix is
	 * then initialized using the x, y, and z coordinates of the transformed position.
	 *
	 * @returns a translation matrix representing the negative of the camera position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns a value of type `Transform`. It retrieves and exposes the internal state
	 * of `transform`, allowing access to its properties or methods through an external
	 * interface. The returned `Transform` object can be used by other parts of the program.
	 *
	 * @returns a reference to the `transform` variable.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that defines a structure for camera-related data. It provides
	 * a mechanism for subclasses to extend and customize their own camera-specific data.
	 * The class also specifies the `getAsMatrix4` method, which is used to obtain the
	 * data as a 4x4 matrix.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
