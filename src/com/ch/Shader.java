package com.ch;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL20;

import com.ch.math.Matrix4f;

/**
 * Handles OpenGL shader program creation, linking, and validation, providing methods
 * for uniform variable setting and shader program binding.
 */
public class Shader {
	
	private int program;
	
	public Shader(int program) {
		this.program = program;
	}
	
	/**
	 * Binds a graphics program to the OpenGL rendering context.
	 */
	public void bind() {
		GL20.glUseProgram(program);
	}
	
	/**
	 * Exposes the `program` field, allowing it to be accessed externally. It returns the
	 * current value of `program` without modifying it.
	 *
	 * @returns the value of the `program` field.
	 */
	public int getProgram() {
		return this.program;
	}
	
	/**
	 * Sets a uniform floating-point value or vector in a shader program based on the
	 * number of values provided, using OpenGL's `glUniform1f`, `glUniform2f`, `glUniform3f`,
	 * or `glUniform4f` functions. It uses a switch statement to determine the correct
	 * function to call based on the number of values.
	 *
	 * @param name location name of the uniform variable to be set.
	 *
	 * @param vals variable number of float values to be passed to the glUniform function,
	 * with its length determining the type of uniform to be set.
	 *
	 * Destructure `vals` into an array of floats with variable length, allowing for a
	 * dynamic number of parameters. Its main properties are a float array with a minimum
	 * length of 1 and a maximum length of 4.
	 */
	public void uniformf(String name, float ...vals) {
		switch (vals.length) {
		case 1:
			GL20.glUniform1f(getLoaction(name), vals[0]);
			break;
		case 2:
			GL20.glUniform2f(getLoaction(name), vals[0], vals[1]);
			break;
		case 3:
			GL20.glUniform3f(getLoaction(name), vals[0], vals[1], vals[2]);
			break;
		case 4:
			GL20.glUniform4f(getLoaction(name), vals[0], vals[1], vals[2], vals[3]);
			break;
		}
	}
	
	/**
	 * Uploads a 4x4 matrix to a uniform location in the OpenGL graphics pipeline. It
	 * takes a uniform location and a 4x4 matrix as input, and uses a utility function
	 * to create a flipped buffer from the matrix data.
	 *
	 * @param name uniform name in the shader program to which the matrix is being bound.
	 *
	 * @param mat matrix data to be passed to the shader.
	 */
	public void unifromMat4(String name, Matrix4f mat) {
		GL20.glUniformMatrix4(getLoaction(name), false, Util.createFlippedBuffer(mat.getLinearData()));
	}
	
	/**
	 * Returns the location of a uniform variable in a shader program. It takes the name
	 * of the variable as a string parameter. The location is retrieved from the OpenGL
	 * context using `GL20.glGetUniformLocation`.
	 *
	 * @param name name of a uniform variable in a shader program.
	 *
	 * @returns a uniform location index of the specified variable in the OpenGL shader
	 * program.
	 */
	public int getLoaction(String name) {
		return GL20.glGetUniformLocation(program, name);
	}
	
	private static final String VERT = ".vert", FRAG = ".frag";
	
	/**
	 * Loads a shader program from a given file, creating a new program, loading vertex
	 * and fragment shaders, and validating the program.
	 *
	 * @param filename file path to a shader file from which the contents are retrieved
	 * using the `getText` method.
	 *
	 * @returns an instance of the `Shader` class.
	 */
	public static Shader loadShader(String filename) {
		int program = GL20.glCreateProgram();
		loadShader(GL20.GL_VERTEX_SHADER, getText(filename + VERT), program);
		loadShader(GL20.GL_FRAGMENT_SHADER, getText(filename + FRAG), program);
		validateProgram(program);
		return new Shader(program);
	}
	
	/**
	 * Compiles a shader from a given source string, checks for compilation errors, and
	 * attaches the compiled shader to a program. It handles compilation failures by
	 * printing the error log and exiting the program.
	 *
	 * @param target type of shader being created, such as GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
	 *
	 * @param src source code of the shader.
	 *
	 * @param program OpenGL program to which the shader is attached.
	 */
	private static void loadShader(int target, String src, int program) {
		int shader = GL20.glCreateShader(target);
		
		GL20.glShaderSource(shader, src);
		GL20.glCompileShader(shader);
		
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		GL20.glAttachShader(program, shader);
	}
	
	/**
	 * Links and validates an OpenGL program. It checks for linking errors and displays
	 * the error log if any. If the program is valid, it also checks for validation errors
	 * and displays the error log if any.
	 *
	 * @param program OpenGL program object being validated and linked.
	 */
	private static void validateProgram(int program) {
		GL20.glLinkProgram(program);
		
		if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
		
		GL20.glValidateProgram(program);
		
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}
	
	/**
	 * Reads a text file specified by the `file` parameter,
	 * converts its contents to a string, and
	 * returns the string.
	 *
	 * @param file file path to be read and processed by the function.
	 *
	 * @returns a string containing the text from the specified file.
	 *
	 * The output is a string containing the text from the specified file.
	 * The string is null-terminated, with the last character being a null byte.
	 */
	private static String getText(String file) {
		String text = "";
		try {
			InputStream is = new FileInputStream(file);
			int ch;
			while ((ch = is.read()) != -1)
				text += (char) ch;
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return text;
	}

}
