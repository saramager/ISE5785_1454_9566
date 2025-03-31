package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

	/**
	 * The center point of the sphere.
	 */
	private final Point center;

	/**
	 * Constructs a sphere with a given center point and radius.
	 *
	 * @param p      the center point of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point p, Double radius) {
		super(radius);
		this.center = p;
	}

	@Override
	public Vector getNormal(Point p) {
		return p.subtract(center).normalize();
	}

	@Override
	public List<Point> findIntersections(Ray ray) {
		Point p0 = ray.getHead(); // ray's starting point
		Point center = this.center; // the sphere's center point
		Vector vec = ray.getDir(); // "the v vector" from the presentation
		if (center.equals(p0)) {
			Point newPoint = p0.add(ray.getDir().scale(radius));
			return List.of(newPoint);
		}

		Vector u = center.subtract(p0);
		double tm = vec.dotProduct(u);
		double d = Math.sqrt(u.lengthSquared() - tm * tm);
		if (d >= radius) {
			return null;
		}
		double th = Math.sqrt(radius * radius - d * d);
		double t1 = tm - th;
		double t2 = tm + th;

		if (t1 > 0 && t2 > 0) {
			Point p1 = p0.add(vec.scale(t1));// ray.getPoint(t1);
			Point p2 = p0.add(vec.scale(t2));// ray.getPoint(t2);
			return List.of(p1, p2);
		}

		if (t1 > 0) {
			Point p1 = p0.add(vec.scale(t1));// ray.getPoint(t1);
			return List.of(p1);
		}

		if (t2 > 0) {
			Point p2 = p0.add(vec.scale(t2));// ray.getPoint(t2);
			return List.of(p2);
		}
		return null;

	}
}
