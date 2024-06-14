package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class with various methods for manipulating views and projections
 * of objects in a 3D space. It takes in a Matrix4f object as its constructor parameter,
 * which is used to calculate the view and projection matrices. The class also has
 * methods for calculating the view matrix, getting the translation matrix, and
 * adjusting to a specified viewport size. Additionally, there are abstract methods
 * for calculating the projection matrix and adjusting to a specific width and height
 * in pixels.
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
	 * Retrieves a Matrix4f object representing the view projection, which combines the
	 * camera's view matrix and the projection matrix. If the view projection matrix has
	 * changed, it will recalculate it.
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
	 * Computes the view matrix based on the camera's rotation and translation. The
	 * rotation is first conjugated and then multiplied with the translation matrix to
	 * produce the final view matrix.
	 * 
	 * @returns a 4x4 matrix representing the view transformation of a 3D camera.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Generates a 4x4 transformation matrix that translates a 3D space by a specified
	 * distance in the x, y, and z axes.
	 * 
	 * @returns a 4x4 matrix representing the translation of the camera position in 3D space.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the `transform` object, providing a way to access and manipulate its contents.
	 * 
	 * @returns a reference to an instance of the `Transform` class.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base for other classes in the Camera package.
	 * It contains an abstract method called "getAsMatrix4" which allows subclasses to
	 * provide their own implementation of a matrix4f structure.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
