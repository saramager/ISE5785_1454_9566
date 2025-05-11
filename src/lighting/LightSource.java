/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The LightSource interface represents a light source in a 3D scene. It
 * provides methods to get the intensity of the light at a specific point and
 * the direction of the light.
 */
public interface LightSource {
	/**
	 * Returns the intensity of the light at a specific point.
	 *
	 * @param point the point at which to get the light intensity
	 * @return the intensity of the light at the specified point
	 */
	Color getIntensity(Point point);

	/**
	 * Returns the direction of the light source at a specific point.
	 *
	 * @param point the point at which to get the light direction
	 * @return the direction of the light source at the specified point
	 */
	Vector getL(Point point);

}
