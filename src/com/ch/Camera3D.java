package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is responsible for handling camera movement and projection in a 3D environment.
 * It extends the Camera class and provides methods to adjust the viewport, calculate
 * projection matrices, and process user input (mouse and keyboard).
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Returns a matrix representing a projection in 3D space. It retrieves a matrix from
	 * a provided `CameraStruct` object and assigns it to a local variable `projection`.
	 * The retrieved matrix is then returned as the result of the function call.
	 *
	 * @param data 3D camera's properties and provides the necessary information to create
	 * a projection matrix, which is then returned by the method.
	 *
	 * @returns a `Matrix4f` object.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts the camera's aspect ratio and calculates projection and view matrices based
	 * on the provided viewport dimensions. It sets the OpenGL viewport to the specified
	 * area and catches any null pointer exceptions during view matrix calculation.
	 *
	 * @param width 2D viewport width and is used to set the aspect ratio of the camera's
	 * projection matrix as well as specify the viewport dimensions for OpenGL rendering.
	 *
	 * @param height 2D viewport's height and is used to calculate the aspect ratio of
	 * the camera and subsequently update the projection matrix.
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
	 * Represents a data structure for storing parameters of a camera in 3D space. It
	 * encapsulates values such as field-of-view, aspect ratio, near and far clipping planes.
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
		 * Initializes a perspective projection matrix based on the provided field of view
		 * (fov), aspect ratio, near clip plane distance, and far clip plane distance. It
		 * returns the initialized Matrix4f object. The initialization is done using the
		 * `initPerspective` method of the Matrix4f class.
		 *
		 * @returns a perspective projection matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Updates camera rotation based on mouse movement and sensitivity, applies speed
	 * modifiers for shift key press, and moves the camera forward, backward, left, or
	 * right based on keyboard input. It uses trigonometry to rotate the camera around
	 * its local axes.
	 *
	 * @param dt 3D time step, used to calculate the movement amount based on the given
	 * speed and keyboard input.
	 *
	 * @param speed 3D movement speed, which is multiplied by the delta time (`dt`) to
	 * determine the actual movement amount and scaled up by a factor of 10 when the left
	 * shift key is pressed.
	 *
	 * @param sens sensitivity of mouse rotation, with higher values resulting in more
	 * rapid camera rotation and lower values resulting in slower rotation.
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
	 * Alters the position of an object by adding a specified amount to its current
	 * direction, scaled by a given amount. It updates the object's transformation to
	 * reflect the new position. The direction and amount are multiplied before being
	 * added to the current position.
	 *
	 * @param dir 3D direction vector that is multiplied by the specified amount `amt`
	 * and added to the object's current position.
	 *
	 * @param amt amount by which to multiply the direction vector, controlling the
	 * distance of the movement.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
