package lighting;

import primitives.Color;

/**
 * class represent Ambient Light
 */
public class AmbientLight extends Light {
//	/**
//	 * The intensity of the ambient light (immutable).
//	 */
//	private final Color intensity;

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
		super(intensity);
	}

}
