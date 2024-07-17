package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is a subclass of the Camera class with additional fields and methods to manipulate
 * the camera's viewport and orientation based on user input. The class has a constructor
 * that takes four parameters: field of view (fov), aspect ratio, near and far
 * distances, which are used to initialize the camera's projection matrix. The
 * calculateProjectionMatrix method is override to return the camera's projection
 * matrix, while the adjustToViewport method calculates the viewport dimensions and
 * sets the camera's position and orientation based on user input. The processInput
 * method processes user input events such as mouse movements and keyboard keys,
 * rotating the camera and moving it in the corresponding direction.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates and returns a Matrix4f object representing the camera's projection
	 * matrix based on the given `CameraStruct` data.
	 * 
	 * @param data 3D camera parameters and is used to construct the projection matrix.
	 * 
	 * @returns a Matrix4f object containing the projection matrix.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts the camera's projection and view matrices to fit within the bounds of a
	 * parent widget's viewport. It sets the aspect ratio of the camera's projection
	 * matrix based on the width and height of the viewport, calculates the view matrix,
	 * and finally sets the viewport size using `GL11.glViewport()`.
	 * 
	 * @param width 2D viewport width in pixels.
	 * 
	 * @param height 2D viewport dimensions, which are used to calculate the projection
	 * and view matrices and set the viewport size in the GL context.
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
	 * Is an extension of the Camera Struct that includes additional parameters for
	 * controlling the perspective projection of a 3D camera. It provides a method to get
	 * a Matrix4f object representing the perspective projection and offers a straightforward
	 * way to calculate the perspective matrix.
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
		 * Initializes a matrix representing a perspective projection, with fields for field
		 * of view, aspect ratio, near and far distances.
		 * 
		 * @returns a 4x4 matrix representing a perspective projection.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes mouse and keyboard input to move an object in a 3D space, scaling its
	 * speed based on the pressed keys and rotating it based on the cursor position and
	 * shift key presses.
	 * 
	 * @param dt time step for which the function is being called, and it is used to
	 * calculate the movement of the entity based on its speed and sensitivity.
	 * 
	 * @param speed 3D movement speed of the object being controlled, and it is multiplied
	 * by the time elapsed (represented by the `dt` parameter) to determine the total
	 * distance traveled during each iteration of the function.
	 * 
	 * @param sens sensitivity of the character's movement in response to user input, and
	 * it is used to calculate the rotation angle of the character based on the user's
	 * mouse movements.
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
	 * Moves a transform's position by a specified distance along a given direction, using
	 * the `setPos()` method.
	 * 
	 * @param dir 3D direction in which the object should move, with its magnitude being
	 * multiplied by the specified amount (`amt`).
	 * 
	 * @param amt amount of movement to be applied to the object's position along the
	 * specified direction.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
