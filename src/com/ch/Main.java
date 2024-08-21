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
 * Initializes LWJGL for rendering and sets up display, graphics, camera, shader,
 * texture, and world objects. It then enters a loop where it updates the game state,
 * clears the screen, renders the scene, and updates the display title with frame
 * rate and memory information. The class handles window close requests, keyboard
 * input, and exits the program when requested.
 */
public class Main {
	
	/**
	 * Initializes display and OpenGL, then enters an infinite loop before terminating
	 * with exit code 0. It is the entry point of the program, responsible for setting
	 * up the environment and running the application's main logic.
	 *
	 * @param args command-line arguments passed to the program when it is launched.
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
	 * Initializes a display mode with specified resolution, creates a display using
	 * PixelFormat and ContextAttribs, enables vertical synchronization, and prints the
	 * OpenGL version. It catches and prints any exceptions that occur during initialization.
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
	 * Initializes various OpenGL settings and objects. It sets clear color, enables
	 * culling face and depth testing, loads shaders, textures, and creates a camera
	 * object. It also initializes a world object but does not generate its model.
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
	 * Initializes a timer and enters an infinite loop, updating the display's title with
	 * frame rate, memory usage, and other information. It continuously updates the game
	 * state, renders graphics, and clears buffers until the user requests window closure
	 * or presses the Escape key.
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
	 * Updates the position and input processing for an object `w`. It processes user
	 * input using the `processInput` method of a controller `c`, and then updates the
	 * position of `w` based on the current transformation position from `c`.
	 *
	 * @param dt 3D time delta, which is used to process user input and update the object's
	 * position by the camera class.
	 */
	private static void update(float dt) {
		c.processInput(dt, 5, .3f);
		w.updatePos(c.getTransform().getPos().getX(), c.getTransform().getPos().getY(), c.getTransform().getPos().getZ());
	}

	/**
	 * Binds a shader and uniforms its color and MVP (model-view-projection) matrices.
	 * It then renders several objects using these settings, specifically 4x4 chunks of
	 * a model, by calling their `draw` method through an array of objects (`ch`). Finally,
	 * it calls the `render` method of another object (`w`).
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
	 * Terminates the Java Virtual Machine (JVM) with a specified exit status. It initiates
	 * an orderly shutdown by propagating any uncaught exceptions to the thread that
	 * invoked it, and then terminates the JVM using the system's `System.exit` method.
	 *
	 * @param status 16-bit integer value that indicates the termination status of the
	 * Java application.
	 */
	private static void exit(int status) {
		System.exit(status);
	}
}
