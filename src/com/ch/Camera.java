package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract base class representing various camera implementations in a 3D
 * graphics system. It encapsulates camera-related data and provides functionality
 * for calculating view matrices and adjusting the camera to fit within a viewport.
 * Abstract methods allow subclasses to implement specific projection matrix calculations
 * and viewport adjustments.
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
	 * Checks if a pre-calculated view projection matrix is available and up-to-date,
	 * updating it if necessary by recalculating the view matrix. If updated, the new
	 * view projection matrix is stored for later retrieval. The current or updated view
	 * projection matrix is then returned.
	 *
	 * @returns a `Matrix4f` representing combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Computes a view matrix by multiplying three matrices: a translation matrix and two
	 * rotation matrices, and stores the result in the `viewProjectionMat4`. The function
	 * combines camera rotation, position, and projection to generate the view matrix.
	 *
	 * @returns a matrix representing the combined view and projection transformation.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Returns a translation matrix based on the negative position of the transformed
	 * object. The translated position is derived by multiplying the transformed position
	 * by -1 and extracting its x, y, z coordinates. A new Matrix4f instance is initialized
	 * with these coordinates as the translation.
	 *
	 * @returns a 4x4 translation matrix representing camera position.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns an instance of a Transform object. The transform object is stored in the
	 * variable named 'transform'. This allows external access to the object's properties
	 * and methods for further manipulation or use within the application.
	 *
	 * @returns a Transform object instance variable named `transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that represents camera-related data and provides a method for
	 * converting it into a matrix form. It has an abstract method to get the data as a
	 * Matrix4f object. The class structure allows for extensions and customizations of
	 * camera properties.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
