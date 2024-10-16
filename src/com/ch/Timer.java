package com.ch;

import org.lwjgl.Sys;

/**
 * Provides a timing mechanism for applications,
 * calculating frame rate and time elapsed,
 * and offering a delta time value for smooth updates.
 */
public class Timer {

	private static float fps;
	private static long lastFPS;
	private static long lastFrame;
	public static float delta;
	public static float currentFPS;
    public static float time;

	/**
	 * Converts the current system time to milliseconds by multiplying it with 1000 and
	 * then dividing by the system's timer resolution. This results in a time value in
	 * milliseconds, allowing for more precise timing measurements.
	 *
	 * @returns a Unix-style timestamp in milliseconds, representing system time.
	 */
	private static long getTimeS() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Records the current time when called and stores it in the `lastFPS` variable.
	 */
	public static void init() {
		lastFPS = getTimeS();
	}

	/**
	 * Calculates the time difference between the current frame and the previous frame,
	 * returning the result in seconds. It updates the `lastFrame` variable to the current
	 * time. The result is an approximate frame rate.
	 *
	 * @returns a time difference in seconds between the current and previous frames.
	 */
	private static float calculateDelta() {
		long time = getTimeS();
		float delta = (int) (time - lastFrame);
		lastFrame = getTimeS();
		return delta;
	}

	/**
	 * Calculates and updates the frames per second (FPS) of an application.
	 * It increments the FPS counter every frame and resets it every 1000 milliseconds
	 * (1 second).
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
	 * Returns the value of the `delta` variable as a float.
	 *
	 * @returns a floating-point value representing the delta.
	 */
	public static float getDelta() {
		return delta;
	}

	/**
	 * Returns the current frames per second value.
	 *
	 * @returns the current frames per second value, a floating-point number.
	 */
	public static float getFPS() {
		return currentFPS;
	}

    /**
     * Returns the current value of the `time` variable.
     * The value is static, indicating it can be accessed without creating an instance
     * of the class.
     * This allows for shared access to the `time` variable across different instances
     * of the class.
     *
     * @returns a static float representing a predefined time value stored in the variable
     * 'time'.
     */
    public static float getTime() {
        return time;
    }

    /**
     * Calculates the time elapsed since the last frame, limits it to a valid range, and
     * adds it to the total time. This function appears to be part of a game loop or
     * animation system. It also calls the `updateFPS` function.
     */
    public static void update() {
        updateFPS();
        delta = ((calculateDelta() / 1000));
        delta = delta < 0 || delta > 1 ? 0 : delta;
        time += delta;
    }

}
