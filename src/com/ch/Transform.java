package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Quaternion;
import com.ch.math.Vector3f;

/**
 * Handles 3D transformations, including position, rotation, and scale, and provides
 * methods to update, transform, and retrieve the current state of a transform. It
 * also supports hierarchical transformations through a parent-child relationship.
 */
public class Transform {

	private Transform parent;
	private Matrix4f parentMatrix;

	private Vector3f pos;
	private Quaternion rot;
	private Vector3f scale;

	private Vector3f oldPos;
	private Quaternion oldRot;
	private Vector3f oldScale;

	public Transform() {
		pos = new Vector3f(0, 0, 0);
		rot = new Quaternion(1, 0, 0, 0);
		scale = new Vector3f(1, 1, 1);
		
		oldPos = new Vector3f(0, 0, 0);
		oldRot = new Quaternion(1, 0, 0, 0);
		oldScale = new Vector3f(1, 1, 1);

		parentMatrix = new Matrix4f().initIdentity();
	}

	/**
	 * Maintains a history of the object's position, rotation, and scale by updating or
	 * initializing the `oldPos`, `oldRot`, and `oldScale` variables based on the current
	 * `pos`, `rot`, and `scale` values.
	 */
	public void update() {
		if (oldPos != null) {
			if (!oldPos.equals(pos))
				oldPos.set(pos);
			if (!oldRot.equals(rot))
				oldRot.set(rot);
			if (!oldScale.equals(scale))
				oldScale.set(scale);
		} else {
			oldPos = new Vector3f().set(pos);
			oldRot = new Quaternion().set(rot);
			oldScale = new Vector3f().set(scale);
		}
	}

	/**
	 * Rotates a 3D object around a specified axis by a given angle, updating the `rot`
	 * quaternion accordingly. It multiplies the new rotation with the existing one and
	 * normalizes the result to maintain a unit quaternion.
	 *
	 * @param axis axis of rotation, around which the object is rotated by the specified
	 * angle.
	 *
	 * @param angle amount of rotation around the specified axis.
	 */
	public void rotate(Vector3f axis, float angle) {
		rot = new Quaternion(axis, angle).mul(rot).normalized();
	}

	/**
	 * Calculates a rotation based on a target point and an up direction. It stores the
	 * result in the `rot` variable. The rotation is typically used to orient a camera
	 * or object in 3D space.
	 *
	 * @param point target location in 3D space for the rotation.
	 *
	 * @param up orientation of the object's local y-axis.
	 */
	public void lookAt(Vector3f point, Vector3f up) {
		rot = getLookAtRotation(point, up);
	}

	/**
	 * Calculates a quaternion representing the rotation from the object's current position
	 * to a specified point, with a specified up vector.
	 *
	 * @param point target point from which the look-at rotation is calculated.
	 *
	 * @param up direction in which the rotation should be oriented.
	 *
	 * @returns a Quaternion representing the rotation needed to look at a target point
	 * while maintaining an upward vector.
	 */
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(pos).normalized(), up));
	}

	/**
	 * Checks if the object has undergone any changes by comparing its position, rotation,
	 * scale, and parent with their previous states. It recursively checks the parent
	 * object if it exists and returns true if any of these values have changed.
	 *
	 * @returns a boolean value indicating whether the object's properties have changed
	 * since the last update.
	 */
	public boolean hasChanged() {
		if (parent != null && parent.hasChanged())
			return true;

		if (!pos.equals(oldPos))
			return true;

		if (!rot.equals(oldRot))
			return true;

		if (!scale.equals(oldScale))
			return true;

		return false;
	}

	/**
	 * Calculates and returns a 4x4 transformation matrix for an object based on its
	 * position, rotation, and scale. The matrix is a composition of translation, rotation,
	 * and scale matrices, combined in a specific order. The result is a single transformation
	 * matrix.
	 *
	 * @returns a 4x4 transformation matrix representing translation, rotation, and scaling
	 * of an object.
	 */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ());
		Matrix4f rotationMatrix = rot.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}

	/**
	 * Returns the transformation matrix of the parent object if it has changed, otherwise
	 * returns the cached transformation matrix.
	 *
	 * @returns a transformation matrix representing the parent's current state.
	 */
	private Matrix4f getParentMatrix() {
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();

		return parentMatrix;
	}

	/**
	 * Sets a Transform object as the parent of the current object, assigning it to the
	 * `parent` field. The `parent` field is likely a reference to a Transform object
	 * that represents a parent node in a graphical hierarchy.
	 *
	 * @param parent transform that is being assigned as the parent of the current transform.
	 */
	public void setParent(Transform parent) {
		this.parent = parent;
	}

	/**
	 * Calculates the transformed position by applying the parent matrix to the current
	 * position.
	 * It returns a transformed Vector3f representing the new position.
	 * This is typically used in 3D graphics or game development to handle object transformations.
	 *
	 * @returns the transformed position of the object, relative to its parent matrix.
	 */
	public Vector3f getTransformedPos() {
		return getParentMatrix().transform(pos);
	}

	/**
	 * Calculates the transformed rotation by multiplying the current object's rotation
	 * with the parent's rotation, if a parent exists. The parent's rotation is retrieved
	 * recursively from its parent, if any. The result is the combined rotation.
	 *
	 * @returns a Quaternion representing the combined rotation of the current object and
	 * its parent object.
	 */
	public Quaternion getTransformedRot() {
		Quaternion parentRotation = new Quaternion(1, 0, 0, 0);

		if (parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.mul(rot);
	}

	/**
	 * Returns the position vector as a `Vector3f` object. The position is presumably
	 * stored in the `pos` variable. This function provides read-only access to the
	 * position data.
	 *
	 * @returns a Vector3f object representing the current position.
	 */
	public Vector3f getPos() {
		return pos;
	}

	/**
	 * Updates the position of an object to the specified value, storing it in the `pos`
	 * variable. The new position is taken from the `Vector3f` object passed as an argument.
	 *
	 * @param pos new position to be set for an object.
	 */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public void addToPos(Vector3f addVec) { this.setPos(this.getPos().add(addVec)); }

	/**
	 * Returns the value of the `rot` field, which is presumably a Quaternion object
	 * representing a rotation.
	 * The function provides read-only access to this field.
	 *
	 * @returns a Quaternion object representing a rotation.
	 */
	public Quaternion getRot() {
		return rot;
	}

	/**
	 * Assigns a Quaternion rotation to the `rot` field of the current object, replacing
	 * any previously stored rotation. The rotation is passed as an argument to the
	 * function. The assignment is performed directly, with no additional processing or
	 * validation.
	 *
	 * @param rotation new rotation value to be assigned to the `rot` attribute.
	 */
	public void setRot(Quaternion rotation) {
		this.rot = rotation;
	}

	/**
	 * Returns the current scale of an object.
	 *
	 * @returns a Vector3f object representing the scale of an object.
	 */
	public Vector3f getScale() {
		return scale;
	}

	/**
	 * Updates the object's scale by assigning a new value from the provided `Vector3f`
	 * object. The new scale is stored in the object's `scale` property, replacing any
	 * previous value.
	 *
	 * @param scale new scale value to be assigned to the object's scale property.
	 */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/**
	 * Overrides the default `toString` method to return an empty string when the object
	 * is converted to a string representation.
	 *
	 * @returns an empty string.
	 */
	@Override
	public String toString() { return "";
	}

}
