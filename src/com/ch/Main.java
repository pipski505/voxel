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
 * Initializes and sets up display and graphics settings using LWJGL library. It then
 * enters a loop that updates and renders the game world, handling user input and
 * updating game logic accordingly. The class also includes code for rendering 3D
 * graphics and managing memory usage.
 */
public class Main {
	
	/**
	 * Initializes display and OpenGL, then enters a loop until exit.
	 *
	 * @param args command-line arguments passed to the program when it is executed, which
	 * can be used by the program for its own purposes.
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
	 * Initializes a display mode with a resolution of 1920x1080, creates a pixel format,
	 * and enables vertical synchronization. It also prints the OpenGL version string to
	 * the console. The function catches any LWJGLExceptions that may occur during
	 * initialization and logs them to the console.
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
	 * Initializes graphics settings by setting clear color, enabling culling and depth
	 * testing, creating a camera, loading shaders and textures, defining vertices and
	 * indices for rendering, and initializing a world object.
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
	 * Initializes a timer and enters an infinite loop that continuously updates the game
	 * state, renders graphics, and updates the display until the user requests closure
	 * or presses the escape key. It displays FPS, memory usage, and other information
	 * on the title bar.
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
	 * Processes input data using a specified constant, then updates the position of an
	 * object (`w`) based on the current position and transformation of another object
	 * (`c`). The update is performed for each frame, with time delta (`dt`) considered.
	 *
	 * @param dt delta time, which is used as an input to the `processInput` method of
	 * the `c` object to update its internal state over time.
	 */
	private static void update(float dt) {
		c.processInput(dt, 5, .3f);
		w.updatePos(c.getTransform().getPos().getX(), c.getTransform().getPos().getY(), c.getTransform().getPos().getZ());
	}

	/**
	 * Initializes a shader and binds it. It then renders a series of objects with different
	 * colors using their model matrices and view projection matrix. Finally, it calls
	 * another rendering method on an object named `w`.
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
	 * Terminates the Java program with a specified exit status. It calls the system's
	 * built-in `System.exit` method, which transfers control to the virtual machine and
	 * exits the application, passing the given status code as an argument to the underlying
	 * operating system.
	 *
	 * @param status 16-bit integer exit status to be passed to the parent process or
	 * shell when the program terminates.
	 */
	private static void exit(int status) {
		System.exit(status);
	}
}
