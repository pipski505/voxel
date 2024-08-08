package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class that provides methods for calculating view and projection
 * matrices, as well as adjusting to the desired viewport size. It also has a
 * `calculateProjectionMatrix` method and an `adjustToViewport` method, both of which
 * are abstract. Additionally, there is a `CameraStruct` class that is a part of this
 * class and provides a way to store camera data.
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
	 * Computes and returns the view-projection matrix, which transforms a 3D point from
	 * world space to the camera's view frustum.
	 * 
	 * @returns a Matrix4f object representing the view projection matrix.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Computes a view matrix for a given camera transformation and translation by
	 * multiplying the rotation matrix of the camera's orientation with the translation
	 * matrix of its position, then applying the projection matrix to the result.
	 * 
	 * @returns a matrix representation of the view transformation, which combines the
	 * rotation and translation of the camera.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Generates a 4x4 translation matrix based on the negative of the position of the
	 * camera transform.
	 * 
	 * @returns a matrix representing the translation of the camera position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves the `transform` object, which is a central component in the application's
	 * data processing pipeline. The returned transform object represents the current
	 * state of the data transformation process.
	 * 
	 * @returns a `Transform` object containing the transformation information.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that extends a Matrix4f with additional functionality for
	 * converting it to another matrix form. It provides an abstract method "getAsMatrix4()"
	 * that allows subclasses to implement their own conversion logic. The class seems
	 * to be a base class for other camera-related classes in the code snippet provided.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
