package com.ch;

import java.util.List;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

/**
 * Provides various utility functions for creating buffers and converting between
 * primitive data types. It also includes methods for removing empty strings from an
 * array and converting lists of integers and floats to arrays.
 */
public class Util {
	
	/**
	 * Creates a new float buffer with the specified `size`. It returns a newly created
	 * float buffer object using `BufferUtils.createFloatBuffer()` method. The returned
	 * buffer is not initialized and needs to be filled with data before being used.
	 *
	 * @param size umber of floating-point values that the returned `FloatBuffer` will
	 * be able to hold.
	 *
	 * @returns a FloatBuffer object created with the specified size.
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	/**
	 * Creates an IntBuffer with a specified size, allocating memory to store integer
	 * values. The buffer is initialized to zero and can be used for storing or retrieving
	 * integer data. It returns the newly created IntBuffer instance.
	 *
	 * @param size umber of integers that can be stored in the created IntBuffer, determining
	 * its capacity and maximum size.
	 *
	 * @returns an instance of `IntBuffer`.
	 */
	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	/**
	 * Returns a new instance of `ByteBuffer` with the specified size using `BufferUtils`.
	 * The created buffer is uninitialized, meaning it does not contain any data. It
	 * provides a mechanism to store and retrieve binary data efficiently.
	 *
	 * @param size umber of bytes to allocate for the resulting byte buffer, which is
	 * used to create a new ByteBuffer object with the specified size.
	 *
	 * @returns a Java `ByteBuffer` object of specified size.
	 */
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}

	/**
	 * Creates an `IntBuffer` from a variable number of integer values, puts the values
	 * into the buffer, and then flips the buffer to prepare it for reading. The resulting
	 * buffer can be used for both writing and reading operations.
	 *
	 * @param values 0-based indices of an array that is used to populate the IntBuffer
	 * with integer values.
	 *
	 * @returns a flipped `IntBuffer` containing the input values.
	 */
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}
	
	/**
	 * Creates a FloatBuffer from an array of float values, puts the values into the
	 * buffer, and then flips the buffer to prepare it for reading. It returns the flipped
	 * buffer.
	 *
	 * @param values float array elements to be written into the FloatBuffer object.
	 *
	 * @returns a flipped FloatBuffer containing the provided float values.
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
	 * Transforms an input array of strings into a new array, excluding any empty strings
	 * and preserving the original order. It achieves this by iterating through the input
	 * array, adding non-empty strings to an ArrayList, and then converting the ArrayList
	 * back to an array.
	 *
	 * @param data 1-D array of strings to be processed by removing empty strings and
	 * returning an updated string array.
	 *
	 * Data is an array of strings, where each string represents a value. The length of
	 * this array is dynamically determined by the number of elements added during execution.
	 *
	 * @returns an array of non-empty strings.
	 *
	 * The output is an array of strings.
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
	 * Converts an array of `Integer` objects into an array of primitive `int` values.
	 * It iterates over the input array, assigning each integer value to a corresponding
	 * element in the output array. The resulting array is returned as a static method result.
	 *
	 * @param data 2D array of integers that is being converted to a primitive int array.
	 *
	 * @returns an integer array converted from a given Integer array.
	 */
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a List of Integer values to an array of integers. It iterates through the
	 * input list, assigning each value to its corresponding index in the resulting array.
	 * The array is then returned as the output.
	 *
	 * @param data List of Integer values to be converted into an integer array.
	 *
	 * @returns an integer array containing elements from the input list.
	 */
	public static int[] toIntArray(List<Integer> data) {
		int[] result = new int[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
	
	/**
	 * Converts a given array of Float objects to an array of primitive float values,
	 * maintaining the original length and order of elements. It iterates over the input
	 * array, copying each element to the corresponding position in the output array.
	 *
	 * @param data 2D array of Float objects to be converted into a 1D array of float
	 * primitive types.
	 *
	 * @returns a new array of floats.
	 */
	public static float[] toFloatArray(Float[] data) {
		float[] result = new float[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a list of floating-point numbers into an array of floats. It iterates
	 * over the input list, assigning each element to its corresponding index in the
	 * output array. The resulting array is then returned.
	 *
	 * @param data List of Float values to be converted into a float array and processed
	 * by the function.
	 *
	 * @returns an array of float values converted from a list.
	 */
	public static float[] toFloatArray(List<Float> data) {
		float[] result = new float[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
}
