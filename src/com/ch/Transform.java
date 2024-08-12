package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Quaternion;
import com.ch.math.Vector3f;

/**
 * Represents a mathematical transformation in 3D space, encapsulating properties
 * such as position, rotation, and scale. It provides various methods to update,
 * rotate, look at, and transform these properties, as well as utilities for getting
 * the transformed positions and rotations. The class also supports hierarchical
 * transformations through its parent-child relationship.
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
	 * Updates old positions, rotations, and scales if they differ from the current ones.
	 * If no old values exist, it creates new ones by copying the current values. The
	 * updated old values are then stored for future comparisons.
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
	 * Applies a rotation to a quaternion-based orientation represented by `rot`. The
	 * rotation is specified by an axis and angle, which are used to create a new quaternion
	 * that is then multiplied with the existing one. The result is normalized to ensure
	 * proper orientation.
	 *
	 * @param axis 3D vector defining the rotation axis around which the object is rotated
	 * by an angle specified by the `angle` parameter.
	 *
	 * @param angle 3D rotation angle around the specified `axis`, which is used to compute
	 * a new quaternion by multiplying the current `rot` quaternion with the rotation
	 * axis and angle.
	 */
	public void rotate(Vector3f axis, float angle) {
		rot = new Quaternion(axis, angle).mul(rot).normalized();
	}

	/**
	 * Calculates a rotation to face a specified `point` while keeping the upward direction
	 * aligned with `up`. The result is stored in `rot`, which represents the necessary
	 * rotation to achieve this orientation.
	 *
	 * @param point 3D location that the object is looking at or gazing towards.
	 *
	 * @param up 3D vector that defines the upwards direction for the rotation calculation.
	 */
	public void lookAt(Vector3f point, Vector3f up) {
		rot = getLookAtRotation(point, up);
	}

	/**
	 * Calculates a quaternion representing a rotation from the current position to a
	 * specified point while keeping a specific direction (up) fixed. It takes two vectors,
	 * point and up, as input parameters and returns a new Quaternion object initialized
	 * with a rotation matrix calculated based on these vectors.
	 *
	 * @param point 3D vector from the origin to the target point, used to calculate the
	 * rotation matrix for the quaternion.
	 *
	 * @param up 3D vector that defines the direction of the up-axis for the rotation,
	 * used to determine the orientation of the resulting quaternion.
	 *
	 * @returns a `Quaternion` object representing a rotation.
	 */
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(pos).normalized(), up));
	}

	/**
	 * Checks whether a node has changed by comparing its position, rotation, and scale
	 * with their previous values and also checks if its parent node has changed. If any
	 * of these conditions are met, it returns `true`; otherwise, it returns `false`.
	 *
	 * @returns a boolean indicating whether any object properties have changed.
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
	 * Combines translation, rotation, and scaling transformations into a single matrix,
	 * returning the result as a product of four matrices: a translation matrix based on
	 * position coordinates, a rotation matrix from a rotational object, a scale matrix
	 * with scale factors, and a parent matrix.
	 *
	 * @returns a combined transformation matrix.
	 */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ());
		Matrix4f rotationMatrix = rot.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}

	/**
	 * Returns a matrix representing the transformation of the object's parent, if it
	 * exists and has changed since the last update. It updates its internal state with
	 * the parent's transformation matrix when necessary.
	 *
	 * @returns a `Matrix4f` object representing the parent's transformation.
	 */
	private Matrix4f getParentMatrix() {
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();

		return parentMatrix;
	}

	/**
	 * Assigns a value to the instance variable `parent`. This variable is expected to
	 * hold an object of type `Transform`, which is presumably a parent or container of
	 * some kind. The assignment updates the internal state of the object, linking it to
	 * its new parent.
	 *
	 * @param parent Transform object that is assigned to the `this.parent` field,
	 * establishing a reference to the parent Transform for further processing.
	 */
	public void setParent(Transform parent) {
		this.parent = parent;
	}

	/**
	 * Multiplies a position (`pos`) by a parent matrix using the `getParentMatrix` and
	 * `transform` methods, returning the resulting transformed position as a `Vector3f`.
	 * This operation applies a transformation to the original position based on the
	 * parent's coordinate system.
	 *
	 * @returns a transformed vector position based on the parent matrix.
	 */
	public Vector3f getTransformedPos() {
		return getParentMatrix().transform(pos);
	}

	/**
	 * Calculates a quaternion representing the combined rotation of an object and its
	 * parent, if any. It starts with a default quaternion as the parent's rotation,
	 * overrides it with the actual parent's rotation if one exists, and then multiplies
	 * it by the object's own rotation to produce the final result.
	 *
	 * @returns a Quaternion object resulting from multiplying `rot` with `parentRotation`.
	 */
	public Quaternion getTransformedRot() {
		Quaternion parentRotation = new Quaternion(1, 0, 0, 0);

		if (parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.mul(rot);
	}

	/**
	 * Retrieves a `Vector3f` object representing the position. This function returns a
	 * reference to an existing object, indicating that it does not create or modify any
	 * data but merely provides access to it. The returned value is a read-only representation
	 * of the position.
	 *
	 * @returns a `Vector3f` object representing the position.
	 */
	public Vector3f getPos() {
		return pos;
	}

	/**
	 * Updates the current position of an object to a new value provided by the `Vector3f`
	 * parameter. This parameter represents the new position in three-dimensional space,
	 * consisting of x, y, and z coordinates. The updated position is stored within the
	 * object for future reference.
	 *
	 * @param pos 3D position to be assigned to the object's current position.
	 */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public void addToPos(Vector3f addVec) { this.setPos(this.getPos().add(addVec)); }

	/**
	 * Returns a Quaternion object named `rot`. The returned object represents a rotation
	 * and is accessible through this method. This allows external code to retrieve and
	 * utilize the stored rotational data.
	 *
	 * @returns a Quaternion object named `rot`.
	 */
	public Quaternion getRot() {
		return rot;
	}

	/**
	 * Sets a Quaternion object representing the rotation to an instance variable `rot`.
	 * This variable is then updated with the provided rotation value. The function updates
	 * the internal state of the object with the new rotation information.
	 *
	 * @param rotation 4D quaternion value that is assigned to the instance variable `rot`.
	 */
	public void setRot(Quaternion rotation) {
		this.rot = rotation;
	}

	/**
	 * Returns a `Vector3f` object representing the scale value. The returned vector
	 * contains the x, y, and z components of the scale factor. The function provides
	 * read-only access to the scale value without modifying it.
	 *
	 * @returns a `Vector3f` object representing the scale of an object.
	 */
	public Vector3f getScale() {
		return scale;
	}

	/**
	 * Assigns a new value to the instance variable `scale`, which is expected to be an
	 * object of type `Vector3f`. This function does not perform any calculations or
	 * operations on the input, it simply updates the internal state of the class. The
	 * new scale vector replaces the previous one.
	 *
	 * @param scale 3D vector used to modify the object's size in the scene.
	 */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/**
	 * Returns an empty string when called. This implies that the object represented by
	 * the class is intended to be unprintable or does not have a meaningful string representation.
	 *
	 * @returns an empty string.
	 */
	@Override
	public String toString() { return "";
	}

}
