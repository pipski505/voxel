package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Is used for rendering 3D models in a graphics application. It takes care of binding
 * vertex arrays and storing data and indices for rendering. The class provides methods
 * to enable and disable vertex attribs, get the VAO ID and size, and load data from
 * an array and indices for rendering.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object, enables vertex attributes, draws a collection of
	 * triangles using `glDrawElements`, and disables the vertex attributes again.
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
	 * Enables two vertex attributes in a 3D graphics context using OpenGL API.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables vertex attribute arrays for both attributes, GL_VERTEX_ARRAY_BUFFER and
	 * GL_NORMAL_ARRAY_BUFFER, using `glDisableVertexAttribArray`.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns the value of the variable `vao`.
	 * 
	 * @returns the value of the `vao` field, which is an integer.
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
	 * Loads a 3D model into memory by creating a Vertex Array Object (VAO), storing index
	 * data, and then binding the VAO to the GPU for rendering.
	 * 
	 * @param vertices 3D model's geometry as a set of floating-point coordinates, which
	 * are stored in an array and passed to the `storeData()` method for storage in the
	 * Vertex Array Object (VAO).
	 * 
	 * @param indices 3D model's vertex array object (VAO) indices, which are used to
	 * bind the model's vertices and indices to the appropriate buffers for rendering.
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
	 * Creates a new vertex array object (VAO) and binds it to the current GL context,
	 * allowing for efficient rendering of 3D models.
	 * 
	 * @returns an integer value representing a valid vertex array object (VAO) handle.
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Generates a vertex buffer object (VBO), binds it, and stores data in it using the
	 * `GL15.glBufferData()` method. It then sets up vertex attributers for the data.
	 * 
	 * @param attrib 2D vertex attribute index that contains the data to be stored in the
	 * VBO.
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
	 * Generates a new buffer for storing indices, binds it, and stores the input array
	 * of indices in it using the `GL15.glBufferData()` method.
	 * 
	 * @param indices 3D model's indices that are to be stored in the element array buffer.
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Disables the Vertex Array Object (VAO) bound to it by passing a value of 0 to the
	 * `glBindVertexArray` method.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
