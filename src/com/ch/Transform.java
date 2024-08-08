package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Quaternion;
import com.ch.math.Vector3f;

/**
 * Is a flexible representation of a 3D object's position, rotation, and scale. It
 * has an inheritance hierarchy with a parent transform, which allows for a tree-like
 * structure of transforms. The class has methods for rotating, looking at a point,
 * and updating its transformation matrix, as well as getting/setting the position,
 * rotation, and scale. It also has a high-level update method that checks for changes
 * in the transform and updates the necessary fields accordingly.
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
	 * Updates an object's position, rotation, and scale based on the current values and
	 * stores them as old values for future reference.
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
	 * Multiplies a quaternion representing a rotation axis by a scalar angle and returns
	 * the resulting normalized quaternion.
	 * 
	 * @param axis 3D rotation axis around which the rotation will be performed.
	 * 
	 * @param angle 3D rotation angle around the axis specified by the `axis` parameter.
	 */
	public void rotate(Vector3f axis, float angle) {
		rot = new Quaternion(axis, angle).mul(rot).normalized();
	}

	/**
	 * Computes the rotation required to face a given point while maintaining a specified
	 * up vector.
	 * 
	 * @param point 3D position that the agent should look at.
	 * 
	 * @param up 3D direction that the model should face after rotating around the specified
	 * `point`.
	 */
	public void lookAt(Vector3f point, Vector3f up) {
		rot = getLookAtRotation(point, up);
	}

	/**
	 * Calculates a quaternion representing the rotation necessary to look at a point
	 * from a specific direction. It takes two vectors as input: `point` represents the
	 * point to look at, and `up` represents the direction of the view. The function
	 * returns a quaternion that rotates the object in the specified direction to face
	 * the given point.
	 * 
	 * @param point 3D point of interest around which the look-at rotation is to be computed.
	 * 
	 * @param up 3D vector that defines the orientation of the look-at axis.
	 * 
	 * @returns a Quaternion representation of the rotation required to face a specified
	 * point in space while maintaining a fixed orientation with respect to a provided
	 * up vector.
	 */
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(pos).normalized(), up));
	}

	/**
	 * Checks if an object's properties have changed by comparing them to their previous
	 * values. If any property has changed, the function returns `true`. Otherwise, it
	 * returns `false`.
	 * 
	 * @returns a boolean value indicating whether the object has changed.
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
	 * Computes a transformation matrix based on position, rotation, and scale values,
	 * and returns it as a Matrix4f object.
	 * 
	 * @returns a transformed matrix representing the combination of translation, rotation,
	 * and scaling.
	 */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ());
		Matrix4f rotationMatrix = rot.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}

	/**
	 * Retrieves and returns the transformation matrix of the parent node in a hierarchical
	 * tree structure, taking into account any changes made to the parent node's transformation.
	 * 
	 * @returns a Matrix4f object representing the parent transformation matrix.
	 */
	private Matrix4f getParentMatrix() {
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();

		return parentMatrix;
	}

	/**
	 * Sets the parent transform of an object, storing the new value in the `parent` field.
	 * 
	 * @param parent Transform object that will serve as the new parent of the current
	 * Transform object.
	 */
	public void setParent(Transform parent) {
		this.parent = parent;
	}

	/**
	 * Takes a `Vector3f` object as input and returns its transformed position after
	 * applying a transformation matrix provided by the parent matrix.
	 * 
	 * @returns a transformed position vector in the form of a `Vector3f`.
	 */
	public Vector3f getTransformedPos() {
		return getParentMatrix().transform(pos);
	}

	/**
	 * Multiplies a given quaternion `rot` by the parent rotation, which is initially set
	 * to the identity quaternion and later updated based on the value of `parent`. The
	 * resulting transformed quaternion is returned.
	 * 
	 * @returns a Quaternion representing the rotated transformation of the parent object
	 * relative to its original rotation.
	 */
	public Quaternion getTransformedRot() {
		Quaternion parentRotation = new Quaternion(1, 0, 0, 0);

		if (parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.mul(rot);
	}

	/**
	 * Returns the position of an object as a `Vector3f` structure.
	 * 
	 * @returns a `Vector3f` object containing the position of the entity.
	 */
	public Vector3f getPos() {
		return pos;
	}

	/**
	 * Sets the position of an object to a specified Vector3f value.
	 * 
	 * @param pos 3D position of an object or entity in the function `setPos`.
	 */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public void addToPos(Vector3f addVec) { this.setPos(this.getPos().add(addVec)); }

	/**
	 * Returns a `Quaternion` object representing the rotation of an entity.
	 * 
	 * @returns a Quaternion object representing the rotation of the game object.
	 */
	public Quaternion getRot() {
		return rot;
	}

	/**
	 * Sets the object's rotation state to the provided `Quaternion` value.
	 * 
	 * @param rotation 4D quaternion value that updates the rotational orientation of the
	 * object instance being represented by the `this` reference.
	 */
	public void setRot(Quaternion rotation) {
		this.rot = rotation;
	}

	/**
	 * Returns the current scale value of a `Vector3f` object.
	 * 
	 * @returns a `Vector3f` object containing the scaling factors for the model.
	 */
	public Vector3f getScale() {
		return scale;
	}

	/**
	 * Sets the scale of an object, which affects its size and shape.
	 * 
	 * @param scale 3D scaling factor for the object being modeled.
	 */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/**
	 * Returns an empty string for any input.
	 * 
	 * @returns an empty string.
	 */
	@Override
	public String toString() { return "";
	}

}
