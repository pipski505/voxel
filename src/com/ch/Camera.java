package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an abstract class that manages camera transformations and projection matrices
 * in 3D graphics rendering. It calculates view matrices based on camera rotations
 * and translations, and provides methods for adjusting the camera to a viewport and
 * calculating projection matrices from data.
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
	 * Returns a `Matrix4f` object representing the view projection matrix. It first
	 * checks if the view projection matrix is null or if the transformation has changed,
	 * and if so, it calculates a new view matrix before returning the result.
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
	 * Computes a view matrix by combining the camera's rotation and translation matrices,
	 * then multiplying it with a projection matrix to produce the final view-projection
	 * matrix. The result is stored in the `viewProjectionMat4` variable.
	 *
	 * @returns a matrix representing the view transformation of a scene.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Transforms a given position into a translation matrix for a camera. It multiplies
	 * the camera's current position by -1 to get the negative position, then initializes
	 * a new Matrix4f object with these coordinates.
	 *
	 * @returns a 4x4 transformation matrix for translation.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Retrieves and returns an object reference to a variable named `transform`, which
	 * is presumably an instance of the `Transform` class, representing a transformation
	 * or position in space.
	 *
	 * @returns a reference to the `transform` object.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Is an abstract class that defines a matrix-based data structure. It serves as a
	 * base class for other camera-related structures and provides a way to retrieve its
	 * internal state as a Matrix4f object. The getAsMatrix4() method allows subclasses
	 * to override this behavior.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
