package geometries;

import static primitives.Util.alignZero;

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
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		Point p0 = ray.getHead(); // ray's starting point
		Point center = this.center; // the sphere's center point
		Vector vec = ray.getDir();
		// "the v vector" from the presentation
		if (center.equals(p0))
			return List.of(new Intersection(this, ray.getPoint(radius), this.getMaterial()));

		Vector u = center.subtract(p0);
		double tm = (vec.dotProduct(u));
		double d2 = (u.lengthSquared() - tm * tm);
		double th2 = alignZero(radiusPow2 - d2);
		if (th2 <= 0)
			return null;

		double th = Math.sqrt(th2);

		double t2 = tm + th;
		if (alignZero(t2) <= 0)
			return null;

		double t1 = tm - th;
		return alignZero(t1) <= 0 ? List.of(new Intersection(this, ray.getPoint(t2), this.getMaterial()))//
				: List.of(new Intersection(this, ray.getPoint(t1), this.getMaterial()),
						new Intersection(this, ray.getPoint(t2), this.getMaterial()));

	}
}
