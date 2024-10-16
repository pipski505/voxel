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
 * Handles the creation, management, and usage of OpenGL shaders, providing a convenient
 * interface for loading and binding shader programs.
 */
public class Shader {
	
	private int program;
	
	public Shader(int program) {
		this.program = program;
	}
	
	/**
	 * Binds the specified program to the current OpenGL context, making it the active
	 * program for rendering.
	 */
	public void bind() {
		GL20.glUseProgram(program);
	}
	
	/**
	 * Returns the value of the program field, allowing access to its contents.
	 *
	 * @returns the value of the `program` field, which is an integer.
	 */
	public int getProgram() {
		return this.program;
	}
	
	/**
	 * Unifies and sends float values to the GPU. It handles arrays of different lengths,
	 * calling the corresponding glUniform function to set a uniform variable.
	 *
	 * @param name location in the OpenGL uniform buffer where the values will be stored.
	 *
	 * @param vals variable number of float values to be passed to the `uniformf` function
	 * for setting a uniform variable in OpenGL.
	 *
	 * Destructure `vals` into an array of floats with a variable length, enabling it to
	 * represent vectors of varying dimensions.
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
	 * Uploads a 4x4 matrix to a uniform location in the OpenGL context.
	 *
	 * @param name location of the uniform matrix in the shader program.
	 *
	 * @param mat 4x4 matrix whose data is to be passed to the GPU.
	 */
	public void unifromMat4(String name, Matrix4f mat) {
		GL20.glUniformMatrix4(getLoaction(name), false, Util.createFlippedBuffer(mat.getLinearData()));
	}
	
	/**
	 * Retrieves the uniform location of a given variable in the OpenGL program. It takes
	 * a string name as input and returns the corresponding uniform location as an integer.
	 * The location is used to set the uniform value.
	 *
	 * @param name name of a uniform variable in the shader program.
	 *
	 * @returns the uniform location of a specified shader program variable.
	 */
	public int getLoaction(String name) {
		return GL20.glGetUniformLocation(program, name);
	}
	
	private static final String VERT = ".vert", FRAG = ".frag";
	
	/**
	 * Loads a shader program from a file, specifying a vertex shader and a fragment
	 * shader. It creates a program, loads the shaders, validates the program, and returns
	 * a new `Shader` object.
	 *
	 * @param filename file name of a shader file, combining it with vertex and fragment
	 * file extensions to load the corresponding shader code.
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
	 * Compiles a shader from a given source string and attaches it to a shader program.
	 * It checks for compilation errors and terminates the program if any occur. The
	 * compiled shader is then attached to the specified program.
	 *
	 * @param target type of shader to be created, such as GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
	 *
	 * @param src source code of the shader.
	 *
	 * @param program identifier of the shader program to which the newly created shader
	 * is attached.
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
	 * Links, validates and checks the status of an OpenGL program. If the program fails
	 * to link or validate, it prints the error log and exits the program with a status
	 * code of 1.
	 *
	 * @param program OpenGL program to be validated.
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
	 * Reads the contents of a specified file, converts it to a string, and returns the
	 * text. It uses a FileInputStream to read the file byte by byte, casting each byte
	 * to a character and appending it to a string.
	 *
	 * @param file path to the file from which text content is to be read.
	 *
	 * @returns the contents of the specified file as a string.
	 *
	 * The output is a string containing the text from the specified file.
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
