package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Handles camera movement and projection in a 3D environment.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Overloads the projection matrix of a camera with the calculated projection matrix
	 * from the provided camera data. It returns the overloaded projection matrix.
	 *
	 * @param data Camera data that is converted into a matrix.
	 *
	 * @returns a Matrix4f object representing the projection matrix of the given CameraStruct.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Sets the camera's aspect ratio to match the viewport dimensions, recalculates the
	 * projection matrix and view matrix, and updates the OpenGL viewport.
	 *
	 * @param width width of the viewport, used to calculate the aspect ratio and update
	 * the OpenGL viewport.
	 *
	 * @param height height of the viewport, used to calculate the aspect ratio and update
	 * the projection matrix.
	 */
	@Override
	public void adjustToViewport(int width, int height) {
		((CameraStruct3D) this.values).aspect = (float) width / height;
		calculateProjectionMatrix(values);
		try {
			calculateViewMatrix();
		} catch (NullPointerException e) {
		}
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Represents a 3D camera's projection matrix data, containing field of view, aspect
	 * ratio, near and far clipping planes.
	 */
	protected class CameraStruct3D extends CameraStruct {

		public float fov, aspect, zNear, zFar;

		public CameraStruct3D(float fov, float aspect, float zNear, float zFar) {
			this.fov = fov;
			this.aspect = aspect;
			this.zNear = zNear;
			this.zFar = zFar;
		}

		/**
		 * Returns a new perspective projection matrix.
		 * The matrix is initialized with the specified field of view (fov), aspect ratio
		 * (aspect), near clipping plane (zNear), and far clipping plane (zFar).
		 *
		 * @returns a 4x4 perspective matrix initialized with specified FOV, aspect, near,
		 * and far plane values.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Rotates the object based on mouse movement, adjusts speed with the left shift key,
	 * and moves the object forward, backward, left, or right based on keyboard input.
	 *
	 * @param dt time elapsed since the last frame update, used to calculate the movement
	 * amount.
	 *
	 * @param speed amount of movement in a given direction, scaled by other factors such
	 * as keyboard input and shift modifier.
	 *
	 * @param sens sensitivity of mouse rotation, affecting how far the view rotates per
	 * mouse movement.
	 */
	public void processInput(float dt, float speed, float sens) {

		float dx = Mouse.getDX();
		float dy = Mouse.getDY();
		float roty = (float)Math.toRadians(dx * sens);
		getTransform().rotate(new Vector3f(0, 1, 0), (float) roty);
		getTransform().rotate(getTransform().getRot().getRight(), (float) -Math.toRadians(dy * sens));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			speed *= 10;
		
		float movAmt = speed * dt;

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			move(getTransform().getRot().getForward(), movAmt);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			move(getTransform().getRot().getForward(), -movAmt);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			move(getTransform().getRot().getLeft(), movAmt);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			move(getTransform().getRot().getRight(), movAmt);
		
	}

	/**
	 * Updates the position of an object by adding a specified amount to its current
	 * position in the direction of a given vector. The amount is calculated by multiplying
	 * the vector by a scalar value. The new position is set on the object's transform.
	 *
	 * @param dir direction in which movement is applied, a vector that is scaled by the
	 * `amt` parameter to determine the movement amount.
	 *
	 * @param amt amount by which the movement direction is scaled.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
