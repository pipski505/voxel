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
 * Is used to load and manage shaders for use in a 3D graphics pipeline. It provides
 * methods for binding the shader program, uniform floats, and uniform matrices, as
 * well as loading shaders from text files. The class also provides utility methods
 * for getting the location of shader variables in the program.
 */
public class Shader {
	
	private int program;
	
	public Shader(int program) {
		this.program = program;
	}
	
	/**
	 * Glues a program to the currently active context, enabling its use for rendering
	 * and other GPU operations.
	 */
	public void bind() {
		GL20.glUseProgram(program);
	}
	
	/**
	 * Retrieves and returns the value of a field named `program`.
	 * 
	 * @returns the value of the `program` field.
	 */
	public int getProgram() {
		return this.program;
	}
	
	/**
	 * Updates a uniform float value in a shader program based on the length of an array
	 * of float values passed as an argument.
	 * 
	 * @param name name of the uniform location for which the values are being set.
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
	 * Updates a matrix in GL by providing its linear data as a flipped buffer to GL20.glUniformMatrix4.
	 * 
	 * @param name 3D uniform matrix to be uploaded to the GPU.
	 * 
	 * @param mat 4x4 homogeneous transformation matrix that is to be uniformly applied
	 * to the specified location.
	 */
	public void unifromMat4(String name, Matrix4f mat) {
		GL20.glUniformMatrix4(getLoaction(name), false, Util.createFlippedBuffer(mat.getLinearData()));
	}
	
	/**
	 * Retrieves the location of a uniform named `name` within a program's shader module
	 * using the `GL20` class.
	 * 
	 * @param name name of the uniform to locate in the program.
	 * 
	 * @returns an integer representing the location of a uniform in a graphics processing
	 * unit (GPU) program.
	 */
	public int getLoaction(String name) {
		return GL20.glGetUniformLocation(program, name);
	}
	
	private static final String VERT = ".vert", FRAG = ".frag";
	
	/**
	 * Loads a shader program from a file, consisting of a vertex shader and a fragment
	 * shader, and validates it before returning a new Shader object representing the program.
	 * 
	 * @param filename filename of the shader to be loaded.
	 * 
	 * @returns a newly created shader object.
	 */
	public static Shader loadShader(String filename) {
		int program = GL20.glCreateProgram();
		loadShader(GL20.GL_VERTEX_SHADER, getText(filename + VERT), program);
		loadShader(GL20.GL_FRAGMENT_SHADER, getText(filename + FRAG), program);
		validateProgram(program);
		return new Shader(program);
	}
	
	/**
	 * Creates a new shader object using the `glCreateShader` method, loads the source
	 * code into the shader using `glShaderSource`, compiles the shader using `glCompileShader`,
	 * and attaches it to a program using `glAttachShader.
	 * 
	 * @param target type of shader being created, which can be either a vertex shader
	 * or a fragment shader.
	 * 
	 * @param src source code of the shader to be compiled.
	 * 
	 * @param program 3D game engine program that the loaded shader will be attached to
	 * and used by.
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
	 * Validates a program object and logs any errors if they occur.
	 * 
	 * @param program 3D program to be linked and validated by the `validateProgram()` function.
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
	 * Reads the contents of a given file and returns the resulting string.
	 * 
	 * @param file file that contains the text to be read.
	 * 
	 * @returns a string representation of the contents of a given file.
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
