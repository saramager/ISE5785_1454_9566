/**
 * 
 */
package lighting;

import primitives.Color;

/**
 * The Light class represents a light source in a 3D scene. It contains the
 * intensity of the light, which is represented as a Color object. The class is
 * abstract, meaning it is intended to be subclassed by specific types of light
 * sources.
 * 
 */
abstract class Light {
	/**
	 * The intensity of the light source.
	 */
	protected final Color intensity;

	/**
	 * Constructor for the Light class.
	 *
	 * @param intensity The intensity of the light.
	 */
	protected Light(Color intensity) {
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
