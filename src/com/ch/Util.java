package com.ch;

import java.util.List;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

/**
 * Is a utility class that provides various methods for manipulating arrays of data,
 * including converting between integer, float, and boolean arrays, as well as removing
 * empty strings from an array. It also provides methods for creating and manipulating
 * buffers for storing vertex positions, tangents, and normals in a 3D graphics
 * context. Additionally, it offers methods for creating matrices in 4D space.
 */
public class Util {
	
	/**
	 * Creates a new FloatBuffer object with the specified size.
	 * 
	 * @param size number of elements to be stored in the FloatBuffer.
	 * 
	 * @returns a FloatBuffer object of specified size, created using `BufferUtils`.
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	/**
	 * Creates an `IntBuffer` instance with the specified size using `BufferUtils`.
	 * 
	 * @param size integer capacity of the IntBuffer to be created, which determines the
	 * number of elements that can be stored in the buffer.
	 * 
	 * @returns an `IntBuffer` object that represents a buffer of integers with the
	 * specified size.
	 */
	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	/**
	 * Creates a new byte buffer with the specified size using the `BufferUtils` class.
	 * 
	 * @param size desired capacity of the byte buffer to be created, which is used by
	 * the `BufferUtils.createByteBuffer()` method to determine the appropriate buffer
	 * size and allocate memory for it.
	 * 
	 * @returns a non-null `ByteBuffer` instance of the specified size.
	 */
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}

	/**
	 * Creates an `IntBuffer` object from a provided array of integers, puts the values
	 * into the buffer, and flips the buffer for efficient access.
	 * 
	 * @returns an flipped `IntBuffer` containing the provided values.
	 */
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}
	
	/**
	 * Creates a new `FloatBuffer` object and populates it with the specified `float`
	 * values, then flips the buffer to position the first element at the beginning. It
	 * returns the flipped buffer.
	 * 
	 * @returns a flipped FloatBuffer containing the input values.
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
	 * Removes empty strings from an array of strings, returning a new array with non-empty
	 * strings.
	 * 
	 * @param data 0-length array of strings that are to be filtered for empty strings.
	 * 
	 * 	- Length: `data.length`
	 * 	- Elements: The array contains multiple strings
	 * 	- Properties of individual elements: Each element is a string that may or may not
	 * be empty
	 * 
	 * @returns a new array of non-empty strings, containing all the elements from the
	 * original array that are not empty.
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
	 * Transforms an array of integers into an integer array with the same length, by
	 * simply copying the elements of the input array to the output array.
	 * 
	 * @param data 1D array of integers that is converted to a 1D integer array by the function.
	 * 
	 * @returns an int array with the same length as the input `data` array, containing
	 * the corresponding integer values.
	 */
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a `List<Integer>` to an `int[]` array, copying the values of the list
	 * into the array.
	 * 
	 * @param data list of integers to be converted into an integer array.
	 * 
	 * @returns an int array containing the elements of the input list.
	 */
	public static int[] toIntArray(List<Integer> data) {
		int[] result = new int[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
	
	/**
	 * Takes a `Float[]` array as input and returns a new `float[]` array with the same
	 * elements.
	 * 
	 * @param data 0-based array of floating-point numbers that will be converted into a
	 * new 0-based array of floating-point numbers.
	 * 
	 * @returns an array of `float` values containing the same elements as the input
	 * `Float[]` array.
	 */
	public static float[] toFloatArray(Float[] data) {
		float[] result = new float[data.length];

		for (int i = 0; i < data.length; i++)
			result[i] = data[i];

		return result;
	}
	
	/**
	 * Converts a list of floating-point numbers to an array of the same size, with each
	 * element corresponding to the value in the input list.
	 * 
	 * @param data list of floating-point numbers that will be converted into an array
	 * of floats by the `toFloatArray()` method.
	 * 
	 * @returns an array of `float` values equal to the size of the input `List<Float>`
	 * containing the corresponding floating-point numbers.
	 */
	public static float[] toFloatArray(List<Float> data) {
		float[] result = new float[data.size()];

		for (int i = 0; i < data.size(); i++)
			result[i] = data.get(i);

		return result;
	}
}
