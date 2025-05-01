package lighting;

import primitives.Color;

/**
 * class represent Ambient Light
 */
public class AmbientLight {
	/**
	 * The intensity of the ambient light (immutable).
	 */
	private final Color intensity;

	/**
	 * A constant representing no ambient light (black color).
	 */
	public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

	/**
	 * Constructs an AmbientLight object with the given intensity.
	 *
	 * @param intensity the color representing the ambient light intensity
	 */
	public AmbientLight(Color intensity) {
		this.intensity = intensity;
	}

	/**
	 * Returns the intensity of the ambient light.
	 *
	 * @return the ambient light intensity as a Color
	 */
	public Color getIntensity() {
		return intensity;
	}

}
