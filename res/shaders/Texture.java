package com.ch.game;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
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

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Is used to handle texture mapping in OpenGL. It provides a simple way to load and
 * bind textures to OpenGL surfaces. The class takes a filename as a constructor
 * parameter and loads the texture data from the file using ImageIO. The loaded texture
 * data is then bound to an OpenGL surface using the `bind()` method. The `getID()`
 * method returns the ID of the loaded texture.
 */
public class Texture {
	
	private String fileName;
	private int id;

	public Texture(String fileName) {
		this.fileName = fileName;
		this.id = Texture.loadTexture(fileName);
	}

	/**
	 * Is a hook that allows for custom cleanup code to be executed when an object is
	 * about to be garbage collected.
	 */
	@Override
	protected void finalize() {
	}

	/**
	 * 0 is used to bind a listener to a particular event.
	 */
	public void bind() {
		bind(0);
	}

	/**
	 * Sets the active texture slot to a specific value (0-31) and binds a texture ID to
	 * that slot using the `glBindTexture()` method.
	 * 
	 * @param samplerSlot 0-based index of a texture slot to bind, with a range of 0 to
	 * 31.
	 */
	public void bind(int samplerSlot) {
		assert (samplerSlot >= 0 && samplerSlot <= 31);
		glActiveTexture(GL_TEXTURE0 + samplerSlot);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	/**
	 * Returns the `id` variable's value.
	 * 
	 * @returns an integer representing the value of the `id` field.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Loads a 2D texture from an image file and stores it in a GL texture object,
	 * optionally generating mipmaps.
	 * 
	 * @param fileName file path of the texture image to load.
	 * 
	 * @returns an integer identifier of a texture object created from an image file.
	 */
	private static int loadTexture(String fileName) {
		try {
			BufferedImage image = ImageIO.read(new File(fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			boolean hasAlpha = image.getColorModel().hasAlpha();

			for (int y = 0; y < image.getHeight(); y++) {
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

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//			GL30.glGenerateMipmap(GL_TEXTURE_2D);
//			GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			
//			if (GL11.glE)
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

			return id;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return 0;
	}

	
}
