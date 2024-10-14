package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Quaternion;
import com.ch.math.Vector3f;

/**
 * Represents a 3D transformation with position, rotation, and scale properties, and
 * provides methods for updating, manipulating, and combining these properties to
 * perform transformations.
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
	 * Syncs position, rotation, and scale data between current and previous states,
	 * updating the previous state if they differ, or initializing previous state if it
	 * does not exist.
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
	 * Updates a quaternion representing a 3D rotation by applying a new rotation around
	 * a specified axis by a given angle, normalizing the resulting quaternion.
	 *
	 * @param axis axis of rotation for the specified quaternion transformation.
	 *
	 * @param angle amount of rotation around the specified axis.
	 */
	public void rotate(Vector3f axis, float angle) {
		rot = new Quaternion(axis, angle).mul(rot).normalized();
	}

	/**
	 * Sets the rotation of an object based on a target point and an up direction, returning
	 * the rotation from the `getLookAtRotation` method. The returned rotation is then
	 * assigned to the `rot` variable. The object's view is adjusted accordingly.
	 *
	 * @param point target location from which the rotation is calculated.
	 *
	 * @param up direction in which the object's "up" direction is aligned, influencing
	 * the rotation calculation.
	 */
	public void lookAt(Vector3f point, Vector3f up) {
		rot = getLookAtRotation(point, up);
	}

	/**
	 * Calculates a quaternion rotation that aligns a scene object's forward direction
	 * with a specified point in 3D space and up direction with a given vector.
	 *
	 * @param point target point in 3D space from which the rotation is calculated.
	 *
	 * @param up desired orientation of the up axis in the resulting rotation.
	 *
	 * @returns a Quaternion representing the rotation to face a target point while
	 * maintaining a specified up direction.
	 */
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(pos).normalized(), up));
	}

	/**
	 * Determines whether an object has changed by comparing its position, rotation, and
	 * scale with their previous values, and also checks if its parent has changed. Returns
	 * true if any of these values have changed or if the parent has changed, otherwise
	 * returns false.
	 *
	 * @returns a boolean value indicating whether the object's properties have changed
	 * since the last update.
	 *
	 * The output is a boolean value indicating whether the object has changed. It is
	 * true if any of the properties (parent, position, rotation, or scale) have been
	 * modified since the last check.
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
	 * Calculates a 4x4 transformation matrix by combining translation, rotation, and
	 * scale matrices. The transformation matrix is then returned by multiplying the
	 * parent matrix with the calculated transformation matrix.
	 *
	 * @returns a 4x4 transformation matrix combining translation, rotation, and scaling
	 * transformations.
	 */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ());
		Matrix4f rotationMatrix = rot.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}

	/**
	 * Returns the parent transformation matrix, updating it if the parent has changed
	 * and has a valid transformation. The function checks for a non-null parent and a
	 * changed parent before updating the parent matrix.
	 *
	 * @returns a `Matrix4f` containing the parent's transformation, or the cached parent
	 * matrix if unchanged.
	 */
	private Matrix4f getParentMatrix() {
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();

		return parentMatrix;
	}

	/**
	 * Sets the parent Transform of the current object, assigning the provided Transform
	 * object.
	 *
	 * @param parent Transform object that will be assigned to the `parent` field of the
	 * current object.
	 */
	public void setParent(Transform parent) {
		this.parent = parent;
	}

	/**
	 * Transforms a position by applying the parent matrix, returning the resulting position.
	 *
	 * @returns a Vector3f representing the position transformed by the parent matrix.
	 */
	public Vector3f getTransformedPos() {
		return getParentMatrix().transform(pos);
	}

	/**
	 * Calculates the transformed rotation by multiplying the current object's rotation
	 * (`rot`) with the parent object's transformed rotation (`parentRotation`). If a
	 * parent object exists, its transformed rotation is recursively calculated.
	 *
	 * @returns the product of the current object's rotation and its parent's rotation.
	 */
	public Quaternion getTransformedRot() {
		Quaternion parentRotation = new Quaternion(1, 0, 0, 0);

		if (parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.mul(rot);
	}

	/**
	 * Returns the current position, represented as a `Vector3f`, stored in the `pos` variable.
	 *
	 * @returns a Vector3f object containing the position coordinates.
	 */
	public Vector3f getPos() {
		return pos;
	}

	/**
	 * Sets the position of an object to the specified `Vector3f` coordinates. It assigns
	 * the provided position vector to the object's position attribute. The function
	 * updates the object's position accordingly.
	 *
	 * @param pos new position to be set.
	 */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public void addToPos(Vector3f addVec) { this.setPos(this.getPos().add(addVec)); }

	/**
	 * Returns the rotation quaternion.
	 * It provides access to the internal `rot` variable.
	 * It does not modify the rotation.
	 *
	 * @returns a Quaternion object representing a rotation.
	 */
	public Quaternion getRot() {
		return rot;
	}

	/**
	 * Assigns a Quaternion rotation value to the `rot` object, updating the object's
	 * rotation state. The assigned rotation is taken from the `rotation` parameter. The
	 * function modifies the object's internal state directly.
	 *
	 * @param rotation new quaternion rotation value to be assigned to the `rot` property.
	 */
	public void setRot(Quaternion rotation) {
		this.rot = rotation;
	}

	/**
	 * Returns the scale value, presumably a 3D vector, stored in the `scale` variable.
	 *
	 * @returns a Vector3f object representing the scale of an object.
	 */
	public Vector3f getScale() {
		return scale;
	}

	/**
	 * Sets the scale of an object to a specified value, replacing the existing scale
	 * with a new one. The new scale is stored in a `Vector3f` object. This function
	 * updates the object's scale property.
	 *
	 * @param scale new scale value to be assigned to the object's scale property.
	 */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/**
	 * Overrides the default `toString` method, returning an empty string when called.
	 * This provides a custom representation of the object, in this case, an empty string.
	 * It serves as a placeholder for objects that do not require a meaningful string representation.
	 *
	 * @returns an empty string, specifically the string "".
	 */
	@Override
	public String toString() { return "";
	}

}
