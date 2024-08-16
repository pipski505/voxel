package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Represents a graphical model that can be drawn using OpenGL. It encapsulates vertex
 * array objects (VAOs) and buffers data for vertices and indices. The class provides
 * a method to load the model from floating-point vertices and integer indices, and
 * another to draw the model.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object (VAO), enables vertex attribute arrays, and then draws
	 * a triangle mesh using indexed rendering. It disables the vertex attribute arrays
	 * and unbinds the VAO after drawing.
	 */
	public void draw() {
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, size);
		GL11.glDrawElements(GL11.GL_TRIANGLES, size, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Enables vertex attribute arrays for rendering graphics on a GPU. It uses OpenGL
	 * functions to enable two specific vertex attributes, presumably for vertices and
	 * colors, allowing their data to be streamed into the shader program.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables two vertex attributes with indices 0 and 1 using the OpenGL API. It stops
	 * processing data for these attributes, effectively halting their usage in rendering
	 * operations. This allows for efficient management of graphics resources and improves
	 * performance.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns an integer representing a vertex array object (VAO). This value is likely
	 * used to identify and manage graphical resources for rendering purposes. The returned
	 * VAO can be utilized by other parts of the program to interact with graphics
	 * processing units.
	 *
	 * @returns an integer value representing a vertex array object (VAO).
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Retrieves and returns the value of a variable named `size`. The function does not
	 * perform any computation or modification, but rather simply exposes the current
	 * state of the `size` variable to its caller.
	 *
	 * @returns an integer value representing the object's size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Creates a Vertex Array Object (VAO), stores vertex data and indices, binds the
	 * VAO, and unbinds it before returning a `Model` object with the created VAO and
	 * vertex count.
	 *
	 * @param vertices 3D vertex data that is stored in the graphics processing unit (GPU)
	 * and used for rendering.
	 *
	 * @param indices 1D array of vertex indices that define the order and connectivity
	 * of vertices to be stored in the Vertex Array Object (VAO).
	 *
	 * @returns a new instance of the `Model` class.
	 */
	public static Model load(float[] vertices, int[] indices) {
		int vao = createVAO();
		storeIndices(indices);
		storeData(0, vertices);
		unbindVAO();
		int v_count = indices.length;
		return new Model(vao, v_count);
	}
	
	/**
	 * Generates a vertex array object (VAO) and binds it, allowing for the specification
	 * of vertices, colors, and other attributes for rendering purposes. It returns the
	 * ID of the generated VAO.
	 *
	 * @returns an integer representing a generated vertex array object.
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Stores vertex buffer object data in OpenGL. It generates a buffer object, binds
	 * it as an array buffer, and loads the provided data into it. The function then
	 * specifies the attributes of the vertices, including their layout and data types,
	 * using glVertexAttribPointer functions.
	 *
	 * @param attrib attribute index to be bound to the vertex buffer object (VBO), used
	 * to specify the location and format of the data stored in the VBO.
	 *
	 * @param data 1D array of floating-point values to be stored in a buffer object for
	 * vertex data.
	 */
	private static void storeData(int attrib, float[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(data), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attrib, 3, GL11.GL_FLOAT, false, 5 * 4,     0);
		GL20.glVertexAttribPointer(attrib + 1, 2, GL11.GL_FLOAT, false, 5 * 4, 3 * 4);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Generates a buffer object using OpenGL and stores an array of indices in it. It
	 * binds the buffer as an element array buffer and loads the data into it with static
	 * drawing.
	 *
	 * @param indices 1D array of indices that are stored in a buffer object on the
	 * graphics processing unit (GPU).
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Binds a default vertex array object (VAO) with an index of 0, effectively releasing
	 * any previously bound VAO for graphics rendering operations to work on the next
	 * available context. This is typically used after using a specific VAO to ensure
	 * proper rendering and resource management.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
