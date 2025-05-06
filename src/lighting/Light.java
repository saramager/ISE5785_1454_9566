/**
 * 
 */
package lighting;

import primitives.Color;

/**
 * 
 */
class Light {
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
