package com.ch;

import org.lwjgl.Sys;

/**
 * Is a utility class that provides functions for measuring and updating frames per
 * second (FPS). The class has several static methods, including `init()`,
 * `calculateDelta()`, `updateFPS()`, `getDelta()`, `getFPS()`, and `getTime()`. These
 * methods calculate the current FPS, delta time, and elapsed time, respectively. The
 * class also includes a `update()` method that updates the FPS and delta time values.
 */
public class Timer {

	private static float fps;
	private static long lastFPS;
	private static long lastFrame;
	public static float delta;
	public static float currentFPS;
    public static float time;

	/**
	 * Calculates the current time in milliseconds, taking into account the resolution
	 * of the system's timer.
	 * 
	 * @returns a long value representing milliseconds since the epoch.
	 */
	private static long getTimeS() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Sets the value of `lastFPS` to the current time.
	 */
	public static void init() {
		lastFPS = getTimeS();
	}

	/**
	 * Calculates the time elapsed since a previous frame was rendered, returning the
	 * delta time as a float value.
	 * 
	 * @returns a floating-point value representing the time difference between two frames.
	 */
	private static float calculateDelta() {
		long time = getTimeS();
		float delta = (int) (time - lastFrame);
		lastFrame = getTimeS();
		return delta;
	}

	/**
	 * Updates the frame rate by incrementing `currentFPS`, resets `fps` to zero, and
	 * updates `lastFPS` with a delay of 1 second.
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
	 * Returns the value of the `delta` field, which is a floating-point number.
	 * 
	 * @returns a floating-point value representing the difference between two values.
	 */
	public static float getDelta() {
		return delta;
	}

	/**
	 * Returns the current frame rate as a floating-point number.
	 * 
	 * @returns the current frame rate of the application, represented as a floating-point
	 * number.
	 */
	public static float getFPS() {
		return currentFPS;
	}

    /**
     * Returns the current system time as a floating-point value.
     * 
     * @returns a floating-point representation of time.
     */
    public static float getTime() {
        return time;
    }

    /**
     * Updates the frame rate, calculates and stores the time elapsed since the last
     * update in milliseconds, and limits the value to a range of 0 to 1.
     */
    public static void update() {
        updateFPS();
        delta = ((calculateDelta() / 1000));
        delta = delta < 0 || delta > 1 ? 0 : delta;
        time += delta;
    }

}
