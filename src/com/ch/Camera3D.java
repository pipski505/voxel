package com.ch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Is an extension of the Camera class that includes additional functionality for
 * manipulating camera views in 3D space. It takes in four parameters to initialize
 * its values, including the field of view (fov), aspect ratio, near plane (zNear),
 * and far plane (zFar). The class has a protected inner class called CameraStruct3D
 * that contains the camera's perspective projection matrix. The processInput method
 * processes input from the mouse and keyboard to rotate the camera and move it in
 * 3D space.
 */
public class Camera3D extends Camera {

	public Camera3D(float fov, float aspect, float zNear, float zFar) {
		super(new Matrix4f());
		this.values = new CameraStruct3D(fov, aspect, zNear, zFar);
		calculateProjectionMatrix(values);
	}

	/**
	 * Calculates and returns a matrix that represents the projection of a 3D scene onto
	 * a 2D plane, based on the data provided by the `CameraStruct` object.
	 * 
	 * @param data 3D camera's configuration, including its position, orientation, and
	 * other parameters that are used to calculate the perspective projection matrix.
	 * 
	 * @returns a Matrix4f object representing the projection matrix.
	 */
	@Override
	public Matrix4f calculateProjectionMatrix(CameraStruct data) {
		return (projection = data.getAsMatrix4());
	}

	/**
	 * Adjusts a 3D camera's aspect ratio and projection matrix based on the viewport
	 * dimensions, then sets the camera's position and orientation using the calculated
	 * view matrix.
	 * 
	 * @param width 2D viewport width of the camera's field of view.
	 * 
	 * @param height 2D viewport size of the display device, which is used to calculate
	 * the projection and view matrices and set the 2D viewport dimensions in the OpenGL
	 * context.
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
	 * Is a subclass of the Camera struct that contains additional fields for controlling
	 * the camera's perspective view. It has a constructor that takes in fov, aspect,
	 * zNear, and zFar parameters to set the camera's perspectives. The class also includes
	 * a getAsMatrix4() method that returns a Matrix4f object representing the camera's
	 * perspective projection matrix.
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
		 * Initializes a matrix representing a perspective projection, with parameters `fov`,
		 * `aspect`, `zNear`, and `zFar`.
		 * 
		 * @returns a 4x4 floating-point matrix representing a perspective projection.
		 */
		public Matrix4f getAsMatrix4() {
			return new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		}

	}

	/**
	 * Processes input events from the mouse and keyboard, applying rotations and movements
	 * to an object's transform based on user inputs.
	 * 
	 * @param dt time step or elapsed time since the last frame, which is used to calculate
	 * the movement of the object based on keyboard inputs.
	 * 
	 * @param speed 3D movement speed of the object being controlled by the user, and it
	 * is multiplied by the time interval `dt` to determine the total distance traveled
	 * during that time.
	 * 
	 * @param sens sensitivity of the movement, which determines how much the character
	 * will move when the user moves the mouse cursor.
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
	 * position along a given direction, using the transform's `setPos()` method.
	 * 
	 * @param dir 3D direction in which to move the object, with the magnitude of the
	 * movement specified by the `amt` parameter.
	 * 
	 * @param amt amount of movement along the specified direction, which is added to the
	 * current position of the object.
	 */
	private void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}

}
