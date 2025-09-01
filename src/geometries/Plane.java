package geometries;

import static primitives.Util.*;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a plane in 3D space, defined by either three points or a point and
 * a normal vector.
 */
public class Plane extends Geometry {

	/**
	 * A point on the plane.
	 */
	private final Point p;

	/**
	 * The normal vector of the plane.
	 */
	private final Vector normal;

	/**
	 * Constructs a plane using three points in 3D space. The normal vector is
	 * computed using the cross product of two edges.
	 *
	 * @param p1 the first point on the plane
	 * @param p2 the second point on the plane
	 * @param p3 the third point on the plane
	 */
	public Plane(Point p1, Point p2, Point p3) {
		p = p1;
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		normal = v1.crossProduct(v2).normalize();
	}

	/**
	 * Constructs a plane using a point and a normal vector.
	 *
	 * @param point  a point on the plane
	 * @param normal the normal vector of the plane (normalized automatically)
	 */
	public Plane(Point point, Vector normal) {
		this.p = point;
		this.normal = normal.normalize();
	}

	@Override
	public Vector getNormal(Point p) {
		return normal;
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
		Vector vecP0P;
		try {
			vecP0P = p.subtract(ray.getHead());
		} catch (IllegalArgumentException e) {
			// Ray head is the same as the plane point, no intersection
			return null;
		}

		double denominator = alignZero(normal.dotProduct(ray.getDir()));
		if (isZero(denominator))
			return null;

		double t = alignZero(normal.dotProduct(vecP0P) / denominator);
		return t <= 0 || alignZero(t - maxDistance) > 0 ? null //
				: List.of(new Intersection(this, ray.getPoint(t)));
	}

}
