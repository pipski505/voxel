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
 * Is used to create and manage shaders for use in a 3D graphics pipeline. It provides
 * methods for binding a program, getting the program, uniform floats, uniform matrices,
 * and validating the program. The class also provides utilities for loading shader
 * code from files.
 */
public class Shader {
	
	private int program;
	
	public Shader(int program) {
		this.program = program;
	}
	
	/**
	 * Glues a program to the current GL context, enabling its use for rendering.
	 */
	public void bind() {
		GL20.glUseProgram(program);
	}
	
	/**
	 * Returns the value of a field called `program`.
	 * 
	 * @returns an integer representation of the program.
	 */
	public int getProgram() {
		return this.program;
	}
	
	/**
	 * In Java takes a string `name` and an array of floating-point values `vals`. It
	 * applies the corresponding GL uniform function to the value at the specified location.
	 * 
	 * @param name name of the uniform location being modified.
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
	 * Sets a 4x4 matrix to a uniform buffer using the `glUniformMatrix4` method from
	 * OpenGL's GL20 class, passing the matrix data as a flipped buffer.
	 * 
	 * @param name 0-based index of a uniform buffer location for storing a 4x4 matrix.
	 * 
	 * @param mat 4x4 matrix of uniform values that will be uploaded to the GPU as a
	 * unified array through the `GL20.glUniformMatrix4()` method.
	 */
	public void unifromMat4(String name, Matrix4f mat) {
		GL20.glUniformMatrix4(getLoaction(name), false, Util.createFlippedBuffer(mat.getLinearData()));
	}
	
	/**
	 * Retrieves the uniform location of a named uniform in a GL20 program.
	 * 
	 * @param name 0-based index of a uniform location to retrieve in the OpenGL 2.0 program.
	 * 
	 * @returns an integer representing the location of a uniform in a GPU program.
	 */
	public int getLoaction(String name) {
		return GL20.glGetUniformLocation(program, name);
	}
	
	private static final String VERT = ".vert", FRAG = ".frag";
	
	/**
	 * Loads a shader program from a file and validates it, returning a new `Shader`
	 * object representing the program.
	 * 
	 * @param filename name of a shader file that contains the vertex and fragment shaders
	 * to be loaded by the `loadShader()` function.
	 * 
	 * @returns a new `Shader` object representing a compiled shader program.
	 */
	public static Shader loadShader(String filename) {
		int program = GL20.glCreateProgram();
		loadShader(GL20.GL_VERTEX_SHADER, getText(filename + VERT), program);
		loadShader(GL20.GL_FRAGMENT_SHADER, getText(filename + FRAG), program);
		validateProgram(program);
		return new Shader(program);
	}
	
	/**
	 * Creates a shader program and loads a shader source code into it. It compiles the
	 * shader and attaches it to the program. If compilation fails, an error message is
	 * printed and the program exits.
	 * 
	 * @param target type of shader being created, which determines the shader's functionality
	 * and is one of the four values defined by the GL20 class.
	 * 
	 * @param src 1:1 representation of the source code for the shader, which is passed
	 * as a string to the `GL20.glShaderSource()` method for compilation.
	 * 
	 * @param program 3D game engine program that the shader will be attached to after
	 * being compiled.
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
	 * Links and validates a GPU program with the given ID, checking for errors and exiting
	 * the application if any are found.
	 * 
	 * @param program 3D graphics program to be validated and linked, which is passed
	 * through the `glLinkProgram()` and `glValidateProgram()` functions for verification
	 * and linking.
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
	 * Reads the contents of a given file as a string, handling potential exceptions gracefully.
	 * 
	 * @param file file whose contents are to be read and returned as a string.
	 * 
	 * @returns a string representation of the contents of the specified file.
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
