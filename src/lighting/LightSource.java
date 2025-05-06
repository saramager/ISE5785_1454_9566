/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * 
 */
public interface LightSource {
	Color getIntensity(Point point);

	Vector getL(Point point);

}
