package geometries;

import java.util.List;
import static primitives.Util.*;
import primitives.*;

/**
 * Represents a triangle in 3D space. A triangle is a specific case of a
 * {@link Polygon} with exactly three vertices.
 */
public class Triangle extends Polygon {

	/**
	 * Constructs a triangle using three points.
	 * 
	 * @param p1 the first vertex of the triangle
	 * @param p2 the second vertex of the triangle
	 * @param p3 the third vertex of the triangle
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}

	@Override
	public List<Point> findIntersections(Ray ray) {

		List<Point> intersections = super.plane.findIntersections(ray);
		if (intersections == null)
			return null;
		Vector n1;
		Vector n2;
		Vector n3;
		Point rayHead = ray.getHead();
		Vector rayDir = ray.getDir();

		try {
			// Create vectors from the triangle vertices to the intersection point
			Vector v1 = vertices.get(0).subtract(rayHead);
			Vector v2 = vertices.get(1).subtract(rayHead);
			Vector v3 = vertices.get(2).subtract(rayHead);

			// Calculate the normals of the triangle's faces
			n1 = v1.crossProduct(v2).normalize();
			n2 = v2.crossProduct(v3).normalize();
			n3 = v3.crossProduct(v1).normalize();

		} catch (IllegalArgumentException ex) {
			return null;
		}
		// Direction of the ray
		double dot1 = alignZero(n1.dotProduct(rayDir));
		double dot2 = alignZero(n2.dotProduct(rayDir));
		double dot3 = alignZero(n3.dotProduct(rayDir));

		// If any dot product is zero, the point is on an edge or vertex
		if (isZero(dot1) || isZero(dot2) || isZero(dot3))
			return null;

		// Check if all dot products have the same sign (positive or negative)
		if ((dot1 > 0 && dot2 > 0 && dot3 > 0) || (dot1 < 0 && dot2 < 0 && dot3 < 0))
			return intersections;

		return null; // No intersection inside the triangle
	}

}
