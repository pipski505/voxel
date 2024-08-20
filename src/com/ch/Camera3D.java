package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is responsible for handling camera operations in a 3D scene, including perspective
 * projection and viewport adjustment. It also allows for user input processing,
 * enabling movement and rotation of the camera based on keyboard and mouse inputs.
 * The class extends the base Camera class, inheriting its basic functionality while
 * adding custom features specific to 3D camera management.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates a projection matrix from a given camera structure. It retrieves the
	 * matrix from the input data and assigns it to the `projection` variable, returning
	 * the resulting matrix as an instance of `Matrix4f`.
	 *
	 * @param data 4x4 transformation matrix from the camera struct, which is used to
	 * calculate and return the projection matrix.
	 *
	 * @returns a `Matrix4f` object.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts a camera's aspect ratio and calculates projection and view matrices based
	 * on the provided viewport dimensions. It also handles any null pointer exceptions
	 * that may occur during the calculation. Finally, it sets the OpenGL viewport to the
	 * specified width and height.
	 *
	 * @param width 2D viewport's width, which is used to calculate the aspect ratio and
	 * set the glViewport for rendering.
	 *
	 * @param height height of the viewport and is used to calculate the aspect ratio for
	 * projection matrix calculation.
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
	 * Is a custom struct for a camera in 3D space. It encapsulates four fundamental
	 * values: field of view, aspect ratio, near plane distance, and far plane distance.
	 * These values are used to generate a perspective projection matrix through the
	 * getAsMatrix4 method.
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
		 * Initializes a perspective matrix with the specified field of view (fov), aspect
		 * ratio, near clipping plane distance (zNear), and far clipping plane distance (zFar).
		 *
		 * @returns a perspective transformation matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Rotates and translates a transform based on mouse input, keyboard controls, and
	 * speed. It updates rotation using dx, dy values, adjusts speed with LShift key
	 * press, and moves the object forward/backward/left/right based on W/S/A/D key presses.
	 *
	 * @param dt delta time, which is used to calculate the movement amount based on the
	 * game speed.
	 *
	 * @param speed 3D movement speed of an object, which is adjusted by a factor of 10
	 * when the LShift key is held down and used to calculate the amount of movement for
	 * each frame.
	 *
	 * @param sens sensitivity of mouse rotations, used to calculate the rotation angles
	 * around the Y-axis and X-axis based on the mouse movement.
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
	 * Modifies an object's position by adding a vector scaled by a given amount to its
	 * current position, using the provided direction and amplitude.
	 *
	 * @param dir 3D direction vector that specifies the direction of movement, which is
	 * multiplied by the specified amount (`amt`) to calculate the new position.
	 *
	 * @param amt amount by which to multiply the direction vector `dir`, effectively
	 * controlling the distance of movement.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
