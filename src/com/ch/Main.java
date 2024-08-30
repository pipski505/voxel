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
 * Initializes and sets up display and OpenGL settings, then enters a main loop where
 * it updates game state and renders graphics continuously until user requests exit.
 * It handles input and displays performance metrics on the window title bar.
 */
public class Main {
	
	/**
	 * Initializes display and OpenGL, then enters a loop before exiting the program with
	 * code 0. It sets up the necessary components for a graphics application to run
	 * smoothly before terminating normally. The loop is likely where game logic or
	 * rendering occurs.
	 *
	 * @param args command line arguments passed to the Java application, but it is not
	 * used in this code snippet as its value is ignored by the main method.
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
	 * Initializes display settings for a Java application, setting the screen resolution
	 * to 1920x1080 and enabling vertical synchronization (VSync). It also creates an
	 * OpenGL context with specific attributes and prints the OpenGL version to the
	 * console. The function catches any LWJGL exceptions that may occur during initialization.
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
	 * Initializes OpenGL settings and loads various components for a 3D application. It
	 * sets clear color, enables culling and depth testing, creates a camera, shader,
	 * texture, and world object, and generates initial positions for the camera.
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
	 * Runs indefinitely until a close request or escape key is detected. It updates game
	 * state, clears and renders graphics, displays FPS, memory usage, and timer delta.
	 * The process repeats at regular intervals determined by the Timer update.
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
	 * Processes game input, updates a camera's position using world coordinates, and
	 * applies transformations based on the camera's current state. It takes a time
	 * interval `dt` as input. The function is designed for use with a game or simulation
	 * that requires continuous update processing.
	 *
	 * @param dt time elapsed since the last update, used to regulate the game's frame
	 * rate and physics simulation.
	 */
	private static void update(float dt) {
		c.processInput(dt, 5, .3f);
		w.updatePos(c.getTransform().getPos().getX(), c.getTransform().getPos().getY(), c.getTransform().getPos().getZ());
	}

	/**
	 * Binds a shader to a surface and renders content using it. The rendering is performed
	 * on a single object `w`, which presumably contains game or application-specific
	 * logic for drawing graphical elements, such as models.
	 *
	 * The commented-out code suggests an alternative approach involving color and position
	 * variation of multiple objects.
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
	 * Terminates the Java application with a specified exit status. It delegates the
	 * actual termination to the built-in `System.exit` method, passing the provided
	 * status code as an argument. The application is shut down immediately after execution
	 * of this statement.
	 *
	 * @param status 0 to 255 status code returned by Java when the process terminates,
	 * indicating successful or unsuccessful program execution.
	 */
	private static void exit(int status) {
		System.exit(status);
	}
}
