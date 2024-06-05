package com.ch;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

/**
 * Is used to handle textures in a 3D graphics application. It allows for loading and
 * binding textures, as well as getting the ID of the loaded texture. The loadTexture
 * method takes a file path as an argument and returns the ID of the loaded texture.
 */
public class Texture {

	private int id;
	private String fileName;

	public Texture(String fileName) {
		this.fileName = fileName;
		this.id = loadTexture(fileName);
	}


	/**
	 * 0 is called and performs an action related to binding.
	 */
	public void bind() {
		bind(0);
	}

	/**
	 * Sets the active texture slot in the GPU to a specified index within a range of 0
	 * to 31, binding a texture to that slot.
	 * 
	 * @param samplerSlot 0-based index of a texture slot within the current active texture
	 * unit, ranging from 0 to 31.
	 */
	public void bind(int samplerSlot) {
		assert (samplerSlot >= 0 && samplerSlot <= 31);
		glActiveTexture(GL_TEXTURE0 + samplerSlot);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	/**
	 * Retrieves and returns the value of a field called `id`.
	 * 
	 * @returns an integer representing the ID of the object.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Loads a 2D texture from a file, generates mipmaps, and returns the ID of the created
	 * texture.
	 * 
	 * @param fileName file path of the texture to load.
	 * 
	 * @returns an integer identifier of a generated texture.
	 * 
	 * 	- The output is an integer, which represents the ID of the generated texture.
	 * 	- The texture is of type 2D and has an RGBA8 internal format.
	 * 	- The texture size is specified by the width and height parameters, which are
	 * obtained from the input file name.
	 * 	- The texture has a linear filter and is repeat-enabled in both the x and y directions.
	 * 	- A mipmap is generated using the `GLGenerateMipmap` function.
	 */
	private static int loadTexture(String fileName) {
		try {
			BufferedImage image = ImageIO.read(new File(fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			boolean hasAlpha = image.getColorModel().hasAlpha();

			for (int y = image.getHeight() - 1; y >= 0; y--) {
				for (int x = 0; x < image.getWidth(); x++) {
					int pixel = pixels[y * image.getWidth() + x];

					buffer.put((byte) ((pixel >> 16) & 0xFF));
					buffer.put((byte) ((pixel >> 8) & 0xFF));
					buffer.put((byte) ((pixel) & 0xFF));
					if (hasAlpha)
						buffer.put((byte) ((pixel >> 24) & 0xFF));
					else
						buffer.put((byte) (0xFF));
				}
			}

			buffer.flip();

			int id = GL11.glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, id);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -.6f);
			
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return 0;
	}

}
