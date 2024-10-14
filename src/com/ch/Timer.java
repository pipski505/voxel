package com.ch;

import org.lwjgl.Sys;

/**
 * Provides a timer system for tracking frame rates and time elapsed.
 * Updates the timer state based on the current system time.
 * Offers methods for accessing the current delta time, frame rate, and elapsed time.
 */
public class Timer {

	private static float fps;
	private static long lastFPS;
	private static long lastFrame;
	public static float delta;
	public static float currentFPS;
    public static float time;

	/**
	 * Converts system time measured in ticks by `Sys.getTime()` into seconds. It multiplies
	 * the tick count by 1000 to convert it to milliseconds, then divides by the system
	 * timer resolution to achieve accurate seconds. The result is a time measurement in
	 * seconds.
	 *
	 * @returns the system time in milliseconds.
	 */
	private static long getTimeS() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Records the current time in milliseconds when called, storing it in the `lastFPS`
	 * variable.
	 */
	public static void init() {
		lastFPS = getTimeS();
	}

	/**
	 * Calculates the time difference between the current and previous frame, returning
	 * the result as a float. It updates the `lastFrame` variable with the current time,
	 * allowing for subsequent delta calculations.
	 *
	 * @returns a time interval in milliseconds between the current and previous frames.
	 */
	private static float calculateDelta() {
		long time = getTimeS();
		float delta = (int) (time - lastFrame);
		lastFrame = getTimeS();
		return delta;
	}

	/**
	 * Tracks the current frames per second (FPS) by incrementing a counter and resetting
	 * it every 1000 milliseconds when a change in FPS is detected. The current FPS is
	 * then updated and stored in a variable.
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
	 * Returns the value of the `delta` variable, presumably a floating-point number
	 * representing a change or difference.
	 *
	 * @returns a float value representing the delta.
	 */
	public static float getDelta() {
		return delta;
	}

	/**
	 * Returns the current frames per second (FPS) value. The FPS value is likely stored
	 * in a global variable named `currentFPS`. This function provides a read-only access
	 * to the current FPS measurement.
	 *
	 * @returns the current frames per second value, a float.
	 */
	public static float getFPS() {
		return currentFPS;
	}

    /**
     * Returns a static float value representing the current time.
     * The value is likely a static variable accessed directly.
     *
     * @returns a static float value representing the current time.
     */
    public static float getTime() {
        return time;
    }

    /**
     * Calculates the time delta between frames, clamps it to a valid range, and adds it
     * to the total time. This is typically used in game development to manage game logic
     * and timing. The function also calls the `updateFPS` function.
     */
    public static void update() {
        updateFPS();
        delta = ((calculateDelta() / 1000));
        delta = delta < 0 || delta > 1 ? 0 : delta;
        time += delta;
    }

}
