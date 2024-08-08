package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Implements a camera for a 3D scene, handling camera movement and projection
 * calculations based on user input. It also adjusts its view to fit within a given
 * viewport width and height.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Overrides the projection matrix calculation. It retrieves a predefined `CameraStruct`'s
	 * `Matrix4f` and assigns it to a local variable `projection`. The assigned value is
	 * then returned as the result.
	 *
	 * @param data 3D camera settings, which are used to obtain and return a matrix
	 * representing the projection transformation from 3D space to 2D image coordinates.
	 *
	 * @returns a Matrix4f object, specifically the projection matrix from the provided
	 * CameraStruct.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts a camera's aspect ratio and calculates its projection and view matrices
	 * based on the given viewport dimensions. It then sets the OpenGL viewport to the
	 * specified size. If an error occurs during view matrix calculation, it is silently
	 * ignored.
	 *
	 * @param width 2D viewport's width and is used to calculate the aspect ratio of the
	 * camera.
	 *
	 * @param height height of the viewport in pixels and is used to calculate the aspect
	 * ratio for projection matrix calculation and to set the viewport dimensions using
	 * GL11.glViewport().
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
	 * Represents a camera structure for 3D rendering, encapsulating essential projection
	 * matrix parameters. It initializes these parameters in its constructor and provides
	 * a method to retrieve the corresponding perspective transformation matrix. This
	 * class serves as an extension of the base CameraStruct class.
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
		 * Initializes a perspective matrix with specified parameters: field-of-view (fov),
		 * aspect ratio (aspect), near clipping plane distance (zNear), and far clipping plane
		 * distance (zFar). It returns a new instance of the Matrix4f class initialized with
		 * these values.
		 *
		 * @returns a perspective projection matrix.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes user input to control a 3D object's movement and rotation. It handles
	 * mouse and keyboard inputs, updating the object's rotation and translation based
	 * on the inputs' magnitude and direction.
	 *
	 * @param dt time elapsed since the last frame, used to calculate the movement amount
	 * based on the speed and sensitivity settings.
	 *
	 * @param speed 3D movement speed, which is scaled by a factor of 10 when the left
	 * shift key is pressed and used to calculate the amount of movement along the forward,
	 * left, or right direction.
	 *
	 * @param sens sensitivity of mouse rotation, used to calculate the rotation angle
	 * based on the dx value from Mouse.getDX().
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
	 * Modifies the position of a transform by adding a product of a direction vector and
	 * an amount to its current position. The result is set as the new position of the
	 * transform, effectively moving it along the specified direction by the given amount.
	 *
	 * @param dir 3D direction vector that is scaled by the `amt` value to calculate the
	 * movement offset for the object's position.
	 *
	 * @param amt scalar multiplier that is applied to the direction vector `dir`,
	 * effectively scaling its magnitude before adding it to the current position of the
	 * object.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
