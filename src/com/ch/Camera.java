package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Defines an abstract camera model with configurable projection and view matrices.
 * It maintains a transformation state and provides methods for calculating view and
 * translation matrices, as well as adjusting to the viewport. The class also includes
 * a nested abstract class for handling camera-specific data.
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
	 * Returns a matrix representing the combined view and projection transformation. It
	 * checks if the cached view-projection matrix is null or the object's transform has
	 * changed, and if so, calculates a new view matrix before returning the result.
	 *
	 * @returns a Matrix4f object representing the combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix, representing the position and orientation of a camera
	 * in 3D space. It does so by multiplying a rotation matrix based on the camera's
	 * rotational transformation with a translation matrix, then multiplying the result
	 * with a projection matrix.
	 *
	 * @returns a combined view-projection matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Transforms a given position into a translation matrix. It multiplies the original
	 * position by -1 and then initializes a new Matrix4f with the resulting x, y, z
	 * coordinates to form a translation matrix.
	 *
	 * @returns a 4x4 matrix representing translation.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the value of a variable named `transform`. It provides access to the current
	 * state of the `transform` object, allowing it to be used or manipulated by other
	 * parts of the program. This method does not modify the `transform` object in any way.
	 *
	 * @returns a reference to the existing `transform` object.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base for other classes to inherit and customize
	 * camera-related data structures. It defines an abstract method getAsMatrix4() to
	 * retrieve the camera's data in a specific matrix format. The purpose of this class
	 * is to provide a flexible interface for camera settings and configurations.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
