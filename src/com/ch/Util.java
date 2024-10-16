package com.ch;

import java.util.List;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

/**
 * Provides utility methods for creating and manipulating buffers, arrays, and lists,
 * including methods for removing empty strings and converting between data types.
 */
public class Util {
	
	/**
	 * Allocates a new FloatBuffer of a specified size, returning it as a FloatBuffer
	 * object. The size parameter determines the initial capacity of the buffer. The
	 * function appears to rely on an external utility class, BufferUtils, to perform the
	 * actual allocation.
	 *
	 * @param size number of elements that the resulting `FloatBuffer` will be able to hold.
	 *
	 * @returns a FloatBuffer object with the specified size.
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	/**
	 * Creates an IntBuffer of a specified size.
	 * It utilizes BufferUtils to generate the buffer.
	 *
	 * @param size number of integers the buffer will be able to hold.
	 *
	 * @returns an instance of `IntBuffer` containing `size` integer elements.
	 */
	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	/**
	 * Allocates a new buffer of a specified size and returns it as a ByteBuffer object.
	 * The buffer size is determined by the input parameter.
	 * The buffer is created using the `BufferUtils` class.
	 *
	 * @param size number of bytes to be allocated in the ByteBuffer.
	 *
	 * @returns a ByteBuffer object with the specified size.
	 */
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}

	/**
	 * Creates a new IntBuffer instance from an array of integers, copies the array
	 * elements into the buffer, and then flips the buffer to mark its current position
	 * as the end of the data.
	 *
	 * @param values array of integer values to be stored in the buffer.
	 *
	 * @returns a flipped IntBuffer, where the position and limit are swapped.
	 */
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}
	
	/**
	 * Creates a FloatBuffer from an array of floats, stores the values in the buffer,
	 * and flips the buffer to a read-only mode, allowing for efficient retrieval of the
	 * stored values. The function returns the modified buffer.
	 *
	 * @param values array of floating-point numbers to be stored in the buffer.
	 *
	 * @returns a flipped `FloatBuffer` containing the input `float` values.
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
	 * Removes empty strings from a given array of strings and returns a new array
	 * containing only the non-empty strings.
	 *
	 * @param data array of strings from which empty strings are to be removed.
	 *
	 * Deconstructed, `data` is an array of strings.
	 *
	 * @returns an array of non-empty strings, filtered from the input array.
	 *
	 * The returned output is an array of strings, with all empty strings removed from
	 * the original input array. The array contains non-empty strings from the original
	 * array in their original order. The array's size is reduced by the number of empty
	 * strings removed.
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
	 * Converts an array of Integer objects to an array of primitive int values. It takes
	 * an array of Integer as input, creates a new array of int, and copies the Integer
	 * values to the int array. The function returns the new int array.
	 *
	 * @param data array of Integer objects to be converted into an array of primitive integers.
	 *
	 * @returns an array of integers with the same elements as the input array.
	 */
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a list of integers into an array of integers. It creates an array of the
	 * same size as the input list and populates it with the list's elements. The function
	 * returns this array.
	 *
	 * @param data list of integers to be converted into an array.
	 *
	 * @returns an array of integers containing the elements from the input list.
	 */
	public static int[] toIntArray(List<Integer> data) {
		int[] result = new int[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
	
	/**
	 * Converts a given `Float[]` array into a `float[]` array, effectively copying the
	 * elements from the original array to the result array. The function does not perform
	 * any data type conversions, as `Float[]` is already an array of primitive float values.
	 *
	 * @param data array of Float objects to be converted into a float array.
	 *
	 * @returns a new array of floats containing the same elements as the input array data.
	 */
	public static float[] toFloatArray(Float[] data) {
		float[] result = new float[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a list of Float objects into a float array, copying the elements from the
	 * list into the array in their original order.
	 *
	 * @param data list of floating-point numbers to be converted into a float array.
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
