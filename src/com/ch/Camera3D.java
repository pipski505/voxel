package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an extension of the Camera class that includes additional functionality for
 * manipulating camera views in a 3D environment. It takes in parameters for field
 * of view (fov), aspect ratio, near and far distances, and provides methods for
 * calculating projection and view matrices, adjusting to the viewport size, and
 * processing user input (such as mouse and keyboard movements).
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Takes a `CameraStruct` object as input and returns a `Matrix4f` representation of
	 * the camera's projection matrix.
	 * 
	 * @param data 3D camera structure that contains the necessary projection parameters
	 * to calculate the projection matrix.
	 * 
	 * @returns a Matrix4f object containing the projection matrix as specified by the
	 * input `CameraStruct` data.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Modifies the camera's projection and view matrices based on the specified width
	 * and height of the viewport, and sets the viewport to the camera's current position.
	 * 
	 * @param width 2D viewport width in pixels.
	 * 
	 * @param height 2D image display size in pixels.
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
	 * Is a subclass of the Camera Struct class that provides additional functionality
	 * related to 3D camera calculations, including perspective projection and viewport
	 * adjustment. The class has fields for fov, aspect, zNear, and zFar, which are used
	 * to initialize a Matrix4f object for perspectives projection, and methods for
	 * calculating the camera's view matrix and adjusting the viewport size based on input
	 * events.
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
		 * Initializes a `Matrix4f` object with perspective projection parameters and returns
		 * the initialized matrix.
		 * 
		 * @returns a 4x4 floating-point matrix representing a perspective projection.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes input events from the mouse and keyboard, scaling the movement speed
	 * based on the LShift key press, and applying rotational movements to the game object
	 * based on the mouse wheel rotation.
	 * 
	 * @param dt time step or elapsed time, which is used to calculate the movement of
	 * the entity based on its speed and sensitivity.
	 * 
	 * @param speed 3D movement speed of the object being controlled, and its value is
	 * multiplied by the time elapsed (represented by `dt`) to calculate the total distance
	 * moved.
	 * 
	 * @param sens sensitivity of the mouse input, which determines how much the object
	 * rotates based on the mouse movement.
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
	 * Moves an object by a specified distance and direction, updating its position accordingly.
	 * 
	 * @param dir 3D direction to move the object in the game world.
	 * 
	 * @param amt amount of movement to be applied to the position of the object in the
	 * specified direction.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
