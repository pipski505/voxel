package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Handles OpenGL vertex array object (VAO) creation and rendering of 3D models.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Renders a 3D object by binding a vertex array object, enabling vertex attribute
	 * arrays, drawing triangles using indices, and then disabling the arrays and unbinding
	 * the vertex array object.
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
	 * Enables two vertex attribute arrays, indexed as 0 and 1, for use in OpenGL rendering.
	 * This is typically done to set up vertex data for rendering 3D objects.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables the vertex attribute arrays 0 and 1.
	 * This is typically done when rendering is complete or when switching to a different
	 * rendering mode.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns the value of the `vao` variable, likely a vertex array object in a graphics
	 * context.
	 *
	 * @returns the value of the `vao` variable.
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns an integer representing the size value.
	 * The size is presumably a class field accessible within the function.
	 *
	 * @returns the integer value of the instance variable `size`.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Loads and initializes a 3D model by creating a Vertex Array Object (VAO), storing
	 * vertex data and indices, and returning a new model instance. It takes float arrays
	 * for vertices and int arrays for indices as input.
	 *
	 * @param vertices 3D vertex data of the model, stored in a float array.
	 *
	 * @param indices array of indices that specify the order in which the vertices are
	 * connected to form the model's geometry.
	 *
	 * @returns a `Model` object containing the VAO ID and the number of vertices.
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
	 * context, and returns the generated ID. It enables the creation of a VAO that can
	 * store vertex buffer object (VBO) settings, vertex attribute settings, and other state.
	 *
	 * @returns a unique identifier for a newly generated Vertex Array Object (VAO).
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Stores vertex data in a buffer object, specifying attributes for vertex position
	 * and texture coordinates, and sets up vertex attribute pointers for OpenGL rendering.
	 * It uses a static buffer for storing data. The data is stored in a static draw mode.
	 *
	 * @param attrib attribute location in the vertex attribute array, used to specify
	 * the location of the attribute in the vertex buffer object (VBO).
	 *
	 * @param data data array to be stored in a vertex buffer object (VBO) in OpenGL.
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
	 * Generates a buffer object, binds it as an element array buffer, and uploads the
	 * provided indices to it in a static draw mode.
	 *
	 * @param indices array of vertex indices to be stored in a buffer object.
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Binds the default vertex array object (VAO), effectively unbinding any previously
	 * bound VAO. This allows other VAOs to be bound without interference. The default
	 * VAO is identified by a value of 0.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
