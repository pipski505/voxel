package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Handles 3D model rendering, utilizing OpenGL for vertex array object (VAO) creation
 * and data storage.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object, enables vertex attribute arrays, draws triangles using
	 * indexed vertices, disables vertex attribute arrays, and unbinds the vertex array
	 * object.
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
	 * Enable vertex attribute arrays with indices 0 and 1 for use by the OpenGL pipeline.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables the vertex attribute array at indices 0 and 1 for OpenGL rendering.
	 * This prevents the GPU from accessing these attributes for further rendering operations.
	 * Attributed arrays are typically used for vertex data such as positions and colors.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns the value of the `vao` variable, which is an integer representing a Vertex
	 * Array Object. The function provides access to the VAO's ID. It is likely used to
	 * retrieve the VAO's ID for further rendering or configuration purposes.
	 *
	 * @returns the value of the `vao` variable.
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns the current size value.
	 *
	 * @returns the current size, a numeric value representing the object's size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Loads model data from given vertices and indices arrays into a Vertex Array Object
	 * (VAO), stores the data, and returns a Model object containing the VAO and vertex
	 * count.
	 *
	 * @param vertices array of vertex data to be stored in the graphics rendering pipeline.
	 *
	 * @param indices array of indices into the vertices array, used to define the order
	 * and connections of the vertices.
	 *
	 * @returns a `Model` object containing a VAO ID and the index count.
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
	 * Generates a unique Vertex Array Object (VAO) ID, binds the VAO to the current
	 * OpenGL context and returns the generated ID.
	 *
	 * @returns a unique identifier for a newly created vertex array object.
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Generates a vertex buffer object (VBO) to store 3D and 2D data, binds the buffer,
	 * loads the data into it, and configures vertex attribute pointers for OpenGL rendering.
	 *
	 * @param attrib attribute location for the vertex attribute array.
	 *
	 * @param data array of floating-point values to be stored in a vertex buffer object.
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
	 * Generates a buffer object, binds it to the element array buffer target, and uploads
	 * the given `indices` array to it in a static draw mode.
	 *
	 * @param indices array of vertex indices that will be stored in a buffer object.
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Unbinds the current Vertex Array Object (VAO) by setting its binding point to 0,
	 * effectively releasing the current VAO.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
