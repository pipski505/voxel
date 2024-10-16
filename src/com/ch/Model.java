package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Handles the rendering of 3D models by creating and managing OpenGL vertex arrays
 * and buffers. It provides a method to load and draw models from vertex and index
 * data. The class also includes utility methods for enabling and disabling vertex
 * attribute access.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object, enables vertex attribute arrays, draws triangles using
	 * vertex indices, and then disables the vertex attribute arrays and unbinds the
	 * vertex array object.
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
	 * Enables two vertex attribute arrays with indices 0 and 1 for OpenGL 2.0. This
	 * allows the GPU to process vertex data for these attributes. The attributes are
	 * likely used for vertex positions and colors.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables the vertex attribute arrays at indices 0 and 1.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns an integer value representing a Vertex Array Object (VAO). The VAO is a
	 * resource used in graphics rendering to manage vertex buffer data.
	 *
	 * @returns the value of the `vao` variable.
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns the value of the `size` variable.
	 * This variable stores a dimension or quantity.
	 * The function provides a getter method for accessing the size.
	 *
	 * @returns the value of the instance variable `size`.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Loads 3D model data into the graphics system. It creates a VAO, stores vertex data
	 * and indices, and returns a model object with the VAO ID and vertex count.
	 *
	 * @param vertices array of vertex data to be stored in the vertex array object.
	 *
	 * @param indices array of integers used to specify the order of vertices to be drawn.
	 *
	 * @returns an instance of the `Model` class, containing a VAO ID and vertex count.
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
	 * Generates a unique Vertex Array Object (VAO) ID, binds it to the current OpenGL
	 * context, and returns the generated ID.
	 *
	 * @returns a unique identifier for a generated Vertex Array Object (VAO).
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Generates a buffer object for storing vertex data, uploads the data to the buffer,
	 * and specifies how the data should be interpreted by the graphics pipeline.
	 *
	 * @param attrib attribute index for the vertex attribute array in OpenGL.
	 *
	 * @param data array of floating-point values that are used to populate a vertex
	 * buffer object, which stores the data for rendering.
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
	 * Generates a buffer object, binds it to the element array buffer, and stores the
	 * provided indices in the buffer using static draw mode.
	 *
	 * @param indices array of indices to be stored in a buffer object for rendering purposes.
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Binds the default vertex array object (VAO) and sets it to 0, effectively unbinding
	 * the current VAO.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
