package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Is used for storing and manipulating data in a 3D model. It can load vertices and
 * indices from an array and enable vertex attribs. It also has methods to draw the
 * model, store data, and disable attribs. Additionally, it provides a way to get the
 * VAO (VerteX Array Object) handle and size of the model.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object, enables vertex attributes, and draws triangles using
	 * either glDrawArrays or glDrawElements depending on the number of vertices provided.
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
	 * Enables vertex attributes 0 and 1 for rendering.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables vertex attribute arrays 0 and 1 for the currently bound graphics context.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns the value of a variable `vao`.
	 * 
	 * @returns an integer value representing the VAO.
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns the current size of an object's storage.
	 * 
	 * @returns the value of the `size` field.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Loads a 3D model from an array of vertices and an array of indices, creates a
	 * Vertex Array Object (VAO), stores the data, binds the VAO, and returns a `Model`
	 * object representing the loaded model.
	 * 
	 * @param vertices 3D vertex data of the model to be loaded.
	 * 
	 * @param indices 3D coordinates of the vertices in the model, which are used to bind
	 * the vertices to the corresponding indices in the vertex array object (VAO) during
	 * rendering.
	 * 
	 * @returns a `Model` object representing the loaded 3D model.
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
	 * Generates a new vertex array object (VAO) using `glGenVertexArrays()` and binds
	 * it to the current context using `glBindVertexArray()`.
	 * 
	 * @returns an integer value representing a valid vertex array object (VBO) handle.
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * - Creates a vertex buffer object (VBO) using `glGenBuffers()`.
	 * 	- Binds the VBO to the GPU using `glBindBuffer()`.
	 * 	- Stores the input data as a float array in the VBO using `glBufferData()`.
	 * 
	 * @param attrib 2D vertex attribute index that stores the data in the VBO.
	 * 
	 * @param data 3D data to be stored in the vertex buffer object (VBO).
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
	 * Stores an array of indices into a buffer for static draw calls.
	 * 
	 * @param indices 3D vertex positions to be stored in an element array buffer for
	 * rendering purposes.
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Disconnects a vertex array object (VAO) from its current binding in the GPU.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
