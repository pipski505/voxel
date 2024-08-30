package com.ch;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.ch.math.Vector3f;
import com.ch.voxel.World;

/**
 * Initializes the display and OpenGL settings in initDisplay and initGL respectively.
 * The loop method contains the game's main rendering loop that continuously updates
 * and renders the scene until the user closes the window or presses the escape key.
 */
public class Main {
	
	/**
	 * Initializes a display, sets up OpenGL, enters an application loop, and exits with
	 * a status code of 0 upon completion of the loop. It is the entry point for the
	 * program's execution sequence. The loop likely contains the main application logic.
	 *
	 * @param args command-line arguments passed to the Java application when it is
	 * executed, allowing for external input and configuration.
	 */
	public static void main(String[] args) {
		
		initDisplay();
		initGL();
		loop();
		exit(0);
		
	}
	
	private static Model m;
	private static Shader s;
	private static Texture t;
	private static Camera3D c;
//	private static Chunk[][][] ch;
	private static World w;
	
	/**
	 * Initializes a display mode with resolution 1920x1080, creates a pixel format and
	 * context with forward-compatible and profile-core enabled attributes, enables VSync,
	 * and prints the OpenGL version to the console. It catches LWJGL exceptions and
	 * prints their stack traces if they occur.
	 */
	private static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(1920, 1080));
			Display.create(new PixelFormat(), new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true));
			Display.setVSyncEnabled(true);
			System.out.println(GL11.glGetString(GL11.GL_VERSION));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes various OpenGL settings and scene elements. It sets clear color, enables
	 * face culling and depth testing, loads a shader and texture, and creates camera,
	 * world, and model objects with default properties. The function prepares the
	 * environment for rendering.
	 */
	private static void initGL() {
		
		GL11.glClearColor(0.1f, 0.7f, 1f, 1);
		
		Mouse.setGrabbed(true);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		c = new Camera3D(70, 16.f/9, .03f, 1000);
		
		s = Shader.loadShader("res/shaders/default");
		
		t = new Texture("res/textures/block0.png");
		
		float[] vertices = {
			-.5f, -.5f, 0,
			-.5f,  .5f, 0,
			 .5f,  .5f, 0,
			 .5f, -.5f, 0,
			
		};
		int[] indices = {
				0, 1, 2, 0, 2, 3
		};
//		ch = new Chunk[4][4][4];
//		for (int i = 0; i < 4; i++)
//			for (int j = 0; j < 4; j++)
//				for (int k = 0; k < 4; k++) {
//					ch[i][j][k] = new Chunk(i, j, k);
//					ch[i][j][k].updateBlocks();
//					ch[i][j][k].genModel();
//				}
		w = new World();
		//m = c.genModel();//Model.load(vertices, indices);
		
		c.getTransform().setPos(new Vector3f(0, 0, 0));
		
	}
	
	/**
	 * Runs a continuous game loop, updating and rendering graphics while checking for
	 * user input to terminate or close the display. The loop continues until the user
	 * closes the display or presses the escape key. It displays the current FPS and
	 * memory usage on the title bar.
	 */
	private static void loop() {
		
		Timer.init();
		
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			
			Timer.update();
			
			Display.setTitle("" + Timer.getFPS() + 
					/* "   " + c.getTransform().getPos().toString() +*/ "   " 
					+ ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + " of " + (Runtime.getRuntime().maxMemory() / 1048576));
			
			update(Timer.getDelta());
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			render();
			
			Display.update();
			
		}
		
	}
	
	/**
	 * Processes input and updates an object's position using a transformation.
	 * Input is processed with a delay and smoothing factor by calling `c.processInput`.
	 * Position update occurs through a call to `w.updatePos`, passing current transform
	 * coordinates.
	 *
	 * @param dt time delta, or the elapsed time since the last update, used to scale
	 * game logic and physics updates.
	 */
	private static void update(float dt) {
		c.processInput(dt, 5, .3f);
		w.updatePos(c.getTransform().getPos().getX(), c.getTransform().getPos().getY(), c.getTransform().getPos().getZ());
	}

	/**
	 * Binds a shader resource and renders objects using a worker object, passing it a
	 * shader and a camera to draw models with their view-projection matrices applied.
	 * The commented-out code previously rendered a 3D cube by generating multiple colors
	 * and model matrices.
	 */
	private static void render() {
		
//		Model.enableAttribs();
		
		s.bind();
//		for (int i = 0; i < 4; i++)
//			for (int j = 0; j < 4; j++)
//				for (int k = 0; k < 4; k++) {
//					float r = (4 - i) / 4f;
//					float g = j / 4f;
//					float b = k / 4f;
//					s.uniformf("color", r, g, b);
//					s.unifromMat4("MVP", (c.getViewProjection().mul(ch[i][j][k].getModelMatrix())));
//					ch[i][j][k].getModel().draw();
//				}
		
		w.render(s, c);
		
//		Model.disableAttribs();
	}
	
	/**
	 * Terminates the Java application with a specified exit status using the `System.exit`
	 * method. The status code is passed as an argument to indicate the reason for
	 * termination. This allows for controlled program shutdown with error codes.
	 *
	 * @param status 16-bit integer value that is passed to System.exit(), indicating the
	 * program's termination status, with lower values typically representing successful
	 * execution and higher values signifying errors or exceptions.
	 */
	private static void exit(int status) {
		System.exit(status);
	}
}
