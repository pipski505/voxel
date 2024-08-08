package com.ch;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Is used for rendering 3D models in an OpenGL context. It manages vertex array
 * objects (VAOs) and buffers for storing vertex data and indices. The class provides
 * methods for loading model data, enabling/disabling attributes, drawing the model,
 * and accessing VAO and size properties.
 */
public class Model {

	private int vao, size;
	
	public Model(int vao, int count) {
		this.vao = vao;
		this.size = count;
	}
	
	/**
	 * Renders a graphical object with vertices and indices stored in the vertex array
	 * object (VAO). It binds the VAO, enables vertex attributes, draws triangles using
	 * indices, disables vertex attributes, and unbinds the VAO to prepare for further
	 * rendering operations.
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
	 * Enables attribute arrays for vertex shader programs. It specifies two attribute
	 * arrays with indices 0 and 1, indicating that these attributes should be enabled
	 * for rendering.
	 */
	public static void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Disables vertex attribute arrays for indices 0 and 1 using OpenGL API calls to GL20.
	 */
	public static void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Returns an integer value representing a vertex array object (VAO). The VAO is
	 * presumably used for rendering graphics, and its returned value can be accessed or
	 * utilized by other parts of the program.
	 *
	 * @returns an integer value representing a Vertex Array Object (VAO).
	 */
	public int getVAO() {
		return vao;
	}
	
	/**
	 * Returns the value of the `size` variable. It does not perform any calculations or
	 * modifications to external variables, but simply retrieves and exposes the current
	 * state of the `size`. The returned value is an integer representing the size.
	 *
	 * @returns an integer representing the value of the variable `size`.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Initializes a Model object by creating a Vertex Array Object (VAO) and storing
	 * vertex data and indices. It then unbinds the VAO before returning the new Model
	 * instance with its unique identifier and index count.
	 *
	 * @param vertices 3D vertex data to be stored and used for rendering a model.
	 *
	 * @param indices 1D array of indices that define the order in which vertices are
	 * used to render geometric shapes.
	 *
	 * @returns an instance of the `Model` class with specified VAO and vertex count.
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
	 * Generates a unique identifier for a vertex array object using `GL30.glGenVertexArrays()`,
	 * then binds it to the current context with `GL30.glBindVertexArray()`. It returns
	 * the generated identifier, enabling the creation of vertex arrays for rendering graphics.
	 *
	 * @returns an integer representing a generated vertex array object.
	 */
	private static int createVAO() {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	/**
	 * Creates a buffer object to store data and binds it to the OpenGL context. It then
	 * loads the data into the buffer using `glBufferData`. The function also sets up
	 * vertex attributes for the stored data, specifying their layout and type.
	 *
	 * @param attrib attribute index that corresponds to the vertex attribute array being
	 * configured for vertex buffer object (VBO) data storage.
	 *
	 * @param data 1D array of floating-point values to be stored in a buffer object and
	 * subsequently used for rendering purposes.
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
	 * Generates a buffer object, binds it to the element array buffer target, and loads
	 * the specified indices into the buffer using static draw mode.
	 *
	 * @param indices 1D array of indices that are used to store the vertex buffer object
	 * (VBO) on the graphics processing unit (GPU).
	 */
	private static void storeIndices(int[] indices) {
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Binds a vertex array object (VAO) to index 0, effectively releasing any previously
	 * bound VAO and making it unavailable for rendering operations until another VAO is
	 * bound. This allows for efficient management of multiple VAOs in OpenGL applications.
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
}
