package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Handles camera movement and projection matrix calculation based on user input and
 * viewport size.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates and returns a 4x4 projection matrix based on the provided `CameraStruct`
	 * data. It assigns the calculated projection matrix to a local variable and returns
	 * the original matrix from the `CameraStruct` data. The projection matrix is stored
	 * in a variable named `projection`.
	 *
	 * @param data camera settings, which are converted to a projection matrix using the
	 * `getAsMatrix4()` method.
	 *
	 * @returns a `Matrix4f` object representing the camera's projection matrix.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts the camera's aspect ratio and projection matrix based on the provided
	 * viewport dimensions, and updates the view matrix, while handling potential
	 * `NullPointerException` exceptions. The function also sets the OpenGL viewport to
	 * the specified dimensions.
	 *
	 * @param width new viewport width, which is used to set the camera's aspect ratio
	 * and update the viewport.
	 *
	 * @param height height of the viewport, used to calculate the aspect ratio for the
	 * camera projection.
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
	 * Represents a 3D camera projection configuration.
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
		 *
		 * @returns a 4x4 matrix representing a perspective projection transformation.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Rotates the object based on mouse movement and updates its position in 3D space
	 * based on keyboard input. It also scales the movement speed by 10 when the left
	 * shift key is pressed. The movement is relative to the object's orientation.
	 *
	 * @param dt delta time, or the change in time since the last frame, used to calculate
	 * the movement amount.
	 *
	 * @param speed movement speed of the object, which is used to calculate the movement
	 * amount.
	 *
	 * @param sens sensitivity of the mouse controls, affecting the rotation speed of the
	 * object.
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
	 * position in a given direction. The direction and amount are multiplied together
	 * to calculate the new position. The new position is then set as the object's position.
	 *
	 * @param dir direction in which movement occurs.
	 *
	 * @param amt amount of movement, scaling the direction vector to determine the
	 * distance to move.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
