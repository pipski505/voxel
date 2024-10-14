package com.ch;

import java.util.List;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

/**
 * Provides utility methods for creating and manipulating buffers, arrays, and lists,
 * including string filtering and data type conversions.
 */
public class Util {
	
	/**
	 * Allocates a FloatBuffer of a specified size and returns it.
	 *
	 * @param size number of float values that the created FloatBuffer will be able to hold.
	 *
	 * @returns an instance of `FloatBuffer` with the specified size.
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	/**
	 * Creates an IntBuffer of a specified size, utilizing the BufferUtils class to
	 * allocate the buffer. The size parameter determines the number of integer elements
	 * the buffer can hold. The function returns the newly created IntBuffer.
	 *
	 * @param size number of elements in the IntBuffer to be created.
	 *
	 * @returns an `IntBuffer` object with a specified capacity.
	 */
	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	/**
	 * Allocates a new ByteBuffer of a specified size, utilizing the BufferUtils class.
	 * It takes an integer size as input and returns the allocated ByteBuffer. The size
	 * parameter determines the capacity of the buffer.
	 *
	 * @param size size of the ByteBuffer to be created.
	 *
	 * @returns a ByteBuffer object initialized with a specified size.
	 */
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}

	/**
	 * Creates an IntBuffer instance, populates it with the specified integer values, and
	 * then flips the buffer to switch from put mode to get mode.
	 *
	 * @param values array of integer values to be placed in the buffer.
	 *
	 * @returns a flipped IntBuffer object, ready for reading from the beginning to the
	 * end.
	 */
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}
	
	/**
	 * Creates a new FloatBuffer from an array of floats, copies the array into the buffer,
	 * and flips the buffer to make it ready for reading.
	 *
	 * @param values array of float values to be stored in the buffer.
	 *
	 * @returns a FloatBuffer instance with the input values in reverse order.
	 */
	public static FloatBuffer createFlippedBuffer(float... values) {
		FloatBuffer buffer = createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}

	/*
	public static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);

		for (int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].getPos().getX());
			buffer.put(vertices[i].getPos().getY());
			buffer.put(vertices[i].getPos().getZ());
			buffer.put(vertices[i].getTexCoord().getX());
			buffer.put(vertices[i].getTexCoord().getY());
			buffer.put(vertices[i].getNormal().getX());
			buffer.put(vertices[i].getNormal().getY());
			buffer.put(vertices[i].getNormal().getZ());
			buffer.put(vertices[i].getTangent().getX());
			buffer.put(vertices[i].getTangent().getY());
			buffer.put(vertices[i].getTangent().getZ());
		}

		buffer.flip();

		return buffer;
	}
	*/

	/*
	public static FloatBuffer createFlippedBuffer(Matrix4f value) {
		FloatBuffer buffer = createFloatBuffer(4 * 4);

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				buffer.put(value.get(i, j));

		buffer.flip();

		return buffer;
	}
	
	public static Matrix4f loatMat4(FloatBuffer vals) {
		
//		vals.flip();
		
		Matrix4f m = new Matrix4f();
		
		int index;
		for (index = 0; index < 16; index++)
			m.set(index % 4, index / 4, vals.get());
		
		return m;
	}
*/
	/**
	 * Removes empty strings from a given array of strings and returns the filtered array.
	 * It iterates over the input array, adding non-empty strings to a temporary list,
	 * and then converts the list back to a string array.
	 *
	 * @param data array of strings from which empty strings are to be removed.
	 *
	 * Deconstruct the input `data` as an array of non-null strings.
	 *
	 * @returns an array of non-empty strings from the input array.
	 *
	 * The output returned is an array of non-empty strings.
	 */
	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < data.length; i++)
			if (!data[i].equals(""))
				result.add(data[i]);

		String[] res = new String[result.size()];
		result.toArray(res);

		return res;
	}

	/**
	 * Converts an array of Integer objects to an array of primitive integers. It iterates
	 * over the input array and assigns each Integer object's value to the corresponding
	 * index in the result array. The function returns the resulting primitive integer array.
	 *
	 * @param data array of Integer objects to be converted into an array of primitive integers.
	 *
	 * @returns an array of integers converted from the input array of Integer objects.
	 */
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a given list of integers into an array of integers, preserving the original
	 * order and size of the input list.
	 *
	 * @param data List of Integer objects to be converted into an array.
	 *
	 * @returns an array of integers representing the input list.
	 */
	public static int[] toIntArray(List<Integer> data) {
		int[] result = new int[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
	
	/**
	 * Converts a Float array to a float array. It creates a new array of the same size
	 * as the input array, then copies each element from the input array to the corresponding
	 * position in the new array.
	 *
	 * @param data array of Float objects to be converted into a float array.
	 *
	 * @returns a copy of the input array, converted to a float array.
	 */
	public static float[] toFloatArray(Float[] data) {
		float[] result = new float[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a list of float values into a float array, copying each element from the
	 * list into the corresponding index in the array. The resulting array has the same
	 * size as the input list.
	 *
	 * @param data list of Float objects to be converted into a float array.
	 *
	 * @returns an array of floats equivalent to the input list of floats.
	 */
	public static float[] toFloatArray(List<Float> data) {
		float[] result = new float[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
}
