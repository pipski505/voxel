package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract base class that represents a camera in a 3D space. It encapsulates
 * essential matrices for rendering and provides mechanisms for calculating view and
 * projection transformations. The class is designed to be extended by concrete camera
 * implementations.
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
	 * matrices. It checks if either the view projection matrix or the transformation has
	 * changed, and if so, calculates a new view matrix before returning the combined result.
	 *
	 * @returns a matrix representing the combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates a view matrix for a given transformation by combining the rotation and
	 * translation components into a single matrix, then multiplying it with a projection
	 * matrix to produce a final result. The output is assigned to the `viewProjectionMat4`.
	 *
	 * @returns a transformed view matrix.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Calculates a translation matrix based on a camera position obtained by multiplying
	 * a given transformed position by -1. The resulting matrix is initialized with the
	 * translated coordinates (x, y, z) using a provided constructor.
	 *
	 * @returns a 4x4 translation matrix.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns a reference to the `transform` object. The function provides read-only
	 * access to the `transform` object, allowing other parts of the program to retrieve
	 * its value without modifying it directly. It does not perform any calculations or
	 * transformations on the input data.
	 *
	 * @returns an instance of class `Transform`, referred to as `transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that serves as a base for struct-like objects in the camera
	 * system. It provides a mechanism to create custom data structures with matrix
	 * transformation capabilities. The class requires subclasses to implement the
	 * getAsMatrix4 method, allowing for specific implementations of matrix-based data
	 * storage and manipulation.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
