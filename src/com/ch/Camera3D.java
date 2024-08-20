package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents a camera object in a 3D environment, handling projection and view matrix
 * calculations, as well as adjusting to the viewport. It also processes user input,
 * including mouse and keyboard events, to control the camera's rotation and movement.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Returns a 4x4 projection matrix based on input from a `CameraStruct`. The input
	 * is directly assigned to the `projection` variable and returned as the output.
	 * 
	 * @param data 4x4 matrix that is used to create the projection matrix, which is then
	 * returned by the function.
	 * 
	 * @returns a `Matrix4f` object representing the projection matrix.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Sets the aspect ratio of a camera and calculates its projection matrix and view
	 * matrix based on the provided viewport dimensions. It also sets up the OpenGL
	 * viewport to match the specified width and height, handling potential null pointer
	 * exceptions.
	 * 
	 * @param width 2D viewport width that determines the aspect ratio for the camera's
	 * projection matrix.
	 * 
	 * @param height height of the viewport and is used to calculate the aspect ratio and
	 * update the glViewport function.
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
	 * Is an extension of the CameraStruct class that represents a camera configuration
	 * for a 3D perspective projection. It encapsulates parameters necessary to initialize
	 * a projection matrix.
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
		 * Initializes a perspective matrix with the given field-of-view (fov), aspect ratio
		 * (aspect), and near/far clipping planes (zNear, zFar) before returning it as a
		 * Matrix4f object. This matrix is used for transformations in computer graphics or
		 * other applications.
		 * 
		 * @returns a perspective projection matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Updates the object's rotation and movement based on user input from mouse and
	 * keyboard. It adjusts the rotation according to mouse movements, and moves the
	 * object forward, backward, left, or right based on keyboard key presses, with
	 * optional speed boost for shift key.
	 * 
	 * @param dt 3D time delta used to calculate the movement amount based on the speed.
	 * 
	 * @param speed 3D movement speed of an object, which is multiplied by the delta time
	 * (`dt`) to calculate the actual movement amount and is adjusted based on the shift
	 * key being pressed.
	 * 
	 * @param sens sensitivity of mouse rotation, affecting the amount of rotation around
	 * the Y-axis and X-axis based on the mouse movement.
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
	 * Modifies the position of an object by adding a specified vector to its current
	 * position, scaled by a given amount. The new position is calculated by multiplying
	 * the direction vector with the amplitude and then adding it to the original position.
	 * 
	 * @param dir 3D direction vector that is scaled by the `amt` value and added to the
	 * current position of an object, resulting in its movement.
	 * 
	 * @param amt scalar amount by which the direction vector `dir` is multiplied to
	 * determine the displacement of the position.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
