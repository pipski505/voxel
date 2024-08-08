package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Represents an object that can be rendered using OpenGL. It manages the vertex array
 * object (VAO), the size of the model, and provides methods to draw and manipulate
 * the model.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object (VAO) and enables two attribute arrays for vertices.
	 * It then draws elements using triangles, specifying the size of the draw operation
	 * and type of data. Finally, it disables the attribute arrays and unbinds the VAO.
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
	 * Enables vertex attribute arrays 0 and 1 for rendering graphics using OpenGL. It
	 * uses the `GL20` class to execute the enabling operations, allowing vertices with
	 * associated attributes to be processed by the GPU. This function prepares the
	 * graphics pipeline for rendering.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables vertex attribute array enablements for two attributes, identified by
	 * indices 0 and 1, using OpenGL API. This operation is performed globally, affecting
	 * all subsequent drawing operations until another attribute is enabled. The disabled
	 * attributes are no longer considered for rendering.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Retrieves and returns an integer value representing a vertex array object (VAO).
	 * It provides read-only access to the VAO, allowing its state to be queried by other
	 * parts of the program. The function does not modify the VAO's state or perform any
	 * operations on it.
	 *
	 * @returns an integer value representing a vertex array object.
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns the value of the `size` variable. It retrieves and exposes the current
	 * size to the caller, providing access to its internal state without modifying it.
	 * The returned integer represents the magnitude of some quantity or measurement.
	 *
	 * @returns an integer representing the value of the `size` variable.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Initializes a model by creating a vertex array object (VAO), storing indices and
	 * vertices data, unbinding VAO, and then returns a `Model` instance with the created
	 * VAO and index count.
	 *
	 * @param vertices 3D vertex data that is stored into the buffer object using the
	 * `storeData` method.
	 *
	 * @param indices 1D array of indices that specify the vertices to be used for rendering
	 * and is stored using the `storeIndices` method.
	 *
	 * @returns a new `Model` object with associated VAO and vertex count.
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
	 * Generates a unique vertex array object (VAO) using OpenGL and binds it to enable
	 * rendering. It returns the generated VAO ID, which can be used for subsequent
	 * graphics operations such as vertex buffer object binding and rendering.
	 *
	 * @returns an integer representing a generated Vertex Array Object (VAO).
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Allocates a vertex buffer object (VBO) to store float array data. It binds and
	 * loads the VBO with the provided data, then sets up two vertex attributes using
	 * glVertexAttribPointer. The attributes are set for a specific attribute index,
	 * number of components per attribute, data type, normalized values, stride, and offset.
	 *
	 * @param attrib attribute index of a vertex shader, which is used to specify the
	 * location and size of the data buffer for subsequent vertex attributes.
	 *
	 * @param data 1D array of floating-point numbers that is stored in a vertex buffer
	 * object (VBO) using `glBufferData`.
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
	 * Generates a buffer object and binds it to store element array indices. It then
	 * creates a buffer from the provided `indices` array, specifies its data format as
	 * static draw, and loads the data into the buffer.
	 *
	 * @param indices 1D array of integers that is used to create a buffer object for
	 * storing element indices and is passed to `Util.createFlippedBuffer()` to generate
	 * a buffer.
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Binds a vertex array object (VAO) with ID 0, effectively releasing any previously
	 * bound VAO. This allows other VAOs to be used or frees system resources allocated
	 * for the previous VAO. The function uses the OpenGL API's `glBindVertexArray`
	 * function for this purpose.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
