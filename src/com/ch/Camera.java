package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class with various methods and fields used for camera manipulation.
 * The class has a Matrix4f representation of the camera's projection and view
 * projection matrices, as well as a Transform structure for storing camera position
 * and rotation information. Additionally, there are methods for calculating the view
 * matrix and translation matrix, and an abstract method for calculating the projection
 * matrix based on a CameraStruct data object.
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
	 * Computes and returns a matrix representing the view projection transformation,
	 * based on the current view matrix and any changes to the transform property.
	 * 
	 * @returns a `Matrix4f` object representing the view projection matrix.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Computes the view matrix for a given camera transformation, including rotation and
	 * translation.
	 * 
	 * @returns a 4x4 matrix representing the view transformation of a camera.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Generates a 4x4 matrix representing a translation from the origin, based on a
	 * vector of coordinates representing the displacement.
	 * 
	 * @returns a 4x4 matrix representing the translation of the camera position from its
	 * previous location.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves the value of the `transform` instance variable and returns it.
	 * 
	 * @returns a reference to an instance of the `Transform` class.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that acts as a base for other classes in the "Camera" package.
	 * It contains an abstract method called getAsMatrix4() and does not have any fields
	 * or methods of its own. The purpose of this class is to provide a common interface
	 * for subclasses of Camera to implement.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
