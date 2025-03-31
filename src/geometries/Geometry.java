package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Abstract class representing a geometric shape in 3D space.
 */
public abstract class Geometry implements Intersectable {

	/**
	 * Calculates and returns the normal vector to the geometry at a given point on
	 * the geomtery surfes .
	 *
	 * @param p the point at which to calculate the normal
	 * @return the normal vector to the geometry at the given point
	 */
	public abstract Vector getNormal(Point p);

	@Override
	public List<Point> findIntersections(Ray ray) {
		return null;
	}

}
