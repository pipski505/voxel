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
 * Initializes an OpenGL display and graphics context, loads shaders, textures, and
 * models, and then enters a rendering loop where it updates and renders the scene
 * based on user input and game logic. The class appears to be part of a 3D game or
 * simulation application.
 */
public class Main {
	
	/**
	 * Initializes a display, sets up OpenGL, enters a loop for rendering or processing,
	 * and then terminates the program with an exit status of 0.
	 *
	 * @param args array of command-line arguments that are passed to the Java program
	 * when it is executed, which can be accessed and used by the main method.
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
	 * Initializes the display mode to a resolution of 1920x1080 and enables vertical
	 * sync. It also creates an OpenGL context with forward compatibility and profile
	 * core support, and prints the version of GL. If any exception occurs during this
	 * process, it is caught and printed to the console.
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
	 * Initializes various graphical settings and objects for a 3D game or application.
	 * It sets clear color, enables culling face and depth testing, loads shaders and
	 * textures, creates a camera and world objects, and sets the initial position of the
	 * camera's transform.
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
	 * Initializes a timer and enters an infinite loop that updates the display title
	 * with performance metrics, updates game state using the timer delta, clears and
	 * renders the game scene, and updates the display until the close request or escape
	 * key is pressed.
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
	 * Processes input using `c.processInput`, updating a position based on the input and
	 * other variables, and then updates another object's position using the provided
	 * coordinates from an entity's transform.
	 *
	 * @param dt delta time, which is used to scale the processing of user input and
	 * transformations by the Camera object `c`.
	 */
	private static void update(float dt) {
		c.processInput(dt, 5, .3f);
		w.updatePos(c.getTransform().getPos().getX(), c.getTransform().getPos().getY(), c.getTransform().getPos().getZ());
	}

	/**
	 * Binds a shader program and renders a scene by setting uniform colors and model
	 * view projection matrices for each cube in a 4x4x4 grid, then calls the render
	 * method of another object (`w`).
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
	 * Terminates the Java virtual machine with a specified status. It calls `System.exit`
	 * method to initiate the termination process. The provided status is used to indicate
	 * the exit code, which can be useful for debugging and testing purposes.
	 *
	 * @param status 16-bit integer value that is passed to the operating system to
	 * indicate the program's termination status, which can be used by other programs to
	 * determine why the program terminated.
	 */
	private static void exit(int status) {
		System.exit(status);
	}
}
