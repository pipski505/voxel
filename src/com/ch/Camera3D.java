package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Defines a camera system for 3D rendering with perspective projection and adjustable
 * field of view, aspect ratio, near, and far clipping planes. It also handles input
 * from the user to rotate and move the camera.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Overrides a method and takes a `CameraStruct` object as input, returning a `Matrix4f`
	 * object calculated from the input data. The function assigns the result to a local
	 * variable named `projection`.
	 *
	 * @param data 4x4 matrix of the camera from which the projection matrix is obtained
	 * and returned.
	 *
	 * @returns a matrix of type `Matrix4f`.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts camera settings and recalculates matrices based on provided viewport
	 * dimensions. It updates aspect ratio, calculates projection matrix, and attempts
	 * to calculate view matrix. If a null pointer exception occurs during view matrix
	 * calculation, it is caught and ignored.
	 *
	 * @param width 2D viewport width to be adjusted and is used to calculate the aspect
	 * ratio for the 3D camera's projection matrix.
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
	 * Is a custom struct for camera settings. It encapsulates and manages various camera
	 * parameters such as field of view, aspect ratio, near and far clipping planes.
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
		 * Creates a new perspective matrix based on the provided field of view (fov), aspect
		 * ratio, and near/far clipping planes (zNear and zFar). The matrix is then returned
		 * as an instance of `Matrix4f`.
		 *
		 * @returns a perspective matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Calculates rotational and translational movements based on mouse and keyboard
	 * input. It applies camera rotation and movement according to user input, adjusting
	 * speed with shift key pressed.
	 *
	 * @param dt time interval since the last frame, used to calculate the movement amount
	 * of the object based on the speed and keyboard input.
	 *
	 * @param speed 3D movement speed of an object, which is multiplied by the delta time
	 * (`dt`) and modified by the shift key to control the magnitude of movement.
	 *
	 * @param sens sensitivity factor used to adjust the rotation of the object based on
	 * the mouse movement.
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
	 * Adds a vector to an object's position by multiplying it with a specified amount
	 * and then sets the new position. The resulting position is obtained from the object's
	 * transform component, which provides its current position, scale, and rotation.
	 *
	 * @param dir 3D direction vector used to calculate the new position by multiplying
	 * it with the specified amount (`amt`) and adding the result to the current position.
	 *
	 * @param amt magnitude of the vector multiplication operation applied to the direction
	 * `dir`, determining the distance of movement.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
