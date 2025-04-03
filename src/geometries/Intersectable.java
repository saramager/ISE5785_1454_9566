/**
 * 
 */
package geometries;

import primitives.*;
import java.util.List;

/**
 *  interface help with all the intersction
 */
public interface Intersectable {
	/**
	 *  find the intersections point between an gematry and ray 
	 * @param ray -check intsersectuion for ray
	 * @return list of Points that intsersectuion
	 */
	List<Point> findIntersections(Ray ray);

}
