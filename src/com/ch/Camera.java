package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class that provides various methods for manipulating and querying
 * camera data. It includes fields for projection, viewProjectionMat4, values,
 * transform, and calculation methods for calculateViewMatrix(), getTranslationMatrix(),
 * and adjustToViewport(). Additionally, it has a sub-class called CameraStruct with
 * its own methods for calculating projection matrices and adjusting to viewports.
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
	 * Retrieves a `Matrix4f` object representing the view projection matrix, which is
	 * used to transform 3D space coordinates into screen coordinates for rendering. If
	 * the matrix has changed or was not previously set, it calculates and caches the
	 * view matrix.
	 * 
	 * @returns a `Matrix4f` object containing the view projection matrix.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Calculates the view matrix by multiplying the camera rotation and translation
	 * matrices, and then applying the projection matrix to the resulting matrix.
	 * 
	 * @returns a 4x4 transformation matrix representing the view and projection of a 3D
	 * camera.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Generates a matrix that represents the translation of a virtual camera in 3D space,
	 * based on its current position and the negative sign used to reflect the direction
	 * of movement.
	 * 
	 * @returns a 4x4 transformation matrix representing the translation of the camera position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves and returns a `Transform` object, which represents a mapping from input
	 * values to output values.
	 * 
	 * @returns a reference to the `transform` object.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base for other classes in the Camera package.
	 * It has an abstract method called `getAsMatrix4()` that returns a Matrix4f object,
	 * but its implementation details are not shown in the provided code snippet. This
	 * class likely provides some common functionality or properties to its subclasses,
	 * such as matrix multiplication or transformation.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
