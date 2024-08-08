package com.ch;

import org.lwjgl.Sys;

/**
 * Is designed to track and manage game-related timing. It initializes and updates
 * frame rates, calculates deltas between frames, and keeps track of time. The class
 * provides methods for getting the current FPS, delta, and time, as well as updating
 * these values based on elapsed time.
 */
public class Timer {

	private static float fps;
	private static long lastFPS;
	private static long lastFrame;
	public static float delta;
	public static float currentFPS;
    public static float time;

	/**
	 * Converts the system time from milliseconds to seconds, utilizing the `Sys.getTime()`
	 * method for retrieving the system time and the `Sys.getTimerResolution()` method
	 * for obtaining the timer resolution. The result is a long integer representing the
	 * system time in seconds.
	 *
	 * @returns the current system time in milliseconds.
	 */
	private static long getTimeS() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Initializes a variable `lastFPS` with the current time obtained from the `getTimeS`
	 * method. This likely sets a starting point for measuring frame rates. The function
	 * does not take any parameters and returns no value.
	 */
	public static void init() {
		lastFPS = getTimeS();
	}

	/**
	 * Measures the elapsed time between two consecutive frames by subtracting the previous
	 * frame's timestamp from the current frame's timestamp. The result is returned as a
	 * float value representing the time difference.
	 *
	 * @returns a floating-point value representing time difference between two frames.
	 */
	private static float calculateDelta() {
		long time = getTimeS();
		float delta = (int) (time - lastFrame);
		lastFrame = getTimeS();
		return delta;
	}

	/**
	 * Updates the frames per second (FPS) counter by incrementing a variable and resetting
	 * it when a certain time interval has passed, ensuring accurate FPS measurement over
	 * a specific period.
	 */
	private static void updateFPS() {
		if (getTimeS() - lastFPS > 1000) {
			currentFPS = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	/**
	 * Returns a floating-point value representing the delta variable. The returned value
	 * is read-only and does not perform any calculations or modifications, simply providing
	 * access to the stored delta value.
	 *
	 * @returns a floating-point value representing the `delta`.
	 */
	public static float getDelta() {
		return delta;
	}

	/**
	 * Retrieves and returns a value representing the current frames per second (FPS).
	 * The returned value is likely stored in a variable named `currentFPS`. This method
	 * appears to provide read-only access to the FPS data without modifying it.
	 *
	 * @returns a floating-point value representing the current Frames Per Second.
	 */
	public static float getFPS() {
		return currentFPS;
	}

    /**
     * Retrieves a floating-point value representing time. It returns this value without
     * modification or calculation. The purpose of this function appears to be providing
     * access to an external time variable, possibly for use in other parts of the program.
     *
     * @returns a floating-point value representing the current time.
     */
    public static float getTime() {
        return time;
    }

    /**
     * Updates the game's state by calculating the frame rate, scaling the elapsed time
     * to a range between 0 and 1, and accumulating it into the total time. This process
     * is done by calling other functions within the scope of the `update` method.
     */
    public static void update() {
        updateFPS();
        delta = ((calculateDelta() / 1000));
        delta = delta < 0 || delta > 1 ? 0 : delta;
        time += delta;
    }

}
