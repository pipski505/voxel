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
	 * Calculates a projection matrix and assigns it to a local variable `projection`,
	 * which is then immediately returned. The function takes a `CameraStruct` object as
	 * input and extracts its projection matrix using the `getAsMatrix4` method.
	 *
	 * @param data camera data that is used to retrieve a 4x4 matrix.
	 *
	 * @returns a matrix of type `Matrix4f` representing the projection matrix of the camera.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Updates the camera's aspect ratio and projection matrix based on the provided
	 * viewport width and height, and sets the viewport using OpenGL. It also attempts
	 * to calculate the view matrix, ignoring any null pointer exceptions.
	 *
	 * @param width width of the viewport and is used to set the aspect ratio of the
	 * camera and the viewport.
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
	 * Represents a 3D camera configuration,
	 * extending the base CameraStruct class,
	 * calculating projection matrices based on its properties.
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
		 * It initializes a Matrix4f object with a specified field of view, aspect ratio, and
		 * near and far clipping planes.
		 *
		 * @returns a 4x4 perspective matrix initialized with the specified parameters.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes user input to control a 3D object's movement and rotation. It updates
	 * the object's rotation based on mouse movement and accelerates its movement when
	 * the left shift key is pressed. The object's movement is controlled using the W,
	 * A, S, and D keys.
	 *
	 * @param dt time delta, used to calculate the movement amount based on the game speed
	 * and time elapsed since the last frame.
	 *
	 * @param speed movement speed of the object, which is multiplied by the delta time
	 * `dt` to determine the actual movement amount.
	 *
	 * @param sens sensitivity of the mouse controls, affecting the rotation speed.
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
	 * Translates an object along a specified direction by a given amount, updating its
	 * position accordingly. The direction and amount are multiplied to determine the
	 * translation vector, which is then added to the object's current position. The
	 * result is a new position for the object.
	 *
	 * @param dir direction in which the object is moved, as a vector.
	 *
	 * @param amt amount by which the direction vector is multiplied to determine the
	 * distance moved.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
