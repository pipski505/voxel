package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is designed to control the camera view in a 3D environment. It calculates projection
 * and view matrices based on input parameters and adjusts them according to the
 * viewport size. The class also allows for camera movement using keyboard controls
 * and mouse rotation.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates a projection matrix from a camera's settings and returns it as a
	 * `Matrix4f` object. The matrix is retrieved directly from the provided `CameraStruct`
	 * data without any transformations or calculations.
	 *
	 * @param data 4x4 projection matrix obtained from the `getAsMatrix4()` method of the
	 * `CameraStruct` class, which is then returned as the result of the `calculateProjectionMatrix`
	 * method.
	 *
	 * @returns a `Matrix4f` object.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts camera settings to match the viewport dimensions, calculates projection
	 * and view matrices, and updates the viewport area.
	 *
	 * @param width 2D viewport's width and is used to set the aspect ratio of the camera.
	 *
	 * @param height 2D viewport's height used to set the aspect ratio of the camera and
	 * calculate the projection matrix.
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
	 * Encapsulates a set of parameters required for perspective projection in 3D graphics.
	 * It is initialized with values such as field of view, aspect ratio, near plane, and
	 * far plane. The class provides a method to generate a perspective projection matrix
	 * based on these parameters.
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
		 * Creates a new instance of `Matrix4f` and initializes it with a perspective projection
		 * matrix using specified fields of view, aspect ratio, near clipping plane distance,
		 * and far clipping plane distance. The resulting matrix represents a perspective transformation.
		 *
		 * @returns a perspective matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes user input to control a 3D object's movement and rotation. It responds
	 * to keyboard keys (W, A, S, D) for translation and mouse movements for rotation,
	 * with optional speed adjustment via the LShift key.
	 *
	 * @param dt delta time, which is used to scale the movement amount based on the frame
	 * rate, allowing for smoother and consistent character movement.
	 *
	 * @param speed 3D movement speed of the object, which is multiplied by the delta
	 * time (`dt`) to determine the actual movement amount and potentially increased when
	 * the left shift key is pressed.
	 *
	 * @param sens sensitivity of camera rotation and movement to mouse and keyboard inputs.
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
	 * Moves an object by a specified amount along a given direction vector. It calculates
	 * the new position by adding the product of the direction vector and the amount to
	 * the current position, then updates the object's position accordingly.
	 *
	 * @param dir 3D direction vector that is scaled by the specified amount (`amt`) and
	 * added to the current position of an object's transform.
	 *
	 * @param amt magnitude of the movement direction specified by the `dir` vector,
	 * controlling how far to translate the object's position.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
