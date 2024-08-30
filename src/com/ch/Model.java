package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Is designed to manage OpenGL vertex data and facilitate rendering of 3D models.
 * It provides functionality for loading and drawing model data using LWJGL libraries.
 * The class includes features for storing vertex arrays, indices, and attribute data
 * in OpenGL buffers.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Binds a vertex array object (VAO), enables two attribute arrays, draws specified
	 * triangles using indices, and then disables the attribute arrays and unbinds the
	 * VAO. It uses OpenGL commands to render the scene. The method ends immediately after
	 * rendering.
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
	 * Enables attribute arrays on the GPU for two attributes, indexed as 0 and 1. This
	 * prepares the data for vertex processing by telling the graphics driver to use these
	 * attributes for rendering. The function modifies the OpenGL state.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables attribute arrays for two vertex attributes, indicated by indices 0 and
	 * 1, using OpenGL's GLSL shading language. This effectively stops the client from
	 * sending data to these attributes until enabled again. The disabling is done via `glDisableVertexAttribArray`.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns an integer value representing a Virtual Array Object (VAO). It appears to
	 * be a getter method, allowing access to the stored VAO. The value is presumably set
	 * elsewhere and retrieved by calling this method.
	 *
	 * @returns an integer representing a vertex array object (VAO) value.
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns an integer representing the current size value stored in the instance
	 * variable `size`. This variable is assumed to be a private member of the class and
	 * is accessed publicly through this getter method. The returned size value can be
	 * used externally for various purposes.
	 *
	 * @returns an integer value representing the current object's size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Creates a new 3D model by binding vertex array data to a VAO and storing vertex
	 * data and indices for rendering. It returns an instance of the Model class with a
	 * unique VAO ID and index count. The VAO is bound throughout the process.
	 *
	 * @param vertices 3D vertex coordinates to be stored in a Vertex Buffer Object (VBO).
	 *
	 * @param indices 1D array of integers that define how to combine vertices from the
	 * `vertices` array into triangles or other primitives for rendering.
	 *
	 * @returns an instance of the `Model` class.
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
	 * Generates a unique OpenGL vertex array object (VAO) ID using `glGenVertexArrays`,
	 * binds it, and returns its ID for further usage. This creates a container to hold
	 * vertex buffer objects (VBOs) and enables efficient rendering of graphics data.
	 *
	 * @returns a unique identifier for a Vertex Array Object.
	 * Allocated VAO ID is returned, which can be used to manage vertex attributes and buffers.
	 * This ID is a positive integer.
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Stores a float array in an OpenGL buffer object, specifying its format and usage
	 * type for rendering purposes. It generates a new VBO, binds it, loads the data into
	 * it, and sets up two attribute pointers for vertex attributes.
	 *
	 * @param attrib 1-based index of an attribute (a vertex component) in the vertex
	 * shader, used to specify which data array corresponds to each attribute.
	 *
	 * @param data 2D array of floating-point values to be stored in an OpenGL buffer object.
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
	 * Generates a buffer object, binds it as an element array buffer, and copies the
	 * provided indices into it. The data is stored in static draw mode for efficient display.
	 *
	 * @param indices 1D array of vertex indices that are being stored as an element
	 * buffer object (EBO).
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Unbinds a vertex array object (VAO) from the current OpenGL context, restoring the
	 * default VAO binding to zero. This allows other VAOs or objects to be used without
	 * interference. The VAO is essentially disconnected from the graphics pipeline.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
